package org.zlp.swing;

import static org.zlp.app.Application.WIN_WIDTH;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public enum TrackPanel {

	INSTANCE {
		@Override
		public JPanel createPanel() {
			testArea = new JTextArea() {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean getScrollableTracksViewportWidth() {
					return (getSize().width < getParent().getSize().width);
				}

				@Override
				public void setSize(Dimension d) {
					if (d.width < getParent().getSize().width) {
						d.width = getParent().getSize().width;
					}
					super.setSize(d);
				}

			};
			JScrollPane scrollPane = new JScrollPane();
			testArea.setEditable(false);
			testArea.setOpaque(false);
			scrollPane.setViewportView(testArea);
			panel.setPreferredSize(new Dimension(WIN_WIDTH - 50, 200));
			panel.setLayout(new BorderLayout());
			panel.add(scrollPane, BorderLayout.CENTER);
			return panel;
		}

		@Override
		public JTextArea getTextArea() {
			return testArea;
		}
	};

	protected JPanel panel = new JPanel();

	protected JTextArea testArea = null;

	public abstract JPanel createPanel();

	public abstract JTextArea getTextArea();

}