package org.zlp.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

public enum SearchFileDialog {

	INSTANCE {
		@Override
		public JDialog createDialog() {
			statusLabel.setPreferredSize(new Dimension(320, 30));
			searchFileDialog.setLayout(new FlowLayout());
			searchFileDialog.add(statusLabel);
			searchFileDialog.setSize(350, 90);
			searchFileDialog.setLocationRelativeTo(MainFrame.INSTANCE.getFrame());
			return searchFileDialog;
		}

		@Override
		public JLabel getStatusLabel() {
			return statusLabel;
		}
	};

	protected JDialog searchFileDialog = new JDialog(MainFrame.INSTANCE.getFrame(), "正在搜索...");

	protected JLabel statusLabel = new JLabel();

	public abstract JDialog createDialog();

	public abstract JLabel getStatusLabel();

}