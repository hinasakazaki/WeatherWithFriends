package com.example.weatherwithfriends.friends.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ImageTable {
	 // Database table
	  public static final String TABLE_IMAGES= "images"; 
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_URI = "uri";
	  public static final String COLUMN_IMAGE = "file";//1 if friend, 0 if me 

	  public static void onCreate(SQLiteDatabase database) {
		StringBuilder sb = new StringBuilder("create table if not exists ").append(TABLE_IMAGES).append("(").append(COLUMN_ID)
		.append(" integer primary key autoincrement, ").append(COLUMN_URI).append(" text, ").append(COLUMN_IMAGE).append(" text ").append(");");
	    database.execSQL(sb.toString());
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(ImageTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
	    onCreate(database);
	  }
}
