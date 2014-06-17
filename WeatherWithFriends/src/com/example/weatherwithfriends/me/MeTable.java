package com.example.weatherwithfriends.me;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MeTable {
	 // Database table
	  public static final String TABLE_ME= "me";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_CITY = "city";
	  public static final String COLUMN_STATE = "state";
	  public static final String COLUMN_COUNTRY = "country";
	  public static final String COLUMN_TEMP = "temp";
	  public static final String COLUMN_TXT = "text";
	  public static final String COLUMN_ICON = "icon";
	  public static final String COLUMN_TIME= "time";


	  public static void onCreate(SQLiteDatabase database) {
		StringBuilder sb = new StringBuilder("create table if not exists ").append(TABLE_ME).append("(").append(COLUMN_ID)
		.append(" integer primary key autoincrement, ").append(COLUMN_CITY) 
		.append(" text, ").append(COLUMN_STATE).append(" text, ").append(COLUMN_COUNTRY).
		append(" text, ").append(COLUMN_TEMP).append(" text, ").append(COLUMN_TXT).append(" text, ").append(COLUMN_ICON)
		.append(" text, ").append(COLUMN_TIME).append(" text not null").append(");");
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