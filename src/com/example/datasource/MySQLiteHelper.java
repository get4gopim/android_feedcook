package com.example.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	  public static final String TABLE_NAME = "feeds";
	  
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_0 = "feedName";
	  public static final String COLUMN_1 = "feedUrl";
	  public static final String COLUMN_2 = "fav";
	  public static final String COLUMN_3 = "feedSource";

	  private static final String DATABASE_NAME = "feeds.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_NAME  + "("  
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_0 + " text not null, "
	      + COLUMN_1 + " text, "
	      + COLUMN_2 + " text, "
	      + COLUMN_3 + " text);";

	  public MySQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	  }

	}
