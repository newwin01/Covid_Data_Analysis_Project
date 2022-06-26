package edu.handong.csee.java.hw5;

import java.util.LinkedHashMap;
/*
 * class implements runnable interface
 * to use thread
 */
public class FileRunnableClass implements Runnable {
	private LinkedHashMap<String,LinkedHashMap<String, Integer>> finalValue;
	private CovidArrayList<String> list;
	/*
	 * receive linked hashmap that has String key and linked hashmap value that has country data
	 */
	public FileRunnableClass(LinkedHashMap<String,LinkedHashMap<String, Integer>> finalValue) {
		this.finalValue = finalValue;
	}
	/*
	 * receive covid arraylist
	 */
	public FileRunnableClass(CovidArrayList<String> list) {
		this.list = list;
	}
	
	@Override
	public void run() {
	}
	/*
	 * return the linked hashmap value
	 */
	public LinkedHashMap<String, LinkedHashMap<String, Integer>> returnHashMap() {
		return finalValue;
	}
	/*
	 * return covidarraylist
	 */
	public CovidArrayList<String> returnList() {
		return list;
	}

}
