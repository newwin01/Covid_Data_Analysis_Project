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
	private CovidArrayList<String> countryList;
	private CovidArrayList<String[]> list;
	private ArrayList<ReadRunnableClass> readRunner;
	private LinkedHashMap<String, Integer> finalValue;
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
		list = new CovidArrayList<String[]>();
		finalValue = new LinkedHashMap<String,Integer>();
		String data = null;
		String exe;
		Options options = createOptions(); 
		int length = 0;
		if(parseOptions(options, args)){
			
			//to find out option is correct
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
			
			//receive data that is zip file using external library
			if(exe.equals("zip")) {
				int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
				int fieldNumber = 0;
				int i=0;
				readRunner = new ArrayList<ReadRunnableClass>();
				ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);
				
				
				ZipFile zipFile = new ZipFile(data);
				try {
					List<FileHeader> unzipFile = zipFile.getFileHeaders();
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
				} catch (ZipException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				executor.shutdown();
				
				while(!executor.isTerminated()) {
				}
				
				for(ReadRunnableClass runner:readRunner) {
					list.add(runner.returnStringArray());
				}
				
				finalValue=Util.convertToHashMap(list,fieldNumber);
			}
			
			//receive data that is csv file 
			else if(exe.equals("csv")) {
				int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
				readRunner = new ArrayList<ReadRunnableClass>();
				ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);
				String[] line = null;
				Reader in = null;
				int i=0;
				
				
				try {
					in = new FileReader(data);
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
								Runnable worker = new ReadRunnableClass(line);
								executor.execute(worker);
								readRunner.add((ReadRunnableClass)worker);
							}
							i++;
							length=record.size();
						}
				} catch (IOException e) {
					printHelp(options);
					return;
				}
				
				
				executor.shutdown(); // no new tasks will be accepted.
				
				while (!executor.isTerminated()) {
		        }
				
				for(ReadRunnableClass runner:readRunner) {
					list.add(runner.returnStringArray());
				}
				
				finalValue=Util.convertToHashMap(list, length);
			}
			
			
			//parse output data, use defined constant to support any operating system
			String fileName = null;
			String fileRoot = null;
			if(output!=null) {
				if(output.contains(File.separator)){
					fileName = output.substring(output.lastIndexOf(File.separator)+1,output.lastIndexOf("."));
					fileRoot = output.substring(0,output.lastIndexOf(File.separator));
				} else {
					fileName = output.substring(0,output.lastIndexOf("."));
				}
			} 
			
			
			//if file root exist, move file to new folder
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
			
			//if there is no country list
			if(data!=null&&country==null) {

				Finalizer finalizer = new Finalizer(finalValue);
				
				if(deadData!=null) {
					Util.printDeadResultNoCountry(finalizer, sort, fileName);
				} else if (confirmedData!=null){
					Util.printConfirmeddResultNoCountry(finalizer, sort, fileName);
				} else if(recoveredData!=null) {
					Util.printRecovereddResultNoCountry(finalizer, sort, fileName);
				}
				if(output!=null) {
					System.out.println("The result file saved in "+output);
				}
				
			}
			
			//if there is country list
			if(data!=null&&country!=null) {
				countryList = new CovidArrayList<String>();
				Reader in = null;
				String[] line = null;
				int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
				readRunner = new ArrayList<ReadRunnableClass>();
				ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);
				
				
				try {
					in = new FileReader(country);
				} catch (FileNotFoundException e) {
					printHelp(options);
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
					printHelp(options);
					return;
				}
				
				executor.shutdown(); // no new tasks will be accepted.
				
				while (!executor.isTerminated()) {
		        }
				
				for(ReadRunnableClass runner:readRunner) {
					countryList.add(Util.appendString(runner.returnStringArray()));
				}
				
				Finalizer finalizer = new Finalizer(finalValue,countryList);
				
				if(deadData!=null) {
					Util.printDeadResultWithCountry(finalizer, sort, fileName);
				} else if (confirmedData!=null){
					Util.printConfirmeddResultWithCountry(finalizer, sort, fileName);
				} else if(recoveredData!=null) {
					Util.printRecovereddResultWithCountry(finalizer, sort, fileName);
				}
				if(output!=null) {
					System.out.println("The result file saved in "+output);
				}

			}
			
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
