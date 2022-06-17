package edu.handong.csee.java.hw5;

import java.util.LinkedHashMap;

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
	
	public static LinkedHashMap<String,Integer> convertToHashMap(CovidArrayList<String[]> list,int length){
		try {
			LinkedHashMap<String, Integer> finalValue = new LinkedHashMap<String,Integer>();
			for(int j=0;j<list.length();j++) {
				String country = list.get(j)[1];
				Integer patientNumber = Util.makeInteger(list.get(j)[length-1]);
				Integer oldPatientNumber;
				if(finalValue.containsKey(country)) {
					oldPatientNumber = finalValue.get(country);
					patientNumber = patientNumber + oldPatientNumber;
					finalValue.put(country, patientNumber);
				} else {
					finalValue.put(country,patientNumber);
				}	
			}
			return finalValue;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
