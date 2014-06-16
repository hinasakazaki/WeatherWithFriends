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
	
	
	public void addSelf (String city, String state, String country) {
		//TODO
	}
	
	public void addFriend(Context c, String name, String city, String state, String country){
	    ContentValues myEntry = new ContentValues();
	    myEntry.put(FriendTable.COLUMN_FRIEND, "ME");
	    myEntry.put(FriendTable.COLUMN_CITY, "San Francisco");
	    myEntry.put(FriendTable.COLUMN_COUNTRY, "");
	    myEntry.put(FriendTable.COLUMN_TIME, today.toString());
	   
	    //call asynctask
	    
	    FindFriendWeather ffw = new FindFriendWeather(c, myEntry, new FetchMyDataTaskCompleteListener());
	  
	    ffw.execute(city, state, country);
	 
	    friendUri = c.getContentResolver().insert(FriendContentProvider.CONTENT_URI, myEntry);
	}
	
	public Cursor getFriends() {
		return null;
		
		//check for update, then asynctask
		//return cursor for fragment to use
		
	}
	
	
	public class FetchMyDataTaskCompleteListener implements AsyncTaskCompleteListener
    {

		@Override
		public void onTaskComplete(String[] result) {

			myEntry.put(FriendTable.COLUMN_TIME, result[0]);
			myEntry.put(FriendTable.COLUMN_TEMP, result[1]);
			myEntry.put(FriendTable.COLUMN_TXT, result[2]);
			myEntry.put(FriendTable.COLUMN_ICON, getImage(result[3])); //to image?? result[3] is image
			//{today.toString(), temperature, txt_forecast, iconurl}

			getContentResolver().update(todoUri, myEntry, null, null)
			
		}
    }
	
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
