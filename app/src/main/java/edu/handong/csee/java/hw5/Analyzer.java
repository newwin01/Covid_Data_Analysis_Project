package edu.handong.csee.java.hw5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
/*
 *
 *
*/
import java.util.Map;
import java.util.Map.Entry;

public class Analyzer {
	private String[][] numberData;
	private String[] fieldInformation;
	private ArrayList<String> csvCountryName;
	private ArrayList<String> countryList;
	private LinkedHashMap<String, Integer> dateCollection;
	private int numberOfCountries;
	private int numberOfData;
	private int numberOfField;
	/*
	 * Analyzer constructor receive dataList as data csv file
	 * parse data as array and organize
	 */
	public Analyzer(String[] dataList) {
		String data[]=dataList;
		String countryData[];//String array to store real data temporary
		String tempData[];//String array to use for parsing
		int i=0;//for loop
		int tempCharPosition1=0;//index to parse
		int tempCharPosition2=0;//index to parse
		String temp;//temporary String variable 
		fieldInformation=data[0].split(",");
		numberOfField=fieldInformation.length;//field length
		numberOfData=data.length-1;//number of country
		countryData = new String[numberOfData];
		numberData = new String[numberOfData][numberOfField];//create instance
		for(i=1;i<=numberOfData;i++) {//S0tore number data to temporary string array
			countryData[i-1]=data[i];
		}
		i=0;
		for(String info:countryData) {
			if(info.indexOf("\"")!=-1) {
				if(info.indexOf("\"")==0) {
					tempCharPosition1=info.indexOf("\"");
					tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
					numberData[i][0]=info.substring(tempCharPosition1+1,tempCharPosition2);
					if(info.indexOf("\"",tempCharPosition2+1)!=-1) {
						tempCharPosition1=tempCharPosition2+1;
						tempCharPosition1=info.indexOf("\"",tempCharPosition1);
						tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
						numberData[i][1]=info.substring(tempCharPosition1+1,tempCharPosition2);
						temp=info.substring(tempCharPosition2+2);
						tempData=temp.split(",");
						for(int j=0;j<numberOfField-2;j++) {
							numberData[i][j+2]=tempData[j];
						}
					}
					else {
						temp=info.substring(tempCharPosition2+2);
						tempData=temp.split(",");
						for(int j=0;j<numberOfField-1;j++) {
							numberData[i][j+1]=tempData[j];
						}
					}
					
				}
				if(info.indexOf("\"")==1) {
					tempCharPosition1=info.indexOf("\"");
					tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
					numberData[i][1]=info.substring(tempCharPosition1+1,tempCharPosition2);
					temp=info.substring(0,tempCharPosition1-1);
					numberData[i][0]=temp;
					temp=info.substring(tempCharPosition2+2);
					tempData=temp.split(",");
					for(int j=0;j<numberOfField-2;j++) {
						numberData[i][j+2]=tempData[j];
					}
				}
			} else {
				numberData[i]=info.split(",");
			}
			i++;
		}
		organize();
	}
	/*
	 * Analyzer constructor receive dataList as data csv file, countryList as selected country csv file  
	 * parse data as array and organize
	 */
	public Analyzer(ArrayList<String> dataList, ArrayList<String> countryList) {
		String data[]=dataList.toArray(new String[0]);
		String countryData[];//String array to store real data temporary
		String tempData[];//String array to use for parsing
		csvCountryName=new ArrayList<String>();
		int i=0;//for loop
		int tempCharPosition1=0;//index to parse
		int tempCharPosition2=0;//index to parse
		String temp;//temporary String variable 
		fieldInformation=data[0].split(",");
		numberOfField=fieldInformation.length;//field length
		numberOfData=data.length-1;//number of country
		countryData = new String[numberOfData];
		numberData = new String[numberOfData][numberOfField];//create instance
		for(i=1;i<=numberOfData;i++) {//S0tore number data to temporary string array
			countryData[i-1]=data[i];
		}
		i=0;
		for(String info:countryData) {
			if(info.indexOf("\"")!=-1) {
				if(info.indexOf("\"")==0) {
					tempCharPosition1=info.indexOf("\"");
					tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
					numberData[i][0]=info.substring(tempCharPosition1+1,tempCharPosition2);
					if(info.indexOf("\"",tempCharPosition2+1)!=-1) {
						tempCharPosition1=tempCharPosition2+1;
						tempCharPosition1=info.indexOf("\"",tempCharPosition1);
						tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
						numberData[i][1]=info.substring(tempCharPosition1+1,tempCharPosition2);
						temp=info.substring(tempCharPosition2+2);
						tempData=temp.split(",");
						for(int j=0;j<numberOfField-2;j++) {
							numberData[i][j+2]=tempData[j];
						}
					}
					else {
						temp=info.substring(tempCharPosition2+2);
						tempData=temp.split(",");
						for(int j=0;j<numberOfField-1;j++) {
							numberData[i][j+1]=tempData[j];
						}
					}
					
				}
				if(info.indexOf("\"")==1) {
					tempCharPosition1=info.indexOf("\"");
					tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
					numberData[i][1]=info.substring(tempCharPosition1+1,tempCharPosition2);
					temp=info.substring(0,tempCharPosition1-1);
					numberData[i][0]=temp;
					temp=info.substring(tempCharPosition2+2);
					tempData=temp.split(",");
					for(int j=0;j<numberOfField-2;j++) {
						numberData[i][j+2]=tempData[j];
					}
				}
			} else {
				numberData[i]=info.split(",");
			}
			i++;
		}
		organize();
		for(String info:countryList) {
			if(!info.equals("Wrong country Name")&&!(info.length()==0)){
				csvCountryName.add(info);
			}
		}
		Collections.sort(csvCountryName);
	}
	
	private void organize() {
		countryList = new ArrayList<String>();//arrayList to delete duplicated name
		String countries[] = new String[numberOfData];
		for(int i=0;i<numberOfData;i++) {
			countries[i]=numberData[i][1].trim();
		}
		for(String country:countries) {
			if(!countryList.contains(country)) {
				countryList.add(country);
			}
		}
		numberOfCountries=countryList.size();
		Collections.sort(countryList);
		dateCollection = new LinkedHashMap<String,Integer>();
		int patients;
		for(String country:countryList) {
			patients=getNumberOfPatientsOfACountry(country);
		    dateCollection.put(country, patients);
		}

	}
	
	
	/*
	 * return total countries of countries in data file 
	 */
	public int getNumberOfCountries() {//
		return numberOfCountries;
	}
	/*
	 * print sorted Data by key
	 */
	public void printSortDataByKey() {
		for(Entry<String, Integer> info:dateCollection.entrySet()) {
			System.out.println("- "+ info.getKey()+ ": " + info.getValue());
		}
	}
	/*
	 * print sorted selected country data by key
	 */
	public void printSortDataByCountryKey() {
		LinkedHashMap<String, Integer> countryData = new LinkedHashMap<String, Integer>();
		for(String country:csvCountryName) {
			if(dateCollection.get(country)!=null) {
				countryData.put(country,dateCollection.get(country));
			}
			
		}
		for(Entry<String, Integer> info:countryData.entrySet()) {
			System.out.println("- "+ info.getKey()+ ": " + info.getValue());
		}
	}
	/*
	 * print sorted Data by value
	 */
	public void printSortDataByValue() {
		List<Map.Entry<String, Integer>> sortedData = new LinkedList<>(dateCollection.entrySet());
		sortedData.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		for(Map.Entry<String, Integer> info:sortedData) {
			System.out.println("- "+info.getKey()+ ": " + info.getValue());
		}
	}
	/*
	 * print sorted selected country data by value
	 */
	public void printSortDataByCountryValue(){
		HashMap<String, Integer> countryData = new HashMap<String, Integer>();
		for(String country:csvCountryName) {
			if(dateCollection.get(country)!=null) {
				countryData.put(country,dateCollection.get(country));
			}
		}
		List<Map.Entry<String, Integer>> sortedData = new LinkedList<>(countryData.entrySet());
		sortedData.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		for(Map.Entry<String, Integer> info:sortedData) {
			System.out.println("- "+info.getKey()+ ": " + info.getValue());
		}
		
	}
	/*
	 * return all number of patient from Data
	 */
	public int getNumberOfAllPatients() {
		int numberOfAllPatients=0;
		for(int i=0;i<numberOfData;i++) {
			numberOfAllPatients=numberOfAllPatients+Util.makeInteger(numberData[i][numberOfField-1]);
		}
		return numberOfAllPatients;
	}
	/*
	 * return number of specific country
	 * argument: country name
	 */
	public int getNumberOfPatientsOfACountry(String country) {
		int numberOfPatientsOfACountry=0;
		for(int i=0;i<numberOfData;i++) {
			if(country.equals(numberData[i][1])){
				numberOfPatientsOfACountry=numberOfPatientsOfACountry+Util.makeInteger(numberData[i][numberOfField-1]);
			}
		}
		return numberOfPatientsOfACountry;
	}
	/*
	 * return Number of Patients from specific Data
	 * argument: specific date
	 */
	public int getNumberOfPatientsFromASpecifiedDate(String date) {
		int numberOfPatientsFromASpecifiedDate=0;
		int fieldNum=0;
		fieldNum=Util.getExclusiveFieldNum(date, fieldInformation);
		for(int i=1;i<numberOfData;i++) {
			numberOfPatientsFromASpecifiedDate=numberOfPatientsFromASpecifiedDate+Util.makeInteger(numberData[i][fieldNum]);
		}
		numberOfPatientsFromASpecifiedDate = getNumberOfAllPatients()-numberOfPatientsFromASpecifiedDate;
		return numberOfPatientsFromASpecifiedDate;
	}
	/*
	 * return Number of Patients Before Specific Date
	 * argument: specific date
	 */
	public int getNumberOfPatientsBeforeASpecifiedDate(String date) {
		int numberOfPatientsBeforeASpecifiedDate=0;
		int fieldNum=0;
		fieldNum=Util.getExclusiveFieldNum(date,fieldInformation);
		if(fieldNum==3) {
			numberOfPatientsBeforeASpecifiedDate=0;
		}
		else {
			for(int i=0;i<numberOfData;i++) {
				numberOfPatientsBeforeASpecifiedDate=numberOfPatientsBeforeASpecifiedDate+Util.makeInteger(numberData[i][fieldNum]);
			}
		}
		return numberOfPatientsBeforeASpecifiedDate;
	}
	/*
	 * return Number of Patients between two dates 
	 * Arguments: specific date1, specific date
	 */
	public int getNumberOfPatientsBetweenTwoDates(String date1, String date2) {
		int numberOfPatientsBetweenTwoDate = 0;
		int fieldNum1=0;
		int fieldNum2=0;
		int numberOfPatient1=0;
		int numberOfPatient2=0;
		fieldNum1=Util.getExclusiveFieldNum(date1,fieldInformation);
		fieldNum2=Util.getInclusiveFieldNum(date2,fieldInformation);
		for(int i=0;i<numberOfData;i++) {
			numberOfPatient2=numberOfPatient2+Util.makeInteger(numberData[i][fieldNum2]);
		}
		for(int i=0;i<numberOfData;i++) {
			numberOfPatient1=numberOfPatient1+Util.makeInteger(numberData[i][fieldNum1]);
		}
		numberOfPatientsBetweenTwoDate=numberOfPatient2-numberOfPatient1;
		return numberOfPatientsBetweenTwoDate;
	}
	private void print() {
		for(int i=0;i<numberOfData;i++) {
			for(int j=0;j<numberOfField;j++) {
				System.out.print(numberData[i][j]+"/");
			}
			System.out.println();
		}
	}

}

