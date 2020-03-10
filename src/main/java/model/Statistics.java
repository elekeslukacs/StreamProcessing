package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistics {
	
	private List<MonitoredData> data;
	private FileWriter fileWriter;

	
	public Statistics() {
		this.data = new ArrayList<MonitoredData>();
		File file = new File("hello.txt");				
		try {
			this.fileWriter = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the data from the Activities.txt file
	 */
	public void read() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		try (Stream<String> stream = Files.lines(Paths.get("Activities.txt"))) {
			data = stream.map(d -> {
				try {
					return new MonitoredData(dateFormat.parse(d.split("\t\t")[0]), 
							dateFormat.parse(d.split("\t\t")[1]),
							d.split("\t\t")[2].trim());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return null;
			})
					.collect(Collectors.toList());	
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Calculates the total number of days
	 */
	public void countDays() {
		Set<Date> days = data.stream().map(d -> {
			try {
				return d.getDateWithoutTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toSet());
		writeResult("Number of days is: " + days.size());
		//System.out.println("Number of days is: " + days.size());
	}
	
	/**
	 * Counts how many times has appeared each activity over the entire monitoring period. 
	 */
	public void countActivities() {
		Map<String, Long> activities = data.stream().collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting())); 
		
		writeResult("\r\nTimes each activity appeared:");
		activities.entrySet().stream().forEach(d -> {writeResult(d.getKey() + "      " + d.getValue());
		//System.out.println(d.getKey() + "      " + d.getValue());
		});
	}
	
	/**
	 * Count how many times has appeared each activity for each day over the monitoring period.
	 */
	public void countEachDayActivities() {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		Map<Date, Map<String, Long>> activities = data.stream().collect(Collectors.groupingBy(
				t -> {
					try {
						return t.getDateWithoutTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return null;
				},
				Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting())));
		
		writeResult("\r\nEach day each activity counted:");
		activities.entrySet().stream().forEach(d -> {writeResult(dFormat.format(d.getKey()) + "      " + d.getValue());
		//System.out.println(d.getKey() + "      " + d.getValue());
		});
	}
	
	/**
	 * For each line from the file map for the activity label the duration recorded on that line 
	 */
	public void countDuration() {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Map<String, Long> result = data.stream().collect(Collectors.toMap(MonitoredData::toString, MonitoredData::getDateDiff));
		writeResult("\r\nEach line containing duration:");
		data.stream().forEach(d->writeResult(dFormat.format(d.getStartTime()) + "   " + dFormat.format(d.getEndTime()) + 
		"     " + d.getActivity() +  "          Duration: " + d.getDateDiff() + " minutes"));
		//result.entrySet().stream().forEach(d->writeResult(d.getKey() + "       Duration: " + d.getValue()));
	}
	
	/**
	 * For each activity compute the entire duration over the monitoring period
	 */
	public void countTotalDuration() {
		Map<String, Long> result = data.stream().collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.summingLong(MonitoredData::getDateDiff)));
		
		writeResult("\r\nTotal duration for every activity:");
		result.entrySet().stream().forEach(d -> writeResult(d.getKey() + "       " + d.getValue() + " minutes"));
	}
	
	/**
	 * Filter the activities that have 90% of the monitoring records with duration less than 5 minutes
	 */
	public void activityFrequency() {
		Map<String, Long> freq = data.stream().collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.mapping(MonitoredData::getDateDiff, 
				Collectors.summingLong(d->{if(d<5) return 1; else return 0;}))));
		Map<String, Long> activities = data.stream().collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting())); 
		
		writeResult("\r\nActivities with duration less than 5 minutes and 90% probability: ");
		
		List<String> names = new ArrayList<String>(freq.keySet());
		List<Long> favorable = new ArrayList<Long> (freq.values());
		List<Long> total = new ArrayList<Long>(activities.values());
		
		for(int i = 0; i < freq.size(); i++) {
			if((double) favorable.get(i)/total.get(i) > 0.9) {
				//System.out.println(names.get(i) + "   " + (double) favorable.get(i)/total.get(i));
				writeResult(names.get(i));
			}
		}	
	}
	
	/**
	 * Writes into the file string
	 * @param result - String to be written in the file
	 */
	public void writeResult(String result) {
		try {
			this.fileWriter.write(result);
			this.fileWriter.write("\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Closes the file
	 */
	public void closeFile() {
		try {
			this.fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
