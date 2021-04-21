package com.tedu.controller;

import java.util.List;
import java.util.Map;


import com.tedu.element.ElementObj;
import com.tedu.element.Play;
import com.tedu.element.Tool;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.manager.MusicManager;

/**
 * @说明 游戏的主线程，用于控制游戏加载，游戏关卡，游戏运行自动化
 * 		游戏判定；游戏地图切换  资源释放和重新读取。。。
 * @author HP
 * @继承 使用继承的方式实现多线程（一般建议使用接口实现）
 */
public class GameThread extends Thread {
	
	private ElementManager em;
	//控制游戏是否结束
	public static boolean isOver = false;
	//gameRun()函数开关
	public static boolean flag = true;
	//控制是上一关还是下一关
	public static int increment = 0;
	//当前关卡
	public int gk = 1;
	public GameThread() {
		em = ElementManager.getManager();
	}
	
	@Override
	public void run() { // 游戏的run方法  主线程
		while(!isOver) { // 扩展，可以将true变为一个变量控制结束	
		//游戏开始前		读进度条，加载游戏资源（场景资源）
			gameLoad();
		//游戏进行时		游戏过程中
			gameRun();
		//游戏场景结束	游戏资源回收（场景资源）
			gameOver();
			
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *游戏的加载 
	 */
	private void gameLoad() {
		//加载地图
		GameLoad.MapLoad(gk);//可以为变量，每一关重新加载
		//加载初始界面
		GameLoad.loadImg();
		//加载主角
		GameLoad.loadPlay();//也可以带参数，单机还是2人
		//加载敌人、NPC等等
		GameLoad.loadEnemyData(gk);
		//加载基地
		GameLoad.loadBase();
		//加载音乐
		GameLoad.loadMusic();
		//全部加载完成，游戏启动
		
	}
	
//	private long gameTime = 0/*System.currentTimeMillis()*/;

	/**
	 * @说明 游戏进行时
	 * @任务说明 游戏过程中需要做的事情：1.自动化玩家的移动、碰撞、死亡
	 * 								   2.新元素的增加（NPC死亡后出现道具）
	 * 								   3.游戏暂停等等。。。
	 * 先实现主角的移动
	 */
	private void gameRun() {
		MusicManager.playGameStartMusic();
		long gameTime = 0L;
		while(flag) { // 预留扩展，true可以变为变量，用于控制管理关卡结束等
			GameLoad.loadEnemy(gameTime);
			Map<GameElement, List<ElementObj>> all = em.getGameElements();
			
			List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
			List<ElementObj> files = em.getElementsByKey(GameElement.PLAYFILE);
			List<ElementObj> enfiles = em.getElementsByKey(GameElement.ENEMYFILE);
			List<ElementObj> base = em.getElementsByKey(GameElement.BASE);
			List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
			List<ElementObj> tool = em.getElementsByKey(GameElement.TOOL);
			
			auto(all, gameTime);//游戏元素自动化方法
			//碰撞判定
			//子弹与基地
			elementPK(files, base);
			//敌人与基地
			elementPK(enemys, base);
			//敌人子弹与玩家
			elementPK(play, enfiles);
			//基地与敌人子弹
			elementPK(base, enfiles);
			//敌人与玩家子弹
			elementPK(files, enemys);
			//玩家子弹与敌人子弹
			elementPK(files, enfiles);
			//道具与玩家碰撞(拾取道具)
			PlayPKTOOL(play,tool);
			
			//敌人、玩家与地图  碰撞
			ElePKMap(enemys);
			ElePKMap(play);
			
			//玩家子弹、敌人子弹与地图碰撞
			filePKMap(files);
			filePKMap(enfiles);
			
			gameTime++;//唯一的时间控制

			try {
				sleep(10);// 默认理解为1秒刷新100次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void PlayPKTOOL(List<ElementObj> listA, List<ElementObj> listB) {
		for (int i = 0; i < listA.size(); i++) {
			ElementObj play = listA.get(i);
			for (int j = 0; j < listB.size(); j++) {
				ElementObj tool = listB.get(j);
				if (play.pk(tool)) {
					// 玩家获得buff，传入道具的属性判定哪种buff
					// 道具消失
					tool.buff(play);// 测试
					tool.setLive(false);
					break;
				}
			}
		}
	}
	
	//敌人和主角和地形碰撞的方法，允许通过GRASS方块，碰撞：回弹
		//listA是与地形碰撞的对象，3重循环，第1重是对所有类型墙体(4种)遍历，GRASS则continue
		//listA是第2重循环，碰撞即将listA强制转换为Enemy类调用回弹
	public void ElePKMap(List<ElementObj> listA) {	
		for(int i = 0; i < listA.size(); i++) {
			ElementObj enemy = listA.get(i);
			for(String key: em.getMapElement().keySet()) {
				if(key.equalsIgnoreCase("GRASS")) {continue;}
				List<ElementObj> listB = em.getMapElementByKey(key);	
				for(int j =0; j < listB.size(); j++) {
					ElementObj file = listB.get(j);
					if(enemy.pk(file)) {
						enemy.Rebound();
					}
				}
			}
		}	
	}
	
	//子弹和地图碰撞，子弹第一重循环
	public void filePKMap(List<ElementObj> listA) {
		// 使用循环，做一对一判定，如果为真，则设置2个对象的死亡状态
		for (int i = 0; i < listA.size(); i++) {
			ElementObj file = listA.get(i);
			for (String key : em.getMapElement().keySet()) {
				if (key.equalsIgnoreCase("RIVER")) {
					continue;
				}
				if (key.equalsIgnoreCase("GRASS")) {
					continue;
				}
				List<ElementObj> listB = em.getMapElementByKey(key);
				for (int j = 0; j < listB.size(); j++) {
					ElementObj map = listB.get(j);
					if (file.pk(map)) {
						// 应该将setLive()变为一个受攻击方法，还可以传入攻击力
						// 当受攻击方法执行时，如果血量减为0时进行设置生存为false
						file.setLive(false);
						map.setLive(false);
						break;
					}
				}
			}
		}
	}
	
	public void elementPK(List<ElementObj> listA, List<ElementObj> listB) {
		
//		使用循环，做一对一判定，如果为真，则设置2个对象的死亡状态
		for (int i = 0; i < listA.size(); i++) {
			ElementObj enemy = listA.get(i);
			for (int j = 0; j < listB.size(); j++) {
				ElementObj file = listB.get(j);
				if (enemy.pk(file)) {
//					应该将setLive()变为一个受攻击方法，还可以传入攻击力
//					当受攻击方法执行时，如果血量减为0时进行设置生存为false
					enemy.setLive(false);
					file.setLive(false);
					break;
				}
			}
		}
	}
	
	//游戏元素自动化方法
	public void auto(Map<GameElement, List<ElementObj>> all, long gameTime) {
		for(String key:em.getMapElement().keySet()) {
			List<ElementObj> list = em.getMapElementByKey(key); // 按定义枚举的顺序获取对应集合
			for(int i = list.size()-1; i >= 0; i--) {
				ElementObj obj =list.get(i); // 读取为基类
			    if (!obj.isLive()) { //如果死亡
//			     启动一个死亡方法(方法中可以做很多事情，如：死亡动画，掉装备)
			    	obj.die();//需要自己补充
			    	list.remove(i);
			    	continue;
			    }
			}
		}
		
//		GameElement.values(); // 隐藏方法 返回值是一个数组， 数组的顺序就是定义枚举的顺序
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge); // 按定义枚举的顺序获取对应集合
			//编写直接操作集合数据的代码不要使用迭代器
			for(int i = list.size() - 1; i >= 0; i--) {
				ElementObj obj = list.get(i); // 读取为基类
				if (!obj.isLive()) { //如果死亡
//					启动一个死亡方法(方法中可以做很多事情，如：死亡动画，掉装备)
					obj.die();//需要自己补充
					list.remove(i);
					continue;
				}
				obj.model(gameTime);//调用模板方法
			}
		}
	}

	/**
	 * 游戏切换场景
	 */
	private void gameOver() {
		//如果游戏结束（指输了或者赢了）
		if (isOver) {
			//执行对应的代码（弹框交互）
			System.out.println("游戏结束");
			return;
		}
		//清空并重新初始化元素管理器
		em.reInit();
		//修改关卡
		changeGk();	
		//重新进行加载
		gameLoad();
		//gameRun()函数的开关
		flag = true;
		gameRun();
		gameOver();
	}
	
	/**
	 * @说明 切换关卡函数，通过变量控制是上一关或是下一关
	 */
	private void changeGk() {
		this.gk = this.gk + increment;
		if (this.gk <= 0) {
			this.gk = 1;
		} 
		if (this.gk > 10) {
			this.gk = 10;
		}
		increment = 0;
	}
	
}
