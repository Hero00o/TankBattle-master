package com.tedu.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.tedu.manager.GameLoad;

public class Boom extends ElementObj{
	
	private int deadTime = 0;
	

	//空构造函数
	public Boom() {}
	
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
		
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

	
	/**
	 * @说明 move方法用以获取时间，目前1秒20帧,1帧50ms
	 * 		爆炸1s后消失
	 */
	@Override
	protected void move() {
		deadTime++;
		if(deadTime == 20) {
			this.setLive(false);
		}
	}
	
	

}
