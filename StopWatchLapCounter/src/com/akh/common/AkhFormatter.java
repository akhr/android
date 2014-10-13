/**
 * 
 */
package com.akh.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Akhash Ramamurthy
 * StopWatchLapCounter
 * May 31, 2012
 */
public class AkhFormatter {
	public static SimpleDateFormat DB_STORAGE_DATE_FORMAT = new SimpleDateFormat("MM:dd:yyyy:HH:mm:ss:SS");
	public static SimpleDateFormat LIST_VIEW_DISPLAY_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat DIALOG_DISPLAY_DATE_FORMAT = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
	
	public static String dbStoreFormatDateToString(Date date){
		return DB_STORAGE_DATE_FORMAT.format(date);
	}
	
	public static String dateToStringFormatter(Date date, SimpleDateFormat formatter){
		return formatter.format(date);
	}
	
	public static Date stringToDateFormatter(String input, SimpleDateFormat formatter){
		try {
			return formatter.parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String lapTimeDisplayFormatter(long lapTime){
		int tens = (int) lapTime;
		int seconds = (int) lapTime / 1000;
		int minutes = seconds / 60;
		int hours = minutes / 60;
		tens = tens % 10;
		seconds = seconds % 60;

		StringBuilder s = new StringBuilder();
		s.append(String.format("%02d",hours) + ":");
		s.append(String.format("%02d",minutes) + ":");
		s.append(String.format("%02d",seconds) + ":");
		s.append(String.format("%02d",tens));

		return s.toString();
	}
	
}
