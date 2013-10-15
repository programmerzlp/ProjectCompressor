package org.zlp.swing;

import static org.zlp.app.Application.WIN_WIDTH;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.zlp.task.CheckFileTask;
import org.zlp.util.PropertiesRead;
import org.zlp.util.ThreadGenerate;

public enum CommandPanel {

	INSTANCE {
		@Override
		public JPanel createPanel() {
			panel.setLayout(new FlowLayout(FlowLayout.CENTER));
			panel.setPreferredSize(new Dimension(WIN_WIDTH - 50, 35));
			panel.add(new JButton(new DetectionStatisticsAction()));
			panel.add(new JButton(new CompressionAction()));
			return panel;
		}
	};

	protected JPanel panel = new JPanel();

	public abstract JPanel createPanel();

	/**
	 * 检测统计
	 */
	class DetectionStatisticsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public DetectionStatisticsAction() {
			super("检测统计");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					SearchFileDialog.INSTANCE.createDialog().setVisible(true);
					CheckFileTask checkFileTask = new CheckFileTask(
							((JTextField) FilePanel.INSTANCE.getPanel().getComponent(1)).getText(),
							Integer.parseInt(PropertiesRead.INSTANCE
									.getProperty("DetectionStatistics.timeout")));
					ThreadGenerate.INSTANCE.createExecutorService().execute(checkFileTask);
				}
			});
		}

	}

	/**
	 * 压缩
	 */
	class CompressionAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CompressionAction() {
			super("开始压缩");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					SearchFileDialog.INSTANCE.createDialog().setVisible(true);
					CheckFileTask checkFileTask = new CheckFileTask(
							((JTextField) FilePanel.INSTANCE.getPanel().getComponent(1)).getText(),
							Integer.parseInt(PropertiesRead.INSTANCE
									.getProperty("Compression.timeout")), 1);
					ThreadGenerate.INSTANCE.createExecutorService().execute(checkFileTask);
				}
			});
		}

	}

}