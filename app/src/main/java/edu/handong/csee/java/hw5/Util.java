package edu.handong.csee.java.hw5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


/*
 * Util class has many class useful to implement Covid Data Parsing Program
 */
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
	/*
	 * put CovidArrayList and convert into LinkedHashpMap
	 * CovidArrayList has String Array generics
	 */
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
	
	/*
	 * convert String into String array
	 */
	
	public static String[] convertToStringArray(String info,int numberOfField) {
		String numberData[] = new String[numberOfField];
		int tempCharPosition1=0;//index to parse
		int tempCharPosition2=0;//index to parse
		String[] tempData;
		String temp;
		if(info.indexOf("\"")!=-1) {
			if(info.indexOf("\"")==0) {
				tempCharPosition1=info.indexOf("\"");
				tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
				numberData[0]=info.substring(tempCharPosition1+1,tempCharPosition2);
				if(info.indexOf("\"",tempCharPosition2+1)!=-1) {
					tempCharPosition1=tempCharPosition2+1;
					tempCharPosition1=info.indexOf("\"",tempCharPosition1);
					tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
					numberData[1]=info.substring(tempCharPosition1+1,tempCharPosition2);
					temp=info.substring(tempCharPosition2+2);
					tempData=temp.split(",");
					for(int j=0;j<numberOfField-2;j++) {
						numberData[j+2]=tempData[j];
					}
				}
				else {
					temp=info.substring(tempCharPosition2+2);
					tempData=temp.split(",");
					for(int j=0;j<numberOfField-1;j++) {
						numberData[j+1]=tempData[j];
					}
				}
				
			}
			if(info.indexOf("\"")==1) {
				tempCharPosition1=info.indexOf("\"");
				tempCharPosition2=info.indexOf("\"",tempCharPosition1+1);
				numberData[1]=info.substring(tempCharPosition1+1,tempCharPosition2);
				temp=info.substring(0,tempCharPosition1-1);
				numberData[0]=temp;
				temp=info.substring(tempCharPosition2+2);
				tempData=temp.split(",");
				for(int j=0;j<numberOfField-2;j++) {
					numberData[j+2]=tempData[j];
				}
			}
		} else {
			numberData=info.split(",");
		}
		return numberData;
	}
	
	/*
	 * get String Array and append it to String
	 */
	
	public static String appendString(String[] line) {
		String appendedLine = line[0];
		int i=1;
		for(i=1;i<line.length;i++) {
			appendedLine=appendedLine+", " + line[i];
		}
		return appendedLine;
	}
	
	/*
	 * print the Result of dead data that countryList is not given, considering options 
	 */
	
	public static void printDeadResultNoCountry(Finalizer finalizer,boolean sort, String output) {
		if(output!=null) {
			File file = new File(output+".csv");
			BufferedWriter writer = null;
			if(!file.exists()) {
				try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"));
				} catch (IOException e) {
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			else {
				 try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"),StandardOpenOption.APPEND,StandardOpenOption.CREATE);
				} catch (IOException e) {
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			if(sort) {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the dead patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by the number of dead patients.)");
					finalizer.printSortDataByValue(printer, output);
				} catch (IOException e) {
					System.out.println("File Error!");
					System.exit(0);
				}
			} else {
				try {
					
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the dead patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
					finalizer.printSortDataByKey(printer, output);
				} catch (IOException e) {
					System.out.println("File Error!");
					System.exit(0);
				}
			}
		}
		else {
			System.out.println("The total number of countries: " + finalizer.printTotalCountries());
			System.out.println("The total number of the dead patients until now: " + finalizer.printTotalPatient());
			if(sort) {
				System.out.println("The total number of patients by the selected countries (Sorted by the number of dead patients.)");
				finalizer.printSortDataByValue();
				
			}
			else {
				System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
				finalizer.printSortDataByKey();
			}
		}
	}
	/*
	 * print the Result of confirmed data that countryList is not given, considering options 
	 */
	
	public static void printConfirmeddResultNoCountry(Finalizer finalizer,boolean sort, String output) {
		if(output!=null) {
			File file = new File(output+".csv");
			BufferedWriter writer = null;
			if(!file.exists()) {
				try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"));
				} catch (IOException e) {
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			else {
				 try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"),StandardOpenOption.APPEND,StandardOpenOption.CREATE);
				} catch (IOException e) {
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			if(sort) {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the confirmed patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by the number of confirmed patients.)");
					finalizer.printSortDataByValue(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			} else {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the confirmed patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
					finalizer.printSortDataByKey(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			}
			
		}
		else {
		
			System.out.println("The total number of countries: " + finalizer.printTotalCountries());
			System.out.println("The total number of the confirmed patients until now: " + finalizer.printTotalPatient());
			if(sort) {
				System.out.println("The total number of patients by the selected countries (Sorted by the number of confirmed patients.)");
				finalizer.printSortDataByValue();
				
			}
			else {
				System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
				finalizer.printSortDataByKey();
			}
		}
	}
	
	/*
	 * print the Result of recovered data that countryList is not given, considering options 
	 */
	public static void printRecovereddResultNoCountry(Finalizer finalizer,boolean sort, String output) {
		if(output!=null) {
			File file = new File(output+".csv");
			BufferedWriter writer = null;
			if(!file.exists()) {
				try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"));
				} catch (IOException e) {
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			else {
				 try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"),StandardOpenOption.APPEND,StandardOpenOption.CREATE);
				} catch (IOException e) {
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			if(sort) {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the recovered patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by the number of recovered patients.)");
					finalizer.printSortDataByValue(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			} else {
				try {
		
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the recovered patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
					finalizer.printSortDataByKey(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			}
		}
		else {
			System.out.println("The total number of countries: " + finalizer.printTotalCountries());
			System.out.println("The total number of the recovered patients until now: " + finalizer.printTotalPatient());
			if(sort) {
				System.out.println("The total number of patients by the selected countries (Sorted by the number of recovered patients.)");
				finalizer.printSortDataByValue();
				
			}
			else {
				System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
				finalizer.printSortDataByKey();
			}
		}
	}
	/*
	 * print the Result of dead data that countryList is given, considering options 
	 */
	public static void printDeadResultWithCountry(Finalizer finalizer,boolean sort, String output) {
		if(output!=null) {
			File file = new File(output+".csv");
			BufferedWriter writer = null;
			if(!file.exists()) {
				try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			else {
				 try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"),StandardOpenOption.APPEND,StandardOpenOption.CREATE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			if(sort) {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the dead patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by the number of dead patients.)");
					finalizer.printSortDataByCountryValue(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			} else {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the dead patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
					finalizer.printSortDataByCountryKey(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			}
			
		}
		else {
			System.out.println("The total number of countries: " + finalizer.printTotalCountries());
			System.out.println("The total number of the dead patients until now: " + finalizer.printTotalPatient());
			
			if(sort) {
				System.out.println("The total number of patients by the selected countries (Sorted by the number of dead patients.)");
				finalizer.printSortDataByCountryValue();
			}
			else {
				System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
				finalizer.printSortDataByCountryKey();
			}
		}
	}
	
	/*
	 * print the Result of recovered data that countryList is given, considering options 
	 */
	
	public static void printRecovereddResultWithCountry(Finalizer finalizer,boolean sort, String output) {
		if(output!=null) {
			File file = new File(output+".csv");
			BufferedWriter writer = null;
			if(!file.exists()) {
				try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"));
				} catch (IOException e) {
					System.out.println("File Error!");
					System.out.println("File Error!");
				}
			}
			else {
				 try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"),StandardOpenOption.APPEND,StandardOpenOption.CREATE);
				} catch (IOException e) {
					
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			if(sort) {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the recovered patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by the number of recovered patients.)");
					finalizer.printSortDataByCountryValue(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			} else {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the recovered patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
					finalizer.printSortDataByCountryKey(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			}
			
		}
		else {
			System.out.println("The total number of countries: " + finalizer.printTotalCountries());
			System.out.println("The total number of the recovered patients until now: " + finalizer.printTotalPatient());
			
			if(sort) {
				System.out.println("The total number of patients by the selected countries (Sorted by the number of recovered patients.)");
				finalizer.printSortDataByCountryValue();
			}
			else {
				System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
				finalizer.printSortDataByCountryKey();
			}
		}
	}
	
	/*
	 * print the Result of confirmed data that countryList is given, considering options 
	 */
	public static void printConfirmeddResultWithCountry(Finalizer finalizer,boolean sort, String output) {
		if(output!=null) {
			File file = new File(output+".csv");
			BufferedWriter writer = null;
			if(!file.exists()) {
				try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("File Error!");
				}
			}
			else {
				 try {
					writer = Files.newBufferedWriter(Paths.get(output+".csv"),StandardOpenOption.APPEND,StandardOpenOption.CREATE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("File Error!");
					System.exit(0);
				}
			}
			if(sort) {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the confirmed patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by the number of confirmed patients.)");
					finalizer.printSortDataByCountryValue(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			} else {
				try {
					CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
					printer.printRecord("The total number of countries: " + finalizer.printTotalCountries());
					printer.printRecord("The total number of the confirmed patients until now: " + finalizer.printTotalPatient());
					printer.printRecord("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
					finalizer.printSortDataByCountryKey(printer, output);
				} catch (IOException e) {
					System.out.println("File Error! Check file path parser");
					System.exit(0);
				}
			}
			
		}
		else {
			System.out.println("The total number of countries: " + finalizer.printTotalCountries());
			System.out.println("The total number of the confirmed patients until now: " + finalizer.printTotalPatient());
			
			if(sort) {
				System.out.println("The total number of patients by the selected countries (Sorted by the number of confirmed patients.)");
				finalizer.printSortDataByCountryValue();
			}
			else {
				System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
				finalizer.printSortDataByCountryKey();
			}
		}
	}
	
}
		
