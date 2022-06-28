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
	 * run method overrides runnable interface, can check thread works correctly
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
	 * return its String
	 */
	public Integer returnInteger() {
		return value;
	}
	public String returnKey() {
		return key;
	}

}
