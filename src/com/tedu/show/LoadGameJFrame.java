package com.tedu.show;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @说明 游戏最先加载的窗体（页面）
 * 		1.背景图片
 * 		2.进入游戏按钮
 * 		3.退出按钮
 * @author HP
 *
 */
public class LoadGameJFrame extends JFrame {
	
	//后续与GameJFrame的宽、高一起放在配置文件读取
	public static int LoadGameX = 807;//面板宽度
	public static int LoadGameY = 630;//面板高度
	
	private JPanel jPanel = null; //正在显示的面板
	
	public LoadGameJFrame() {
		init();
	}

	/**
	 * 初始化窗体的基本设置
	 */
	public void init() {
		this.setSize(LoadGameX, LoadGameY); // 设置窗体大小
		this.setTitle("坦克大战"); // 设置窗体标题
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置退出并关闭
		this.setLocationRelativeTo(null); // 屏幕居中显示		
		this.setResizable(false);
	}
	
	/**
	 * 启动方法
	 */
	public void start() {
		if(jPanel != null) {
			this.add(jPanel);
		}
		this.setVisible(true);//界面显示
	}

	public void setjPanel(JPanel jPanel) {
		this.jPanel = jPanel;

	}	
	
}
