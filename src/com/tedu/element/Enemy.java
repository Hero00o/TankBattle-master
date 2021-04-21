package com.tedu.element;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

/**	
 * @说明	敌人类
 * 移动，开火，生命值，承受攻击
 * @author inback
 *
 */
public class Enemy extends ElementObj{

	protected boolean left = false; // 左
	protected boolean up = false; // 上
	protected boolean right = false; // 右
	protected boolean down = false; // 下
	
	protected int HP = 1;	//生命值
	
	protected int speed = 1;	//速度
	
	/**
	 * @fx:专门记录当前敌人面向的方向，默认为是 up
	 * @DirFlag	随机获取应该切换方向的时刻的数值；
	 * @DirFlagNum	随函数调用次数自增字段，用以确定已经过去的时间
	 */
	protected String fx = "up";
	protected int DirFlag = 0;
	protected int DirFlagNum = 0;
	
	//是否攻击状态 ，true攻击  false 停止	
	protected boolean pkType = false;
	
	protected int pkRestNum = 0;
	protected int pkRest = new Random().nextInt(31)+10;
	
	//空构造函数
	public Enemy() {}
	
	//存活判定
	@Override
	public boolean isLive() {
		return super.isLive();
	}
	
	/**
	 * @说明 解析获取的字符串，新建对象
	 * 坐标X，坐标Y，图片（配置文件GameData的key值）
	 */
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
	
	//换装函数，仅负责了自动注入”由方向决定的面朝图片“
	@Override
	protected void updateImage(long gameTime) {
//		if(!this.isLive()) {return;}	//如果死亡，不再自动注入”由方向决定的面朝图片“
		this.setIcon(GameLoad.imgMap.get("bot_"+fx));
	}
	
	//绘图函数
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
	}
	
	@Override
	public void setLive(boolean live) {
		this.HP--;
		if(this.HP > 0) {
			return;
		}
		super.setLive(live);
	}
	
	/**
	 * @说明 死亡函数，将图片切换为爆炸
	 */
	@Override
	public void die() {
		this.setLive(false);
//		this.setIcon(GameLoad.imgMap.get("boom"));
//		ElementManager.getManager().addElement(this, GameElement.DIE);
		GameLoad.loadObj();
		ElementObj element = GameLoad.getObj("boom").createElement(this.BoomString()); 
		ElementManager.getManager().addElement(element, GameElement.DIE);
	}
	
	protected String BoomString() {
		return this.getX() + "," + this.getY() + "," + "boom";  
	}
	
	/**
	 * @说明	随机移动函数
	 * 尝试每0.5s~2s切换方向，目前50ms刷新一次，刷新率为20帧
	 * 即每10~40次函数调用改变一次方向
	 */
	@Override
	protected void move() {
//		if(!this.isLive()) {return;}	//如果死亡，不允许移动，直接return
		if(this.DirFlagNum == this.DirFlag) {
			RandomFx();
		}
		this.DirFlagNum++;
		if (this.left && this.getX() > 0) {
			this.setX(this.getX() - speed);
		}
		if (this.up && this.getY() > 0) {
			this.setY(this.getY() - this.speed);
		}
		if (this.right && this.getX() < 776 + this.speed/*900 - this.getW()*/) {
			this.setX(this.getX() + this.speed);
		}
		if (this.down && this.getY() < 543 + this.speed/*600 - this.getH()*/) {
			this.setY(this.getY() + this.speed);
		}
	}
	
	protected void RandomFx(){
		int x = 0;
		Random r = new Random();
		x = r.nextInt(4);
		switch(x) {
		case 0:
			this.up = false;
			this.down = false;
			this.right = false;
			this.left = true;
			this.fx = "left";
			break;
		case 1:
			this.right = false;
			this.left = false;
			this.down = false;
			this.up = true;
			this.fx = "up";
			break;
		case 2:
			this.up = false;
			this.down = false;
			this.left = false;
			this.right = true;
			this.fx = "right";
			break;
		case 3:
			this.right = false;
			this.left = false;
			this.up = false;
			this.down = true;
			this.fx = "down";
			break;
		}
		this.DirFlagNum = 0;
		this.DirFlag = new Random().nextInt(31)+10;
	}
	
	/**
	 * @说明 	碰撞回弹方法，根据目前的方向，反方向回弹一个速度距离
	 */
	public void Rebound() {
		if(this.left) {
			this.setX(this.getX() + this.speed);
		}
		if(this.up) {
			this.setY(this.getY() + this.speed);
		}
		if(this.right) {
			this.setX(this.getX() - speed);
		}
		if(this.down) {
			this.setY(this.getY() - this.speed);
		}
	}
	
	public void hurt() {
		this.HP--;
		if(this.HP == 0) {
			this.die();
		}
	}
	
	@Override
	protected void add(long gameTime) {
		randomShoot(100,20);
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
		ElementManager.getManager().addElement(element, GameElement.ENEMYFILE);
	}
	
	/**
	 * @说明 随机发射函数
	 * 
	 */
	protected void randomShoot(int x1, int x2) {
		if(this.pkRestNum == this.pkRest) {
			this.pkType = true;
			this.pkRestNum = 0;
			this.pkRest = new Random().nextInt(x1+1)+x2;//两个参数，前者是随机发射，后者每次发射必须经过的间隔
		}
		this.pkRestNum++;
	}
	
	@Override
	public String toString() {
		int x = this.getX();
		int y = this.getY();
		int l = 1;
		switch(this.fx) {
		case "up": x+=13;y-=5;break;
		case "down": y+=30;x+=13;break;
		case "left": y+=13;x-=5;break;
		case "right": y+=13;x+=30;break;
		}
		//X 和 Y加上和坦克的相对位置偏移
//		return "x:"+(this.getX()+20)+",y:"+(this.getY()+20)+",f:"+this.fx;
		return "x:"+x+",y:"+y+",f:"+this.fx+",l"+l;
	}

}
