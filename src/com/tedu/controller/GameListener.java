package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @说明 监听类，用于监听用户的操作
 * @author HP
 *
 */
public class GameListener implements KeyListener {
	
	private ElementManager em = ElementManager.getManager();

	/*能否通过一个集合来记录所有按下的键，如果重复触发，就直接结束
	 * 同时，第一次按下就记录在集合中，第二次判定集合中是否有。
	 * 	松开就直接删除集合中的记录
	 * set集合
	 * 
	 */
	private Set<Integer> set = new HashSet<Integer>();
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	/**
	 * 按下监听: 左37  上38  右39  下40 按tab无反应
	 * 实现主角的移动
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(set.contains(e.getKeyCode())) { // 如果集合中已存在此对象，直接结束
			return;
		}
		//如果集合中不存在，就加入
		set.add(e.getKeyCode());
		
		//拿到玩家集合
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for (ElementObj obj : play) {
			obj.keyClick(true, e.getKeyCode());
		}
	}
	
	/**
	 * 松开监听
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if(!set.contains(e.getKeyCode())) { // 如果集合中没有此对象，直接返回
			return;
		}
		//移除数据
		set.remove(e.getKeyCode());
		
		//拿到玩家集合
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for (ElementObj obj : play) {
			obj.keyClick(false, e.getKeyCode());
		}
	}

	
	
}
