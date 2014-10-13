/**
 * 
 */
package com.akh.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.akh.activity.R;
import com.akh.common.AkhFormatter;
import com.akh.constants.FontFaceConstants;
import com.akh.dataBean.GroupDataSet;
import com.akh.dataBean.LapEntry;

/**
 * @author akhashr
 *
 */
public class AkhExpandableListViewAdapter extends BaseExpandableListAdapter {

	private static final String TAG = AkhExpandableListViewAdapter.class.getSimpleName();

	private Typeface childRowTypeFace;
	private Typeface groupRowTypeFace;
	private Context context = null;
	protected List<GroupDataSet> data;

	public AkhExpandableListViewAdapter(Context context, List<GroupDataSet> data) {
		this.context = context;
		this.data = data;
		childRowTypeFace = Typeface.createFromAsset(context.getAssets(),FontFaceConstants.fontFace2);
		groupRowTypeFace = Typeface.createFromAsset(context.getAssets(),FontFaceConstants.fontFace4);;
	}

	public void updateDataList(List<GroupDataSet> data){
		this.data = data;
		Log.e(TAG, "Inside refreshing : group count = "+ data.size());
		notifyDataSetChanged();
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

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Log.e(TAG,"getChildView() gPos = "+groupPosition+" cPos = "+childPosition);

		if(getChild(groupPosition, childPosition) == null)
			return null;

		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.child_row_layout, parent, false);
		}

		/** Formatting Loading the Lap Time*/
		LapEntry lapEntry = (LapEntry)getChild(groupPosition, childPosition);
		String lap = AkhFormatter.lapTimeDisplayFormatter(lapEntry.getLapTime());
		((TextView) convertView.findViewById(R.id.lapTimeValue)).setTypeface(childRowTypeFace);
		((TextView) convertView.findViewById(R.id.lapTimeValue)).setText(lap);

		/** Formatting Loading the Lap Start Time*/
		String start = AkhFormatter.dateToStringFormatter(lapEntry.getLapStartTime(), AkhFormatter.LIST_VIEW_DISPLAY_DATE_FORMAT);
		((TextView) convertView.findViewById(R.id.lapStartTimeValue)).setTypeface(childRowTypeFace);
		((TextView) convertView.findViewById(R.id.lapStartTimeValue)).setText(start);

		/** Formatting Loading the Lap Stop Time*/
		String stop = AkhFormatter.dateToStringFormatter(lapEntry.getLapStopTime(), AkhFormatter.LIST_VIEW_DISPLAY_DATE_FORMAT);
		((TextView) convertView.findViewById(R.id.lapStopTimeValue)).setTypeface(childRowTypeFace);
		((TextView) convertView.findViewById(R.id.lapStopTimeValue)).setText(stop);

		if(lapEntry.getComment() == null || lapEntry.getComment().length() <= 0){
			convertView.findViewById(R.id.lapCommentContainer).setVisibility(View.GONE);
		}

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

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.group_row_layout, parent, false);
		} 
		
		

		if(isExpanded){
			LayoutParams layoutParams = ((ViewGroup)convertView.findViewById(R.id.group_container)).getLayoutParams();
			layoutParams.height = 95;
			((View)convertView.findViewById(R.id.group_container)).setLayoutParams(layoutParams);
			convertView.findViewById(R.id.lapHeaderContainer) .setVisibility(View.VISIBLE);
		}else{
			LayoutParams layoutParams = ((ViewGroup)convertView.findViewById(R.id.group_container)).getLayoutParams();
			layoutParams.height = 60;
			((View)convertView.findViewById(R.id.group_container)).setLayoutParams(layoutParams);
			convertView.findViewById(R.id.lapHeaderContainer) .setVisibility(View.GONE);
		}
			
		((TextView)convertView.findViewById(R.id.lapTimeHeader)).setTypeface(groupRowTypeFace);
		((TextView)convertView.findViewById(R.id.lapStartTimeHeader)).setTypeface(groupRowTypeFace);
		((TextView)convertView.findViewById(R.id.lapStopTimeHeader)).setTypeface(groupRowTypeFace);
		
		((TextView)convertView.findViewById(R.id.groupHeaderValue)).setTypeface(groupRowTypeFace);
		((TextView)convertView.findViewById(R.id.groupHeaderValue)).setText(data.get(groupPosition).getKey());
		
		return convertView;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
