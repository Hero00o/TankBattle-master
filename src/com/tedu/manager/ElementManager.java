package com.tedu.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tedu.element.ElementObj;

/**
 * @说明 本类是元素管理器，专门存储所有的元素，同时，提供方法
 * 		给予视图和控制获取数据
 * @author HP
 * 
 * @问题一： 存储所有元素数据，怎么存放？list、map、set
 * @问题二： 管理器是视图和控制要访问，管理器就必须只有一个，单例模式
 */
public class ElementManager {

	/*
	 * String 作为key匹配所有的元素 play -> List<Object> listPlay
	 * 							  enemy -> List<Object> listEnemy
	 * 枚举类型 当做map的key用来区分不一样的资源，用于获取资源
	 * List中元素的泛型 应该是 元素 基类
	 * 所有元素都可以存放到map集合中，显示模块只需要获取到 这个map就可以
	 * 显示时有的界面需要显示的元素(调用元素基类的showElement())
	 */
	private Map<GameElement, List<ElementObj>> gameElements;
	
	private Map<String, List<ElementObj>> MapElements;	//Map专门存储<墙体类型，墙体对象>
	
	//获取地图管理器
	public Map<String, List<ElementObj>> getMapElement() {
		return MapElements;
	}
	
	//根据key获取地图
	public List<ElementObj> getMapElementByKey(String str) {
		return MapElements.get(str);
	}
	
	//添加  地图  元素（多半由加载器调用）
	public void addMapElement(ElementObj obj, String str) {
		MapElements.get(str).add(obj);//添加对象到集合中，		
	}
	
	//本方法一定不够用
	/**
	 * 获取所有元素
	 * @return Map
	 */
	public Map<GameElement, List<ElementObj>> getGameElements() {
		return gameElements;
	}
	
	//添加元素（多半由加载器调用）
	public void addElement(ElementObj obj, GameElement ge) {
		//添加对象到集合中，按key值就行存储
		gameElements.get(ge).add(obj);
	}
	
	//依据key返回list集合，取出某一类元素
	public List<ElementObj> getElementsByKey(GameElement ge) {
		return gameElements.get(ge);
	}
	
	/**
	 * 单例模式：内存中有且只有一个实例
	 * 饿汉模式：启动就自动加载实例
	 * 饱汉模式：需要使用的时候才加载实例
	 * 
	 * 编写方式：
	 * 1.需要一个静态的属性 (常量) 单例的引用
	 * 2.提供一个静态方法（返回此实例）return 单例的引用
	 * 3.一般为防止其他人自己使用（类是可以实例化）,所以会私有化构造方法
	 * 		ElementManager em = new ElementManager();
	 */
	private static ElementManager EM = null; //引用
	
	//synchronized 线程锁 -> 保证本方法执行中只有一个线程
	public static synchronized ElementManager getManager() {
		if (EM == null) {//空值判定
			EM = new ElementManager();
		}		
		return EM;
	}
	
	private ElementManager(){//私有化构造方法
		init(); //实例化方法
	}
	
/*	static { // 饿汉实例化对象 //静态语句块是在类被加载的时候直接执行
		EM = new ElementManager();//只会执行一次
	}*/
	
	/**
	 * 实例化对象
	 * 本方法是为了将来可能出现的功能扩展，重写 init 方法准备的
	 */
	public void init() {
		// HashMap hash散列
		gameElements = new HashMap<GameElement, List<ElementObj>>();
		//将每种元素集合都放入到map中
		for (GameElement ge: GameElement.values()) { //通过循环读取枚举类型的方式添加集合
			gameElements.put(ge, new ArrayList<ElementObj>());
		}
		//初始化MapElement，使其拥有4个key，值为空ArrayList<ElementObj>
		MapElements = new HashMap<String, List<ElementObj>>();
		for(int i = 0; i< 4; i++) {
			MapElements.put("BRICK", new ArrayList<ElementObj>());
			MapElements.put("GRASS", new ArrayList<ElementObj>());
			MapElements.put("RIVER", new ArrayList<ElementObj>());
			MapElements.put("IRON", new ArrayList<ElementObj>());
		}
		//可增加道具、子弹、爆炸效果、死亡效果……
	}
	
	/**
	 * @说明 该方法用于清空元素管理器中所有的元素，并重新初始化
	 */
	public void reInit() {
		getGameElements().clear();
		init();
	}
	
}
