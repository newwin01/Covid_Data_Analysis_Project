package edu.handong.csee.java.hw5;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVPrinter;

import java.util.TreeMap;


public class Finalizer {
	
	private LinkedHashMap<String,Integer> finalValue;
	private CovidArrayList<String> csvCountryName;
	private int totalCountry;
	private int totalPatient;
	/*
	 * constructor receive LinkedHashMap to organize the value
	 */
	public Finalizer(LinkedHashMap<String, Integer> value){
		finalValue = new LinkedHashMap<String,Integer>();
		finalValue = value;
		for(Entry<String, Integer> info:finalValue.entrySet()) {
			totalPatient=totalPatient+info.getValue();
		}
		totalCountry=finalValue.size();
	}
	/*
	 * constructor receive LinkedHashMap and countryList  to organize the value 
	 */
	public Finalizer(LinkedHashMap<String, Integer> value,CovidArrayList<String> countryList){
		finalValue = new LinkedHashMap<String,Integer>();
		finalValue = value;
		csvCountryName = countryList;
		for(Entry<String, Integer> info:finalValue.entrySet()) {
			totalPatient=totalPatient+info.getValue();
		}
		totalCountry=finalValue.size();
	}
	
	/*
	 * print sorted Data by key
	 */
	public void printSortDataByKey() {
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(finalValue);
		for(Entry<String, Integer> info:map.entrySet()) {
			System.out.println("- "+ info.getKey()+ ": " + info.getValue());
		}
	}
	/*
	 * save sorted all data by key in the Comma separate file
	 */
	public void printSortDataByKey(CSVPrinter printer, String output) throws IOException{
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(finalValue);
		for(Entry<String, Integer> info:map.entrySet()) {
			printer.printRecord("- "+info.getKey()+ ": " + info.getValue());
		}
		printer.flush();
		printer.close();
	}
	/*
	 * print sorted selected country data by key
	 */
	public void printSortDataByCountryKey() {
		HashMap<String, Integer> countryData = new HashMap<String, Integer>();
		for(int i=0;i<csvCountryName.length();i++) {
			String country = csvCountryName.get(i);
			if(finalValue.get(country)!=null) {
				countryData.put(country,finalValue.get(country));
			}
		}
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(countryData);
		for(Entry<String, Integer> info:map.entrySet()) {
			System.out.println("- "+ info.getKey()+ ": " + info.getValue());
		}
	}
	/*
	 * print sorted selected country data by key in the comma seperate file
	 */
	public void printSortDataByCountryKey(CSVPrinter printer, String output) throws IOException{
		HashMap<String, Integer> countryData = new HashMap<String, Integer>();
		for(int i=0;i<csvCountryName.length();i++) {
			String country = csvCountryName.get(i);
			if(finalValue.get(country)!=null) {
				countryData.put(country,finalValue.get(country));
			}
		}
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(countryData);
		for(Entry<String, Integer> info:map.entrySet()) {
			printer.printRecord("- "+info.getKey()+ ": " + info.getValue());
		}
		printer.flush();
		printer.close();
	}
	/*
	 * print sorted Data by value 
	 */
	public void printSortDataByValue() {
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(finalValue);
		List<Map.Entry<String, Integer>> sortedData = new LinkedList<>(map.entrySet());
		sortedData.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		for(Map.Entry<String, Integer> info:sortedData) {
			System.out.println("- "+info.getKey()+ ": " + info.getValue());
		}
	}
	/*
	 * save sorted all data by value in the Comma separate file
	 */
	public void printSortDataByValue(CSVPrinter printer, String output) throws IOException{
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(finalValue);
		List<Map.Entry<String, Integer>> sortedData = new LinkedList<>(map.entrySet());
		sortedData.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		for(Map.Entry<String, Integer> info:sortedData) {
			printer.printRecord("- "+info.getKey()+ ": " + info.getValue());
		}
		printer.flush();
		printer.close();
	}
	
	/*
	 * print sorted selected country data by value
	 */
	public void printSortDataByCountryValue(){
		HashMap<String, Integer> countryData = new HashMap<String, Integer>();
		for(int i=0;i<csvCountryName.length();i++) {
			String country = csvCountryName.get(i);
			if(finalValue.get(country)!=null) {
				countryData.put(country,finalValue.get(country));
			}
		}
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(countryData);
		List<Map.Entry<String, Integer>> sortedData = new LinkedList<>(countryData.entrySet());
		sortedData.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		for(Map.Entry<String, Integer> info:sortedData) {
			System.out.println("- "+info.getKey()+ ": " + info.getValue());
		}
		
	}
	/*
	 * save sorted selected country data by value in the Comma separate file
	 */
	public void printSortDataByCountryValue(CSVPrinter printer, String output) throws IOException{
		HashMap<String, Integer> countryData = new HashMap<String, Integer>();
		for(int i=0;i<csvCountryName.length();i++) {
			String country = csvCountryName.get(i);
			if(finalValue.get(country)!=null) {
				countryData.put(country,finalValue.get(country));
			}
		}
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(countryData);
		List<Map.Entry<String, Integer>> sortedData = new LinkedList<>(countryData.entrySet());
		sortedData.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		for(Map.Entry<String, Integer> info:sortedData) {
			printer.printRecord("- "+info.getKey()+ ": " + info.getValue());
		}
		printer.flush();
		printer.close();
	}
	/*
	 * print the total country in the data
	 */
	public int printTotalCountries() {

		return totalCountry;
	}
	/*
	 * print the total patient in the data
	 */
	public int printTotalPatient() {

		return totalPatient;
	}


}
