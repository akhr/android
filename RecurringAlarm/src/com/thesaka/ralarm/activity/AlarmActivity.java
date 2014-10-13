package com.thesaka.ralarm.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.thesaka.ralarm.R;
import com.thesaka.ralarm.service.AlarmLocalService;
import com.thesaka.ralarm.service.LocalAlarmBinder;
import com.thesaka.ralarm.util.Logger;
import com.thesaka.ralarm.view.CustomNumberPicker;

public class AlarmActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(AlarmActivity.class, "onCreate()");
		initUI();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Logger.debug(AlarmActivity.class, "onStart()");
		if(mService == null){
			startAndBindService();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.debug(AlarmActivity.class, "onResume()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Logger.debug(AlarmActivity.class, "onConfigurationChanged()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.debug(AlarmActivity.class, "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.debug(AlarmActivity.class, "onStop()");
		if(mService != null){
			unbindService(mServiceConnection);
			mService = null;
		}
		
		try {
			unregisterForLocalServiceBroadcast();
		} catch (Exception e) {}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.debug(AlarmActivity.class, "onDestroy()");
	}

	private void initUI(){
		setContentView(R.layout.main_activity);
		mStartBtn = (Button)findViewById(R.id.startBtn);
		mStopBtn = (Button)findViewById(R.id.stopBtn);
		
		mStartBtn.setOnClickListener(mStartBtnListener);
		mStopBtn.setOnClickListener(mStopBtnListener);

		mNumberPicker = (CustomNumberPicker)findViewById(R.id.numberPicker);
		mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mNumberPicker.setMaxValue(60);
		mNumberPicker.setMinValue(1);
		mNumberPicker.setWrapSelectorWheel(true);
	}
	
	private void restoreUI(){
		if(mService != null){
			if(mService.isAlarmRunning()){
				setEnabledBtn(mStartBtn, false);
				setEnabledBtn(mStopBtn, true);
				setAlarmInterval(mService.getAlarmInterval());
				mNumberPicker.setEnabled(false);
			}else{
				setEnabledBtn(mStartBtn, true);
				setEnabledBtn(mStopBtn, false);
			}
		}
	}

	private void startAndBindService(){
		Logger.debug(AlarmActivity.class, "Starting and Binding with AlarmLocalService");
		Intent intent = new Intent();
		intent.setClass(AlarmActivity.this, AlarmLocalService.class);
		startService(intent);
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE | Context.BIND_ABOVE_CLIENT);
	}

	private void registerLocalServiceBroadcast() {
		if(!mLocalReceiverRegistered){
			Logger.debug(AlarmActivity.class, "Registering AlarmLocalServ broadcast rx");
			mService.registerLocalServiceBroadcast(mReceiver);
			mLocalReceiverRegistered = true;
		}
	}

	private void unregisterForLocalServiceBroadcast() {
		Logger.debug(AlarmActivity.class, "Un-Registering AlarmLocalServ broadcast rx");
		mService.unregisterLocalServiceBroadcast();
		mLocalReceiverRegistered = false;
	}

	private boolean handleIntent(Intent intent){
		boolean handleResult = true;
		String action = intent.getAction();

		if(null == action){
			Logger.error(AlarmActivity.class,"Invalid intent action");
			return false;
		}

		if(AlarmLocalService.ACTION_ALARM_SET.equals(action)){
			Logger.debug(AlarmActivity.class, "Received Alarm set broadcast");
			setEnabledBtn(mStartBtn, false);
			setEnabledBtn(mStopBtn, true);
			mNumberPicker.setEnabled(false);
		}else if(AlarmLocalService.ACTION_ALARM_CANCEL.equals(action)){
			Logger.debug(AlarmActivity.class, "Received Alarm cancelled broadcast");
			setEnabledBtn(mStartBtn, true);
			setEnabledBtn(mStopBtn, false);
			mNumberPicker.setEnabled(true);
		}else if(AlarmLocalService.ACTION_ALARM_RING.equals(action)){
			Logger.debug(AlarmActivity.class, "Received Alarm ring broadcast");

		}else if(AlarmLocalService.ACTION_ALARM_TRIGGERED.equals(action)){
			Logger.debug(AlarmActivity.class, "Received Alarm triggered broadcast");
		}
		return handleResult;
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

	private OnClickListener mStartBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(mService != null)
				mService.setAlarmAfterXmins(getAlarmInterval());
			else
				startAndBindService();
		}
	};

	private OnClickListener mStopBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(mService != null)
				mService.cancelAlarm();
			else
				startAndBindService();
		}
	};

	private int getAlarmInterval(){
		return mNumberPicker.getValue();
	}

	private void setAlarmInterval(int val){
		mNumberPicker.setValue(val);
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Logger.debug(AlarmActivity.class, "ServiceConnection:onServiceDisconnected()");
			mService = null;
			unregisterForLocalServiceBroadcast();
			restoreUI();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Logger.debug(AlarmActivity.class, "ServiceConnection:onServiceConnected()");
			mService = ((LocalAlarmBinder)service).getService();
			registerLocalServiceBroadcast();
			restoreUI();
		}
	};

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Logger.debug(AlarmActivity.class, "Received broadcast message");
			if(null == intent){
				Logger.error(AlarmActivity.class,"mLocalServiceReceiver.onReceive() invalid intent");
				return;
			}

			if(AlarmLocalService.ACTION_LOCAL_SERVICE_BROADCAST.equals(intent.getAction())){
				Logger.debug(AlarmActivity.class, "Received local service broadcast message");
				Intent localServiceIntent = intent.getParcelableExtra(AlarmLocalService.EXTRA_LOCAL_SERVICE_INTENT);
				if(null != localServiceIntent){
					handleIntent(localServiceIntent);
				}else{
					Logger.error(AlarmActivity.class,"mLocalServiceReceiver.onReceive() missing local service intent");
				}
			}
		}
	};

	private Button mStartBtn;
	private Button mStopBtn;
	private CustomNumberPicker mNumberPicker;
	private AlarmLocalService mService;
	private boolean mLocalReceiverRegistered;

}
