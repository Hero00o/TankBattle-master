package com.tedu.element;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * 所有元素的基类
 * @author HP
 *
 */
public abstract class ElementObj {

	private int x;
	private int y;
	private int w;
	private int h;
	private ImageIcon icon;
	private boolean live = true; //生存状态 true代表生存，false代表死亡
						  //可以采用枚举值来定义这个（生存、死亡、隐身、无敌 ）
//	还有其他各种必要状态。。
//	注明：当重新定义一个用于判定状态的变量，需要思考：1、初始化  2、值的改变  3、值的判定
	public ElementObj() {
		
	}
	
	/**
	 * 带参数的构造方法； 可以由子类传输数据到父类
	 * @param x		左上角X坐标
	 * @param y 	左上角Y坐标
	 * @param w 	w宽度
	 * @param h 	h高度
	 * @param icon 	图片
	 */
	public ElementObj(int x, int y, int w, int h, ImageIcon icon) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.icon = icon;
	}
	
	/**
	 * @说明 抽象方法，显示元素
	 * @param g 画笔 用于进行绘画
	 */
	public abstract void showElement(Graphics g);
	
	/**
	 * @说明 使用父类定义接收键盘事件的方法
	 * 		只有需要实现键盘监听的子类，重写这个方法（约定）
	 * @说明 方式2使用接口的方式；使用接口方式需要在监听类进行类型转换
	 * @题外话 约定 配置 现在大部分的Java框架都是需要进行配置的
	 * 		  	约定优于配置
	 * @param bl 点击的类型，true代表按下，false代表松开
	 * @param key 代表触发的键盘的code值
	 * @扩展 本方法是否可以分为两个方法？1个接收按下、1个接收松开(扩展使用)
	 */
	public void keyClick(boolean bl, int key) {	// 此方法不是强制必须重写。
		
	}
	
	/**
	 * @说明 移动方法；需要移动的子类，请重写这个方法
	 */
	protected void move() {
		
	}
	
	/**
	 * @说明 碰撞回弹方法，子类需要的重写
	 */
	public void Rebound() {}
	
	/**
	 * @设计模式 模板模式；在模板模式汇总定义对象执行方法的先后顺序，由子类选择性重写方法
	 * 			1.移动	2.换装	3.子弹发射 。。。
	 * @param gameTime 
	 */
	public final void model(long gameTime) {
		//先换装
		updateImage(gameTime);
		//再移动
		move();
		//再发射子弹
		add(gameTime);
	}
	
	/**
	 * @说明 换装方法
	 */
	protected void updateImage(long gameTime) {}
	
	/**
	 * @说明 添加子弹（元素）方法
	 * @param gameTime
	 */
	protected void add(long gameTime) {}
	
	//死亡方法	给子类继承的	死亡也是一个对象
	public void die(){}
	
//	对创建这个对象的过程进行封装，外界只需要传输必要的约定参数，返回值就是对象实体
	public ElementObj createElement(String tr) {
		
		return null;
	}
	
	/**
	 * @说明 本方法返回元素的碰撞矩形对象（实时返回）
	 * @return
	 */
	public Rectangle getRectangle() {
//		可以将这个 数据进行处理--
		return new Rectangle(x, y, w, h);
	}
	
	/**
	 * @说明 碰撞方法
	 * 一个是this对象，一个是传入值 obj
	 * @param 元素基类ElementObj对象 obj
	 * @return boolean  返回true说明有碰撞 ， 返回false说明没有碰撞
	 */
	public boolean pk(ElementObj obj) {		
		return this.getRectangle().intersects(obj.getRectangle());
	}
	
	/**
	 * 
	 * @param elementObj
	 * @return
	 */
	public ElementObj buff(ElementObj elementObj) {
		return elementObj;
	}

	/*
	 * 只要会VO类 就要为属性生成get和set方法 
	 */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}	
		
}
