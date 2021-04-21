package com.tedu.manager;

import com.tedu.element.Music;

/**
 * @说明 音乐管理器
 * @author HP
 * @方法 提供播放各种音乐方法，包括游戏开始音乐、子弹射击音效、爆炸音效 
 */
public class MusicManager {
	
//	private static MusicManager MM = null;
//	
//	public static synchronized MusicManager getManager() {
//		if (MM == null) {//空值判定
//			MM = new MusicManager();
//		}		
//		return MM;
//	}
	
	/**
	 * @说明 游戏初始的背景音
	 */
	public static void playGameStartMusic() {
		new Music("gameStartBGM").play();
	}
	
	/**
	 * @说明 主角玩家射中敌人/boss 的音效
	 */
	public static void playFireMusic() {
		new Music("fireSound").play();
	}
	
	/**
	 * @说明 玩家被打爆的音效
	 */
	public static void  playBoom() {
		new Music("playBoom").play();
	}
	
	/**
	 * @说明 游戏失败结束的音效
	 */
	public static void gameOver() {
		new Music("gameOver").play();
	}

}
