package edu.handong.csee.java.hw5;

public class CSVRunnableClass implements Runnable {
	/*
	 * A class to read CSVCountryList using thread
	 */
	private String line[];
	private String stringLine;
	/*
	 * constructor receive String array
	 */
	public CSVRunnableClass(String line[]) {
		this.line = line;
	}
	/*
	 * make String array to String 
	 */
	@Override
	public void run() {
		stringLine = Util.appendString(line);
	}
	/*
	 * return String value
	 */
	public String returnString() {
		return stringLine;
	}

}
