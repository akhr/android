/**
 * 
 */
package com.akh.activity;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.akh.adapter.AkhExpandableListViewAdapter;
import com.akh.adapter.AkhListAdapterHelper;
import com.akh.adapter.AkhListAdapterHelperInterface;
import com.akh.common.AkhFormatter;
import com.akh.constants.Constants;
import com.akh.constants.FontFaceConstants;
import com.akh.dataBean.LapEntry;
import com.akh.db.AkhSQLiteDataSource;

/**
 * @author Akhash Ramamurthy
 * StopWatchLapCounter
 * May 25, 2012
 */
public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private boolean isPortrait = true;

	private TextView hoursTV;
	private TextView minsTV;
	private TextView secsTV;
	private TextView tenthTV;
	private TextView hoursColon;
	private TextView minsColon;
	private TextView secsColon;
	private Button btn1;
	private Button btn2;
	private ExpandableListView expandableListView;

	private Typeface timeDisplayTypeFace;
	private Typeface btnTypeFace;

	private LapMeterState currLapMeterState;
	private AkhSQLiteDataSource sqlLiteDataSource;

	private AkhListAdapterHelperInterface listViewHelper;

	private LapEntry currDisplayedLapEntry;
	private LapEntry currEditingLapEntry;

	private Long startedAt;
	private Long pausedAt;
	private long displayTimeValue = 0L;

	private Handler uiHandler = new Handler();
	private static final SimpleDateFormat timeDisplayFormat = new SimpleDateFormat("hh:mm:ss:S");


	/** **************************   LIFE CYCLE EVENTS   ********************************************/

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main_layout);
		init();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/** **************************   LIFE CYCLE EVENTS   ********************************************/


	private void init(){
		sqlLiteDataSource = new AkhSQLiteDataSource(getApplicationContext());

		hoursTV =(TextView)findViewById(R.id.hours);
		minsTV =(TextView)findViewById(R.id.mins);
		secsTV =(TextView)findViewById(R.id.secs);
		tenthTV =(TextView)findViewById(R.id.tenth);
		hoursColon = (TextView)findViewById(R.id.hoursColon);
		minsColon = (TextView)findViewById(R.id.minsColon);
		secsColon = (TextView)findViewById(R.id.secsColon);

		btn1 = (Button)findViewById(R.id.startStopBtn);
		btn2 = (Button)findViewById(R.id.pauseResumeBtn);

		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) 
			isPortrait = false;

		if(isPortrait){
			expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
			listViewHelper = new AkhListAdapterHelper(this, expandableListView);
			//		listViewHelper = new AkhListAdapterHelper(this);
		}

		setUITypeFace();
		setLapMeterState(LapMeterState.NOT_STARTED);

		resetDisplay();
		addButtonListerners();
	}

	private void setUITypeFace(){
		//		timeDisplayTypeFace = Typeface.createFromAsset(getAssets(),FontFaceConstants.fontFace19);
		timeDisplayTypeFace = Typeface.createFromAsset(getAssets(),FontFaceConstants.fontFace4);
		btnTypeFace = Typeface.createFromAsset(getAssets(),FontFaceConstants.fontFace19);

		hoursTV.setTypeface(timeDisplayTypeFace);
		minsTV.setTypeface(timeDisplayTypeFace);
		secsTV.setTypeface(timeDisplayTypeFace);
		tenthTV.setTypeface(timeDisplayTypeFace);
		hoursColon.setTypeface(timeDisplayTypeFace);
		minsColon.setTypeface(timeDisplayTypeFace);
		secsColon.setTypeface(timeDisplayTypeFace);

		btn1.setTypeface(btnTypeFace);
		btn2.setTypeface(btnTypeFace);
	}

	private void addButtonListerners(){
		btn1.setOnClickListener(startStopBtnListener);
		btn2.setOnClickListener(pauseResumeBtnListener);
	}

	View.OnClickListener startStopBtnListener = new View.OnClickListener() {

		public void onClick(View v) {
			if(currLapMeterState == LapMeterState.NOT_STARTED ||
					currLapMeterState == LapMeterState.STOPPED)
				startTimer();
			else if (currLapMeterState == LapMeterState.RUNNING ||
					currLapMeterState == LapMeterState.RESUMED)
				stopTimer();
			else if (currLapMeterState == LapMeterState.PAUSED){
				//				resetDisplay();
				//				setLapMeterState(LapMeterState.STOPPED);
				stopTimer();
			}

		}
	};

	View.OnClickListener pauseResumeBtnListener = new View.OnClickListener() {

		public void onClick(View v) {
			if(currLapMeterState == LapMeterState.RUNNING)
				pauseTimer();
			else if(currLapMeterState == LapMeterState.PAUSED)
				resumeTimer();

		}
	};

	View.OnClickListener resetBtnListener = new View.OnClickListener() {

		public void onClick(View v) {
			resetTimer();
		}
	};

	private void startTimer(){
		Log.e(TAG, "Start Time Func Called");
		setLapMeterState(LapMeterState.RUNNING);
		startedAt = System.currentTimeMillis();
		currDisplayedLapEntry = new LapEntry(startedAt);
		uiHandler.removeCallbacks(timerTask);
		uiHandler.postDelayed(timerTask, 100);
	}

	private void pauseTimer(){
		Log.e(TAG, "Pause Time Func Called");
		setLapMeterState(LapMeterState.PAUSED);
		currDisplayedLapEntry.setWasPaused(true);
		uiHandler.removeCallbacks(timerTask);
		pausedAt = System.currentTimeMillis();
	}

	private void resumeTimer(){
		Log.e(TAG, "Resume Time Func Called");
		setLapMeterState(LapMeterState.RESUMED);
		uiHandler.removeCallbacks(timerTask);
		uiHandler.postDelayed(timerTask, 100);
	}

	private void stopTimer(){
		Log.e(TAG, "Stop Time Func Called");
		setLapMeterState(LapMeterState.STOPPED);
		uiHandler.removeCallbacks(timerTask);
		//		showDialog(Constants.STOP_DIALOG);
		saveLapEntry();
	}

	private void resetTimer(){
		Log.e(TAG, "Reset Time Func Called");
		setLapMeterState(LapMeterState.NOT_STARTED);
		uiHandler.removeCallbacks(timerTask);
		resetDisplay();
	}

	private void saveLapEntry(){
		currDisplayedLapEntry.setLapStopTime(System.currentTimeMillis());
		currDisplayedLapEntry.setLapTime(displayTimeValue);
		saveLapEntryToDB();
		resetCurrLapEntry();
		resetDisplay();
		uiHandler.removeCallbacks(timerTask);
		if(isPortrait)
			listViewHelper.refreshListView();
	}

	private void resetDisplay(){
		Log.e(TAG, "Reset DISPLAY Func Called");
		hoursTV.setText("00");
		minsTV.setText("00");
		secsTV.setText("00");
		tenthTV.setText("00");
	}

	private void resetCurrLapEntry(){
		this.currDisplayedLapEntry = null;
	}

	private void setLapMeterState(LapMeterState newState){
		//		Log.e(TAG, "Setting new LapMeterState == "+ newState.name());
		this.currLapMeterState = newState;
		setBtnText(currLapMeterState.getBtn1text(), currLapMeterState.getBtn2text());
	}

	private void setBtnText(String btn1text, String btn2Text){
		btn1.setText(btn1text);
		btn2.setText(btn2Text);
	}

	public void setDisplay() {

		//		 long displayTimeValue = 0L;

		Log.i(TAG, "startedAt = "+ startedAt);
		Log.i(TAG, "pausedAt = "+ pausedAt);

		if(currLapMeterState == LapMeterState.RUNNING)
			displayTimeValue = System.currentTimeMillis() - startedAt;
		else if(currLapMeterState == LapMeterState.RESUMED){
			displayTimeValue = pausedAt - startedAt;
			startedAt = (System.currentTimeMillis() - pausedAt) + startedAt;
		}

		Log.i(TAG, "displayTime = "+ displayTimeValue);

		int tens = (int) displayTimeValue;
		int seconds = (int) displayTimeValue / 1000;
		int minutes = seconds / 60;
		int hours = minutes / 60;
		tens = tens % 10;
		seconds = seconds % 60;

		hoursTV.setText(String.format("%02d",hours));
		minsTV.setText(String.format("%02d",minutes));
		secsTV.setText(String.format("%02d",seconds));
		tenthTV.setText(String.format("%02d",tens));

		setLapMeterState(LapMeterState.RUNNING);
	}

	private Runnable timerTask = new Runnable() {
		public void run() {
			if (currLapMeterState == LapMeterState.RUNNING || 
					currLapMeterState == LapMeterState.RESUMED) {
				setDisplay();
				uiHandler.postDelayed(timerTask, 100);
			}
		}
	};

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch(id) {
		case Constants.STOP_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Stop Lap");
			builder.setIcon(android.R.drawable.ic_menu_save);
			builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					currDisplayedLapEntry.setLapStopTime(System.currentTimeMillis());
					currDisplayedLapEntry.setLapTime(displayTimeValue);
					saveLapEntryToDB();
					resetCurrLapEntry();
					resetDisplay();
					uiHandler.removeCallbacks(timerTask);
					if(isPortrait)
						listViewHelper.refreshListView();
				}
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});

			builder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					resetCurrLapEntry();
					resetDisplay();
					uiHandler.removeCallbacks(timerTask);
				}
			});

			dialog = builder.create();
			break;

		case Constants.EDIT_DIALOG:
			if(currEditingLapEntry == null)
				return null;

			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setTitle("Edit");
			builder1.setIcon(android.R.drawable.ic_menu_edit);

			LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(this.LAYOUT_INFLATER_SERVICE);
			View view=layoutInflater.inflate(R.layout.edit_lap_entry_layout,null);
			TextView lapTimeTV = (TextView) view.findViewById(R.id.LapTimeValue);
			TextView lapStartTimeTV = (TextView) view.findViewById(R.id.LapStartValue);
			TextView lapStopTimeTV = (TextView) view.findViewById(R.id.LapStopValue);
			EditText commetET = (EditText) view.findViewById(R.id.CommentValue);

			lapTimeTV.setText(AkhFormatter.lapTimeDisplayFormatter(currEditingLapEntry.getLapTime()));
			lapStartTimeTV.setText(AkhFormatter.dateToStringFormatter(currEditingLapEntry.getLapStartTime(), AkhFormatter.DIALOG_DISPLAY_DATE_FORMAT));
			lapStopTimeTV.setText(AkhFormatter.dateToStringFormatter(currEditingLapEntry.getLapStopTime(), AkhFormatter.DIALOG_DISPLAY_DATE_FORMAT));
			commetET.setText(currEditingLapEntry.getComment());
			commetET.setCursorVisible(true);

			boolean flag = view==null?false:true;
			Log.e(TAG, " is dialog layout inflated : " + flag);
			boolean flag2 = view.findViewById(R.id.LapTimeHeader)==null?false:true;
			Log.e(TAG, " is LapTimeHeader  inflated : " + flag2);
			boolean flag3 = view.findViewById(R.id.LapTimeValue)==null?false:true;
			Log.e(TAG, " is LapTimeValue  inflated : " + flag3);



			builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					currDisplayedLapEntry.setLapStopTime(System.currentTimeMillis());
					currDisplayedLapEntry.setLapTime(displayTimeValue);
					saveLapEntryToDB();
					resetCurrLapEntry();
					resetDisplay();
					uiHandler.removeCallbacks(timerTask);
					if(isPortrait)
						listViewHelper.refreshListView();
				}
			});

			builder1.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});

			builder1.setView(view);

			dialog = builder1.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	public AkhSQLiteDataSource getDataSource(){
		return this.sqlLiteDataSource;
	}

	private boolean saveLapEntryToDB(){	
		try {
			return getDataSource().addLapEntry(currDisplayedLapEntry.clone()) <= 0 ? false : true;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return false;

	}

	public void showEditDialogFor(LapEntry selectedLapEntry){
		if(selectedLapEntry == null)
			return;
		this.currEditingLapEntry = selectedLapEntry;
		showDialog(Constants.EDIT_DIALOG);
	}
}
