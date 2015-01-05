/**
 * 
 */
package com.thesaka.fragments_poc.ui.fragments;

import java.io.File;

import com.thesaka.fragments_poc.R;
import com.thesaka.fragments_poc.utility.Logger;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Akhash Ramamurthy (Thesaka) 
 * Oct 14, 2014
 * MenuPaneFragment.java
 */
public class MenuPaneFragment extends MenuFragment {
	
	private View mFragmentContainerView;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		Logger.debug(MenuPaneFragment.class, "onAttach()");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Logger.debug(MenuPaneFragment.class, "onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		Logger.debug(MenuPaneFragment.class, "onCreateView()");
		mFragmentContainerView = inflater.inflate(R.layout.menu_fragment, container, false);
		ListView drawerListView = (ListView)mFragmentContainerView.findViewById(R.id.menu_list_view);
		init(drawerListView);
		return mFragmentContainerView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
//		Logger.debug(MenuPaneFragment.class, "onCreateOptionsMenu()");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		Logger.debug(MenuPaneFragment.class, "onActivityCreated()");
	}
	
	@Override
	public void onStart() {
		super.onStart();
//		Logger.debug(MenuPaneFragment.class, "onStart()");
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		Logger.debug(MenuPaneFragment.class, "onResume()");
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		Logger.debug(MenuPaneFragment.class, "onPause()");
	}
	
	@Override
	public void onStop() {
		super.onStop();
//		Logger.debug(MenuPaneFragment.class, "onStop()");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		Logger.debug(MenuPaneFragment.class, "onDestroyView()");
	}
	
	@Override
	public void onDestroyOptionsMenu() {
		super.onDestroyOptionsMenu();
//		Logger.debug(MenuPaneFragment.class, "onDestroyOptionsMenu()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		Logger.debug(MenuPaneFragment.class, "onDestroy()");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
//		Logger.debug(MenuPaneFragment.class, "onDetach()");
	}

}
