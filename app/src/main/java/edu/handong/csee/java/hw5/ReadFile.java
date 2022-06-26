package edu.handong.csee.java.hw5;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
/*
 * ReadFile class is class that uses thread to read csv or zip data
 */
public class ReadFile {
	/*
	 * readZipfile by using external library
	 * it receives options and data
	 * return LinkedHashMap 
	 * key: country name
	 * value: accumulated patients  
	 */
	public static LinkedHashMap<String, Integer> readZipFile(Options options,String data) {
		CovidArrayList<String[]> list = new CovidArrayList<String[]>();
		LinkedHashMap<String, Integer> finalValue = new LinkedHashMap<String,Integer>();
		int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
		int fieldNumber = 0;
		int i=0;
		ArrayList<ReadRunnableClass> readRunner = new ArrayList<ReadRunnableClass>();
		ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);
		
		
		ZipFile zipFile = new ZipFile(data);
		try {
			List<FileHeader> unzipFile = zipFile.getFileHeaders();
			if(unzipFile.isEmpty()) {
				
				System.exit(0);
			}
			InputStream inputStream = zipFile.getInputStream(unzipFile.get(0));
			Scanner scannerFile = new Scanner(inputStream);
			while (scannerFile.hasNextLine ()) {
				String line = scannerFile.nextLine ();
				if(i==0) {
					fieldNumber=line.split(",").length;
				}
				else {
					Runnable worker = new ReadRunnableClass(Util.convertToStringArray(line, fieldNumber));
					executor.execute(worker);
					readRunner.add((ReadRunnableClass)worker);
				}
				i++;
			}
			scannerFile.close();
			zipFile.close();
		} catch (ZipException e) {
			e.printStackTrace();
			System.out.println("Zip File Error!");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("File Error!");
			System.exit(0);
		}
		
		
		executor.shutdown();
		
		while(!executor.isTerminated()) {
		}
		
		for(ReadRunnableClass runner:readRunner) {
			list.add(runner.returnStringArray());
		}
		finalValue=Util.convertToHashMap(list,fieldNumber);
		return finalValue;
		
	}
	/*
	 * read CSV file by using external library
	 * it receives options and data
	 * return LinkedHashMap 
	 * key: country name
	 * value: accumulated patients  
	 */
	
	public static LinkedHashMap<String, Integer>  readCSVFile(Options options,String data) {
		CovidArrayList<String[]> list= new CovidArrayList<String[]>();
		LinkedHashMap<String, Integer> finalValue = new LinkedHashMap<String,Integer>();
		int length = 0;
		int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
		ArrayList<ReadRunnableClass> readRunner = new ArrayList<ReadRunnableClass>();
		ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);
		String[] line = null;
		Reader in = null;
		int i=0;
		
		
		try {
			in = new FileReader(data);
		} catch (FileNotFoundException e) {
			System.out.println("File is not found!");
			System.exit(0);
		}
		try {
			CSVParser parse = CSVFormat.DEFAULT.parse(in);
				for(CSVRecord record:parse) {
					if(i!=0) {
						line =  new String[record.size()];
						for(int j=0;j<record.size();j++) {
							line[j]=record.get(j);
						}
						Runnable worker = new ReadRunnableClass(line);
						executor.execute(worker);
						readRunner.add((ReadRunnableClass)worker);
					}
					i++;
					length=record.size();
				}
		} catch (IOException e) {
			System.out.println("File Error!");
			System.exit(0);
		}
		
		executor.shutdown(); // no new tasks will be accepted.
		
		while (!executor.isTerminated()) {
        }
		
		for(ReadRunnableClass runner:readRunner) {
			list.add(runner.returnStringArray());
		}
		finalValue=Util.convertToHashMap(list,length);
		return finalValue;
	}
	
	/*
	 * read CSV country file by using external library
	 * it receives options and data
	 * return LinkedHashMap 
	 * key: country name
	 * value: accumulated patients  
	 */
	public static CovidArrayList<String> readCSVCountryFile (String country){
		CovidArrayList<String> countryList = new CovidArrayList<String>();
		Reader in = null;
		String[] line = null;
		int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
		ArrayList<ReadRunnableClass> readRunner = new ArrayList<ReadRunnableClass>();
		ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);
		
		
		try {
			in = new FileReader(country);
		} catch (FileNotFoundException e) {
			System.out.println("File is not found!");
		}
		
		//use thread to read country list
		try {
			CSVParser parse = CSVFormat.DEFAULT.parse(in);
				for(CSVRecord record:parse) {
					line =  new String[record.size()];
					for(int j=0;j<record.size();j++) {
						line[j]=record.get(j).trim();
					}
					Runnable worker = new ReadRunnableClass(line);
					executor.execute(worker);
					readRunner.add((ReadRunnableClass)worker);
				}
		} catch (IOException e) {
			System.out.println("File Error!");
			System.exit(0);
		}
		
		executor.shutdown(); // no new tasks will be accepted.
		
		while (!executor.isTerminated()) {
        }
		
		for(ReadRunnableClass runner:readRunner) {
			
			countryList.add(Util.appendString(runner.returnStringArray()));
		}
		
		
		return countryList;
		
	}
		
}
