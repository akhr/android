/**
 * 
 */
package com.akh.dataBean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Akhash Ramamurthy
 * StopWatchLapCounter
 * May 30, 2012
 */
public class LapEntry implements Cloneable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Calendar CALENDAR= Calendar.getInstance();

	private long _id;
	private boolean wasPaused = false;
	private Date lapStartTime;
	private Date lapStopTime;
	private long lapTime = 0L;
	private String comment = null;
	private double longitude = 0.0;
	private double latitude = 0.0;

	/**
	 * 
	 */
	
	private LapEntry(Date lapStartTime){
		this.lapStartTime = lapStartTime;
	}
	
	public LapEntry(long lapStartTime){
		this(lapStartTime, 0L, 0L, false, null, 0.0, 0.0); 
	}
	
	public LapEntry(long lapStartTime, long lapEndTime, long lapTime, boolean wasPaused,
			String comment, double longitude, double latitude) {

		setLapStartTime(lapStartTime);
		setLapStopTime(lapEndTime);
		this.lapTime = lapTime;
		this.wasPaused = wasPaused;
		this.comment = comment;
		this.longitude = longitude;
		this.latitude = latitude;
	} 

	public LapEntry(Date lapStartTime, Date lapEndTime, String lapTime, boolean wasPaused,
			String comment, String longitude, String latitude) {

		this.lapStartTime = lapStartTime;
		this.lapStopTime = lapEndTime; 
		this.lapTime = Long.valueOf(lapTime);
		this.wasPaused = wasPaused;
		this.comment = comment;
		this.longitude = Double.valueOf(longitude);
		this.latitude = Double.valueOf(latitude);
	}
	
	public void setWasPaused(boolean wasPaused) {
		this.wasPaused = wasPaused;
	}

	public boolean wasPaused() {
		return wasPaused;
	}

	public long get_id() {
		return _id;
	}

	public Date getLapStartTime() {
		return lapStartTime;
	}

	private void setLapStartTime(long lapStartTime) {
		CALENDAR.setTimeInMillis(lapStartTime);
		this.lapStartTime = CALENDAR.getTime();
	}

	public Date getLapStopTime() {
		return lapStopTime;
	}

	public void setLapStopTime(long lapEndTime) {
		CALENDAR.setTimeInMillis(lapEndTime);
		this.lapStopTime = CALENDAR.getTime();;
	}

	public long getLapTime() {
		return lapTime;
	}

	public void setLapTime(long lapTime) {
		this.lapTime = lapTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "LapEntry === StartTime = "+this.lapStartTime.toString()/* +
				"EndTime = "+this.lapEndTime.toString()+
				"LapTime = "+this.lapTime+
				"WasPaused = "+this.wasPaused+
				"Comment = "+this.comment+
				"Longitude = "+this.longitude+
				"Latitude = "+this.latitude*/;
	}

	@Override
	public LapEntry clone() throws CloneNotSupportedException {
		LapEntry newEntry = new LapEntry(this.lapStartTime);
		newEntry.lapStopTime = this.lapStopTime;
		newEntry.lapTime = this.lapTime;
		newEntry.wasPaused = this.wasPaused;
		newEntry.comment = this.comment;
		newEntry.longitude = this.longitude;
		newEntry.latitude = this.latitude;
		return newEntry;
	}

	

}
