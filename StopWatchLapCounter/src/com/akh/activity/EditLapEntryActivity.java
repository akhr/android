/**
 * 
 */
package com.akh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.akh.common.AkhFormatter;
import com.akh.constants.Constants;
import com.akh.dataBean.LapEntry;

/**
 * @author Akhash
 *
 */
public class EditLapEntryActivity extends Activity {

	private static final String TAG = EditLapEntryActivity.class.getSimpleName();
	private LapEntry editableLapEntry;
	private TextView lapTimeTV;
	private TextView startTimeTV;
	private TextView stopTimeTV;
	private EditText commentTV;
	private Button saveButton;
	private Button deleteButton;


	/** **************************   LIFE CYCLE EVENTS   ********************************************/

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		//		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.edit_lap_entry_activity_title);

		setContentView(R.layout.edit_lap_entry_layout);

		init((LapEntry)((Bundle) getIntent().getExtras()).getSerializable(Constants.TO_BE_EDITED_LAP_ENTRY));
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

	private void init(LapEntry editableLapEntry){
		Log.e(TAG, "EditLapEntryActivity Fired");
		this.editableLapEntry = editableLapEntry;

		this.lapTimeTV = (TextView) findViewById(R.id.LapTimeValue);
		this.startTimeTV = (TextView) findViewById(R.id.LapStartValue);
		this.stopTimeTV = (TextView) findViewById(R.id.LapStopValue);
		this.commentTV = (EditText) findViewById(R.id.CommentValue);
/*		this.saveButton = (Button) findViewById(R.id.saveButton);
		this.deleteButton = (Button) findViewById(R.id.deleteButton);*/

		Log.e(TAG, "Lap time = "+ (AkhFormatter.lapTimeDisplayFormatter(editableLapEntry.getLapTime())));
		Log.e(TAG, "Lap Start time = "+ (AkhFormatter.dateToStringFormatter(editableLapEntry.getLapStartTime(), AkhFormatter.LIST_VIEW_DISPLAY_DATE_FORMAT)));
		Log.e(TAG, "Lap Stop time = "+(AkhFormatter.dateToStringFormatter(editableLapEntry.getLapStopTime(), null)));
		Log.e(TAG, "Lap Comment = "+ editableLapEntry.getComment());

		lapTimeTV.setText(AkhFormatter.lapTimeDisplayFormatter(editableLapEntry.getLapTime()));
		startTimeTV.setText(AkhFormatter.dbStoreFormatDateToString(editableLapEntry.getLapStartTime()));
		stopTimeTV.setText(AkhFormatter.dbStoreFormatDateToString(editableLapEntry.getLapStopTime()));
		commentTV.setText(editableLapEntry.getComment());

		addButtonListeners();
	}

	private void addButtonListeners(){
		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(Constants.EDITED_LAP_ENTRY, editableLapEntry);
				setResult(Constants.RESULT_SAVE, i);
			}
		});

		deleteButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(Constants.TO_BE_DELETED_LAP_ENTRY, editableLapEntry);
				setResult(Constants.RESULT_DELETE, i);
			}
		});

	}






}
