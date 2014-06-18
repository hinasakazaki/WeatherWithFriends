package com.example.weatherwithfriends;


import com.example.weatherwithfriends.friends.database.FriendTable;
import com.example.weatherwithfriends.me.MeTable;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
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

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		
		FriendController fc = new FriendController();
		
		Cursor cur = fc.getSelf(rootView.getContext());
		
		if (cur.moveToFirst()) {
			fillData(rootView, cur);
		}
		
		
		return rootView;
	}
	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		
	
	}
	
	private void fillData(View v, Cursor cur) {
		
		MyContentObserver mObserver = new MyContentObserver(new Handler(), v, cur);
		cur.registerContentObserver(mObserver);
		
		int locColumn = cur.getColumnIndex(MeTable.COLUMN_LOCATION);
		int tempColumn = cur.getColumnIndex(MeTable.COLUMN_TEMP);
		int txtColumn = cur.getColumnIndex(MeTable.COLUMN_TXT);
		int iconColumn = cur.getColumnIndex(MeTable.COLUMN_ICON);
		int timeColumn = cur.getColumnIndex(MeTable.COLUMN_TIME);
		
		
		TextView loc = (TextView)v.findViewById(R.id.location);
		TextView tv = (TextView)v.findViewById(R.id.temperature);
		TextView dv = (TextView)v.findViewById(R.id.description);
		ImageView iv = (ImageView)v.findViewById(R.id.icon);
		
		//load from table, update
		while (!loaded) {
			String location = cur.getString(locColumn);
			String temp = cur.getString(tempColumn);
			String txtForecast = cur.getString(txtColumn);
			Bitmap icon = BitmapFactory.decodeByteArray(cur.getBlob(iconColumn), 0, cur.getBlob(iconColumn).length);
				
			loc.setText(location);
	
			tv.setText(temp);
				
			dv.setText(txtForecast);
			
			//image view -- where to construct
			iv.setImageBitmap(icon); 
			
		}
	}
	private class MyContentObserver extends ContentObserver {
		private View view;
		private Cursor cursor;
		MyContentObserver(Handler handler, View view, Cursor cursor) {  
			super(handler);  
			this.cursor = cursor;
			this.view = view;
		}  

		public boolean deliverSelfNotifications() {  
			return true;  
		}  

		public void onChange(boolean selfChange) {  
			super.onChange(selfChange);  
			//refill? 
			Log.v("Saw change", "refresh!?");
			fillData(view, cursor);
		}  
	}  
	
	

}
