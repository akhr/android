package com.thesaka.fragments_poc.ui.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.thesaka.fragments_poc.R;
import com.thesaka.fragments_poc.utility.Logger;

/**
 * @author Akhash Ramamurthy (Thesaka) 
 * Oct 14, 2014
 * MenuNavigationDrawerFragment.java
 */
public class MenuNavigationDrawerFragment extends MenuFragment {


	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	private ActionBarDrawerToggle mDrawerToggle;

	DrawerLayout mDrawerLayout;
	View mFragmentContainerView;

	private boolean mUserLearnedDrawer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(this.getClass(), "onCreate()");
		
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
		Logger.debug(this.getClass(), "onCreate() - mUserLearnedDrawer - "+mUserLearnedDrawer);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.debug(this.getClass(), "onCreateView() - Inflating fragment_navigation_drawer.xml -ListView"); 
		mFragmentContainerView = inflater.inflate(R.layout.menu_fragment, container, false);
		ListView drawerListView = (ListView)mFragmentContainerView.findViewById(R.id.menu_list_view);
		init(drawerListView);
		return mFragmentContainerView;
	}

	public boolean isDrawerOpen() {
		Logger.debug(this.getClass(), "isDrawerOpen()");
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		Logger.debug(this.getClass(), "setUp(int fragmentId, DrawerLayout drawerLayout)");
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		Logger.debug(this.getClass(), "setUp() - creating ActionBarDrawerToggle");
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
				mDrawerLayout, 
				R.drawable.ic_drawer, 
				R.string.drawer_open, 
				R.string.drawer_close 
				) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				Logger.debug(this.getClass(), "ActionBarDrawerToggle.onDrawerClosed()");
				if (!isAdded()) {
					return;
				}
				getActivity().supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Logger.debug(this.getClass(), "ActionBarDrawerToggle.onDrawerOpened()");
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
					.commit();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}
		};

		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	@Override
	protected void selectItem(MenuItemsEnum selectedMenu) {
		super.selectItem(selectedMenu);
		mDrawerLayout.closeDrawer(mFragmentContainerView);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.debug(this.getClass(), "onAttach()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.debug(this.getClass(), "onDetach()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		Logger.debug(this.getClass(), "onConfigurationChanged()");
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		
		Logger.debug(this.getClass(), "onCreateOptionsMenu()");
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Logger.debug(this.getClass(), "onOptionsItemSelected()");
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		if (item.getItemId() == R.id.action_example) {
			Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT)
			.show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		Logger.debug(this.getClass(), "showGlobalContextActionBar()");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

}
