/**
 * 
 */
package com.thesaka.ralarm.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.thesaka.ralarm.activity.AlarmActivity;
import com.thesaka.ralarm.util.AlarmBroadcastReceiver;
import com.thesaka.ralarm.util.Logger;

/**
 * @author Akhash Ramamurthy
 *
 */
public class AlarmLocalService extends Service {

	public static final String ACTION_ALARM_TRIGGERED = "com.thesaka.ralarm.service.ACTION_ALARM_TRIGGERED";
	public static final String ACTION_LOCAL_SERVICE_BROADCAST = "com.thesaka.ralarm.service.ACTION_LOCAL_SERVICE_BROADCAST";
	public static final String ACTION_ALARM_SET = "com.thesaka.ralarm.service.ACTION_ALARM_SET";
	public static final String ACTION_ALARM_CANCEL = "com.thesaka.ralarm.service.ACTION_ALARM_CANCEL";
	public static final String ACTION_ALARM_RING = "com.thesaka.ralarm.service.ACTION_ALARM_RING";
	public static final String ALARM_TIME_REMAINING = "com.thesaka.ralarm.service.ALARM_TIME_REMAINING";
	public static final String EXTRA_LOCAL_SERVICE_INTENT = "com.thesaka.ralarm.service.EXTRA_LOCAL_SERVICE_INTENT";

	public static final String ALARM_PREF_FILE= "alarm_pref_file";
	public static final String ALARM_RUNNING = "alarm_running";
	public static final String ALARM_TRIGGER_INTERVAL = "alarm_trigger_interval";
	public static final String ALARM_NEXT_TRIGGER = "alarm_next_trigger";

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.debug(AlarmLocalService.class, "onCreate()");

		mHandler = new Handler();
		prepareRingtone();
		restoreAlarms();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.debug(AlarmLocalService.class, "onDestroy()");
		try{
			unregisterLocalServiceBroadcast();
		}catch(Exception e){}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.debug(AlarmLocalService.class, "onStartCommand() - IsAlarmRunning --> "+isAlarmRunning());
		if(intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_ALARM_TRIGGERED)){
			onAlarmTriggered(intent.getLongExtra(ALARM_NEXT_TRIGGER, 0));
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Logger.debug(AlarmLocalService.class, "onUnBind() - return true");
		return true;
	}

	@Override
	public void onRebind(Intent intent) {
		Logger.debug(AlarmLocalService.class, "onReBind()");
		super.onRebind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Logger.debug(AlarmLocalService.class, "onBind()");
		LocalAlarmBinder binder = new LocalAlarmBinder(AlarmLocalService.this);
		return binder;
	}

	private void prepareRingtone(){
		Uri ringNotify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mRingTone = RingtoneManager.getRingtone(getApplicationContext(), ringNotify);
	}

	private void restoreAlarms(){
		SharedPreferences settings = getSharedPreferences(ALARM_PREF_FILE, Context.MODE_PRIVATE);
		mIsAlarmRunning = settings.getBoolean(ALARM_RUNNING, false);
		long triggerInterval = settings.getLong(ALARM_TRIGGER_INTERVAL, -1);
		long nextTrigger = settings.getLong(ALARM_NEXT_TRIGGER, -1);

		Logger.debug(AlarmLocalService.class, "RestoreAlarms()- isAlarmRun-->"+isAlarmRunning()+" triggerInterval-->"+triggerInterval+" nextTrigger-->"+nextTrigger);

		if(mIsAlarmRunning && triggerInterval != -1 && nextTrigger != -1){
			mAlarmIntervalInMills = triggerInterval;
			Calendar cal = Calendar.getInstance();
			if(cal.getTimeInMillis() > nextTrigger){ //Alarm time passed
				long diff = cal.getTimeInMillis() - nextTrigger;
				Logger.error("Time: diff = "+diff);
				int addFactor = (int)Math.ceil(diff/triggerInterval);
				Logger.error("Time: addFactor = "+addFactor);
				Logger.error("Time: newTrigger = "+nextTrigger+addFactor);
				setAlarmAt(nextTrigger+addFactor);
			}else{
				setAlarmAt(nextTrigger);
			}
		}
	}

	private long convertMinsToMills(int mins){
		return mins*60*1000;
	}

	private int convertMillsToMins(long mills){
		return (int) ((mills/60)/1000);
	}

	private void sendLocalBroadcast(Intent data){
		LocalBroadcastManager broadcastMgr = LocalBroadcastManager.getInstance(AlarmLocalService.this);
		Intent intent = new Intent();
		intent.setAction(ACTION_LOCAL_SERVICE_BROADCAST);
		intent.putExtra(EXTRA_LOCAL_SERVICE_INTENT, data);
		broadcastMgr.sendBroadcast(intent);
		Logger.debug(AlarmActivity.class, "Local broadcast sent for - "+data.getAction());
	}

	public int getAlarmInterval(){
		return convertMillsToMins(mAlarmIntervalInMills);
	}

	private void setAlarmInterval(long intervalInMills){
		mAlarmIntervalInMills = intervalInMills;
	}

	public void setAlarmAfterXmins(int intervalInMins){
		setAlarmAfterXMillisec(convertMinsToMills(intervalInMins));
	}

	private void setAlarmAfterXMillisec(long intervalInMills){
		setAlarmInterval(intervalInMills);
		Calendar cal = Calendar.getInstance();
		long triggerTime = cal.getTimeInMillis() + mAlarmIntervalInMills;
		setAlarmAt(triggerTime);
	}

	private void setAlarmAt(long triggerTime){
		AlarmManager alarmManager = (AlarmManager) AlarmLocalService.this.getSystemService(Context.ALARM_SERVICE);

		//Pending Intent to be fired once the alarm goes off. This intent will identify and deliver to broadcast receiver 
		Intent intent = new Intent(ACTION_ALARM_TRIGGERED);
		intent.setClass(AlarmLocalService.this, AlarmBroadcastReceiver.class);
		intent.putExtra(ALARM_NEXT_TRIGGER, triggerTime);
		mPendingIntent = PendingIntent.getBroadcast(AlarmLocalService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, mPendingIntent);

		mIsAlarmRunning = true;
		sendLocalBroadcast(new Intent(ACTION_ALARM_SET));
		storeAlarmData(triggerTime);

		Logger.debug(AlarmLocalService.class, "setAlarmInterval : "+mAlarmIntervalInMills);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(triggerTime);
		Logger.debug("setAlarmInterval - Next triggerTime - "+cal.getTime());
	}

	public void cancelAlarm(){
		if(mPendingIntent != null){
			AlarmManager alarmManager = (AlarmManager) AlarmLocalService.this.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(mPendingIntent);
			mIsAlarmRunning = false;
			sendLocalBroadcast(new Intent(ACTION_ALARM_CANCEL));
			resetAlarmData();
			Logger.debug(AlarmLocalService.class, "cancelAlarm()");
		}
	}

	private void storeAlarmData(long timeInMills){
		SharedPreferences alarmSettings = getSharedPreferences(ALARM_PREF_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = alarmSettings.edit();
		editor.putBoolean(ALARM_RUNNING, isAlarmRunning());
		editor.putLong(ALARM_TRIGGER_INTERVAL, mAlarmIntervalInMills);
		editor.putLong(ALARM_NEXT_TRIGGER, timeInMills);
		editor.commit();
	}

	private void resetAlarmData(){
		SharedPreferences alarmSettings = getSharedPreferences(ALARM_PREF_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = alarmSettings.edit();
		editor.putBoolean(ALARM_RUNNING, false);
		editor.putLong(ALARM_TRIGGER_INTERVAL, -1);
		editor.putLong(ALARM_NEXT_TRIGGER, -1);
		editor.commit();
	}

	private void onAlarmTriggered(long triggerTime) {
		String logMsg = "AlarmLocalService onAlarmTriggered - Alarm received time - "+ Calendar.getInstance().getTime();
		Logger.debug(logMsg);
		ringDevice();
		if(isAlarmRunning())
			setAlarmAfterXMillisec(mAlarmIntervalInMills);
	}

	public boolean isAlarmRunning(){
		return mIsAlarmRunning;
	}

	private void ringDevice(){
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mRingTone.play();
				sendLocalBroadcast(new Intent(ACTION_ALARM_RING));
			}
		});
	}

	public void registerLocalServiceBroadcast(BroadcastReceiver receiver){
		mLocalServiceBroadcastReceiver = receiver;
		IntentFilter filter = new IntentFilter(ACTION_LOCAL_SERVICE_BROADCAST);
		LocalBroadcastManager.getInstance(AlarmLocalService.this).registerReceiver(receiver, filter);
	}

	public void unregisterLocalServiceBroadcast(){
		try{
			LocalBroadcastManager.getInstance(AlarmLocalService.this).unregisterReceiver(mLocalServiceBroadcastReceiver);
		}catch (Exception e){}
	}

	private PendingIntent mPendingIntent;
	private Handler mHandler;
	private Ringtone mRingTone;
	private long mAlarmIntervalInMills;
	private int mRingDuration = 3000;
	private boolean mIsAlarmRunning = false;

	private AlarmBroadcastReceiver mAlarmReceiver;
	private BroadcastReceiver mLocalServiceBroadcastReceiver;

}
