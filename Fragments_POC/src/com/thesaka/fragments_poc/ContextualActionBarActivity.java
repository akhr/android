/**
 * 
 */
package com.thesaka.fragments_poc;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.thesaka.fragments_poc.ui.fragments.PlainListFragment;
import com.thesaka.fragments_poc.utility.Logger;

/**
 * @author Akhash Ramamurthy (Thesaka)
 */
public class ContextualActionBarActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.debug(this.getClass(), "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contextual_action_bar_activity);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.placeholder, new PlainListFragment(), null);
		transaction.commit();
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
