/**
 * 
 */
package com.akh.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.akh.constants.Constants;
import com.akh.dataBean.GroupDataSet;
import com.akh.dataBean.LapEntry;

/**
 * @author akhashr
 *
 */
public class AkhUtil {

	public static List<GroupDataSet> groupByDataset(List<LapEntry> inputList){
		Calendar cal = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		StringBuffer buffer = new StringBuffer();
		List<GroupDataSet> dataSet = new ArrayList<GroupDataSet>();
		List<String> keyList = new ArrayList<String>();

		for(LapEntry lapEntry : inputList){
			cal.setTime(lapEntry.getLapStartTime());
			buffer.delete(0, buffer.length());
			buffer.append(cal.get(Calendar.YEAR)).append("_").append(cal.get(Calendar.DAY_OF_YEAR));
			
			if(!keyList.contains(buffer.toString())){
				keyList.add(buffer.toString());
				buffer.delete(0, buffer.length());
				if(cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
					buffer.append("Today");
				else{
					buffer.append(Constants.DAY_MON_YR_FORMAT.format(lapEntry.getLapStartTime()));
					
					/*
					switch (cal.get(Calendar.DAY_OF_WEEK)) {
					case 1:
						buffer.append("Sun ");
						break;
					case 2:
						buffer.append("Mon ");
						break;
					case 3:
						buffer.append("Tues ");
						break;
					case 4:
						buffer.append("Wed ");
						break;
					case 5:
						buffer.append("Thurs ");
						break;
					case 6:
						buffer.append("Fri ");
						break;
					case 7:
						buffer.append("Sat ");
						break;
					}
					*/
					
					
				}
				dataSet.add(new GroupDataSet(buffer.toString(), lapEntry));
			}else
				dataSet.get(keyList.indexOf(buffer.toString())).addToSubList(lapEntry);
		}
		return dataSet;
	}
	
}
