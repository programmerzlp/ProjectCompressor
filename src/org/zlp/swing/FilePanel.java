package org.zlp.swing;

import static org.zlp.app.Application.WIN_WIDTH;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public enum FilePanel {

	INSTANCE {
		@Override
		public JPanel createPanel() {
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			panel.setPreferredSize(new Dimension(WIN_WIDTH - 50, 35));
			panel.add(new JLabel("目录浏览："));
			panel.add(new JTextField(30));
			panel.add(new JButton(new ChooseButtonAction()));
			return panel;
		}

		@Override
		public JPanel getPanel() {
			return panel;
		}
	};

	protected JPanel panel = new JPanel();

	public abstract JPanel createPanel();

	public abstract JPanel getPanel();

	/**
	 * 浏览按钮
	 */
	class ChooseButtonAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ChooseButtonAction() {
			super("浏览...");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fileChooser.showDialog(panel, "确定");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				((JTextField) panel.getComponent(1)).setText(fileChooser.getSelectedFile()
						.getAbsolutePath());
			}
		}

	}

}