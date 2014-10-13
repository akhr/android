/**
 * 
 */
package com.akh.adapter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.akh.activity.R;
import com.akh.common.AkhFormatter;
import com.akh.dataBean.LapEntry;

/**
 * @author Akhash Ramamurthy
 * StopWatchLapCounter
 * May 25, 2012
 */
public class AKExpandableListViewAdapter extends BaseExpandableListAdapter {
	
	private static final String TAG = AkhListViewAdapter.class.getSimpleName();

	private Context context = null;
	private int collapsedGroupLayout, expandedGroupLayout;
	protected List<Entry<String, List<LapEntry>>> data;
	protected LayoutInflater inflate;

	public AKExpandableListViewAdapter(Context context, List<Entry<String, List<LapEntry>>> data) {
		this.context = context;
		this.data = data;
	}

	public Object getChild(int groupPosition, int childPosition) {
//		Entry<String, List<LapEntry>> entry = data.get(groupPosition);
//		List<LapEntry> childList = entry.getValue();
//		return childList.get(childPosition);
		return  data.get(groupPosition).getValue().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return data.get(groupPosition).getValue().size();
	}

	

	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
	/*	Log.e(TAG,"AkhArrayListAdapter GetView()");

		if(getChild(groupPosition, childPosition) == null)
			return null;
		
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_view_child_row, parent, false);
		}
		
		LapEntry lapEntry = (LapEntry)getChild(groupPosition, childPosition);
		String lap = AkhFormatter.lapTimeDisplayFormatter(lapEntry.getLapTime());
		((TextView) convertView.findViewById(R.id.lapTimeValue)).setText(lap);

		String start = AkhFormatter.dateToStringFormatter(lapEntry.getLapStartTime(), AkhFormatter.LIST_VIEW_DISPLAY_DATE_FORMAT);
		((TextView) convertView.findViewById(R.id.lapStartTimeValue)).setText(start);
		
		String stop = AkhFormatter.dateToStringFormatter(lapEntry.getLapStopTime(), AkhFormatter.LIST_VIEW_DISPLAY_DATE_FORMAT);
		((TextView) convertView.findViewById(R.id.lapStopTimeValue)).setText(stop);
		
		if(lapEntry.getComment() == null || lapEntry.getComment().length() <= 0){
			convertView.findViewById(R.id.lapCommentContainer).setVisibility(View.GONE);
		}
	*/
		return convertView;
	}

	public Object getGroup(int groupPosition) {
		return data.get(groupPosition);
	}

	public int getGroupCount() {
		return data.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
