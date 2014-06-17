package com.example.weatherwithfriends;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.view.View;

import com.example.weatherwithfriends.friends.contentprovider.FriendContentProvider;
import com.example.weatherwithfriends.friends.database.FriendTable;

public class FriendController {
	private boolean status = false;
	private Uri friendUri;
	Time today = new Time(Time.getCurrentTimezone());
	public boolean asyncDone = false;
	
	public FriendController() {
		today.setToNow();
	}
	
	
	public void addSelf (Context c, String city, String state, String country) {
		 ContentValues myEntry = new ContentValues();
		 myEntry.put(FriendTable.COLUMN_FRIEND, "ME");
		 myEntry.put(FriendTable.COLUMN_CITY, city);
		 myEntry.put(FriendTable.COLUMN_STATE, state);
		 myEntry.put(FriendTable.COLUMN_COUNTRY, country);
		 myEntry.put(FriendTable.COLUMN_TIME, today.toString());
		 
		 friendUri = c.getContentResolver().insert(FriendContentProvider.CONTENT_URI, myEntry);
	}
	
	public void addFriend(Context c, String name, String city, String state, String country){
	    ContentValues myEntry = new ContentValues();
	    myEntry.put(FriendTable.COLUMN_FRIEND, name);
	    myEntry.put(FriendTable.COLUMN_CITY, city);
	    myEntry.put(FriendTable.COLUMN_STATE, state);
	    myEntry.put(FriendTable.COLUMN_COUNTRY, country);
	    myEntry.put(FriendTable.COLUMN_TIME, today.toString());
	 
	    friendUri = c.getContentResolver().insert(FriendContentProvider.CONTENT_URI, myEntry);
	}
	
	public void deleteFriend(View v, Long id) {
		//View parent = (View) v.getParent();
		Uri uri = Uri.parse(FriendContentProvider.CONTENT_URI + "/" + id);
		FriendContentProvider fcp = new FriendContentProvider();
		fcp.delete(uri, null, null);
	}
	
	public Cursor getSelf() {
		return null;
	}
	
	public Cursor getFriends(Context c) {
			
		Uri friendsUri = FriendContentProvider.CONTENT_URI;
		
		Cursor cur = c.getContentResolver().query(friendsUri, null, null, null, null);
		
		int dateCol = cur.getColumnIndex(FriendTable.COLUMN_TIME);
		int idCol = cur.getColumnIndex(FriendTable.COLUMN_ID);
		int cityCol = cur.getColumnIndex(FriendTable.COLUMN_CITY);
		int stateCol = cur.getColumnIndex(FriendTable.COLUMN_STATE);
		int countryCol = cur.getColumnIndex(FriendTable.COLUMN_COUNTRY);
		String uTime;
		
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			uTime = cur.getString(dateCol);
			if (!UpdateOk(today, uTime)) {
				//need to async!!! 
				FindFriendWeather ffw = new FindFriendWeather(c, cur.getString(idCol), null);
				ffw.execute(cur.getString(cityCol), cur.getString(stateCol),cur.getString(countryCol));
				
				c.getContentResolver().update(friendUri + "/friend" + cur.getString(idCol), values, null, null);
			}
		}
		return cur;
	}
	
	public void UpdateFriendWeather() {
		
	}
	/*
	
	public class FetchMyDataTaskCompleteListener implements AsyncTaskCompleteListener
    {

		@Override
		public void onTaskComplete(String[] result) {

			myEntry.put(FriendTable.COLUMN_TIME, result[0]);
			myEntry.put(FriendTable.COLUMN_TEMP, result[1]);
			myEntry.put(FriendTable.COLUMN_TXT, result[2]);
			myEntry.put(FriendTable.COLUMN_ICON, getImage(result[3])); 
			//{today.toString(), temperature, txt_forecast, iconurl}

			getContentResolver().update(todoUri, myEntry, null, null)
			
		}
    }
	
	*/
	private boolean UpdateOk(Time t, String uT) {
		//toString time is stored YYYYMMDDTHHMMSS
		int uYear = Integer.parseInt(uT.substring(0,4));
		int uMonth = Integer.parseInt(uT.substring(4,6));
		int uDate = Integer.parseInt(uT.substring(6, 8));
		int uHour = Integer.parseInt(uT.substring(9,11));
		int uMinute = Integer.parseInt(uT.substring(11, 13));

		if (uYear == t.year && uMonth == t.month && uDate == t.monthDay) {
			if (uHour == t.hour) {
				//same hour
				if (t.minute - uMinute > 15) {
					return true;
				}
				else {
					return false;
				}
			}
			else if (t.hour > uHour) {
				//difference between hours
				if (((60-uMinute) + (t.minute)) <= 15) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	private byte[] getImage(String iconurl) {
		//get image stuff
		URL iUrl = null;
		try {
			iUrl = new URL(iconurl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			URLConnection ucon = iUrl.openConnection();
			
			InputStream is = ucon.getInputStream();BufferedInputStream bis = new BufferedInputStream(is);
			
			ByteArrayBuffer baf = new ByteArrayBuffer(500);
			int current = 0;
			
			while((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			
			return baf.toByteArray();
		} catch (Exception e) {
			Log.d("ImageManager", "Error" +e.toString());
		}

		return null;

	}
}
