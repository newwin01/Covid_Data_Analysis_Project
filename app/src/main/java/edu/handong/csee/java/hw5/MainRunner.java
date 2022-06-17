package edu.handong.csee.java.hw5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.imageio.IIOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.zip.*;
/*
 * MainRunner class receive arguments and to specific function 
 */
public class MainRunner {
	private String deadData;
	private String confirmedData;
	private String recoveredData;
	private String country;
	private boolean sort;
	private boolean help;
	private String output;
	private CovidArrayList<String[]> list;
	LinkedHashMap<String, Integer> finalValue;
/*
 * main class to receive arguments and create MainRunner instance 
 */
	public static void main(String[] args) {
		MainRunner runner = new MainRunner();
		runner.run(args);

	}
	private void run(String args[]) {
		list = new CovidArrayList<String[]>();
		finalValue = new LinkedHashMap<String,Integer>();
		byte[] buffer = new byte[1024];
		String data = null;
		String exe;
		Options options = createOptions(); 
		int length = 0;
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			if(confirmedData==null&&recoveredData==null&&deadData==null) {
				printHelp(options);
				return; 
			} else if(confirmedData!=null&&recoveredData!=null) {
				printHelp(options);
				return;
			} else if(confirmedData!=null&&deadData!=null) {
				printHelp(options);
				return;
			} else if(recoveredData!=null&&deadData!=null) {
				printHelp(options);
				return;
			}
			else {
				if(confirmedData!=null) {
					data = confirmedData;
				} else if(recoveredData!=null) {
					data = recoveredData;
				} else if(deadData!=null) {
					data = deadData;
				}
				exe = data.substring(data.lastIndexOf(".")+1);
			}
			if(exe.equals("zip")) {
				try {
					ZipInputStream zis = new ZipInputStream(new FileInputStream(data));
					ZipEntry zipEntry = zis.getNextEntry();
				} catch (FileNotFoundException e) {
					printHelp(options);
					return;
				} catch (IOException e) {
					printHelp(options);
					return;
				}
			}
			
			
			if(data!=null&&country==null) {
				String[] line = null;
				Reader in = null;
				int i=0;
				try {
					in = new FileReader(confirmedData);
				} catch (FileNotFoundException e) {
					printHelp(options);
				}
				try {
					CSVParser parse = CSVFormat.DEFAULT.parse(in);
						for(CSVRecord record:parse) {
							if(i!=0) {
								line =  new String[record.size()];
								for(int j=0;j<record.size();j++) {
									line[j]=record.get(j);
								}
								list.add(line);
							}
							i++;
							length=record.size();
						}
				} catch (IOException e) {
					printHelp(options);
					return;
				}
				
				finalValue=Util.convertToHashMap(list, length);
				
				Finalizer finalizer = new Finalizer(finalValue);
				finalizer.printSortDataByKey();
				
				
				
				
//				Analyzer analyzer = new Analyzer(dataList);
//				System.out.println("The total number of countries: " + analyzer.getNumberOfCountries());
//				System.out.println("The total number of the accumulated patients until now: " + analyzer.getNumberOfAllPatients());
//				
//				if(sort) {
//					System.out.println("The total number of patients by the selected countries (Sorted by the number of confirmed patients.)");
//					analyzer.printSortDataByValue();
//				}
//				else {
//					System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
//					analyzer.printSortDataByKey();
//				}
				
				
			}
//			if(data!=null&&country!=null) {
//				BufferedReader inputStream1;
//				BufferedReader inputStream2;
//				try {
//					inputStream1 = new BufferedReader(new FileReader(data));
//					while((line=inputStream1.readLine()) != null) {
//						dataList.add(line);
//					}
//					
//				} catch (FileNotFoundException e1) {
//					printHelp(options);
//					return;
//				} catch (IOException e) {
//				}
//				try {
//					inputStream2 = new BufferedReader(new FileReader(country));
//					while((line=inputStream2.readLine())!=null) {
//						countryList.add(line.trim());
//					}
//				}  catch (FileNotFoundException e1) {
//					printHelp(options);
//					return;
//				} catch (IOException e) {
//					
//				}
//				Analyzer analyzer = new Analyzer(dataList,countryList);
//				System.out.println("The total number of countries: " + analyzer.getNumberOfCountries());
//				System.out.println("The total number of the accumulated patients until now: " + analyzer.getNumberOfAllPatients());
//				if(sort) {
//					System.out.println("The total number of patients by the selected countries (Sorted by the number of confirmed patients.)");
//					analyzer.printSortDataByCountryValue();
//				}else {
//					System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
//					analyzer.printSortDataByCountryKey();
//					
//				}
//			}
			
		}
	}
	
	
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
