package edu.handong.csee.java.hw5;

import java.util.LinkedHashMap;

public class FileRunnableClass implements Runnable {
	private LinkedHashMap<String,LinkedHashMap<String, Integer>> finalValue;
	private CovidArrayList<String> list;
	
	public FileRunnableClass(LinkedHashMap<String,LinkedHashMap<String, Integer>> finalValue) {
		this.finalValue = finalValue;
	}
	
	public FileRunnableClass(CovidArrayList<String> list) {
		this.list = list;
	}
	
	@Override
	public void run() {
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Integer>> returnHashMap() {
		return finalValue;
	}
	
	
	public CovidArrayList<String> returnList() {
		return list;
	}

}
