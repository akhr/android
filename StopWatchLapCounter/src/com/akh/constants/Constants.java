/**
 * 
 */
package com.akh.constants;

import java.text.SimpleDateFormat;

/**
 * @author Akhash Ramamurthy
 * StopWatchLapCounter
 * May 25, 2012
 */
public interface Constants {
	
	public static final String START = "START";
	public static final String STOP = "STOP";
	public static final String PAUSE = "PAUSE";
	public static final String RESUME = "RESUME";
	
	public static final int STOP_DIALOG = 1;
	public static final int EDIT_DIALOG = 2;
	
	public static final String TO_BE_EDITED_LAP_ENTRY = "TO_BE_EDITED_LAP_ENTRY";
	public static final String EDITED_LAP_ENTRY = "EDITED_LAP_ENTRY";
	public static final String TO_BE_DELETED_LAP_ENTRY = "TO_BE_DELETED_LAP_ENTRY";
	
	
	public static int RESULT_SAVE = 1;
	public static int RESULT_DELETE = 2;
	
	/** Activity Communication Action*/
	public static final String ACTION_EDIT = "ACTION_EDIT";
	
	public static SimpleDateFormat DAY_MON_YR_FORMAT = new SimpleDateFormat("E MMM dd yyyy");
	
	
}
