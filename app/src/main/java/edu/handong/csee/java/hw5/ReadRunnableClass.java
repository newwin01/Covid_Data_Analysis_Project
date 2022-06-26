package edu.handong.csee.java.hw5;
/*
 * class implements runnable interface
 * to use thread
 */
public class ReadRunnableClass implements Runnable {
	
	private String line[];
	private String lineString;
	
	/*
	 * constructor for thread that receive string array
	 */
	public ReadRunnableClass(String line[]) {
		this.line = line;
	}
	/*
	 * constructor for thread that receive string
	 */
	public ReadRunnableClass(String line) {
		this.lineString = line;
	}
	/*
	 * run method overrides runnable interface, can check thread works correctly
	 */
	@Override
	public void run() {
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
	public String returnString() {
		return lineString;
	}

}
