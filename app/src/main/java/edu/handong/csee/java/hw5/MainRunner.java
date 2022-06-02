package edu.handong.csee.java.hw5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.IIOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
/*
 * MainRunner class receive arguments and to specific function 
 */
public class MainRunner {
	private String data;
	private String country;
	private boolean sort;
	private boolean help;
/*
 * main class to receive arguments and create MainRunner instance 
 */
	public static void main(String[] args) {
		MainRunner runner = new MainRunner();
		runner.run(args);

	}
	private void run(String args[]) {
		
		Options options = createOptions();
		String line;
		ArrayList<String> dataList = new ArrayList<String>();
		ArrayList<String> countryList = new ArrayList<String>();
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			if(data!=null&&country==null) {
				BufferedReader inputStream;
				try {
					inputStream = new BufferedReader(new FileReader(data));
					while((line=inputStream.readLine()) != null) {
						dataList.add(line);
					}
				} catch (FileNotFoundException e1) {
					printHelp(options);
					return;
				} catch (IOException e) {
				}
				Analyzer analyzer = new Analyzer(dataList);
				System.out.println("The total number of countries: " + analyzer.getNumberOfCountries());
				System.out.println("The total number of the accumulated patients until now: " + analyzer.getNumberOfAllPatients());
				
				if(sort) {
					System.out.println("The total number of patients by the selected countries (Sorted by the number of confirmed patients.)");
					analyzer.printSortDataByValue();
				}
				else {
					System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
					analyzer.printSortDataByKey();
				}
				
				
			}
			if(data!=null&&country!=null) {
				BufferedReader inputStream1;
				BufferedReader inputStream2;
				try {
					inputStream1 = new BufferedReader(new FileReader(data));
					while((line=inputStream1.readLine()) != null) {
						dataList.add(line);
					}
					
				} catch (FileNotFoundException e1) {
					printHelp(options);
					return;
				} catch (IOException e) {
				}
				try {
					inputStream2 = new BufferedReader(new FileReader(country));
					while((line=inputStream2.readLine())!=null) {
						countryList.add(line.trim());
					}
				}  catch (FileNotFoundException e1) {
					printHelp(options);
					return;
				} catch (IOException e) {
					
				}
				Analyzer analyzer = new Analyzer(dataList,countryList);
				System.out.println("The total number of countries: " + analyzer.getNumberOfCountries());
				System.out.println("The total number of the accumulated patients until now: " + analyzer.getNumberOfAllPatients());
				if(sort) {
					System.out.println("The total number of patients by the selected countries (Sorted by the number of confirmed patients.)");
					analyzer.printSortDataByCountryValue();
				}else {
					System.out.println("The total number of patients by the selected countries (Sorted by country names in alphabetical order.)");
					analyzer.printSortDataByCountryKey();
					
				}
			}
			
		}
	}
	
	private Options createOptions() {
		Options options = new Options();
	
		options.addOption(Option.builder("d").longOpt("data")
				.desc("Set the data file for the confirmed patient numbers")
				.hasArg()
				.argName("data path")
				.required()
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
	
		return options;
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			data = cmd.getOptionValue("d");
			country = cmd.getOptionValue("l");
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
		String footer ="\n https://github.com/ISEL-JAVA/2022-1-java-hw4-HGUISEL";
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("COVID-19 program",header, options, footer, true);
	}
	
}
