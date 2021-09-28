package edu.sdse.csvprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.Math;

public class CityCSVProcessor {
	
	public void readAndProcess(File file) {
		//Try with resource statement (as of Java 8)
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			//Discard header row
			br.readLine();
			
			String line;

			List<CityRecord> allrecords = new ArrayList<CityRecord>();
			Map<String, List<CityRecord>> recordsByCity = new HashMap<>();
			
			while ((line = br.readLine()) != null) {
				// Parse each line
				String[] rawValues = line.split(",");
				
				int id = convertToInt(rawValues[0]);
				int year = convertToInt(rawValues[1]);
				String city = convertToString(rawValues[2]);
				int population = convertToInt(rawValues[3]);
				
				//System.out.println("id: " + id + ", year: " + year + ", city: " + city + ", population: " + population);
				
				//TODO: Extend the program to process entries!
				CityRecord record = new CityRecord(id, year, city, population);
				//System.out.println(record);
				allrecords.add(record);
				List<CityRecord> toinsert = recordsByCity.getOrDefault( city, new ArrayList<CityRecord>());
				toinsert.add(record);
				recordsByCity.put(
						city,
						toinsert
				);
			}
			//System.out.println(allrecords);
			//System.out.println(recordsByCity);
			
			for (Entry<String, List<CityRecord>> entry : recordsByCity.entrySet()) {
				int minYear = entry.getValue().get(0).getYear();
				int maxYear = entry.getValue().get(0).getYear();
				int numEntries = 0;
				int totalPopulation = 0;
				for (CityRecord record : entry.getValue()) {
					minYear = Math.min(minYear, record.getYear());
					maxYear = Math.max(maxYear, record.getYear());
					numEntries += 1;
					totalPopulation += record.getPopulation();
				}
				int avgPopulation = totalPopulation/numEntries;
				System.out.printf(
					"Average population of %s (%d-%d; %d entries): %d%n",
					entry.getKey(), minYear, maxYear, numEntries, avgPopulation
				);
			}
			
			
			
		}
		catch (Exception e) {
			System.err.println("An error occurred:");
			e.printStackTrace();
		}
	}
	
	private String cleanRawValue(String rawValue) {
		return rawValue.trim();
	}
	
	private int convertToInt(String rawValue) {
		rawValue = cleanRawValue(rawValue);
		return Integer.parseInt(rawValue);
	}
	
	private String convertToString(String rawValue) {
		rawValue = cleanRawValue(rawValue);
		
		if (rawValue.startsWith("\"") && rawValue.endsWith("\"")) {
			return rawValue.substring(1, rawValue.length() - 1);
		}
		
		return rawValue;
	}
	
	public static final void main(String[] args) {
		CityCSVProcessor reader = new CityCSVProcessor();
		
		File dataDirectory = new File("data/");
		File csvFile = new File(dataDirectory, "Cities.csv");
		
		reader.readAndProcess(csvFile);
	}
}
