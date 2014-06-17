package com.example.weatherwithfriends;


import com.example.weatherwithfriends.friends.database.FriendTable;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
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

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		return rootView;
	}
	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		
		//let's figure out where I am!
		loc = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		if (loc.getAllProviders().size() > 0) {
			provider = loc.getAllProviders().get(0);
			here = loc.getLastKnownLocation(provider);
		}
		
		else {
			Log.d("Couldn't find current location", loc.toString());
		}
	
		FriendController fc = new FriendController();
		fc.addSelf(activity, "San Francisco", "CA", "");
	
		Cursor cur = fc.getSelf(activity);
		cur.moveToFirst();
		MyContentObserver mObserver = new MyContentObserver(new Handler());
		cur.registerContentObserver(mObserver);
		
		int nameColumn = cur.getColumnIndex(FriendTable.COLUMN_FRIEND);
		int cityColumn = cur.getColumnIndex(FriendTable.COLUMN_CITY);
		int stateColumn = cur.getColumnIndex(FriendTable.COLUMN_STATE);
		int tempColumn = cur.getColumnIndex(FriendTable.COLUMN_TEMP);
		int txtColumn = cur.getColumnIndex(FriendTable.COLUMN_TXT);
		int iconColumn = cur.getColumnIndex(FriendTable.COLUMN_ICON);
		int timeColumn = cur.getColumnIndex(FriendTable.COLUMN_TIME);
		
		
		//load from table, update
		if (cur.getString(nameColumn) == "ME") {
			//UIs
			//image view -- where to construct?
			 ImageView iv = (ImageView) getView().findViewById(R.id.icon);
			 iv.setImageBitmap(BitmapFactory.decodeByteArray(cur.getBlob(iconColumn), 0, cur.getBlob(iconColumn).length)); 
		
			 TextView loc = (TextView)getView().findViewById(R.id.location);
			 loc.setText(cur.getString(cityColumn) + ", " + cur.getString(stateColumn));
		
			 TextView tv = (TextView)getView().findViewById(R.id.temperature);
			 tv.setText(cur.getString(tempColumn));
		
			 TextView dv = (TextView)getView().findViewById(R.id.description);
			 dv.setText(cur.getString(txtColumn));
		} else {
			fc.addSelf(activity, "San Francisco", "CA", null);
		}
	}
	private class MyContentObserver extends ContentObserver {  
		MyContentObserver(Handler handler) {  
			super(handler);  
		}  

		public boolean deliverSelfNotifications() {  
			return true;  
		}  

		public void onChange(boolean selfChange) {  
			super.onChange(selfChange);  
			//refill? 
			Log.v("Saw change", "refresh!?");
		}  
	}  
	

}
