package com.example.weatherwithfriends;


import com.example.weatherwithfriends.friends.database.FriendTable;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment{
	
	Location here;
	String provider;
	LocationManager loc;
	public static boolean loaded = false;
	View mView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);		
		mView = rootView;
		fillData();
		return rootView;
	}
	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
	}
	
	private void fillData() {

		Log.v("Fill data is called", "yay?");
		
		Cursor cur = FriendController.getSelf(mView.getContext());
		
		if (!cur.moveToFirst()) {
			TextView tv = (TextView) mView.findViewById(R.id.loading);
		 	tv.setText("Counting Sunrays...");
		}
		
		
		MyContentObserver mObserver = new MyContentObserver(new Handler(), mView);
		cur.registerContentObserver(mObserver);
		
		if (cur.moveToFirst()) {
			Log.v("Should be filling stuff up", "lol");
			int locColumn = cur.getColumnIndex(FriendTable.COLUMN_LOCATION);
			int tempColumn = cur.getColumnIndex(FriendTable.COLUMN_TEMP);
			int txtColumn = cur.getColumnIndex(FriendTable.COLUMN_TXT);
			int iconColumn = cur.getColumnIndex(FriendTable.COLUMN_ICON);
		
			
			TextView loc = (TextView)mView.findViewById(R.id.location);
			TextView tempv = (TextView)mView.findViewById(R.id.temperature);
			TextView dv = (TextView)mView.findViewById(R.id.description);
			ImageView iv = (ImageView)mView.findViewById(R.id.icon);
			
		//load from table, update
	
			String location = cur.getString(locColumn);
			String temp = cur.getString(tempColumn);
			String txtForecast = cur.getString(txtColumn);
			
			byte[] iconByteArray = FriendController.getImage(mView.getContext(), cur.getString(iconColumn));
			//throws error because not there yet
			Bitmap icon = null;
			if (iconByteArray != null) {
				icon = BitmapFactory.decodeByteArray(iconByteArray, 0, iconByteArray.length);
			}	
			
			loc.setText(location);
	
			tempv.setText(temp);
				
			dv.setText(txtForecast);
			//image view -- where to construct
			iv.setImageBitmap(icon); 
		
		}
		cur.unregisterContentObserver(mObserver);
	}
	
	private class MyContentObserver extends ContentObserver {
		private View view;
		MyContentObserver(Handler handler, View view) {  
			super(handler);  
			this.view = view;
		}  

		public boolean deliverSelfNotifications() {  
			return true;  
		}  

		public void onChange(boolean selfChange) {  
			super.onChange(selfChange);  
			//refill? 
			Log.v("Home Fragment show change", "refresh!?");
			fillData();
		}  
	}  
	
	

}
