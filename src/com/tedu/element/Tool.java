package com.tedu.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;
/**	
 * @说明	工具类
 * 只存在位置这一属性，以及被捡前后的存活状态（被拾取后消失）
 * @author inback
 *
 */
public class Tool extends ElementObj{
	//道具的type，  可以使用枚举
	private String name;
	
	public String getName() {
		return name;
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}
	
	public Play buff(Play play) {
		if(this.getName()=="Armour_PiercingBullet") {
			//改变子弹为穿甲弹，顺改一下，这里我不会
			play.setTpye(2);
		}else if(this.getName()=="LaserBullet")	{
			//改子弹为激光弹，两种子弹图片都在image有
			//穿甲弹参数设置伤害加高，激光弹参数设置速度变快
			play.setTpye(3);
		}else if(this.getName()=="SpeedUp")	{
			play.setSpeed(2);
		}else if(this.getName()=="Star")	{
			play.setHP(4);
		}
		return play;
	}
	
	@Override // 如果可以传入  强类型，x，y
	public ElementObj createElement(String str) {
		String[] arr = str.split(",");
		ImageIcon icon = null;
		switch (arr[0]) {
		//穿甲弹，伤害提高
		case "Armour_PiercingBullet":
			icon = new ImageIcon("image/tool/01.png");
			name = "Armour_PiercingBullet";
			break;
		//激光弹，子弹速度变快
		case "LaserBullet":
			icon = new ImageIcon("image/tool/02.png");
			name = "LaserBullet";
			break;
		//加速，坦克速度变快
		case "SpeedUp":
			icon = new ImageIcon("image/tool/03.png");
			name = "SpeedUp";
			break;
		//加生命值
		case "Star":
			icon = new ImageIcon("image/tool/04.png");
			name = "Star";
			break;
		}		
		this.setX(Integer.parseInt(arr[1]));
		this.setY(Integer.parseInt(arr[2]));
		this.setW(icon.getIconWidth());
		this.setH(icon.getIconHeight());
		this.setIcon(icon);
		return this;
	}

}
