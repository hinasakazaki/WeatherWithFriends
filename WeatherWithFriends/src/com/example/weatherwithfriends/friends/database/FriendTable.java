package com.example.weatherwithfriends.friends.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FriendTable {
	 // Database table
	  public static final String TABLE_FRIENDS= "friends"; 
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_FRIEND = "friend";
	  public static final String COLUMN_CITY = "city";
	  public static final String COLUMN_STATE = "state";
	  public static final String COLUMN_COUNTRY = "country";
	  public static final String COLUMN_TEMP = "temp";
	  public static final String COLUMN_TXT = "text";
	  public static final String COLUMN_ICON = "icon";
	  public static final String COLUMN_TIME= "time";
	  public static final String COLUMN_LOCATION= "fulllocation";
	  public static final String COLUMN_STATUS = "friendorme"; //1 if friend, 0 if me 

	  public static void onCreate(SQLiteDatabase database) {
		StringBuilder sb = new StringBuilder("create table if not exists ").append(TABLE_FRIENDS).append("(").append(COLUMN_ID)
		.append(" integer primary key autoincrement, ").append(COLUMN_FRIEND).append(" text, ").append(COLUMN_CITY) 
		.append(" text, ").append(COLUMN_STATE).append(" text, ").append(COLUMN_STATUS).append(" text, ").append(COLUMN_COUNTRY).
		append(" text, ").append(COLUMN_TEMP).append(" text, ").append(COLUMN_TXT).append(" text, ").append(COLUMN_ICON)
		.append(" text, ").append(COLUMN_TIME).append(" text, ").append(COLUMN_LOCATION).append(" text ").append(");");
		Log.v("blah", sb.toString());
	    database.execSQL(sb.toString());
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(FriendTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
	    onCreate(database);
	  }
}
