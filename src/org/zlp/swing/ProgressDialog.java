package org.zlp.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public enum ProgressDialog {

	INSTANCE {
		@Override
		public JDialog createDialog() {
			progressBar.setValue(0);
			progressBar.setStringPainted(true);
			progressBar.setPreferredSize(new Dimension(320, 30));
			statusLabel.setPreferredSize(new Dimension(320, 30));
			progressDialog.setLayout(new FlowLayout());
			progressDialog.add(progressBar);
			progressDialog.add(statusLabel);
			progressDialog.setSize(350, 120);
			// progressDialog.setAlwaysOnTop(true);
			progressDialog.setLocationRelativeTo(MainFrame.INSTANCE.getFrame());
			return progressDialog;
		}

		@Override
		public JProgressBar getProgressBar() {
			return progressBar;
		}

		@Override
		public JLabel getStatusLabel() {
			return statusLabel;
		}
	};

	protected JDialog progressDialog = new JDialog(MainFrame.INSTANCE.getFrame(), "正在处理...");

	protected JProgressBar progressBar = new JProgressBar(0, 100);

	protected JLabel statusLabel = new JLabel();

	public abstract JDialog createDialog();

	public abstract JProgressBar getProgressBar();

	public abstract JLabel getStatusLabel();

}