/**
 * 
 */
package com.thesaka.fragments_poc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.thesaka.fragments_poc.ui.fragments.DetailsPaneFragment;
import com.thesaka.fragments_poc.ui.fragments.MenuFragment;
import com.thesaka.fragments_poc.ui.fragments.MenuFragment.MenuItemsEnum;
import com.thesaka.fragments_poc.ui.fragments.MenuFragment.MenuSelectionCallback;
import com.thesaka.fragments_poc.ui.fragments.MenuNavigationDrawerFragment;
import com.thesaka.fragments_poc.ui.fragments.MenuPaneFragment;
import com.thesaka.fragments_poc.utility.Logger;

/**
 * @author Akhash Ramamurthy (Thesaka)
 * Oct 17, 2014
 * BaseNavActivity.java
 */
public abstract class BaseNavActivity extends ActionBarActivity implements MenuSelectionCallback, OnBackStackChangedListener{

	private MenuFragment mMenuFragment;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(this.getClass(), "onCreate()");
		setContentView(R.layout.activity_main);
		mTitle = getTitle();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Logger.debug(this.getClass(), "onPostCreate()");
		ViewGroup mMenuPaneFrame = (ViewGroup)findViewById(R.id.menu_pane_frag_placeholder);
		if(mMenuPaneFrame != null){
			setUpMenuPane();
		}else{
			setUpMenuNavigationDrawer();
		}
		getSupportFragmentManager().addOnBackStackChangedListener(BaseNavActivity.this);
		
		if(savedInstanceState == null){
			onMenuItemSelected(MenuItemsEnum.Item_1);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Logger.debug(this.getClass(), "onStart()");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * setDrawerMenuType() should be called prior to calling setContentView()
	 * @param layoutResID - Layout to inflate 
	 */
	@Override
	public void setContentView(int layoutResID) {
		Logger.debug(this.getClass(), "setContentView()");
		super.setContentView(R.layout.activity_main);
	}

	private void setUpMenuPane(){
		Logger.debug(this.getClass(), "setUpMenuPane()");
		mMenuFragment = new MenuPaneFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transc = fragmentManager.beginTransaction();
		transc.add(R.id.menu_pane_frag_placeholder,mMenuFragment);
		transc.commit();
	}

	private void setUpMenuNavigationDrawer(){
		Logger.debug(this.getClass(), "setUpMenuNavigationDrawer()");
		mMenuFragment = (MenuNavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		((MenuNavigationDrawerFragment)mMenuFragment).setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onMenuItemSelected(MenuItemsEnum selectedMenu) {
		Logger.error(this.getClass(), "onMenuItemSelected() - selectedMenu = "+selectedMenu.ordinal()+" --> "+selectedMenu.getName());
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transc = fragmentManager.beginTransaction();
		Fragment fragment = fragmentManager.findFragmentByTag(selectedMenu.getName());
		if(fragment == null){
			fragment = getRightPaneFragment(selectedMenu);
		}
		transc.replace(R.id.details_pane_frag_placeholder,fragment, selectedMenu.getName());
		transc.addToBackStack(null);
		transc.commit();
	}

	@Override
	public void onBackStackChanged() {
		Logger.debug(this.getClass(), "onBackStackChanged()");
	}

	protected abstract Fragment getRightPaneFragment(MenuItemsEnum menu);

	public void onSectionAttached(MenuItemsEnum selectedMenu) {
		switch (selectedMenu) {
		case Item_1:
			mCurrentMenuSelection = selectedMenu;
			break;
		case Item_2:
			mCurrentMenuSelection = selectedMenu;
			break;
		case Item_3:
			mCurrentMenuSelection = selectedMenu;
			break;
		case Item_4:
			mCurrentMenuSelection = selectedMenu;
			break;
		}
		mTitle = selectedMenu.getName();
		setTitle(mTitle);
		
		if(mMenuFragment != null)
			mMenuFragment.setCurrentSelection(selectedMenu);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!((MenuNavigationDrawerFragment)mMenuFragment).isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public MenuItemsEnum getCurrentSelection(){
		return mCurrentMenuSelection;
	}
	
	private MenuItemsEnum mCurrentMenuSelection;
}
