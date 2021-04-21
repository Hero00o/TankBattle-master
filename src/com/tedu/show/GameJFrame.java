package com.tedu.show;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;

/**
 * @说明 游戏窗体 主要实现功能：关闭、显示、最大化、最小化
 * @author HP
 * @功能说明：需要嵌入面板，启动主线程等等
 * @窗体说明：swing awt 窗体大小（记录用户上次使用软件的窗体样式）
 * 
 * @分析 1.面板绑定到窗体
 * 		 2.监听绑定
 * 		 3.游戏主线程启动
 * 		 4.显示窗体
 */
public class GameJFrame extends JFrame {

	public static int GameX = 817;//GAMEX
	public static int GameY = 630;//GAMEY
	
	private JPanel jPanel = null; //正在显示的面板
	private KeyListener keyListener = null; //键盘监听
	private MouseMotionListener mouseMotionListener = null; // 鼠标监听
	private MouseListener mouseListener = null;
	private Thread thread = null; //游戏主线程
	Box b1;
	
	public GameJFrame() {
		init();
	}	
	
	public void init() {
		this.setSize(GameX, GameY); // 设置窗体大小
		this.setTitle("坦克大战"); // 设置窗体标题
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置退出并关闭
		this.setLocationRelativeTo(null); // 屏幕居中显示
		this.setResizable(false);
		this.setFocusable(true);
	}
	
	/*窗体布局：可以将 存档，读档...button、下拉框在这布局*/
	public void addButton() {
//		this.setLayout(manager); // 布局格式，可以添加控件
		JButton btn_pre = new JButton("上一关");
		JButton btn_reStart = new JButton("重新开始");
		JButton btn_next = new JButton("下一关");
		btn_pre.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn_pre.setFocusable(false);
				btn_reStart.setFocusable(false);
				btn_next.setFocusable(false);
				GameThread.flag = false;	
				GameThread.increment = -1;
			}
		});
		
		btn_next.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn_pre.setFocusable(false);
				btn_reStart.setFocusable(false);
				btn_next.setFocusable(false);
				GameThread.flag = false;	
				GameThread.increment = 1;
			}
		});
		
		btn_reStart.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn_pre.setFocusable(false);
				btn_reStart.setFocusable(false);
				btn_next.setFocusable(false);
				GameThread.flag = false;	
				GameThread.increment = 0;
			}
		});
		b1 = Box.createVerticalBox();
		Box b2 = Box.createHorizontalBox();
		b2.add(btn_pre);
		b2.add(btn_reStart);
		b2.add(btn_next);
		b1.add(b2);
		this.add(b1);
	}
	
	/**
	 * 启动方法
	 */
	public void start() {
		addButton();
		if(jPanel != null) {
			b1.add(jPanel);
		}
		if(keyListener != null) {
			this.addKeyListener(keyListener);
		}
		if(thread != null) {
			thread.start();//启动线程
		}
		this.setVisible(true);//界面显示
		//界面刷新
		//如果jPanel是Runnable的子类实体对象
		if(this.jPanel instanceof Runnable) {
			//已做判定，强制转换不会出错
			new Thread((Runnable)this.jPanel).start();
		}
	}
	
	/*set注入：等学习ssm  通过set方法注入 从配置文件中读取的数据；将配置文件
	 * 中的数据赋值为类的属性
	 * 
	 * 构造注入：需要配合构造方法
	 * Spring 中ioc 进行对象的自动生成、管理
	 * */
	
	public void setjPanel(JPanel jPanel) {
		this.jPanel = jPanel;
	}

	public void setKeyListener(KeyListener keyListener) {
		this.keyListener = keyListener;
	}

	public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
		this.mouseMotionListener = mouseMotionListener;
	}

	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}
	
	/**
	 * 弹出交互对话框
	 */
	public static void showDialog() {
		String options[]={"上一关","重新开始","下一关"};
		int value=JOptionPane.showOptionDialog(null, "选择一个方案：",
		"本局游戏结束", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
		options, "重新开始");
		if (value!=JOptionPane.CLOSED_OPTION) {
			switch (value) {
				case 0:
					GameThread.flag = false;	
					GameThread.increment = -1;
					break;
				case 1:
					GameThread.flag = false;	
					GameThread.increment = 0;
					break;
				case 2:
					GameThread.flag = false;	
					GameThread.increment = 1;
					break;
				default:
					break;
			}
		} else {
//			//如果点了  ×  则游戏直接结束，需要重新运行程序了。。。
//			GameThread.isOver = true;
//			GameThread.flag = false;
			showDialog();
		}
	}
	
}
