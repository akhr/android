/**
 * 
 */
package com.thesaka.ralarm.util;

import com.thesaka.ralarm.activity.AlarmActivity;
import com.thesaka.ralarm.service.AlarmLocalService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * @author ramamurthy
 *
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.debug(AlarmBroadcastReceiver.class,"Alarm trigger received from AlarmManager");
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Thesaka Alarm");
		
		wl.acquire();
	    Intent newIntent = new Intent();
	    newIntent.setClass(context,AlarmLocalService.class);
	    newIntent.setAction(AlarmLocalService.ACTION_ALARM_TRIGGERED);
	    context.startService(newIntent);
		wl.release();
	}
	
}
