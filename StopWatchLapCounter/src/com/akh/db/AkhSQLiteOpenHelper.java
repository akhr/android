/**
 * 
 */
package com.akh.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Akhash Ramamurthy
 * StopWatchLapCounter
 * May 30, 2012
 */
public class AkhSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "stopWatchLapCounter.db";
	private static final int DATABASE_VERSION = 2;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ DBConstants.LAP_HISTORY_TABLE + "( " 
			+ DBConstants.COLUMN_LAP_START_TIME + " TEXT primary key, "
			+ DBConstants.COLUMN_LAP_END_TIME + " TEXT, "
			+ DBConstants.COLUMN_LAP_TIME + " TEXT, "
			+ DBConstants.COLUMN_WAS_PAUSED + " INTEGER, "
			+ DBConstants.COLUMN_COMMENT + " TEXT, "
			+ DBConstants.COLUMN_LONGITUDE + " TEXT, "
			+ DBConstants.COLUMN_LATITUDE + " TEXT);";
	
	/**int flag = (boolValue)? 1 : 0;
	 * @param context
	 */
	public AkhSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(AkhSQLiteOpenHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.LAP_HISTORY_TABLE);
		onCreate(db);
	}

}
