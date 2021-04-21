package com.tedu.element;

import java.awt.Graphics;
import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.manager.MusicManager;
import com.tedu.show.GameJFrame;

public class Play extends ElementObj /* implements Comparable<Play> */ {

	/**
	 * 移动属性: 
	 * 	1.单属性 配合 方向枚举类型使用；一次只能移动一个方向
	 *  2.双属性 上下 和 左右 配合boolean值使用
	 * 		例如：true代表上false为下 需要另一个变量来确定是否按下方向键 约定
	 * 		 0代表不动 1代表上 2代表下 
	 * 	3.四属性 上下左右都可以	boolean配合使用 true代表移动 false代表不移动
	 * 						 同时按上和下怎么办？后按的会重置先按的 
	 * 
	 * 说明：以上3中方式 是代码编写和判定方式 不一样 
	 * 说明：游戏中非常多的判定，建议灵活使用盘判定属性；很多状态值也使用判定属性
	 * 	 多状态 可以使用map<泛型, boolean>； set<判定对象> 判定对象中有时间
	 * 
	 * @问题 1.图片要读取到内存中：加载器    临时处理方式，手动编写到存储到内存中
	 * 		 2.什么时候进行修改图片（因为图片是在父类中的属性存储
	 * 		 3.图片应该使用什么集合进行存储
	 */
	private boolean left = false; // 左
	private boolean up = false; // 上
	private boolean right = false; // 右
	private boolean down = false; // 下
	public int tpye = 1;//子弹类型
	
	public int getTpye() {
		return tpye;
	}

	public void setTpye(int tpye) {
		this.tpye = tpye;
	}
	
	//专门记录当前主角面向的方向，默认为是 up
	private String fx = "up";
	//是否攻击状态 ，true攻击  false 停止
	private boolean pkType = false;
	//每次移动距离，相当于速度
	private int speed = 1;
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	//血量
	private int HP = 2;
	
	public void setHP(int hP) {
		HP = hP;
	}
	
	public Play(){}
	
	public Play(int x, int y, int w, int h, ImageIcon icon) {
		super(x, y, w, h, icon);
	}
	
	@Override
	public ElementObj createElement(String str) {
		String[] split = str.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon icon = GameLoad.imgMap.get(split[2]);
		this.setW(icon.getIconWidth());
		this.setH(icon.getIconHeight());
		this.setIcon(icon);
		return this;
	}

	/**
	 * 面向对象第一个思想：对象自己的事情自己做
	 */
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}	

	/**
	 * @说明 重写父类方法
	 * @重点 监听的数据需要改变状态值
	 */
	@Override
	public void keyClick(boolean bl, int key) { // 只有按下或者松开才会调用
//		System.out.println("测试：" + key);
		if (bl) {// 按下
			switch (key) {// 如何 优化：加监听会持续触发，有没有方法只触发一次
			case 65:
				this.up = false;
				this.down = false;
				this.right = false;
				this.left = true;
				this.fx = "left";
				break;
			case 87:				
				this.right = false;
				this.left = false;
				this.down = false;
				this.up = true;
				this.fx = "up";
				break;
			case 68:
				this.up = false;
				this.down = false;
				this.left = false;
				this.right = true;
				this.fx = "right";
				break;
			case 83:				
				this.right = false;
				this.left = false;
				this.up = false;
				this.down = true;
				this.fx = "down";
				break;
			case 32:
				this.pkType= true;//按下空格开启攻击状态
				break;
			}
		} else {
			switch (key) {
			case 65:
				this.left = false;
				break;
			case 87:
				this.up = false;
				break;
			case 68:
				this.right = false;
				break;
			case 83:
				this.down = false;
				break;
			case 32:
				this.pkType= false;//松开空格开启攻击状态
				break;
			}
		}
	}

	/**
	 * 
	 */
	@Override
	protected void move() {
		if (this.left && this.getX() > 0) {
			this.setX(this.getX() - speed);
		}
		if (this.up && this.getY() > 0) {
			this.setY(this.getY() - speed);
		}
		if (this.right && this.getX() < 776 + speed/*900 - this.getW()*/) {
			this.setX(this.getX() + speed);
		}
		if (this.down && this.getY() < 543 + speed/*600 - this.getH()*/) {
			this.setY(this.getY() + speed);
		}
	}
	
	@Override
	protected void updateImage(long gameTime) {
		this.setIcon(GameLoad.imgMap.get(fx));
	}
	
	//添加本地时间
	private long fileTime = 0;
//	fileTime 和传入的时间 gameTime 进行比较，赋值等操作运算，控制子弹间隔
	
	/**
	 * @题外问题：1.请问重写的方法的访问修饰符是否可以修改？
	 * 			  2.请问下面的add方法是否可以自动抛出异常？
	 * @重写规则：1.重写 方法的方法名称和返回值必须和父类的一样
	 * 			  2.重写方法的传入参数类型序列，必须和父类的一样
	 * 			  3.重写的方法访问修饰符  只能   比父类的更加宽泛
	 * 				比方说：父类的方法是受保护的，但是现在需要在非子类中调用
	 * 						可以直接子类继承，重写并super.父类方法。public方法
	 * 			  4.重写的方法抛出的异常	不可以比父类更加宽泛
	 * 子弹的添加  需要的是 发射这的坐标位置，发射者的方向  如果你可以变换子弹（思考，怎么处理？）
	 */
	@Override	//添加子弹
	protected void add(long gameTime) {
//		如果当前不是攻击状态，则直接返回
		if (!this.pkType) {
			return;
		}
		this.pkType = false;
//		new PlayFile(); //构造一个类  需要做的工作比较多   可以选择一种方式，使用小工厂
//		将构造对象的多个步骤进行封装成为一个方法，返回值直接是这个对象。
//		会帮助返回对象的实体，并初始化数据
//		传递一个固定格式 {x:3,y:5,f:up} json 格式
		ElementObj element = GameLoad.getObj("playfile").createElement(this.toString()); //以后的框架学习中会碰到
//		装入到集合中
		ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
//		如果要控制子弹速度等等。。。还需要代码编写
	}
	
	@Override
	public String toString() {
		int x = this.getX();
		int y = this.getY();
//		setTpye(2);
		int type = this.getTpye();	//子弹类型：1为基础子弹，2为穿甲弹，3为激光弹
//		System.out.println(type);
		switch(this.fx) {
		case "up": x+=13;y-=5;break;
		case "down": y+=30;x+=13;break;
		case "left": y+=13;x-=5;break;
		case "right": y+=13;x+=30;break;
		}
		//X 和 Y加上和坦克的相对位置偏移
//		return "x:"+(this.getX()+20)+",y:"+(this.getY()+20)+",f:"+this.fx;
		return "x:"+x+",y:"+y+",f:"+this.fx+",t:"+type;
	}
	
	/**
	 *
	 * @说明 	碰撞回弹方法，根据目前的方向，反方向回弹一个速度距离
	 */
	@Override
	public void Rebound() {
		if(this.left) {
			this.setX(this.getX() + this.speed);
		}
		if(this.up) {
			this.setY(this.getY() + this.speed);
		}
		if(this.right) {
			this.setX(this.getX() - this.speed);
		}
		if(this.down) {
			this.setY(this.getY() - this.speed);
		}
	}
	
	@Override
	public void setLive(boolean live) {
		this.HP--;
		if(this.HP > 0) {
			return;
		}
		super.setLive(live);
	}
	
	@Override
	public void die() {
		this.setLive(false);
		GameLoad.loadObj();
		ElementObj element = GameLoad.getObj("play").createElement(this.playToString()); 
		ElementManager.getManager().addElement(element, GameElement.DIE);
		MusicManager.gameOver();
		//弹出交互对话框
		GameJFrame.showDialog();
	}
	
	public String playToString() {
		return this.getX() + "," + this.getY() + "," + "boom";  
	}

}
