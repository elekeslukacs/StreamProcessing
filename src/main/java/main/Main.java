package main;

import java.text.ParseException;
import model.Statistics;

public class Main {
	

	public static void main(String[] args) throws ParseException {
		
		Statistics s = new Statistics();
		s.read();
		s.countDays();
		s.countActivities();
		s.countEachDayActivities();
		s.countDuration();
		s.countTotalDuration();
		s.activityFrequency();
		s.closeFile();

	}

}
