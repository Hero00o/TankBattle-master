package com.tedu.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class MapObj extends ElementObj {
	
	//血量
	private int hp;
	//墙的type，  可以使用枚举
	private String name;

	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}
	
	@Override // 如果可以传入  强类型，x，y
	public ElementObj createElement(String str) {
		String[] arr = str.split(",");
//		先写个假图片
		ImageIcon icon = null;
		switch (arr[0]) {
		case "GRASS":
			icon = new ImageIcon("image/wall/grass.png");
			name = "GRASS";
			break;
		case "BRICK":
			icon = new ImageIcon("image/wall/brick.png");
			name = "BRICK";
			this.hp = 1;
			break;
		case "RIVER":
			icon = new ImageIcon("image/wall/river.png");
			name = "RIVER";
			break;
		case "IRON":
			icon = new ImageIcon("image/wall/iron.png");
			this.hp = 4;
			name = "IRON";
			break;
		}
		this.setX(Integer.parseInt(arr[1]));
		this.setY(Integer.parseInt(arr[2]));
		this.setW(icon.getIconWidth());
		this.setH(icon.getIconHeight());
		this.setIcon(icon);
		return this;
	}
	
	@Override
	public void setLive(boolean live) {
		//调用一次就减少一次血
		if("IRON".equals(name) || "BRICK".equals(name)) {//钢墙需要四下
			this.hp--;
			if(this.hp > 0) {
				return;
			}
		}
		super.setLive(live);
	}

}
