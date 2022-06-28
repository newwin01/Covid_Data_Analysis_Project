package edu.handong.csee.java.hw5;
/*
 * class implements runnable interface
 * to use thread
 */
public class ReadRunnableClass implements Runnable {
	
	private String line[];
	private Integer value;
	private String key;
	
	/*
	 * constructor for thread that receive string array
	 */
	public ReadRunnableClass(String line[]) {
		this.line = line;
	}
	/*
	 * run method overrides runnable interface
	 * get key and value for hashamp using thread
	 */
	@Override
	public void run() {
		value = Util.makeInteger(line[line.length-1]);
		key = line[1];
	}
	/*
	 *  return its String Array
	 */
	public String[] returnStringArray() {
		return line;
	}
	/*
	 * return value for hashmap
	 */
	public Integer returnInteger() {
		return value;
	}
	/*
	 * return Key value for hashmap
	 */
	public String returnKey() {
		return key;
	}

}
