package edu.handong.csee.java.hw5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class Finalizer {
	
	private LinkedHashMap<String,Integer> finalValue;
	private CovidArrayList<String> csvCountryName;
	/*
	 * constructor receive LinkedHashMap to organize the value
	 */
	public Finalizer(LinkedHashMap<String, Integer> value){
		finalValue = new LinkedHashMap<String,Integer>();
		finalValue = value;
	}
	/*
	 * constructor receive LinkedHashMap and countryList to organize the value 
	 */
	public Finalizer(LinkedHashMap<String, Integer> value,CovidArrayList<String> countryList){
		finalValue = new LinkedHashMap<String,Integer>();
		finalValue = value;
		csvCountryName = countryList;
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
	 * print sorted selected country data by key
	 */
	public void printSortDataByCountryKey() {
		LinkedHashMap<String, Integer> countryData = new LinkedHashMap<String, Integer>();
		for(int i=0;i<csvCountryName.length();i++) {
			String country = csvCountryName.get(i);
			if(finalValue.get(country)!=null) {
				countryData.put(country,finalValue.get(country));
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
		List<Map.Entry<String, Integer>> sortedData = new LinkedList<>(finalValue.entrySet());
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
		for(int i=0;i<csvCountryName.length();i++) {
			String country = csvCountryName.get(i);
			if(finalValue.get(country)!=null) {
				countryData.put(country,finalValue.get(country));
			}
		}
		List<Map.Entry<String, Integer>> sortedData = new LinkedList<>(countryData.entrySet());
		sortedData.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		for(Map.Entry<String, Integer> info:sortedData) {
			System.out.println("- "+info.getKey()+ ": " + info.getValue());
		}
		
	}
	
	/*
	 * print the total country in the data
	 */
	public int printTotalCountries() {
		return finalValue.size();
	}
	/*
	 * print the total patient in the data
	 */
	public int printTotalPatient() {
		int total=0;
		for(Entry<String, Integer> info:finalValue.entrySet()) {
			total=total+info.getValue();
		}
		return total;
	}
	
}
