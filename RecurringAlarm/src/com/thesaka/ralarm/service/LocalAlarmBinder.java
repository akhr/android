package com.thesaka.ralarm.service;

import android.os.Binder;

public class LocalAlarmBinder extends Binder {

	private AlarmLocalService mService;

	public LocalAlarmBinder(AlarmLocalService service) {
		mService = service;
	}

	public AlarmLocalService getService() {
		return mService;
	}

}
