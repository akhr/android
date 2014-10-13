/**
 * 
 */
package com.akh.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.akh.common.AkhFormatter;
import com.akh.dataBean.LapEntry;

/**
 * @author Akhash Ramamurthy
 * StopWatchLapCounter
 * May 30, 2012
 */
public class AkhSQLiteDataSource {

	private static final String TAG = AkhSQLiteDataSource.class.getSimpleName();
	private AkhSQLiteOpenHelper dbHelper;

	/**
	 * 
	 */
	public AkhSQLiteDataSource(Context context) {
		dbHelper = new AkhSQLiteOpenHelper(context);
	}

	public void close() {
		dbHelper.close();
	}

	public long addLapEntry(LapEntry lapEntry) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_LAP_START_TIME, AkhFormatter.dbStoreFormatDateToString(lapEntry.getLapStartTime()));
		values.put(DBConstants.COLUMN_LAP_END_TIME, AkhFormatter.dbStoreFormatDateToString(lapEntry.getLapStopTime()));
		values.put(DBConstants.COLUMN_LAP_TIME, lapEntry.getLapTime());
		values.put(DBConstants.COLUMN_WAS_PAUSED, lapEntry.wasPaused() ? 1 : 0);
		values.put(DBConstants.COLUMN_COMMENT, lapEntry.getComment());
		values.put(DBConstants.COLUMN_LONGITUDE, lapEntry.getLongitude());
		values.put(DBConstants.COLUMN_LATITUDE, lapEntry.getLatitude());

		long newRowIdInserted = db.insert(DBConstants.LAP_HISTORY_TABLE, null, values);
		db.close(); // Closing database connection
		return newRowIdInserted;
	}

	public List<LapEntry> getAllLapEntries(){

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<LapEntry> lapEntries = new ArrayList<LapEntry>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + DBConstants.LAP_HISTORY_TABLE;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToLast()) {
			do {
				LapEntry lapEntry = new LapEntry(
						AkhFormatter.stringToDateFormatter(cursor.getString(0), AkhFormatter.DB_STORAGE_DATE_FORMAT), 
						AkhFormatter.stringToDateFormatter(cursor.getString(1), AkhFormatter.DB_STORAGE_DATE_FORMAT), 
						cursor.getString(2),
						cursor.getInt(3) == 0 ? false : true,
						cursor.getString(4),
						cursor.getString(5),
						cursor.getString(6));
				Log.e(TAG, lapEntry.toString());
				lapEntries.add(lapEntry);
			} while (cursor.moveToPrevious());
		}

		db.close(); // Closing database connection
		return lapEntries;
	}

	public int deleteLapEntry(LapEntry lapEntry) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String recordId = AkhFormatter.dbStoreFormatDateToString(lapEntry.getLapStartTime());
		Log.e(TAG, "Lap Entry with Start Time ---- " + recordId + " ---- is deleted");
		
		int noOfRowsAffected = db.delete(DBConstants.LAP_HISTORY_TABLE, DBConstants.COLUMN_LAP_START_TIME + " = ?", new String[] { recordId });		
		db.close();
		return noOfRowsAffected;
	}
	
	public int updateLapEntry(LapEntry lapEntry){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String recordId = AkhFormatter.dbStoreFormatDateToString(lapEntry.getLapStartTime());
		
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_LAP_START_TIME, AkhFormatter.dbStoreFormatDateToString(lapEntry.getLapStartTime()));
		values.put(DBConstants.COLUMN_LAP_END_TIME, AkhFormatter.dbStoreFormatDateToString(lapEntry.getLapStopTime()));
		values.put(DBConstants.COLUMN_LAP_TIME, lapEntry.getLapTime());
		values.put(DBConstants.COLUMN_COMMENT, lapEntry.getComment());
		values.put(DBConstants.COLUMN_LONGITUDE, lapEntry.getLongitude());
		values.put(DBConstants.COLUMN_LATITUDE, lapEntry.getLatitude());
		
		// updating row
	    int rowNumberAffected = db.update(DBConstants.LAP_HISTORY_TABLE, values, DBConstants.COLUMN_LAP_START_TIME + " = ?",
	            new String[] { AkhFormatter.dbStoreFormatDateToString(lapEntry.getLapStartTime()) });
	    
	    db.close();
	    return rowNumberAffected;
	}


}
