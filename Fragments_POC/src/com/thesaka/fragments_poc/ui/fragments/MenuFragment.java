package com.thesaka.fragments_poc.ui.fragments;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.thesaka.fragments_poc.BaseNavActivity;
import com.thesaka.fragments_poc.R;
import com.thesaka.fragments_poc.utility.Logger;

public abstract class MenuFragment extends Fragment {

	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	
	public enum MenuItemsEnum {
		Item_1("Item 1"),
		Item_2("Item 2"),
		Item_3("Item 3"),
		Item_4("Item 4");

		MenuItemsEnum(String name){
			this.name = name;
		}

		public static MenuItemsEnum getEnum(String name){
			if(name.equalsIgnoreCase(Item_2.name))
				return Item_2;
			else if(name.equalsIgnoreCase(Item_3.name))
				return Item_3;
			else if(name.equalsIgnoreCase(Item_4.name))
				return Item_4;
			else
				return Item_1;
		}

		public static MenuItemsEnum getEnum(int ordinal){
			switch (ordinal) {
			case 1:
				return Item_1;
			case 2:
				return Item_2;
			case 3:
				return Item_3;
			default:
				return Item_4;
			}
		}

		public String getName(){
			return this.name;
		}

		private String name;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.debug(this.getClass(), "onAttach()"); 
		try {
			mCallBack = (MenuSelectionCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement MenuSelectionCallback");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(this.getClass(), "onCreate()"); 
		/*if (savedInstanceState != null) {
			mCurrentSelection = (MenuItemsEnum) savedInstanceState.getSerializable(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}else{
			mCurrentSelection = mCurrentSelection==null ? MenuItemsEnum.Item_1 : mCurrentSelection;
		}*/
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.debug(this.getClass(), "onActivityCreated()"); 
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.debug(this.getClass(), "onSaveInstanceState()");
//		outState.putSerializable(STATE_SELECTED_POSITION, mCurrentSelection);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Logger.debug(this.getClass(), "onDetach() - Callbacks = NULL");
		mCallBack = null;
	}
	
	protected final void init(ListView listView) {
		Logger.debug(this.getClass(), "initMenuListView()");
		this.mListView = listView;
		prepareListAdapter();
		prepareListView();
		prepareActionBar();
	}
	
	protected void prepareListAdapter(){
		int[] listImageArray = new int[]{
				R.drawable.available_offline,
				R.drawable.cloud,
				R.drawable.place,
				R.drawable.location_searching
		};
		int menuListLength = EnumSet.allOf(MenuItemsEnum.class).size();
		String[] menuList = new String[menuListLength];
		int i=0;
		for(MenuItemsEnum menu : EnumSet.allOf(MenuItemsEnum.class)){
			menuList[i++] = menu.getName();
		}
		
		mAdapter = new MenuListAdapter(getActivity(), menuList, listImageArray);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Logger.error(this.getClass(), "mListView.setOnItemClickListener - onItemClick - "+position);
				selectItem(MenuItemsEnum.getEnum(position+1));
			}
		});
	}
	
	protected void prepareListView(){
		MenuItemsEnum selectedMenu = ((BaseNavActivity)getActivity()).getCurrentSelection();
		if(selectedMenu == null)
			selectedMenu = MenuItemsEnum.Item_1;
		mListView.setItemChecked(selectedMenu.ordinal(), true);
	}
	
	protected void prepareActionBar(){
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	protected void selectItem(MenuItemsEnum selectedMenu) {
//		Logger.debug(MenuNavigationDrawerFragment.class, "selectItem() - "+selectedMenu.ordinal());
		mCallBack.onMenuItemSelected(selectedMenu);
	}
	
	public void setCurrentSelection(MenuItemsEnum selectedMenu){
		if (mListView != null) {
			mListView.setItemChecked(selectedMenu.ordinal(), true);
		}
	}
	
	protected ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}
	
	protected ListView getListView() {
		return mListView;
	}
	
	protected void setCallBack(MenuSelectionCallback callBack){
		this.mCallBack = callBack;
	}
	
	public class MenuListAdapter extends BaseAdapter {

		private List<Entry<String,Integer>> mData;
		private Context mContext;

		public MenuListAdapter(Context context, String[] menuList, int[] resourceList){
			this.mContext = context;
			
			List<Entry<String, Integer>> drawerData = new ArrayList<Map.Entry<String,Integer>>();
			this.mData = drawerData;
			int i=0;
			for(String menu : menuList){
				Entry<String, Integer> entry = new NavigationDrawerEntry(menu, resourceList[i++]);
				drawerData.add(entry);
			}
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			Logger.debug(this.getClass(), "DrawerListItemAdapter.getView()");
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(convertView == null){
				convertView = inflater.inflate(R.layout.drawer_list_item, null, false);
			}
			ImageView iv = (ImageView) convertView.findViewById(R.id.drawer_item_image_view);
			TextView tv = (TextView)convertView.findViewById(R.id.drawer_item_text_view);

			Entry<String, Integer> rowData = (Entry<String, Integer>) getItem(position);
			tv.setText(rowData.getKey());
			iv.setImageResource(rowData.getValue());
			return convertView;
		}
		
		private class NavigationDrawerEntry implements Map.Entry<String, Integer>{

			NavigationDrawerEntry(String key, Integer value) {
				this.key = key;
				this.value = value;
			}

			@Override
			public String getKey() {
				return this.key;
			}

			@Override
			public Integer getValue() {
				return this.value;
			}

			@Override
			public Integer setValue(Integer object) {
				this.value = object;
				return this.value;
			}

			String key;
			Integer value;
		}

	}
	
	public static interface MenuSelectionCallback {
		void onMenuItemSelected(MenuItemsEnum selectedMenu);
	}
	
	private ListView mListView;
	private MenuListAdapter mAdapter;
	private MenuSelectionCallback mCallBack;
//	private MenuItemsEnum mCurrentSelection = MenuItemsEnum.Item_1;
	protected boolean mFromSavedInstanceState;

}