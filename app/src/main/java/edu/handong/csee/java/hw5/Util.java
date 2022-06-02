package edu.handong.csee.java.hw5;

public class Util {
	/*
	 * class make string to integer
	 */
	 public static int makeInteger(String data) {
		 int numberToData;
		 numberToData=Integer.parseInt(data);
		return numberToData;
	 }
	 /*
	  * get field number that includes field number
	  */
	public static int getInclusiveFieldNum(String date,String[] fieldInformation) {
			int fieldNum=0;
			for(int i=0;i<fieldInformation.length;i++) {
				if(date.equals(fieldInformation[i])) {
					fieldNum=i;
					break;
				}
			}
			return fieldNum;
		}
	/*
	 * get field number that excludes field number
	 */
	public static int getExclusiveFieldNum(String date,String[] fieldInformation) {
		int fieldNum=0;
		for(int i=0;i<fieldInformation.length;i++) {
			if(date.equals(fieldInformation[i])) {
				fieldNum=i;
				break;
			}
		}
		return fieldNum-1;
	}
}
