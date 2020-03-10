package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MonitoredData {
	private Date startTime;
	private Date endTime;
	private String activity;
	
	public MonitoredData(Date start, Date end, String activity) {
		this.startTime = start;
		this.endTime = end;
		this.activity = activity;
	}
		
	public Date getStartTime() {
		return startTime;
	}
	
	/**
	 * Gets the date without the hours meaning that hour is set to 00:00:00
	 * @return Date without hour
	 * @throws ParseException
	 */
	public Date getDateWithoutTime() throws ParseException {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date result = dFormat.parse(dFormat.format(startTime));
		return result;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	/**
	 * Gets the difference between endTime and startTime
	 * @return difference in minutes
	 */
	public long getDateDiff() {
	    long diffInMillies = endTime.getTime() - startTime.getTime();
	    return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activity == null) ? 0 : activity.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonitoredData other = (MonitoredData) obj;
		if (activity == null) {
			if (other.activity != null)
				return false;
		} else if (!activity.equals(other.activity))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
	
	
}
