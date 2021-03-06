package com.example.weatherwithfriends;

import android.app.Activity;
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
import com.example.weatherwithfriends.friends.database.ImageTable;

public class FriendController {
	Time today;
	public boolean asyncDone = false;

	
	public static void addFriend(final Context c, String name, String city, String state, String country){
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		
		Cursor cur = c.getContentResolver().query(FriendContentProvider.FRIEND_CONTENT_URI, null, null, null, null);
		final Long numRows = Long.valueOf(cur.getCount());
		Log.v("Number of rows", ""+ numRows);
		cur.close(); 
		
		ContentValues myEntry = new ContentValues();
	    myEntry.put(FriendTable.COLUMN_FRIEND, name);
	    myEntry.put(FriendTable.COLUMN_CITY, city);
	    myEntry.put(FriendTable.COLUMN_STATE, state);
	    myEntry.put(FriendTable.COLUMN_COUNTRY, country);
	    myEntry.put(FriendTable.COLUMN_TIME, today.toString());
	    myEntry.put(FriendTable.COLUMN_STATUS, 1);
	    c.getContentResolver().insert(FriendContentProvider.FRIEND_CONTENT_URI, myEntry);
	    
		FindFriendWeather ffw = new FindFriendWeather(new CallMeBack() {
			@Override
			public void onTaskDone(String[] result) {
				AddFragment.worked(true);
			}

			@Override
			public void onTaskError() {
				//delete friend
				Log.v("addFriend", "onTaskError");
				Activity activity = (Activity)c;
				FriendController.deleteFriend(activity.getWindow().getDecorView().getRootView(), numRows+1);
				AddFragment.worked(false);
			}

			@Override
			public byte[] onTaskFinished(byte[] result) {
				// TODO Auto-generated method stub
				return result;
			}
		});
				
		ffw.execute(city, state, country);		
	}
	
	
	public static void deleteFriend(View v, Long id) {
		Log.v("At delete friend!", "row " + id); 
		v.getContext().getContentResolver().delete(FriendContentProvider.FRIEND_CONTENT_URI, FriendTable.COLUMN_ID+ "=" + id, null);
	}
	
	public static Cursor getSelf(Context c) {
		LocationManager loc = null;
		Location here = null;
		String provider;
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		
		Log.v("At getSelf", "good!");
		
		Cursor cur = c.getContentResolver().query(FriendContentProvider.FRIEND_CONTENT_URI, null, FriendTable.COLUMN_STATUS+ "="+ "0", null, null);

		
		if (!cur.moveToFirst() || needsUpdate(today, cur.getString(cur.getColumnIndex(FriendTable.COLUMN_TIME)))) {
			//deal
			Log.v("updating for getSelf", "Don't know if this ever happens");
			
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
	
	
	public static void addSelf (final Context c, Location loc) {
		String i = ""+ 0;
//		FindWeather fw = new FindWeather(c, i);
		Cursor cur = c.getContentResolver().query(FriendContentProvider.FRIEND_CONTENT_URI, null, FriendTable.COLUMN_STATUS+ "="+ "0", null, null);
//		fw.execute(loc);
		
		FindWeather fw = new FindWeather(new CallMeBack() {
            @Override
            public void onTaskDone(String[] result) {
            	
            	Log.v("Myself is updated", result[1]);
            	
            	ContentValues myEntry = new ContentValues();
        		
        		Time today = new Time(Time.getCurrentTimezone());
        		today.setToNow();
        		
        		myEntry.put(FriendTable.COLUMN_TIME, result[0]);
        		myEntry.put(FriendTable.COLUMN_TEMP, result[1]);
        		myEntry.put(FriendTable.COLUMN_TXT, result[2]);
        		myEntry.put(FriendTable.COLUMN_LOCATION, result[4]);
        		myEntry.put(FriendTable.COLUMN_ICON, result[3]);
        		myEntry.put(FriendTable.COLUMN_STATUS,  0);
        		myEntry.put(FriendTable.COLUMN_TIME, today.toString());
        		
        		Cursor cur = c.getContentResolver().query(FriendContentProvider.FRIEND_CONTENT_URI, null, FriendTable.COLUMN_STATUS+ "="+ "0", null, null);
        		
        		if (!cur.moveToFirst()) {
        			c.getContentResolver().insert(FriendContentProvider.FRIEND_CONTENT_URI, myEntry);
        		} else {
        			c.getContentResolver().update(FriendContentProvider.FRIEND_CONTENT_URI, myEntry, FriendTable.COLUMN_STATUS+"="+0, null);
        		}
        		
            }

			@Override
			public void onTaskError() {
				Log.v("This shouldn't happen", "because it's myself, it doesn't error");
			}

			@Override
			public byte[] onTaskFinished(byte[] result) {
				return result;
				// TODO Auto-generated method stub
				
			}
		});
		
		fw.execute(loc);
	}
	
	public static byte[] getImage(final Context c, final String url) {
		//if imageAlready exists in dataabase
		Cursor cur = c.getContentResolver().query(FriendContentProvider.IMAGE_CONTENT_URI, null, ImageTable.COLUMN_URL+ "="+ "'" + url  + "'", null, null);
		if (cur.moveToFirst()) {
			return cur.getBlob(cur.getColumnIndex(ImageTable.COLUMN_FILE));
		} else {
			//this part definitely happens
			GetImage gi = new GetImage(new CallMeBack(){
				@Override
				public void onTaskDone(String[] result) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void onTaskError() {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public byte[] onTaskFinished(byte[] result) {
					
					ContentValues myEntry = new ContentValues();
	        		
	        		Time today = new Time(Time.getCurrentTimezone());
	        		today.setToNow();
	        		
	        		myEntry.put(ImageTable.COLUMN_URL, url);
	        		myEntry.put(ImageTable.COLUMN_FILE, result);
	        		myEntry.put(ImageTable.COLUMN_DATE, today.toString());
	        		
	        		c.getContentResolver().insert(FriendContentProvider.IMAGE_CONTENT_URI, myEntry);
	        		
	        		return result;
				}
				
			});
			gi.execute(url);
		}
		return null;
	}
	
	public static Cursor getFriends(Context c) {
			
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		//doesnt return anything 
		Cursor cur = c.getContentResolver().query(FriendContentProvider.FRIEND_CONTENT_URI, null, FriendTable.COLUMN_STATUS+ "="+ "1", null, null);
		
		int dateCol = cur.getColumnIndex(FriendTable.COLUMN_TIME);
		int idCol = cur.getColumnIndex(FriendTable.COLUMN_ID);
		int cityCol = cur.getColumnIndex(FriendTable.COLUMN_CITY);
		int stateCol = cur.getColumnIndex(FriendTable.COLUMN_STATE);
		int countryCol = cur.getColumnIndex(FriendTable.COLUMN_COUNTRY);
		int tempCol = cur.getColumnIndex(FriendTable.COLUMN_TEMP);
		String uTime;
		
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			//Log.v("are we even here", "UGH"); yes we are
			uTime = cur.getString(dateCol);
			boolean timesUp = (needsUpdate(today, uTime));
			boolean noTemp = (cur.getString(tempCol) == null);
			if (timesUp || noTemp) {
				//need to async!!! 
				if (cur.getString(idCol) != null) {
					updateFriend(c, cur.getString(idCol), cur.getString(cityCol), cur.getString(stateCol),cur.getString(countryCol));		
				}
			}
		}
		return cur;
	}
	
	private static void  updateFriend(final Context c, final String id, String city, String state, String country) {
		Log.v("UpdateFriend", "is executing");
		if (id == null) {
			return;
		}
		FindFriendWeather ffw = new FindFriendWeather(new CallMeBack() {
            @Override
            public void onTaskDone(String[] result) {
            
            	//updateFriendWeather part
            	ContentValues myEntry = new ContentValues();
        		
        		Time today = new Time(Time.getCurrentTimezone());
        		today.setToNow();
        		
        		Log.v("UpdateFreindWeather", "we're here!"); //never here -- well, it is, but I have no fucking clue why
        		myEntry.put(FriendTable.COLUMN_TIME, result[0]);
        		myEntry.put(FriendTable.COLUMN_TEMP, result[1]);
        		myEntry.put(FriendTable.COLUMN_TXT, result[3]);
        		Log.v("This hould be full location", result[2]);
        		myEntry.put(FriendTable.COLUMN_LOCATION, result[2]);
        		myEntry.put(FriendTable.COLUMN_ICON, result[4]);
        		myEntry.put(FriendTable.COLUMN_STATUS,  1);
        		myEntry.put(FriendTable.COLUMN_TIME, today.toString());
        		
        		c.getContentResolver().update(FriendContentProvider.FRIEND_CONTENT_URI, myEntry, FriendTable.COLUMN_ID+"="+id, null);
            }

			@Override
			public void onTaskError() {
				Log.v("addFriend", "onTaskError");
				Activity activity = (Activity)c;
				FriendController.deleteFriend(activity.getWindow().getDecorView().getRootView(), Long.getLong(id));
				AddFragment.worked(false);
			}

			@Override
			public byte[] onTaskFinished(byte[] result) {
				return result;
				// TODO Auto-generated method stub
				
			}
		});
		ffw.execute(city, state, country);		
	}
	
	

	private static boolean needsUpdate(Time t, String uT) {
		//toString time is stored YYYYMMDDTHHMMSS
		//Log.v("at needs Update", t.toString() + uT);
		int uYear = Integer.parseInt(uT.substring(0,4));
		int uMonth = Integer.parseInt(uT.substring(4,6));
		int uDate = Integer.parseInt(uT.substring(6, 8));
		int uHour = Integer.parseInt(uT.substring(9,11));
		int uMinute = Integer.parseInt(uT.substring(11, 13));
		
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
				if (((60-uMinute) + (t.minute)) < 15) {
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
