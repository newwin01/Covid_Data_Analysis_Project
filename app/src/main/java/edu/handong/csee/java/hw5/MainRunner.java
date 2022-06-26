package edu.handong.csee.java.hw5;

import java.io.File;
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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

/*
 * MainRunner class receive arguments and to specific function 
 */
public class MainRunner {
	private String deadData;
	private String confirmedData;
	private String recoveredData;
	private String country;
	private String output;
	private boolean sort;
	private boolean help;
	private LinkedHashMap<String, String> fileList;
	private CovidArrayList<String> countryList;
	private LinkedHashMap<String, LinkedHashMap<String, Integer>> finalList;
	private LinkedHashMap<String, Integer> finalValue1;
	private LinkedHashMap<String, Integer> finalValue2;
	private LinkedHashMap<String, Integer> finalValue3;
	
/*
 * main class to receive arguments and create MainRunner instance,
 * receive the options and perform right sequence
 */
	public static void main(String[] args) {
		MainRunner runner = new MainRunner();
		runner.run(args);

	}
	
	
	@SuppressWarnings("resource")
	private void run(String args[]) {
		String exe;
		Options options = createOptions(); 
		if(parseOptions(options, args)){
			
			//to find out option is correct
			if (help){
				printHelp(options);
				return;
			}
			
			String fileName = null;
			String fileRoot = null;
			if(output!=null) {
				if(output.contains(File.separator)){
					fileName = output.substring(output.lastIndexOf(File.separator)+1,output.lastIndexOf("."));
					fileRoot = output.substring(0,output.lastIndexOf(File.separator));
				} else {
					fileName = output.substring(0,output.lastIndexOf("."));
				}
				System.out.println("The result file saved in "+ output);
			}
			
			
			if(fileRoot!=null) {
				File file = new File(fileRoot);
				if (!file.exists()) {   
					try {
						file.mkdirs();
					}catch (Exception e){
						e.printStackTrace();
					}
				}
				fileName=fileRoot+File.separator+fileName;
			}
			
			fileList = new LinkedHashMap<String,String>();
			if(confirmedData!=null) {
				fileList.put("con", confirmedData);
			}
			if(deadData!=null) {
				fileList.put("dead", deadData);
			}
			if(recoveredData!=null) {
				fileList.put("rec", recoveredData);
			}
			if(country!=null) {
				fileList.put("country",country);
			}
			
			
			ArrayList<FileRunnableClass> readRunner = new ArrayList<FileRunnableClass>();
			ExecutorService executor = Executors.newFixedThreadPool(4);
			
			
			for(String data1:fileList.keySet()) {
				String data = fileList.get(data1);
				exe = data.substring(data.lastIndexOf(".")+1);
				LinkedHashMap<String,LinkedHashMap<String, Integer>> value = new LinkedHashMap<String,LinkedHashMap<String, Integer>>();
				if(exe.equals("zip")) {
					value.put(data1, ReadFile.readZipFile(options, data));
					Runnable worker = new FileRunnableClass(value);
					executor.execute(worker);
					readRunner.add((FileRunnableClass)worker);
				}
				else if(exe.equals("csv")) {
					if(data.equals(country)) {
						Runnable worker = new FileRunnableClass(ReadFile.readCSVCountryFile(data));
						executor.execute(worker);
						readRunner.add((FileRunnableClass)worker);
					} else { 
						value.put(data1, ReadFile.readCSVFile(options, data));
						Runnable worker = new FileRunnableClass(value);
						executor.execute(worker);
						readRunner.add((FileRunnableClass)worker);
					}
				}
			}
			
			executor.shutdown();
			
			while(!executor.isTerminated()) {
			}
			
			
			finalList = new LinkedHashMap<String,LinkedHashMap<String, Integer>> ();
			for(FileRunnableClass runner:readRunner) {
				if(runner.returnHashMap()!=null) {
					finalList = runner.returnHashMap();
					if(runner.returnHashMap().containsKey("con")) {
						finalValue1 = finalList.get("con");
					}
				
					else if(runner.returnHashMap().containsKey("dead")) {
						finalValue2 = finalList.get("dead");
					}
					else if(runner.returnHashMap().containsKey("rec")) {
						finalValue3 = finalList.get("rec");
					}
				}
				else {
					countryList = runner.returnList();
				}
			}
			//delete file if older file exist
			File fileExistance = new File(fileName+".csv");
			if(fileExistance.exists()) {
				fileExistance.delete();
			}
			
			if(finalValue1!=null) {//confirmed 
				if(country==null) {
					Finalizer finalyzer = new Finalizer(finalValue1);
					Util.printConfirmeddResultNoCountry(finalyzer, sort, fileName);
				}
				else {
					Finalizer finalyzer = new Finalizer(finalValue1,countryList);
					Util.printConfirmeddResultWithCountry(finalyzer, sort, fileName);
				}
			}
			if(finalValue2!=null) {//dead
				if(country==null) {
					Finalizer finalyzer = new Finalizer(finalValue2);
					Util.printDeadResultNoCountry(finalyzer, sort, fileName);
				}
				else {
					Finalizer finalyzer = new Finalizer(finalValue2,countryList);
					Util.printDeadResultWithCountry(finalyzer, sort, fileName);
				}
			}
			
			if(finalValue3!=null) {//rec
				if(country==null) {
					Finalizer finalyzer = new Finalizer(finalValue3);
					Util.printRecovereddResultNoCountry(finalyzer, sort, fileName);
				}
				else {
					Finalizer finalyzer = new Finalizer(finalValue3,countryList);
					Util.printRecovereddResultWithCountry(finalyzer, sort, fileName);
				}
			}
			
			//if file root exist, move file to new folder
			
			
			//if there is no country list
			//if file extension is zip, make as zip file, delete existing file
			if(output!=null) {
				String zipExe = output.substring(output.lastIndexOf(".")+1);
				if(zipExe.equals("zip")) {
					try {
						new ZipFile(fileName+".zip").addFile(fileName+".csv");
						File file = new File(fileName+".csv");
						file.delete();
					} catch (ZipException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	//options
	
	private Options createOptions() {
		Options options = new Options();
	
		options.addOption(Option.builder("c").longOpt("confirmed")
				.desc("A data file path for the confirmed patient")
				.hasArg()
				.argName("data path")
				.build());
		
		options.addOption(Option.builder("d").longOpt("death")
				.desc("A data file path for the death patient")
				.hasArg()
				.argName("data path")
				.build());
		
		options.addOption(Option.builder("r").longOpt("recovered")
				.desc("A data file path for the recovered patient")
				.hasArg()
				.argName("data path")
				.build());
		
		options.addOption(Option.builder("l").longOpt("countrylist")
				.desc("Set the csv file that contains the country names")
				.hasArg()
				.argName("country list path")
				.build());
		
		options.addOption(Option.builder("s").longOpt("numsort")
				.desc("Sort by the number of patients of each country")
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Show a help page")
		        .build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Save the results as a file")
				.hasArg()
				.argName("filepath")
				.build());
	
		return options;
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			deadData = cmd.getOptionValue("d");
			confirmedData = cmd.getOptionValue("c");
			recoveredData  = cmd.getOptionValue("r");
			country = cmd.getOptionValue("l");
			output= cmd.getOptionValue("o");
			sort = cmd.hasOption("s");
			help = cmd.hasOption("h");

		} catch (Exception e) {
			printHelp(options);
			return false;
		}
		return true;
	}


	private void printHelp(Options options) {
		String header = "COVID-19 program";
		String footer ="\n https://github.com/ISEL-JAVA/2022-1-java-hw5-HGUISEL";
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("COVID-19 program",header, options, footer, true);
	}
	
}
