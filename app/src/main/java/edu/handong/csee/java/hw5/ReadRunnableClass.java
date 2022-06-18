package edu.handong.csee.java.hw5;

public class ReadRunnableClass implements Runnable {
	
	private String line[];
	private String lineString;
	
	
	public ReadRunnableClass(String line[]) {
		this.line = line;
	}
	
	public ReadRunnableClass(String line) {
		this.lineString = line;
	}
	
	@Override
	public void run() {
	}
	
	public String[] returnStringArray() {
		return line;
	}
	
	public String returnString() {
		return lineString;
	}

}
