package com.example.weatherwithfriends.friends.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ImageTable {
	 // Database table
	  public static final String TABLE_IMAGES= "images"; 
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_DATE = "date";
	  public static final String COLUMN_URL = "url";
	  public static final String COLUMN_FILE = "file";

	  public static void onCreate(SQLiteDatabase database) {
		StringBuilder sb = new StringBuilder("create table if not exists ").append(TABLE_IMAGES).append("(").append(COLUMN_ID)
		.append(" integer primary key autoincrement, ").append(COLUMN_DATE).append(" text, ").append(COLUMN_URL) 
		.append(" text, ").append(COLUMN_FILE).append(" text ").append(");");
	
		Log.v("making imagedatabase", sb.toString());
	    database.execSQL(sb.toString());
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(FriendTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
	    onCreate(database);
	  }
}
