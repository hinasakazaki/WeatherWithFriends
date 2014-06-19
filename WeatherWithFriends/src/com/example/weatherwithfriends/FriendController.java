package com.example.weatherwithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.view.View;

import com.example.weatherwithfriends.friends.contentprovider.FriendContentProvider;
import com.example.weatherwithfriends.friends.database.FriendTable;

public class FriendController {
	Time today;
	public boolean asyncDone = false;
	
	public static void addSelf (Context c, Location loc) {
		String i = ""+ 0;
		FindWeather fw = new FindWeather(c, i);
		fw.execute(loc);
	}
	
	public static void addFriend(Context c, String name, String city, String state, String country){
		Time today = new Time(Time.getCurrentTimezone());
		
		ContentValues myEntry = new ContentValues();
	    myEntry.put(FriendTable.COLUMN_FRIEND, name);
	    myEntry.put(FriendTable.COLUMN_CITY, city);
	    myEntry.put(FriendTable.COLUMN_STATE, state);
	    myEntry.put(FriendTable.COLUMN_COUNTRY, country);
	    myEntry.put(FriendTable.COLUMN_TIME, today.toString());
	    myEntry.put(FriendTable.COLUMN_STATUS, 1);
	    c.getContentResolver().insert(FriendContentProvider.CONTENT_URI, myEntry);
	}
	
	public static void deleteFriend(View v, Long id) {
		v.getContext().getContentResolver().delete(FriendContentProvider.CONTENT_URI, FriendTable.COLUMN_ID+ "=" + id, null);
	}
	
	public static Cursor getSelf(Context c) {
		LocationManager loc = null;
		Location here = null;
		String provider;
		
		Log.v("At getSelf", "good!");
		
		Cursor cur = c.getContentResolver().query(FriendContentProvider.CONTENT_URI, null, FriendTable.COLUMN_STATUS+ "="+ "0", null, null);

		if (!cur.moveToFirst()) {
			//deal
			loc = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
			
			if (loc.getAllProviders().size() > 0) {
				provider = loc.getAllProviders().get(0);
				here = loc.getLastKnownLocation(provider);
			}
			
			else {
				Log.d("Couldn't find current location", loc.toString());
			}
			addSelf(c, here);
		}
		return cur;
	}
	
	public static Cursor getFriends(Context c) {
			
		Uri friendsUri = FriendContentProvider.CONTENT_URI;
		Time today = new Time(Time.getCurrentTimezone());
		//doesnt return anything 
		Cursor cur = c.getContentResolver().query(friendsUri, null, FriendTable.COLUMN_STATUS+ "="+ "1", null, null);
		
		int dateCol = cur.getColumnIndex(FriendTable.COLUMN_TIME);
		int idCol = cur.getColumnIndex(FriendTable.COLUMN_ID);
		int cityCol = cur.getColumnIndex(FriendTable.COLUMN_CITY);
		int stateCol = cur.getColumnIndex(FriendTable.COLUMN_STATE);
		int countryCol = cur.getColumnIndex(FriendTable.COLUMN_COUNTRY);
		int tempCol = cur.getColumnIndex(FriendTable.COLUMN_TEMP);
		String uTime;
		
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			uTime = cur.getString(dateCol);
			boolean timesUp = (needsUpdate(today, uTime));
			boolean noTemp = (cur.getString(tempCol) == null);
			if (timesUp || noTemp) {
				//need to async!!! 
				if (cur.getString(idCol) != null) {
					FindFriendWeather ffw = new FindFriendWeather(c, cur.getString(idCol));
					ffw.execute(cur.getString(cityCol), cur.getString(stateCol),cur.getString(countryCol));	
				}
			}
		}
		return cur;
	}
	
	public static void UpdateFriendWeather(long id, Context c, String[] result, byte[] image) {
		//today.toString(), temperature, txt_forecast, iconurl
		ContentValues myEntry = new ContentValues();
		
		Time today = new Time(Time.getCurrentTimezone());
		
		Log.v("UpdateFreindWeather", "we're here!"); //never here
		myEntry.put(FriendTable.COLUMN_TIME, result[0]);
		myEntry.put(FriendTable.COLUMN_TEMP, result[1]);
		myEntry.put(FriendTable.COLUMN_TXT, result[2]);
		myEntry.put(FriendTable.COLUMN_ICON, image); 	
		myEntry.put(FriendTable.COLUMN_STATUS,  1);
		myEntry.put(FriendTable.COLUMN_TIME, today.toString());
		//FriendTable.COLUMN_ID+ "=" + id
		c.getContentResolver().update(FriendContentProvider.CONTENT_URI, myEntry, FriendTable.COLUMN_ID+"="+id, null);
	}

	public static void UpdateMyWeather(long id, Context mContext, String[] result, byte[] image){
		ContentValues myEntry = new ContentValues();
		Time today = new Time(Time.getCurrentTimezone());
		
		myEntry.put(FriendTable.COLUMN_TIME, result[0]);
		myEntry.put(FriendTable.COLUMN_TEMP, result[1]);
		myEntry.put(FriendTable.COLUMN_TXT, result[2]);
		myEntry.put(FriendTable.COLUMN_ICON, image); 	
		myEntry.put(FriendTable.COLUMN_LOCATION, result[4]);
		myEntry.put(FriendTable.COLUMN_STATUS, 0);
		myEntry.put(FriendTable.COLUMN_TIME, today.toString());
		Log.v("UpdateMyWeather!", "Putting in entry");
		mContext.getContentResolver().insert(FriendContentProvider.CONTENT_URI, myEntry);
	}
	
	private static boolean needsUpdate(Time t, String uT) {
		//toString time is stored YYYYMMDDTHHMMSS
		int uYear = Integer.parseInt(uT.substring(0,4));
		int uMonth = Integer.parseInt(uT.substring(4,6));
		int uDate = Integer.parseInt(uT.substring(6, 8));
		int uHour = Integer.parseInt(uT.substring(9,11));
		int uMinute = Integer.parseInt(uT.substring(11, 13));
		
//		Log.v("The two dates", t.toString() + uT);
//		Log.v("Month", ""+ t.month);
//		Log.v("year", ""+ t.year);
//		Log.v("daye", ""+ t.monthDay);
		
		if (uYear == t.year && uMonth == (t.month+1) && uDate == t.monthDay) {
			if (uHour == t.hour) {
				//same hour
				if (t.minute - uMinute < 15) {
					return false;
				}
				else {
					Log.v("Needs to be updated!", ""+(t.minute-uMinute));
					return true;
				}
			}
			else if (t.hour > uHour) {
				//difference between hours
				if (((60-uMinute) + (t.minute)) <= 15) {
					return false;
				}
				else {
					Log.v("Needs to be updated!", ""+((60-uMinute) + (t.minute)));
					return true;
				}
			}
			else {
				Log.v("Needs to be updated!", ""+((60-uMinute) + (t.minute)));
				return true;
			}
		}
		Log.v("Needs to be updated!", "different year, date or month");
		return true;
	}
}
