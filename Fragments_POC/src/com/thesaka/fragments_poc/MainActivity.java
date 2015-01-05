package com.thesaka.fragments_poc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.thesaka.fragments_poc.ui.fragments.DetailsPaneFragment;
import com.thesaka.fragments_poc.ui.fragments.MenuFragment.MenuItemsEnum;
import com.thesaka.fragments_poc.utility.Logger;

/**
 * @author Akhash Ramamurthy (Thesaka) 
 * Oct 14, 2014
 * DetailsPaneFragment.java
 */

public class MainActivity extends BaseNavActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(MainActivity.class, "onCreate()");
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
//		addRightPaneFragment(getCurrentSelection());
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
	
	@Override
	protected Fragment getRightPaneFragment(MenuItemsEnum menu){
		Logger.debug(this.getClass(), "getFragment()");
		switch (menu) {
		case Item_1:
			break;
		case Item_2:
			break;
		case Item_3:
			break;
		case Item_4:
			break;
		default:
			break;
		}
		return DetailsPaneFragment.newInstance(menu);
	}

	private void addRightPaneFragment(MenuItemsEnum menu){

		Logger.debug(MainActivity.class, "addDetailsFragment()");

		FragmentTransaction tranc = getSupportFragmentManager().beginTransaction();
		
		if(findViewById(R.id.details_pane_frag_placeholder) != null){
			tranc.add(R.id.details_pane_frag_placeholder, getRightPaneFragment(menu));
		}
		tranc.commit();
	}
}
