package com.example.datasource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.domain.FeedData;

public class TableDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
									MySQLiteHelper.COLUMN_0, 
									MySQLiteHelper.COLUMN_1,
									MySQLiteHelper.COLUMN_2,
									MySQLiteHelper.COLUMN_3 };

	public TableDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		
		database = dbHelper.getWritableDatabase();
		//dbHelper.onCreate(database);
	}

	public void close() {
		dbHelper.close();
	}

	public void addFeed(FeedData obj) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_0, obj.getFeedName());
		values.put(MySQLiteHelper.COLUMN_1, obj.getFeedUrl());
		values.put(MySQLiteHelper.COLUMN_2, obj.getFav());
		values.put(MySQLiteHelper.COLUMN_3, obj.getFeedSource());
		
		long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null, values);
	}
	
	public void saveFeedById(FeedData obj, long id) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_0, obj.getFeedName());
		values.put(MySQLiteHelper.COLUMN_1, obj.getFeedUrl());
		values.put(MySQLiteHelper.COLUMN_2, obj.getFav());
		values.put(MySQLiteHelper.COLUMN_3, obj.getFeedSource());
		
		database.update(MySQLiteHelper.TABLE_NAME, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public FeedData getFeedById(long id) {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		FeedData obj = cursorToObj(cursor);
		cursor.close();
		return obj;
	}

	public void deleteFeed(FeedData obj) {
		long id = obj.getId();
		System.out.println("FeedData deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public List<FeedData> getAllFeeds() {
		List<FeedData> listData = new ArrayList<FeedData>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FeedData obj = cursorToObj(cursor);
			listData.add(obj);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return listData;
	}

	private FeedData cursorToObj(Cursor cursor) {
		FeedData obj = new FeedData();
		obj.setId(cursor.getLong(0));
		obj.setFeedName(cursor.getString(1));
		obj.setFeedUrl(cursor.getString(2));
		obj.setFav(cursor.getString(3));
		obj.setFeedSource(cursor.getString(4));
		return obj;
	}
}