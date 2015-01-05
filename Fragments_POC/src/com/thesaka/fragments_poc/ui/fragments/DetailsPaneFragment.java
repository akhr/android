/**
 * 
 */
package com.thesaka.fragments_poc.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thesaka.fragments_poc.BaseNavActivity;
import com.thesaka.fragments_poc.R;
import com.thesaka.fragments_poc.SecondActivity;
import com.thesaka.fragments_poc.ui.fragments.MenuFragment.MenuItemsEnum;
import com.thesaka.fragments_poc.utility.Logger;

/**
 * @author Akhash Ramamurthy (Thesaka) 
 * Oct 14, 2014
 * DetailsPaneFragment.java
 */
public class DetailsPaneFragment extends Fragment {
	
	private static final String ARG_MENU_SELECTION = "section_number";

	public static DetailsPaneFragment newInstance(MenuItemsEnum menu) {
		DetailsPaneFragment detailsFrag = new DetailsPaneFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_MENU_SELECTION, menu);
		detailsFrag.setArguments(args);
		return detailsFrag;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.debug(DetailsPaneFragment.class, "onAttach()");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(DetailsPaneFragment.class, "onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.debug(DetailsPaneFragment.class, "onCreateView()");
		return inflater.inflate(R.layout.details_fragment, container, false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		Logger.debug(DetailsPaneFragment.class, "onCreateOptionsMenu()");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.debug(DetailsPaneFragment.class, "onActivityCreated()");
		mImageView = (ImageView) getView().findViewById(R.id.imageView);
		mTextView = (TextView) getView().findViewById(R.id.textView);
		mMenuEnum = (MenuItemsEnum)getArguments().getSerializable(ARG_MENU_SELECTION);
		mTextView.setText(mMenuEnum.getName());
		getView().findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.debug(DetailsPaneFragment.class, "Button.onClick()");
				startActivityForResult(new Intent(getActivity(), SecondActivity.class), 100);
				Toast.makeText(getActivity(), "Start Second Activity for Result - request code = "+100, Toast.LENGTH_LONG).show();
			}
		});
		
		getView().findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.debug(DetailsPaneFragment.class, "Dialog Button.onClick()");
				MyDialogFragment dialog = new MyDialogFragment();
				dialog.show(getFragmentManager(), "myDialog");
			}
		});
		
		int imageRes = 0;
		switch (mMenuEnum) {
		case Item_1:
			imageRes = R.drawable.image_1;
			break;
		case Item_2:
			imageRes = R.drawable.image_2;
			break;
		case Item_3:
			imageRes = R.drawable.image_3;
			break;
		case Item_4:
			imageRes = R.drawable.image_4;
			break;
		}
		mImageView.setImageResource(imageRes);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Logger.debug(DetailsPaneFragment.class, "onStart()");
		((BaseNavActivity) getActivity()).onSectionAttached(mMenuEnum);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Logger.debug(DetailsPaneFragment.class, "onResume()");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Logger.debug(DetailsPaneFragment.class, "onPause()");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Logger.debug(DetailsPaneFragment.class, "onStop()");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.debug(DetailsPaneFragment.class, "onDestroyView()");
	}
	
	@Override
	public void onDestroyOptionsMenu() {
		super.onDestroyOptionsMenu();
		Logger.debug(DetailsPaneFragment.class, "onDestroyOptionsMenu()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.debug(DetailsPaneFragment.class, "onDestroy()");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Logger.debug(DetailsPaneFragment.class, "onDetach()");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Logger.debug(this.getClass(), "onActivityResult() - Request code - "+requestCode);
		Logger.debug(this.getClass(), "onActivityResult() - Result code - "+resultCode);
		Toast.makeText(getActivity(), "Received result from Second Activity - request code = "+requestCode, Toast.LENGTH_LONG).show();
	}
	
	public MenuItemsEnum getMenuItemEnum(){
		return this.mMenuEnum;
	}
	
	private ImageView mImageView;
	private TextView mTextView;
	private MenuItemsEnum mMenuEnum;

}
