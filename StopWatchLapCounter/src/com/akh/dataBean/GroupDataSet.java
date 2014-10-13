/**
 * 
 */
package com.akh.dataBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.util.Log;

/**
 * @author akhashr
 *
 */
public class GroupDataSet implements Entry<String, List<LapEntry>> {

	private static final String TAG = GroupDataSet.class.getSimpleName();

	private String dateKey;
	private List<LapEntry> subList;

	/**
	 * 
	 */
	public GroupDataSet(String key, LapEntry firstEntry) {
		Log.d(TAG, "Creating New Entry with key = "+ dateKey);
		subList = new ArrayList<LapEntry>();
		this.dateKey = key;
		subList.add(firstEntry);
	}


	public List<LapEntry> setValue(List<LapEntry> object) {
		return subList;
	}

	public List<LapEntry> getValue() {
		return subList;
	}

	public String getKey() {
		return dateKey;
	}
	
	public int getSubListSize(){
		return subList.size();
	}
	
	public void addToSubList(LapEntry lapEntry){
		subList.add(lapEntry);
	}

}
