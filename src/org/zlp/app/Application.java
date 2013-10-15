package org.zlp.app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.zlp.swing.MainFrame;

/**
 * Created with eclipse
 * 
 * @Description: 应用程序入口
 * @author: programmer.zlp@qq.com
 * @Date: 2013-7-11
 * @Time: 上午10:27:39
 * 
 */
public class Application {

	public static final int WIN_WIDTH = 400;

	public static final int WIN_HEIGHT = 400;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		System.setProperty("java.awt.im.style", "on-the-spot");// 系统属性设置，消除中文浮动框
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());// 设置成系统样式
		SwingUtilities.invokeLater(new Runnable() { 
			@Override
			public void run() {
				MainFrame.INSTANCE.createMainFrame();
			}
		});
	}

}