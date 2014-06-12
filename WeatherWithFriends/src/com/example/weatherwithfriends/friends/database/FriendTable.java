package com.example.weatherwithfriends.friends.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FriendTable {
	 // Database table
	  public static final String TABLE_FRIENDS= "friend";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_FRIEND= "friend";
	  public static final String COLUMN_CITY = "city";
	  public static final String COLUMN_STATE = "state";
	  public static final String COLUMN_COUNTRY = "country";
	  public static final String COLUMN_TEMP = "temp";
	  public static final String COLUMN_TXT = "text";
	  public static final String COLUMN_ICON = "icon";


	  public static void onCreate(SQLiteDatabase database) {
		StringBuilder sb = new StringBuilder("create table if not exists ").append(TABLE_FRIENDS).append("(").append(COLUMN_ID)
		.append(" integer primary key autoincrement, ").append(COLUMN_FRIEND).append(" text not null, ").append(COLUMN_CITY) 
		.append(" text not null, ").append(COLUMN_STATE).append(" text not null, ").append(COLUMN_COUNTRY).append(" text not null").append(")");
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
