package com.tedu.show;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @说明 游戏的主要面板
 * @author HP
 * @功能说明 主要进行元素的显示，同时进行界面的刷新（多线程）
 * 
 * @题外话 java开发实现思考的应该是：做继承或者是接口实现
 * @多线程刷新 1.本类实现Runnable接口
 * 			   2.本类中定义一个内部类实现
 */
public class GameMainJPanel extends JPanel implements Runnable {

	//联动管理器
	private ElementManager em;
	
	public GameMainJPanel() {
		init();
	}
	
	public void init() {
		em = ElementManager.getManager(); // 得到元素管理器对象
	}
	
	/**
	 * paint方法是进行绘画元素。
	 * 绘画时是有固定的顺序， 先绘画的图片会在底层，后绘画的会覆盖先绘画的
	 * 约定：本方法只执行一次，想实时刷新需要使用多线程
	 */
	@Override  //用于绘画的 Graphic 画笔专门用于绘画的
	public void paint(Graphics g) {
		super.paint(g);
		//map key-value key是无序不可重复的
		//set 和map的key一样是无序不可重复的
		Map<GameElement, List<ElementObj>> all = em.getGameElements();
//		GameElement.values(); // 隐藏方法 返回值是一个数组， 数组的顺序就是定义枚举的顺序
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge); // 按定义枚举的顺序获取对应集合
			for(int i = 0; i < list.size(); i++) {
				ElementObj obj = list.get(i); // 读取为基类
				obj.showElement(g); //调用每个类自己的showElement方法
			}
		}
		//单独获取GRASS地图块，最顶层绘制
		List<ElementObj> GRASSList = em.getMapElementByKey("GRASS");
		for(int i = 0;i < GRASSList.size(); i++) {
			ElementObj obj = GRASSList.get(i);
			obj.showElement(g);
		}
	}

	@Override
	public void run() { //接口实现
		while(true) {
			//重新绘画
			this.repaint();
			//一般情况下多线程都会使用一个休眠，控制速度
			try {
				Thread.sleep(30); // 休眠50毫秒 1秒刷新20次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
