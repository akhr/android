/**
 * 
 */
package com.akh.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.akh.activity.MainActivity;
import com.akh.activity.R;
import com.akh.common.AkhUtil;
import com.akh.dataBean.LapEntry;

/**
 * @author Akhash
 *
 */
public class AkhListAdapterHelper implements AkhListAdapterHelperInterface {

	private ListView listView;
	private ExpandableListView expandableListView;
	private static final String TAG = AkhListAdapterHelper.class.getSimpleName();

	private AkhListViewAdapter adapter;
	private AkhExpandableListViewAdapter expandableListViewAdapter;
	//	private List<LapEntry> listDataStructure;

	private MainActivity context;
	
	public AkhListAdapterHelper(Context context){
		this.context = (MainActivity)context;
	}

	public AkhListAdapterHelper(Context context, ExpandableListView expandableListView) {
		this.context = (MainActivity)context;
//		this.listView = (ListView)((Activity)context).findViewById(R.id.list);
//		addListViewAdapter();
//		addlistViewListener();
		
		this.expandableListView = expandableListView;
		addExpandableListViewAdapter();
		
	}

	private void addListViewAdapter(){
		adapter = new AkhListViewAdapter(context, context.getDataSource().getAllLapEntries());
		listView.setAdapter(adapter);
	}
	
	private void addExpandableListViewAdapter(){
		expandableListViewAdapter = new AkhExpandableListViewAdapter(context, AkhUtil.groupByDataset(context.getDataSource().getAllLapEntries()));
		expandableListView.setAdapter(expandableListViewAdapter);
	}

	public void refreshListView(){
		Log.e(TAG, "refreshing the ListView");
//		adapter.updateDataList(context.getDataSource().getAllLapEntries());
		expandableListViewAdapter.updateDataList(AkhUtil.groupByDataset(context.getDataSource().getAllLapEntries()));
	}

	private void addlistViewListener(){
		listView.setOnItemClickListener(new OnItemClickListener() { 
			
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
				Log.e(TAG,"ListView - onItemClick - position : "+ position);
				Log.e(TAG,"ListView - onItemClick - row id : "+ id);
				Log.e(TAG, "Lap time = "+ ((LapEntry)adapter.getItem(position)).getLapTime());
				Log.e(TAG, "Lap Start time = "+ ((LapEntry)adapter.getItem(position)).getLapStartTime().toString());
				Log.e(TAG, "Lap Stop time = "+ ((LapEntry)adapter.getItem(position)).getLapStopTime().toString());
				Log.e(TAG, "Lap Comment = "+ ((LapEntry)adapter.getItem(position)).getComment());
/*				Intent i = new Intent(context, EditLapEntryActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constants.TO_BE_EDITED_LAP_ENTRY, (LapEntry)adapter.getItem(position));
				i.putExtras(bundle);
				context.startActivity(i);*/
				
				context.showEditDialogFor((LapEntry)adapter.getItem(position));
			}
		});
	}
	
}
