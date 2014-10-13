/**
 * 
 */
package com.akh.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akh.activity.R;
import com.akh.common.AkhFormatter;
import com.akh.dataBean.LapEntry;

/**
 * @author Akhash
 *
 */
public class AkhListViewAdapter extends BaseAdapter {

	private static final String TAG = AkhListViewAdapter.class.getSimpleName();

	List<LapEntry> dataList;
	Context context;

	/**
	 * 
	 */
	public AkhListViewAdapter(Context context, List<LapEntry> dataList) {
		this.context = context;
		this.dataList = dataList;
	}

	public void updateDataList(List<LapEntry> dataList){
		this.dataList = dataList;
		Log.e(TAG, "Inside refreshing : list count = "+ dataList.size());
		notifyDataSetChanged();
	}

	public int getCount() {
		return dataList.size();
	} 

	public Object getItem(int position) {
		return dataList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		Log.e(TAG,"AkhArrayListAdapter GetView()");

		if(dataList.get(position) == null)
			return null;
		
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.child_row_layout, parent, false);
		}
		
		/** Formatting Loading the Lap Time*/
		LapEntry lapEntry = (LapEntry)getItem(position);
		String lap = AkhFormatter.lapTimeDisplayFormatter(lapEntry.getLapTime());
		((TextView) convertView.findViewById(R.id.lapTimeValue)).setText(lap);

		/** Formatting Loading the Lap Start Time*/
		String start = AkhFormatter.dateToStringFormatter(lapEntry.getLapStartTime(), AkhFormatter.LIST_VIEW_DISPLAY_DATE_FORMAT);
		((TextView) convertView.findViewById(R.id.lapStartTimeValue)).setText(start);
		
		/** Formatting Loading the Lap Stop Time*/
		String stop = AkhFormatter.dateToStringFormatter(lapEntry.getLapStopTime(), AkhFormatter.LIST_VIEW_DISPLAY_DATE_FORMAT);
		((TextView) convertView.findViewById(R.id.lapStopTimeValue)).setText(stop);
		
		if(lapEntry.getComment() == null || lapEntry.getComment().length() <= 0){
			convertView.findViewById(R.id.lapCommentContainer).setVisibility(View.GONE);
		}

		return convertView;

	}

	@Override
	public void notifyDataSetChanged() {
		Log.e(TAG, "notifyDataSetChanged called");

		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	}
	
	 public static class ViewHolder {
	        public TextView lap;
	        public TextView start;
	        public TextView stop;
	        public TextView comment;
	    }



}
