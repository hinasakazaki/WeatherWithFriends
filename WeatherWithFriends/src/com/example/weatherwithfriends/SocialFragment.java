package com.example.weatherwithfriends;

import java.util.ArrayList;

import com.example.weatherwithfriends.HomeFragment.FindWeather;
import com.example.weatherwithfriends.adapter.FriendArrayAdapter;
import com.example.weatherwithfriends.friends.contentprovider.FriendContentProvider;
import com.example.weatherwithfriends.friends.database.FriendTable;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SocialFragment extends Fragment{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.social_fragment, container, false);
 
		ArrayList<Friend> friendsList = new ArrayList<Friend>();
		ListView list = (ListView) rootView.findViewById(R.id.list);
		Uri friendsUri = FriendContentProvider.CONTENT_URI;
		//An array specifying which columns to return
		String[] projection = new String[]{FriendTable.COLUMN_FRIEND, FriendTable.COLUMN_CITY, FriendTable.COLUMN_STATE, FriendTable.COLUMN_COUNTRY};
		
		ArrayList<String> friendsName = new ArrayList<String>(); 
		ArrayList<String> friendsCity = new ArrayList<String>(); 
		ArrayList<String>  friendsState = new ArrayList<String>(); 
		ArrayList<String>  friendsCountry = new ArrayList<String>(); 
		
		Cursor cur = getActivity().getContentResolver().query(friendsUri, 
				projection, 
				null,
				null,
				null);
		int i = 0;
		int nameColumn = cur.getColumnIndex(FriendTable.COLUMN_FRIEND);
		int cityColumn = cur.getColumnIndex(FriendTable.COLUMN_CITY);
		int stateColumn = cur.getColumnIndex(FriendTable.COLUMN_STATE);
		int countryColumn = cur.getColumnIndex(FriendTable.COLUMN_COUNTRY);
		
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
			//make sure nullpointers are resolved
			if (cur.getString(nameColumn) == null) {
				friendsName.add(i, " ");
			}
			friendsName.add(i, cur.getString(nameColumn));
			
			
			if (cur.getString(cityColumn) == null) {
				friendsCity.add(i, " ");
			}
			friendsCity.add(i, cur.getString(cityColumn));
			
			
			if (cur.getString(stateColumn) == null) {
				friendsState.add(i, " ");
			}
			friendsState.add(i, cur.getString(stateColumn));
			
			if (cur.getString(countryColumn) == null) {
				friendsCountry.add(i, " ");
			}
			friendsCountry.add(i, cur.getString(countryColumn));
			i++;
		}
		
		cur.close();
		
		int j = 0;
		if (friendsName.size() > 0) {
			for (String s : friendsName) {
				String f = null;
				String c = null;
				String st = null;
				String co = null;
				
				if (friendsName.get(j) != null) {
					f = friendsName.get(j);
				}
				if (friendsCity.get(j) != null) {
					c = friendsCity.get(j);
				}
				if (friendsState.get(j) != null) {
					st = friendsState.get(j);
				}
				if (friendsCountry.get(j) != null) {
					co = friendsCountry.get(j);
				}
				friendsList.add(new Friend(f, c, st, co));
				j++;
			}
		} else {
			TextView tv = (TextView) rootView.findViewById(R.id.add_friends);
		 	tv.setText("It's lonely in here... Add some friends!");
		}
		
		if (friendsList != null) {
			FriendArrayAdapter adapter = new FriendArrayAdapter(this.getActivity().getBaseContext(), friendsList);
			list.setAdapter(adapter);
		}
		
      
        return rootView;
    }

	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
	

	}
	
}
