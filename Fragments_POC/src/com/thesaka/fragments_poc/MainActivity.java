package com.thesaka.fragments_poc;

import com.thesaka.fragments_poc.ui.fragments.DetailsFragment;
import com.thesaka.fragments_poc.ui.fragments.ListFragment;
import com.thesaka.fragments_poc.utility.Logger;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

/**
 * @author Akhash Ramamurthy (Thesaka) 
 * Oct 14, 2014
 * DetailsFragment.java
 */

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(MainActivity.class, "onCreate()");
		setContentView(R.layout.activity_main);
		createAndAddFragments();
		if (savedInstanceState == null) {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Logger.debug(MainActivity.class, "onStart()");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Logger.debug(MainActivity.class, "onResume()");
//		createAndAddFragments();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Logger.debug(MainActivity.class, "onPause()");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Logger.debug(MainActivity.class, "onStop()");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.debug(MainActivity.class, "onDestroy()");
	}
	
	private void createAndAddFragments(){
		
		Logger.debug(MainActivity.class, "createAndAddFragments()");
		
		ListFragment listFragment = new ListFragment();
		DetailsFragment detailsFragment = new DetailsFragment();
		FragmentTransaction tranc = getSupportFragmentManager().beginTransaction();
		tranc.add(R.id.list_frag_placeHolder, listFragment);
		tranc.add(R.id.details_frag_placeHolder, detailsFragment);
		tranc.commit();
	}
}
