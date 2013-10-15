package org.zlp.swing;

import static org.zlp.app.Application.WIN_HEIGHT;
import static org.zlp.app.Application.WIN_WIDTH;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.zlp.util.PropertiesRead;

/**
 * Created with eclipse
 * 
 * @Description: 主框架
 * @author: programmer.zlp@qq.com
 * @Date: 2013-7-11
 * @Time: 上午10:25:34
 * 
 */
public enum MainFrame {

	INSTANCE {
		@Override
		public JFrame createMainFrame() {
			mainFrame.setJMenuBar(MenuBar.INSTANCE.createMenuBar());
			mainFrame.setContentPane(ContainerPanel.INSTANCE.createPanel());
			mainFrame.setTitle("项目压缩器");
			mainFrame.setSize(WIN_WIDTH, WIN_HEIGHT);
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setVisible(true);

			mainFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent event) {
					// 加载配置文件
					try {
						PropertiesRead.INSTANCE.load("/config.properties");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			return mainFrame;
		}

		@Override
		public JFrame getFrame() {
			return mainFrame;
		}
	};

	protected JFrame mainFrame = new JFrame();

	public abstract JFrame createMainFrame();

	public abstract JFrame getFrame();

}