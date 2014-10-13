/**
 * 
 */
package com.thesaka.ralarm.activity;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.thesaka.ralarm.R;
import com.thesaka.ralarm.util.AlarmBroadcastReceiver;
import com.thesaka.ralarm.util.Logger;
import com.thesaka.ralarm.view.CustomNumberPicker;

/**
 * @author ramamurthy
 * Aug 8 2014
 */
public class MainActivity extends Activity {

	private static final String INTENT_ALARM_ACTION = "com.thesaka.ralarm";
	private static final String LOG_FILE_DIR = "RecurringAlarm/logs";
	private static final String LOG_FILE_NAME = "log.txt";
	private static final double MAX_LOG_FILE_SIZE_IN_MB = 100*1024;  //100KB


	private Uri mNotification;
	private Ringtone mRingTone;

	private TextView mTitleTV;
	private CustomNumberPicker mNumberPicker;
	private Button mStartBtn;
	private Button mStopBtn;

	private Handler mHandler;
	private int mAlarmInterval;
	private int mRingDuration = 3000;
	private AlarmBroadcastReceiver mAlarmReceiver;
	private AlarmManager mAlarmManager;
	private PendingIntent mPendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterReceiver();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
	}

	private void init(){
		mHandler = new Handler();
		mTitleTV = (TextView)findViewById(R.id.titleTV);
		mTitleTV.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				readFromLogFile();
				return false;
			}
		});
		mStartBtn = (Button)findViewById(R.id.startBtn);
		mStopBtn = (Button)findViewById(R.id.stopBtn);
		mAlarmInterval = 1*60*1000; // default 1 min
		
		prepareNumberPicker();
		prepareRingtone();
		prepareAlarmItems();
		updateButton();
	}

	private void prepareNumberPicker(){
		mNumberPicker = (CustomNumberPicker)findViewById(R.id.numberPicker);
		mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mNumberPicker.setMaxValue(60);
		mNumberPicker.setMinValue(1);
		mNumberPicker.setWrapSelectorWheel(true);
		mNumberPicker.setOnValueChangedListener(mOnValueChangeListener);
	}

	private void prepareRingtone(){
		mNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mRingTone = RingtoneManager.getRingtone(getApplicationContext(), mNotification);
	}

	private void prepareAlarmItems(){
		//AlarmManager System Service
		if(mAlarmManager == null)
			mAlarmManager =(AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);

		//Pending Intent to be fired once the alarm goes off. This intent will identify and deliver to broadcast receiver 
		if(mPendingIntent == null){
			Intent intent = new Intent(INTENT_ALARM_ACTION);
			mPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		}

		//Our alarm broadcast receiver
		if(mAlarmReceiver == null){
//			mAlarmReceiver = new AlarmBroadcastReceiver(getApplicationContext(), mCallBack);
			registerReceiver();
		}
	}

	private void registerReceiver(){
		IntentFilter intentFilter = new IntentFilter(INTENT_ALARM_ACTION);
		registerReceiver(mAlarmReceiver, intentFilter);
	}

	private void unRegisterReceiver(){
		try{
			unregisterReceiver(mAlarmReceiver);
		}catch(IllegalArgumentException e){
			Logger.error("AlarmReceiver already unregistered");
		}
	}

	private void updateButton(){
		setEnabledBtn(mStartBtn, true);
		setEnabledBtn(mStopBtn, false);
		mStartBtn.setOnClickListener(mStartBtnListener);
		mStopBtn.setOnClickListener(mStopBtnListener);
	}

	private void setEnabledBtn(Button button, boolean enabled){
		if(enabled){
			button.setEnabled(true);
			button.setTextColor(Color.BLACK);
		}else{
			button.setEnabled(false);
			button.setTextColor(Color.GRAY);
		}
	}

	private OnValueChangeListener mOnValueChangeListener = new OnValueChangeListener() {

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			Logger.debug("NumberPicker - OldV -->"+oldVal);
			Logger.debug("NumberPicker - NewV -->"+newVal);
			mAlarmInterval = newVal*60*1000;
			Logger.debug("New Alarm Duration - "+mAlarmInterval);
		}
	};


	private OnClickListener mStartBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setAlarmOnAlarmManager();
			setEnabledBtn(mStartBtn, false);
			setEnabledBtn(mStopBtn, true);
		}
	};


	private OnClickListener mStopBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			cancelAlarmOnAlarmManager();
			setEnabledBtn(mStopBtn, false);
			setEnabledBtn(mStartBtn, true);
		}
	};

	public void setAlarmOnAlarmManager()
	{
		Calendar cal = Calendar.getInstance();
		long triggerTime = cal.getTimeInMillis() + mAlarmInterval;
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, mPendingIntent);
		Logger.debug("setAlarmOnAlarmManager() - mAlarmInterval - "+mAlarmInterval);
		cal.setTimeInMillis(triggerTime);
		Logger.debug("setAlarmOnAlarmManager() - Next triggerTime - "+cal.getTime());
	}

	public void cancelAlarmOnAlarmManager()
	{
		//		Intent intent = new Intent(INTENT_ALARM_ACTION);
		//		PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
		//		AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
		mAlarmManager.cancel(mPendingIntent);
		unRegisterReceiver();
	}

	private boolean isAlarmRunning(){
		boolean alarmUp = false;
		alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 0, 
				new Intent(INTENT_ALARM_ACTION), 
				PendingIntent.FLAG_NO_CREATE) != null);
		Logger.debug("Is the alarm already running - "+alarmUp);
		return alarmUp;
	}

	private void ringDevice(){
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mRingTone.play();
			}
		});
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mRingTone.play();
			}
		}, mRingDuration);
	}

	private boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	private String getLogFilePath(){
		if(isExternalStorageWritable()){
			File root =  Environment.getExternalStorageDirectory();

			String logfilePath = root.getAbsolutePath().endsWith(File.separator) ? 
					root.getAbsolutePath()+LOG_FILE_DIR :
						root.getAbsolutePath()+File.separator+LOG_FILE_DIR;

			Logger.debug("Log file path created = "+logfilePath);
			return logfilePath;
		}
		return null;	
	}

	private void writeToLogFile(String msg){
		String filePath = getLogFilePath();

		if(filePath != null && !TextUtils.isEmpty(filePath)){
			try {
				File dir = new File(filePath);
				if(!dir.exists()){
					dir.mkdirs();
				}

				File file = new File(dir, LOG_FILE_NAME);
				if(!file.exists()){
					file.createNewFile();
				}

				boolean shdAppend = true;
				if((MAX_LOG_FILE_SIZE_IN_MB - file.length()) <= 100){
					shdAppend = false;
				}

				Logger.debug("Log File absolute path - "+file.getAbsolutePath());
				Logger.debug("Log file exist ? "+file.exists());
				Logger.debug("Log file isDirectory ? "+file.isDirectory());
				Logger.debug("Log file canWrite ? "+file.canWrite());
				Logger.debug("Log file size ? "+file.length());

				FileWriter fileWrite = new FileWriter(file, shdAppend);
				fileWrite.write(msg + "\n");
				fileWrite.close();
			} catch (FileNotFoundException e) {
				Logger.error("Actual write to log file ERROR - FileNotFound",e);
			}catch (IOException ioe){
				Logger.error("Actual write to log file ERROR - IOException",ioe);
			}
		}
	}

	private void readFromLogFile(){
		String filePath = getLogFilePath();
		if(filePath != null && !TextUtils.isEmpty(filePath)){
			try {
				File dir = new File(filePath);
				if(!dir.exists())
					dir.mkdirs();

				File file = new File(dir, LOG_FILE_NAME);
				if(!file.exists())
					file.createNewFile();

				Logger.debug("Log File absolute path - "+file.getAbsolutePath());
				Logger.debug("Log file exist ? "+file.exists());
				Logger.debug("Log file isDirectory ? "+file.isDirectory());
				Logger.debug("Log file canRead ? "+file.canRead());

				BufferedReader br = new BufferedReader(new FileReader(file));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append("\n");
					Logger.debug("Log : "+line);
					line = br.readLine();
				}
				br.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

/*	private AlarmBroadcastReceiver.CallBack mCallBack = new CallBack() {

		@Override
		public void onAlarmTriggered() {
			String logMsg = "AlarmBroadcastReceiver.CallBack received time - "+ Calendar.getInstance().getTime();
			Logger.debug(logMsg);
			writeToLogFile(logMsg);
			ringDevice();
			setAlarmOnAlarmManager();
		}
	};*/



}
