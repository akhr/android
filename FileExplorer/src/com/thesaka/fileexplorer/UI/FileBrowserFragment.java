/**
 * 
 */
package com.thesaka.fileexplorer.UI;

import com.thesaka.fileexplorer.R;
import com.thesaka.fileexplorer.common.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ramamurthy
 * 
 */
public class FileBrowserFragment extends Fragment {
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.debug(FileBrowserFragment.class, "onAttach()");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(FileBrowserFragment.class, "onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.debug(FileBrowserFragment.class, "onCreateView()");
		View rootView = inflater.inflate(R.layout.fragment_file_broswer, container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.debug(FileBrowserFragment.class, "onActivityCreated()");
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Logger.debug(FileBrowserFragment.class, "onStart()");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Logger.debug(FileBrowserFragment.class, "onResume()");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Logger.debug(FileBrowserFragment.class, "onPause()");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Logger.debug(FileBrowserFragment.class, "onStop()");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.debug(FileBrowserFragment.class, "onDestroyView()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.debug(FileBrowserFragment.class, "onDestroy()");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Logger.debug(FileBrowserFragment.class, "onDetach()");
	}

}
