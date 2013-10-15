package org.zlp.swing;

import static org.zlp.app.Application.WIN_WIDTH;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public enum OptionPanel {

	INSTANCE {
		@Override
		public JPanel createPanel() {
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			panel.setPreferredSize(new Dimension(WIN_WIDTH - 50, 35));
			panel.add(new JLabel("压缩选项："));
			panel.add(new JCheckBox("JS", true));
			panel.add(new JCheckBox("CSS", true));
			return panel;
		}
	};

	protected JPanel panel = new JPanel();

	public abstract JPanel createPanel();

}