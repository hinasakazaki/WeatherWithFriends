package com.example.weatherwithfriends;

import java.util.ArrayList;

import com.example.weatherwithfriends.HomeFragment.FindWeather;
import com.example.weatherwithfriends.adapter.FriendArrayAdapter;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SocialFragment extends Fragment{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.social_fragment, container, false);
         
        return rootView;
    }
	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		
		
		ArrayList<Friend> friendsList = null;
		
		//get da list
		Activity mActivity = getActivity();
	
		
		if (mActivity instanceof MainActivity) {
			friendsList = ((MainActivity)mActivity).getFriendsList();
		}
		
		FriendArrayAdapter adapter = new FriendArrayAdapter(this.getActivity().getBaseContext(), friendsList);
		
		
		
	}
	
}
