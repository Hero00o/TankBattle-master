package com.tedu.element;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

/**	
 * @说明	boss类
 * 移动，开火，生命值，承受攻击
 * @author inback
 *
 */
public class Boss extends Enemy{
	
	private int HP = 5;	//生命值
	
	private int speed = 2;	//速度
	
	//空构造函数
	public Boss() {}
	
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
		this.setIcon(GameLoad.imgMap.get("boss_"+fx));
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
	
	@Override
	protected String BoomString() {
		return super.BoomString();
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
	
	@Override
	protected void RandomFx() {
		super.RandomFx();
	}
	
	/**
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
			this.setX(this.getX() - speed);
		}
		if(this.down) {
			this.setY(this.getY() - this.speed);
		}
	}
	
	@Override
	public void hurt() {
		super.hurt();
	}
	
	@Override
	protected void add(long gameTime) {
		super.add(gameTime);
	}
	
	@Override
	protected void randomShoot(int x1, int x2) {
		super.randomShoot(50, 20);
	}
	
	@Override
	public String toString() {
		int x = this.getX();
		int y = this.getY();
		int l = 3;
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
