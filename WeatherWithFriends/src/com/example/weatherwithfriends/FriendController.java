package com.example.weatherwithfriends;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.format.Time;

import com.example.weatherwithfriends.FindFriendWeather.OnTaskCompleted;
import com.example.weatherwithfriends.friends.contentprovider.FriendContentProvider;
import com.example.weatherwithfriends.friends.database.FriendTable;

public class FriendController {
	private boolean status = false;
	private Uri friendUri;
	Time today = new Time(Time.getCurrentTimezone());
	
	public FriendController() {
		today.setToNow();
	}
	
	
	public void addSelf (String city, String state, String country) {
		
	}
	
	private OnTaskCompleted listener = new OnTaskCompleted() {
		@Override
		public void onTaskCompleted() {
			status = true;
		}
    };
	
	public void addFriend(Context c, String name, String city, String state, String country){
	    ContentValues myEntry = new ContentValues();
	    myEntry.put(FriendTable.COLUMN_FRIEND, "ME");
	    myEntry.put(FriendTable.COLUMN_CITY, "San Francisco");
	    myEntry.put(FriendTable.COLUMN_COUNTRY, "");
	    myEntry.put(FriendTable.COLUMN_TIME, today.toString());
	   
	    //call asynctask
	    
	    FindFriendWeather ffw = new FindFriendWeather(listener);
	  
	    ffw.execute(city, state, country);
	    
	    friendUri = c.getContentResolver().insert(FriendContentProvider.CONTENT_URI, myEntry);
	}
	
	public void getFriends() {
		
		//check for update, then asynctask
		//return cursor for fragment to use
		
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
	
	private Drawable getImage(String iconurl) {
		//get image stuff
		URL iUrl = null;
		try {
			iUrl = new URL(iconurl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream content = null;
		try {
			content = (InputStream) iUrl.getContent();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Drawable icon = Drawable.createFromStream(content, "src");
		
		return icon;

	}
}
