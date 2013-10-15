package org.zlp.task;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.zlp.bean.Result;
import org.zlp.swing.MainFrame;
import org.zlp.swing.ProgressDialog;
import org.zlp.swing.SearchFileDialog;
import org.zlp.util.ThreadGenerate;

/**
 * Created with eclipse
 * 
 * @Description: 检查并且获取需要的文件任务
 * @author: programmer.zlp@qq.com
 * @Date: 2013年8月28日
 * @Time: 上午10:57:25
 * 
 */
public class CheckFileTask extends SwingWorker<Result<List<File>>, Void> {

	private String path = null;
	private int timeout = 10;
	private int flag = 0;

	/**
	 * @param path
	 *            文件路径
	 * @param timeout
	 *            扫描文件的超时时间
	 */
	public CheckFileTask(String path, int timeout) {
		this.path = path;
		this.timeout = timeout;
	}

	/**
	 * @param path
	 *            文件路径
	 * @param timeout
	 *            扫描文件的超时时间
	 * @param flag
	 *            后续操作任务执行
	 */
	public CheckFileTask(String path, int timeout, int flag) {
		this.path = path;
		this.timeout = timeout;
		this.flag = flag;
	}

	@Override
	protected Result<List<File>> doInBackground() throws Exception {
		Result<List<File>> result = new Result<List<File>>();
		File file = new File(path);
		if (!file.isDirectory()) {
			if (file.getName().matches(".+\\.js$") || file.getName().matches(".+\\.css$")) {
				List<File> list = new ArrayList<File>();
				list.add(file);
				result.setData(list);
				return result;
			} else {
				result.setCode(1);
				result.setMsg("该文件不是js或者css文件");
				return result;
			}
		}
		try {
			File parentFile = file.getParentFile();
			if (parentFile == null) {
				result.setCode(2);
				result.setMsg("不存在父目录");
				return result;
			}
		} catch (Exception e) {
			result.setCode(2);
			result.setMsg("不存在父目录");
			result.setException(e);
			return result;
		}
		List<File> jscssFileList = null;
		FutureTask<List<File>> futureTask = new FilterJSAndCSS(path);
		ThreadGenerate.INSTANCE.createExecutorService().execute(futureTask);
		try {
			jscssFileList = futureTask.get(timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			futureTask.cancel(true);
			result.setCode(2);
			result.setMsg("获取JS和CSS文件超时");
			result.setException(e);
			return result;
		}
		result.setData(jscssFileList);
		return result;
	}

	@Override
	protected void done() {
		SearchFileDialog.INSTANCE.createDialog().setVisible(false);
		try {
			Result<List<File>> result = get();
			switch (flag) {
			case 0:
				JOptionPane.showMessageDialog(MainFrame.INSTANCE.getFrame(), "包含JS和CSS文件共 "
						+ result.getData().size() + " 个");
				result.getData().clear();
				break;
			case 1:
				if (result.getCode() == 0) {
					ProgressDialog.INSTANCE.createDialog().setVisible(true);
					CompressFileTask compressFileTask = new CompressFileTask(result.getData());
					compressFileTask.addPropertyChangeListener(new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							String strPropertyName = evt.getPropertyName();
							if ("progress".equals(strPropertyName)) {
								int progress = (Integer) evt.getNewValue();
								ProgressDialog.INSTANCE.getProgressBar().setValue(progress);
							}
						}
					});
					compressFileTask.execute();
				} else {
					JOptionPane.showMessageDialog(MainFrame.INSTANCE.getFrame(), result.getMsg());
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainFrame.INSTANCE.getFrame(), "检查文件异常,运行终止！");
		}
	}

	/**
	 * 过滤并获取文件夹中CSS和JS文件
	 */
	class FilterJSAndCSS extends FutureTask<List<File>> {

		public FilterJSAndCSS(final String path) {

			super(new Callable<List<File>>() {

				@Override
				public List<File> call() throws Exception {
					File file = new File(path);
					List<File> jscssFileList = new ArrayList<File>();
					filter(file, jscssFileList);
					return jscssFileList;
				}

				protected void filter(File file, List<File> jscssFileList) {
					if (file.getAbsolutePath().length() > 40) {
						SearchFileDialog.INSTANCE.getStatusLabel().setText(
								"搜索:"
										+ file.getAbsolutePath().substring(0, 10)
										+ "..."
										+ file.getAbsolutePath().substring(
												file.getAbsolutePath().length() - 25,
												file.getAbsolutePath().length()));
					} else {
						SearchFileDialog.INSTANCE.getStatusLabel().setText(
								"搜索:" + file.getAbsolutePath());
					}
					jscssFileList.addAll(Arrays.asList(file.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return name.matches(".+\\.js$") || name.matches(".+\\.css$");
						}
					})));
					for (File childFile : file.listFiles()) {
						if (childFile.isDirectory()) {
							filter(childFile, jscssFileList);
						}
					}
				}

			});
		}

	}

}