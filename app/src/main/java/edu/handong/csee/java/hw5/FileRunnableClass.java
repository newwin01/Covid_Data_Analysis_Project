package edu.handong.csee.java.hw5;

import java.util.LinkedHashMap;
/*
 * class implements runnable interface
 * to use thread
 */
public class FileRunnableClass implements Runnable {
	private String type;
	private String data;
	private LinkedHashMap<String,LinkedHashMap<String, Integer>> finalValue;
	private CovidArrayList<String> list;
	/*
	 * receive linked hashmap that has String key and linked hashmap value that has country data
	 */
	public FileRunnableClass(String type, String data) {
		this.data = data;
		this.type = type;
	}
	
	/*
	 * receive covid arraylist
	 */
	public FileRunnableClass(CovidArrayList<String> list) {
		this.list = list;
	}
	/*
	 * distinguish type of input file, 
	 * read file and get appropriate value 
	 */
	@Override
	public void run() {
		if(!type.equals("country")) {
			finalValue = new LinkedHashMap<String,LinkedHashMap<String, Integer>>();
			LinkedHashMap<String, Integer> tempValue;
			String exe = data.substring(data.lastIndexOf(".")+1);
			if(exe.equals("zip")) {
				tempValue = ReadFile.readZipFile(data);
				finalValue.put(type, tempValue);
			}
			else if(exe.equals("csv")) {
				tempValue = ReadFile.readCSVFile(data);
				finalValue.put(type, tempValue);
			}
		} else {
			list = ReadFile.readCSVCountryFile(data);
		}
	}
	/*
	 * return the linked hash map with data and value
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
