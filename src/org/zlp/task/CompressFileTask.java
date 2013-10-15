package org.zlp.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.zlp.plugin.CustomCssCompressor;
import org.zlp.swing.MainFrame;
import org.zlp.swing.ProgressDialog;
import org.zlp.swing.TrackPanel;
import org.zlp.util.ThreadGenerate;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * Created with eclipse
 * 
 * @Description: 压缩给定的文件集合任务
 * @author: programmer.zlp@qq.com
 * @Date: 2013年8月28日
 * @Time: 下午1:05:03
 * 
 */
public class CompressFileTask extends SwingWorker<Integer, Void> {

	private List<File> fileList = null;

	public CompressFileTask(List<File> fileList) {
		this.fileList = fileList;
	}

	@Override
	protected Integer doInBackground() throws Exception {
		TrackPanel.INSTANCE.getTextArea().setText("");
		int failureCount = 0;
		Map<List<Object>, YUICompressor> taskMap = new HashMap<List<Object>, YUICompressor>();
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			File minFile = new File(file.getAbsolutePath() + ".min");
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),
					"UTF-8");
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(
					minFile), "UTF-8");
			YUICompressor yuiCompressor = new YUICompressor(file, inputStreamReader,
					outputStreamWriter);
			ThreadGenerate.INSTANCE.createTempExecutorService().execute(yuiCompressor);
			List<Object> fileList = new ArrayList<Object>();
			fileList.add(file);
			fileList.add(minFile);
			fileList.add(inputStreamReader);
			fileList.add(outputStreamWriter);
			taskMap.put(fileList, yuiCompressor);
		}
		int p = 0;// 进度值
		for (Map.Entry<List<Object>, YUICompressor> entry : taskMap.entrySet()) {
			File file = (File) entry.getKey().get(0);
			File minFile = (File) entry.getKey().get(1);
			InputStreamReader inputStreamReader = (InputStreamReader) entry.getKey().get(2);
			OutputStreamWriter outputStreamWriter = (OutputStreamWriter) entry.getKey().get(3);
			YUICompressor yuiCompressor = entry.getValue();
			if (file.getAbsolutePath().length() > 40) {
				ProgressDialog.INSTANCE.getStatusLabel().setText(
						"正在处理:"
								+ file.getAbsolutePath().substring(0, 10)
								+ "..."
								+ file.getAbsolutePath().substring(
										file.getAbsolutePath().length() - 25,
										file.getAbsolutePath().length()));
			} else {
				ProgressDialog.INSTANCE.getStatusLabel().setText("正在处理:" + file.getAbsolutePath());
			}
			try {
				boolean flag = yuiCompressor.get(10, TimeUnit.SECONDS);
				if (flag) {
					file.delete();// 删除原文件
					minFile.renameTo(file);
					TrackPanel.INSTANCE.getTextArea().setText(
							TrackPanel.INSTANCE.getTextArea().getText() + file.getAbsolutePath()
									+ " 压缩成功！" + "\n");
				} else {
					minFile.delete();
					failureCount++;
					TrackPanel.INSTANCE.getTextArea().setText(
							TrackPanel.INSTANCE.getTextArea().getText() + file.getAbsolutePath()
									+ " 压缩失败！" + "\n");
				}
			} catch (Exception e) {
				yuiCompressor.cancel(true);
				outputStreamWriter.close();
				inputStreamReader.close();
				minFile.delete();
				failureCount++;
				TrackPanel.INSTANCE.getTextArea().setText(
						TrackPanel.INSTANCE.getTextArea().getText() + file.getAbsolutePath()
								+ " 压缩失败（超时）！" + "\n");
			}
			p++;
			setProgress((100 * p) / fileList.size());
		}
		int successCount = fileList.size() - failureCount;
		TrackPanel.INSTANCE.getTextArea().setText(
				TrackPanel.INSTANCE.getTextArea().getText() + "\n总共：" + fileList.size() + " 成功："
						+ successCount + "  失败：" + failureCount);
		fileList.clear();
		taskMap.clear();
		return failureCount;
	}

	@Override
	protected void done() {
		ProgressDialog.INSTANCE.createDialog().setVisible(false);
		try {
			int failureCount = get();
			if (failureCount > 0) {
				JOptionPane.showMessageDialog(MainFrame.INSTANCE.getFrame(), "压缩有失败文件！请查看以下记录。");
			} else {
				JOptionPane.showMessageDialog(MainFrame.INSTANCE.getFrame(), "压缩成功！");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainFrame.INSTANCE.getFrame(), "压缩异常！");
		}
		ThreadGenerate.INSTANCE.destroyTempExecutorService();
	}

	/**
	 * YUI压缩
	 */
	class YUICompressor extends FutureTask<Boolean> {

		public YUICompressor(final File file, final InputStreamReader inputStreamReader,
				final OutputStreamWriter outputStreamWriter) {
			super(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					boolean flag = true;
					if (file.getName().matches(".+\\.js$")) {
						try {
							JavaScriptCompressor compressor = new JavaScriptCompressor(
									inputStreamReader, new ErrorReporter() {
										public void warning(String message, String sourceName,
												int line, String lineSource, int lineOffset) {
										}

										public void error(String message, String sourceName,
												int line, String lineSource, int lineOffset) {
										}

										public EvaluatorException runtimeError(String message,
												String sourceName, int line, String lineSource,
												int lineOffset) {
											error(message, sourceName, line, lineSource, lineOffset);
											return new EvaluatorException(message);
										}
									});
							compressor.compress(outputStreamWriter, -1, true, false, false, false);
						} catch (Exception e) {
							flag = false;
						} finally {
							outputStreamWriter.close();
							inputStreamReader.close();
						}
					} else {
						try {
							CustomCssCompressor compressor = new CustomCssCompressor(
									inputStreamReader);
							compressor.compress(outputStreamWriter, -1);
						} catch (Exception e) {
							flag = false;
						} finally {
							outputStreamWriter.close();
							inputStreamReader.close();
						}
					}
					return flag;
				}
			});
		}

	}

}