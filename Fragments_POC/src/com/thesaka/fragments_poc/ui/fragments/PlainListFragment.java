package com.thesaka.fragments_poc.ui.fragments;


import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thesaka.fragments_poc.R;
import com.thesaka.fragments_poc.ui.fragments.MenuFragment.MenuSelectionCallback;
import com.thesaka.fragments_poc.utility.Logger;

public class PlainListFragment extends Fragment {

	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	private enum ModeEnum{
		SELECT,
		EDIT;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.debug(this.getClass(), "onAttach()"); 
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(this.getClass(), "onCreate()"); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listview_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.debug(this.getClass(), "onActivityCreated()"); 
		setHasOptionsMenu(true);
		init();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.debug(this.getClass(), "onSaveInstanceState()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.debug(this.getClass(), "onDetach() - Callbacks = NULL");
	}

	protected final void init() {
		Logger.debug(this.getClass(), "initMenuListView()");
		this.mListView = (ListView) getView().findViewById(R.id.list_view);
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

		TypedArray rawTextArray = getResources().obtainTypedArray(R.array.plain_list_items);
		String[] listItemArray = new String[rawTextArray.length()];
		for(int i=0; i<rawTextArray.length(); i++){
			listItemArray[i] = rawTextArray.getString(i);
		}

		mAdapter = new ListAdapter(getActivity(), listItemArray, listImageArray);
		mListView.setAdapter(mAdapter);
	}

	protected void prepareListView(){
		mMode = ModeEnum.SELECT;
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		mListView.setMultiChoiceModeListener(mActionModeListener);

		/*		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
				mListView.setMultiChoiceModeListener(mActionModeListener);
				return false;
			}
		});*/
	}

	protected void prepareActionBar(){
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
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

	private AbsListView.MultiChoiceModeListener mActionModeListener = new AbsListView.MultiChoiceModeListener() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mMode = ModeEnum.SELECT;
			mActionMode = null;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mMode = ModeEnum.EDIT;
			mActionMode = mode;
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.multi_select_contextual_menu, menu);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				mAdapter.remove();
				mode.finish();
				return true;
			default:
				return false;
			}

		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			Logger.error(this.getClass(), "mListView.MultiChoiceModeListener - onItemCheckedStateChanged - pos -"+position+" , Checked - "+checked);
			Toast.makeText(getActivity(), "User selected item # "+position, Toast.LENGTH_SHORT).show();
			mAdapter.toggleSelection(position, checked);
			mode.setTitle(String.valueOf(mAdapter.getSelectedCount()) + " selected");
		}
	};

	public class ListAdapter extends BaseAdapter {

		private List<Entry<String,Integer>> mData;
		private SparseBooleanArray mSelectedArray;
		private Context mContext;
		private Integer mSelectedRadio = null;

		public ListAdapter(Context context, String[] menuList, int[] resourceList){
			this.mContext = context;
			this.mSelectedArray = new SparseBooleanArray();

			List<Entry<String, Integer>> drawerData = new ArrayList<Entry<String,Integer>>();
			this.mData = drawerData;

			int i=0,j=0;
			for(String menu : menuList){
				j=i;
				Entry<String, Integer> entry = new SimpleEntry<String, Integer>(menu, resourceList[j%4]);
				i++;
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(convertView == null){
				convertView = inflater.inflate(R.layout.list_row_item, parent, false);
			}
			final CheckBox cB = (CheckBox) convertView.findViewById(R.id.row_item_checkBox);
			final RadioButton rB = (RadioButton)convertView.findViewById(R.id.row_item_radioBtn);
			TextView tv = (TextView)convertView.findViewById(R.id.row_item_text_view);

			cB.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Logger.error(this.getClass(), "mListView.checkBox.onClick() - isChecked - "+cB.isChecked());	
					if(mActionMode == null){
						getActivity().startActionMode(mActionModeListener);
					}
					mListView.setItemChecked(position, cB.isChecked());
					toggleSelection(position, cB.isChecked());
				}
			});

			if(mSelectedRadio != null && mSelectedRadio.intValue() == position)
				rB.setChecked(true);
			else
				rB.setChecked(false);
			
			rB.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Logger.error(this.getClass(), "rB.onClick() - ischecked - "+rB.isChecked()+" , mSelectedRadio - "+mSelectedRadio);
					mSelectedRadio = Integer.valueOf(position);
					notifyDataSetChanged();
				}
			});

			if(mMode == ModeEnum.SELECT){
				rB.setVisibility(View.VISIBLE);
				cB.setVisibility(View.GONE);
			}else{
				rB.setVisibility(View.GONE);
				cB.setVisibility(View.VISIBLE);
			}

			Entry<String, Integer> rowData = (Entry<String, Integer>) getItem(position);
			tv.setText(rowData.getKey());
			cB.setChecked(isPositionChecked(position));
			return convertView;
		}

		public boolean isPositionChecked(int position) {
			Boolean result = mSelectedArray.get(position);
			return result == null ? false : result;
		}

		public void removeItem(Entry<String,Integer> object) {
			mData.remove(object);
			notifyDataSetChanged();
		}

		public void remove(){
			for (int i = (mSelectedArray.size() - 1); i >= 0; i--) {
				if(mSelectedArray.valueAt(i)){
					Entry<String, Integer> item = (Entry<String, Integer>) getItem(mSelectedArray.keyAt(i));
					removeItem(item);
				}
			}
		}

		public void toggleSelection(int position, boolean select){
			((CheckBox)mListView.getChildAt(position).findViewById(R.id.row_item_checkBox)).setChecked(select);

			if(select){
				if(!mSelectedArray.get(position, false))
					mSelectedArray.put(position, select);
			}else{
				mSelectedArray.delete(position);
			}
		}

		public int getSelectedCount() {
			return mSelectedArray.size();
		}

		public SparseBooleanArray getSelectedIds() {
			return mSelectedArray;
		}
	}

	private ListView mListView;
	private ListAdapter mAdapter;
	private MenuSelectionCallback mCallBack;
	protected boolean mFromSavedInstanceState;
	private ActionMode mActionMode;
	private ModeEnum mMode;

}