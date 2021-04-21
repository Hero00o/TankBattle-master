package com.tedu.show;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.manager.GameLoad;

/**
 * @说明 游戏最先加载窗体的面板
 * @author HP
 *
 */
public class LoadGameJPanel extends JPanel {
	
	private ImageIcon imageIcon = null;
	private JButton playBtn = null;
	private JFrame jFrame;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(imageIcon.getImage(), 0, 0, null);
	}
	
	public LoadGameJPanel(JFrame jFrame) {
		this.jFrame = jFrame;
		init();
	}

	/**
	 * @说明 初始化面板，设置面板的各种属性
	 */
	private void init() {
		//加载图片
		GameLoad.loadGameImg();
		//获取背景图片
		this.imageIcon = GameLoad.loadGameImgMap.get("backgroundImg");
		this.setLayout(null);
		initPlayBtn();
		this.add(playBtn);
	}

	/**
	 * @说明 初始化点击进入游戏按钮的设置
	 * @内容 设置默认图片、点击图片、无边框、内部无聚焦...
	 */
	private void initPlayBtn() {
		this.playBtn = new JButton();
		//设置按钮默认图片
		this.playBtn.setIcon(GameLoad.loadGameImgMap.get("btnPlay"));
		//设置按钮点击图片
		this.playBtn.setPressedIcon(GameLoad.loadGameImgMap.get("btnPlayClick"));
		//其他为了美观设置
		this.playBtn.setContentAreaFilled(false);
		this.playBtn.setFocusPainted(false);
		this.playBtn.setBounds(350, 400, 97, 54);
		this.playBtn.setBorderPainted(false);
		this.playBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				jFrame.setVisible(false);
				
				GameJFrame gj = new GameJFrame();
				/*实例化面板，注入到JFrame中*/
				GameMainJPanel jp = new GameMainJPanel();
				//实例化监听
				GameListener listener = new GameListener();	
				//实例化主线程
				GameThread th = new GameThread();
				//注入
				gj.setjPanel(jp);//注入面板
				gj.setKeyListener(listener);//注入监听
				gj.setThread(th);//注入线程
				
				gj.start();	
//				jFrame.dispose();
			}
		});
		
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public JButton getPlayBtn() {
		return playBtn;
	}
	
	
	
}
