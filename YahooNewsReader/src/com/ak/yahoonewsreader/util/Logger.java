package com.ak.yahoonewsreader.util;

import android.util.Log;

public class Logger {
	
	public static final String TAG = "YahooNewsReader";
	
	public static void debug(String msg){
		Log.d(TAG, msg);
	}
	
	public static void error(String msg){
		Log.e(TAG, msg);
	}
	
	public static void error(String msg, Exception e){
		Log.e(TAG, msg, e);
	}

}
