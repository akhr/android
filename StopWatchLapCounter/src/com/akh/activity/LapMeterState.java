/**
 * 
 */
package com.akh.activity;

import com.akh.constants.Constants;

/**
 * @author Akhash Ramamurthy
 * StopWatchLapCounter
 * May 25, 2012
 */
public enum LapMeterState {
	
	NOT_STARTED(Constants.START, Constants.PAUSE),
	RUNNING(Constants.STOP, Constants.PAUSE),
	PAUSED(Constants.STOP, Constants.RESUME),
	STOPPED(Constants.START, Constants.PAUSE),
	RESUMED(Constants.STOP, Constants.PAUSE);
	
	private String btn1Text = null;
	private String btn2Text = null;
	
	private LapMeterState(String btn1text, String btn2text){
		this.btn1Text = btn1text;
		this.btn2Text = btn2text;
	}
	
	public String getBtn1text(){
		return this.btn1Text;
	}
	
	public String getBtn2text(){
		return this.btn2Text;
	}

}
