package com.thesaka.ralarm.util;

import android.util.Log;

@SuppressWarnings("rawtypes")
public class Logger {
	
	private static final String TAG = "RecurringAlarm";
	
	public static void debug(String msg){
		Log.d(TAG, msg);
	}
	
	public static void debug(Class className, String msg){
		Log.d(TAG, className.getSimpleName()+" "+msg);
	}
	
	public static void warn(String msg){
		Log.w(TAG, msg);
	}
	
	public static void warn(Class className, String msg){
		Log.d(TAG, className.getSimpleName()+" "+msg);
	}

	public static void error(String msg){
		Log.e(TAG, msg);
	}
	
	public static void error(Class className, String msg){
		Log.d(TAG, className.getSimpleName()+" "+msg);
	}
	
	public static void error(String msg, Throwable e){
		Log.e(TAG, msg, e);
	}
	
	public static void error(Class className, String msg, Throwable e){
		Log.d(TAG, className.getSimpleName()+" "+msg, e);
	}

}
