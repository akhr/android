/**
 * 
 */
package com.thesaka.fragments_poc;

import com.thesaka.fragments_poc.utility.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * @author Akhash Ramamurthy (Thesaka)
 * Nov 3, 2014
 * SecondActivity.java
 */
public class SecondActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.debug(this.getClass(), "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.debug(this.getClass(), "Button.onClick()");
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	@Override
	protected void onStart() {
		Logger.debug(this.getClass(), "onStart()");
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		Logger.debug(this.getClass(), "onResume()");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Logger.debug(this.getClass(), "onPause()");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		Logger.debug(this.getClass(), "onStop()");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Logger.debug(this.getClass(), "onDestroy()");
		super.onDestroy();
	}
	

}
