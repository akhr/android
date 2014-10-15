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
import android.support.v4.app.Fragment;
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
 * DetailsFragment.java
 */
public class ListFragment extends Fragment {

	public ListFragment() {
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.debug(ListFragment.class, "onAttach()");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(ListFragment.class, "onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.debug(ListFragment.class, "onCreateView()");
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		Logger.debug(ListFragment.class, "onCreateOptionsMenu()");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.debug(ListFragment.class, "onActivityCreated()");
		mListView = (ListView) getView().findViewById(R.id.list_view);
		mAdapter = createListAdapter();
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Logger.debug(ListFragment.class, "onStart()");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Logger.debug(ListFragment.class, "onResume()");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Logger.debug(ListFragment.class, "onPause()");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Logger.debug(ListFragment.class, "onStop()");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.debug(ListFragment.class, "onDestroyView()");
	}
	
	@Override
	public void onDestroyOptionsMenu() {
		super.onDestroyOptionsMenu();
		Logger.debug(ListFragment.class, "onDestroyOptionsMenu()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.debug(ListFragment.class, "onDestroy()");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Logger.debug(ListFragment.class, "onDetach()");
	}
	
	private ArrayAdapter<String> createListAdapter(){
		String[] values = getResources().getStringArray(R.array.sample_list_items);
		
		return new ArrayAdapter<String>(getActivity(),
				android.R.layout.select_dialog_item,
				android.R.id.text1,
				values){
		};
	}
	
	private ListView mListView;
	private ArrayAdapter<String> mAdapter;

}
