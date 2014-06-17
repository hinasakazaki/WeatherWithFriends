package com.example.weatherwithfriends;


import com.example.weatherwithfriends.friends.database.FriendTable;

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

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		

		FriendController fc = new FriendController();
	
		//check first time install
		Cursor cur = fc.getSelf(rootView.getContext());
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
		
		
		TextView loc = (TextView)rootView.findViewById(R.id.location);
		TextView tv = (TextView)rootView.findViewById(R.id.temperature);
		TextView dv = (TextView)rootView.findViewById(R.id.description);
		ImageView iv = (ImageView)rootView.findViewById(R.id.icon);
		
		//load from table, update
		
		String name = cur.getString(nameColumn);
		String city = cur.getString(cityColumn);
		String state = cur.getString(stateColumn);
		String temp = cur.getString(tempColumn);
		String txtForecast = cur.getString(txtColumn);
		Bitmap icon = BitmapFactory.decodeByteArray(cur.getBlob(iconColumn), 0, cur.getBlob(iconColumn).length);
		
		if (name.equals("ME")) {
			//UIs
			loc.setText(city + ", " + state);

			tv.setText(temp);
			
			dv.setText(txtForecast);

			//image view -- where to construct
			iv.setImageBitmap(icon);  
		} else 
		{
			fc.addSelf(rootView.getContext(), "San Francisco", "CA", "");
		}
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
