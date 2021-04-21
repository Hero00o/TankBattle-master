package com.tedu.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.ImageIcon;

import com.tedu.element.ElementObj;

/**
 * @说明 加载器（工具：用于读取配置文件的工具）工具类，大多提供的是static方法
 * @author HP
 *
 */
public class GameLoad {
	
	//获取资源管理器
	private static ElementManager em = ElementManager.getManager();

	//玩家图片集合	使用map来进行存储	枚举类型配合移动（扩展）
	public static Map<String, ImageIcon> imgMap = new HashMap<>();
	//敌人图片集合	使用map来进行存储	枚举类型配合移动（扩展）
	public static Map<String, ImageIcon> enemyImgMap;
	//GameLoad在gameLoad()的时候提取配置文件中的数据
	public static Map<String, String> EnemyMap = new HashMap<>();
	//初始界面图片集合 使用map来进行存储
	public static Map<String, ImageIcon> loadGameImgMap = new HashMap<>();
	//游戏音乐集合
	public static Map<String, File> musicMap = new HashMap<>();
	//基地集合
	public static Map<String, ImageIcon> baseMap = new HashMap<>();
	
	
	//用户读取文件的类
	private static Properties pro = new Properties();
	
	/**
	 * @说明 传入地图id有加载方法依据文件规则自动生成地图文件名称，加载文件
	 * @param mapId 文件编号 文件Id
	 */
	public static void MapLoad(int mapId) {
		// 首先加载Obj
		loadObj();
		//得到文件路径
		String mapName = "com/tedu/text/"+mapId+".map";
		//使用IO流来获取文件对象      得到类加载器		
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream maps = classLoader.getResourceAsStream(mapName);
		if (maps == null) {
			System.out.println("配置文件读取异常！");
			return;
		}
		pro.clear();
		try {
			//以后都是xml和json
			pro.load(maps);
			//可以动态获取所有key，有key就可以获取value
			Enumeration<?> names = pro.propertyNames();
			while(names.hasMoreElements()) {
				//获取key
				String key = names.nextElement().toString();
				//根据key取values
				//字符串分割，并加入对应墙的类型
				String[] arrs = pro.getProperty(key).split(";");
				for (int i = 0; i < arrs.length; i++) {
					ElementObj obj = getObj("map");
					ElementObj element = obj.createElement(key + "," + arrs[i]);
					em.addElement(element, GameElement.MAPS);
					em.addMapElement(element, key);	//将地图对象加入MapElement
				}				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @说明 加载图片函数
	 * 加载图片  代码和图片之间差一个路径
	 */
	public static void loadImg() {//可以带参数，因为不同关卡也可能需要不一样的图片资源
		String textUrl = "com/tedu/text/GameData.pro";//文件的命名可以更加有规律
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream texts = classLoader.getResourceAsStream(textUrl);
//		imgMap用于存放数据
		pro.clear();
		try {
			pro.load(texts);
			Set<Object> set = pro.keySet();
			for (Object obj : set) {
				String url = pro.getProperty(obj.toString());
				imgMap.put(obj.toString(), new ImageIcon(url));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @说明 加载玩家函数
	 */
	public static void loadPlay() {
		// 首先加载Obj
		loadObj();
		String playStr = "425,525,up";//应在配置文件
		ElementObj obj = getObj("play");		
		ElementObj play = obj.createElement(playStr);
//		 解耦，降低代码和代码之间的耦合
		em.addElement(play, GameElement.PLAY);
	}
	
	/**
	 * @说明 加载基地函数
	 */
	public static void loadBase() {
		//首先加载obj
		loadObj();
		String baseStr = "360,525,base";
		ElementObj obj = getObj("base");
		ElementObj base = obj.createElement(baseStr);
		em.addElement(base, GameElement.BASE);
	}
	
	/**
	 * @说明 加载enemy数据
	 * 
	 */
	public static void loadEnemyData(int gk) {	//可带参数，不同关卡可能需要不同图片
		String texturl = "com/tedu/text/EnemyData/EnemyData" + Integer.toString(gk) + ".pro";
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream texts = classLoader.getResourceAsStream(texturl);
		pro.clear();
		try {
			pro.load(texts);
			Set<Object> set = pro.keySet();
			for(Object o : set) {
				String data = pro.getProperty(o.toString());
				EnemyMap.put(o.toString(), data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadEnemy(long gameTime) {
		loadObj();
//		String enemyStr1 = "300,300,bot_up";//应在配置文件	
		for(String key: GameLoad.EnemyMap.keySet()) {
			if(Long.parseLong(key) == gameTime) {
				String enemyStr= GameLoad.EnemyMap.get(key);
				ElementObj obj = getObj(enemyStr.split(",")[3]);
				ElementObj enemy = obj.createElement(enemyStr);
//				 解耦，降低代码和代码之间的耦合
				em.addElement(enemy, GameElement.ENEMY);
			}
		}
	}
	
	/**
	 * 根据key创建元素实例对象
	 * @param str 配置文件中对应的key
	 * @return 返回一个元素对象实例
	 */
	public static ElementObj getObj(String str) {
//		loadObj();
		Class<?> class1 = objMap.get(str);		
		try {
			Object newInstance = class1.newInstance();
			if (newInstance instanceof ElementObj) {
				return (ElementObj)newInstance; //这个对象和new Play()等价
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//用于存放元素
	private static Map<String, Class<?>> objMap = new HashMap<>();
	
	/**
	 * 扩展：使用配置文件，来实例化对象  通过固定的key（字符串来实例化）
	 */
	public static void loadObj() {
		String textUrl = "com/tedu/text/obj.pro";//文件的命名可以更加有规律
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream texts = classLoader.getResourceAsStream(textUrl);
		pro.clear();
		try {
			pro.load(texts);
			Set<Object> set = pro.keySet();
			for (Object obj : set) {
				String classUrl = pro.getProperty(obj.toString());
//				使用反射的方式直接将类进行获取
				Class<?> forName = Class.forName(classUrl);
				objMap.put(obj.toString(), forName);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @说明 此函数用于加载游戏初始页面所需要的图片
	 * 		比如：背景图片、点击按钮图片...
	 */
	public static void loadGameImg() {
		String loadGameProUrl = "com/tedu/text/loadGamePro.pro";//文件的命名可以更加有规律
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream texts = classLoader.getResourceAsStream(loadGameProUrl);
		pro.clear();
		try {
			pro.load(texts);
			Set<Object> set = pro.keySet();
			for (Object obj : set) {
				String url = pro.getProperty(obj.toString());
				loadGameImgMap.put(obj.toString(), new ImageIcon(url));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @说明 此函数用于加载游戏所需要的音乐
	 */
	public static void loadMusic() {
		String loadMusicUrl = "com/tedu/text/music.pro";
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream texts = classLoader.getResourceAsStream(loadMusicUrl);
		pro.clear();
		try {
			pro.load(texts);
			Set<Object> set = pro.keySet();
			for (Object obj : set) {
				String url = pro.getProperty(obj.toString());
				musicMap.put(obj.toString(), new File(url));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
