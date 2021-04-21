package com.tedu.manager;

public enum GameElement {
	/**
	 * PLAY 玩家
	 * MAPS 地图
	 * ENEMY 敌人
	 * BOSS boss
	 * PLAYFILE 子弹
	 * DIE  死亡元素集合
	 * BASE 基地
	 * TOOL 道具
	 * ...
	 */
	MAPS, PLAY, ENEMY, BOSS, PLAYFILE, ENEMYFILE, DIE, BASE, TOOL; //枚举的遍历顺序是定义顺序
	//我们定义的枚举类型，在编译的时候，虚拟机（JDK）会自动生成class文件，并且
	//加载很多的代码和方法
	//比如构造方法...
}
