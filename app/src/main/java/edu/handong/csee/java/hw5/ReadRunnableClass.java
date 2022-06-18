package edu.handong.csee.java.hw5;

public class ReadRunnableClass implements Runnable {
	
	private String line[];
	private String lineString;
	
	//constructor for thread that receive string array
	public ReadRunnableClass(String line[]) {
		this.line = line;
	}
	//constructor for thread that receive string
	public ReadRunnableClass(String line) {
		this.lineString = line;
	}
	//run method overrides runnable interface, can check thread works correctly
	@Override
	public void run() {
	}
	// return String Array
	public String[] returnStringArray() {
		return line;
	}
	// return String
	public String returnString() {
		return lineString;
	}

}
