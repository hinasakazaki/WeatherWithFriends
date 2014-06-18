package com.example.weatherwithfriends.me;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MeTable {
	 // Database table
	  public static final String TABLE_ME= "me";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_LOCATION = "location";
	  public static final String COLUMN_TEMP = "temp";
	  public static final String COLUMN_TXT = "text";
	  public static final String COLUMN_ICON = "icon";
	  public static final String COLUMN_TIME= "time";


	  public static void onCreate(SQLiteDatabase database) {
		StringBuilder sb = new StringBuilder("create table if not exists ").append(TABLE_ME).append("(").append(COLUMN_ID)
		.append(" integer primary key autoincrement, ").append(COLUMN_LOCATION) 
		.append(" text, ").append(COLUMN_TEMP).append(" text, ").append(COLUMN_TXT).append(" text, ").append(COLUMN_ICON)
		.append(" text, ").append(COLUMN_TIME).append(" text").append(");");
	    database.execSQL(sb.toString());
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(MeTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_ME);
	    onCreate(database);
	  }
}