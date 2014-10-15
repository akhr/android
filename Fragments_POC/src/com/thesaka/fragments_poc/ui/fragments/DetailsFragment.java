/**
 * 
 */
package com.thesaka.fragments_poc.ui.fragments;

import com.thesaka.fragments_poc.R;
import com.thesaka.fragments_poc.utility.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * @author Akhash Ramamurthy (Thesaka) 
 * Oct 14, 2014
 * DetailsFragment.java
 */
public class DetailsFragment extends Fragment {

	public DetailsFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.debug(DetailsFragment.class, "onAttach()");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(DetailsFragment.class, "onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.debug(DetailsFragment.class, "onCreateView()");
		return inflater.inflate(R.layout.details_fragment, container, false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		Logger.debug(DetailsFragment.class, "onCreateOptionsMenu()");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.debug(DetailsFragment.class, "onActivityCreated()");
		mImageView = (ImageView) getView().findViewById(R.id.imageView);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Logger.debug(DetailsFragment.class, "onStart()");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Logger.debug(DetailsFragment.class, "onResume()");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Logger.debug(DetailsFragment.class, "onPause()");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Logger.debug(DetailsFragment.class, "onStop()");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.debug(DetailsFragment.class, "onDestroyView()");
	}
	
	@Override
	public void onDestroyOptionsMenu() {
		super.onDestroyOptionsMenu();
		Logger.debug(DetailsFragment.class, "onDestroyOptionsMenu()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.debug(DetailsFragment.class, "onDestroy()");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Logger.debug(DetailsFragment.class, "onDetach()");
	}
	
	private ImageView mImageView;

}
