package com.tedu.element;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.tedu.manager.GameLoad;

/**
 * @说明 音乐类，创建时必须传入对应音乐名字
 * @author HP
 * @方法 提供创建、音乐播放、循环播放、停止播放方法
 *
 */
public class Music {

	private String name;
	private AudioClip aau;
	
	public Music(String name){
		this.name = name;
		init();
	}	

	private void init() {
		File file = GameLoad.musicMap.get(name);
		URI uri = file.toURI();
		try {
			URL url = uri.toURL();
			aau = Applet.newAudioClip(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @说明 音乐开始播放
	 */
	public void play() {
		this.aau.play();
	}
	
	/**
	 * @说明 音乐开始循环播放
	 */
	public void loop() {
		this.aau.loop();
	}
	
	/**
	 * @说明 音乐停止播放
	 */
	public void stop() {
		this.aau.stop();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AudioClip getAau() {
		return aau;
	}		
}
