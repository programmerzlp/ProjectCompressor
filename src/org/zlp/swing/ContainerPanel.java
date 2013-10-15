package org.zlp.swing;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * Created with eclipse
 * 
 * @Description: 容器
 * @author: programmer.zlp@qq.com
 * @Date: 2013-7-11
 * @Time: 上午10:15:15
 * 
 */
public enum ContainerPanel {

	INSTANCE {
		@Override
		public JPanel createPanel() {
			Box box = Box.createVerticalBox();
			box.add(Box.createVerticalStrut(10));
			box.add(OptionPanel.INSTANCE.createPanel());
			box.add(FilePanel.INSTANCE.createPanel());
			box.add(CommandPanel.INSTANCE.createPanel());
			box.add(TrackPanel.INSTANCE.createPanel());
			panel.add(box);
			return panel;
		}
	};

	protected JPanel panel = new JPanel();

	public abstract JPanel createPanel();

}