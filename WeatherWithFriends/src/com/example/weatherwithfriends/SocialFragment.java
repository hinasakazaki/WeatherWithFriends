package com.example.weatherwithfriends;

import java.util.ArrayList;

import com.example.weatherwithfriends.HomeFragment.FindWeather;
import com.example.weatherwithfriends.adapter.FriendArrayAdapter;
import com.example.weatherwithfriends.friends.database.FriendTable;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SocialFragment extends Fragment{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.social_fragment, container, false);
        
		ListView list = (ListView) rootView.findViewById(R.id.list);
		

		ArrayList<Friend> friendsList = null;
		
//		ArrayList<String> friendsName = null;
//		
//		ArrayList<String> friendsCity = null;
//		
//		ArrayList<String> friendsState = null;
//		
//		ArrayList<String> friendsCountry = null;
		
		String[] friendsName = new String[] {FriendTable.COLUMN_FRIEND};
		String[] friendsCity = new String[] {FriendTable.COLUMN_CITY};
		String[] friendsState = new String[] {FriendTable.COLUMN_STATE};
		String[] friendsCountry = new String[] {FriendTable.COLUMN_COUNTRY};
		
		int i = 0;
		for (String s : friendsName) {
			friendsList.add(new Friend(friendsName[i], friendsCity[i], friendsState[i], friendsCountry[i]));
			i++;
		}
		
		/*
		 * //get da list
		Activity mActivity = getActivity();
	
		
		if (mActivity instanceof MainActivity) {
			friendsList = ((MainActivity)mActivity).getFriendsList();
		}
		
		FriendArrayAdapter adapter = new FriendArrayAdapter(this.getActivity().getBaseContext(), friendsList);
		
        */
		
		//get list from SQLite onto friendsList
		
		
		FriendArrayAdapter adapter = new FriendArrayAdapter(this.getActivity().getBaseContext(), friendsList);
		
		list.setAdapter(adapter);
		
        return rootView;
    }
	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		

	}
}
