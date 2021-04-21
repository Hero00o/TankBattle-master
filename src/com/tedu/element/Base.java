package com.tedu.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.tedu.controller.GameThread;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.manager.MusicManager;
import com.tedu.show.GameJFrame;

/**
 * @说明 基地类，只需要进行显示，死亡、不需要移动以及其他。。
 * @author HP
 *
 */
public class Base extends ElementObj {

	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(),
				this.getW(), this.getH(), null);
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
	
	@Override //基地爆炸后应该直接结束游戏
	public void die() {
		this.setLive(false);
		GameLoad.loadObj();
		ElementObj element = GameLoad.getObj("base").createElement(this.baseToString()); 
		ElementManager.getManager().addElement(element, GameElement.DIE);
		MusicManager.gameOver();
		//弹出交互对话框
		GameJFrame.showDialog();
	}
	
	//获取当前位置、为创建基地爆炸做准备
	private String baseToString() {
		return this.getX() + "," + this.getY() + "," + "break_base";  
	}

}
