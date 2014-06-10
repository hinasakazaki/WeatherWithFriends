package com.example.weatherwithfriends;

import java.util.ArrayList;

import com.example.weatherwithfriends.HomeFragment.FindWeather;
import com.example.weatherwithfriends.adapter.FriendArrayAdapter;

import android.support.v4.app.Fragment;
import android.app.Activity;
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
		
		View listView = getActivity().findViewById(R.id.list);
		
		ArrayList<Friend> friendsList = null;
		
		//get da list
		Activity mActivity = getActivity();
	
		
		if (mActivity instanceof MainActivity) {
			friendsList = ((MainActivity)mActivity).getFriendsList();
		}
		
		FriendArrayAdapter adapter = new FriendArrayAdapter(this.getActivity().getBaseContext(), friendsList);
		
	
		
	}
	private List<Model> getModel() {
		List<Model> list = new ArrayList<Model>();
		list.add(get("Linux"));
		list.add(get("Windows7"));
		list.add(get("Suse"));
		list.add(get("Eclipse"));
		list.add(get("Ubuntu"));
		list.add(get("Solaris"));
		list.add(get("Android"));
		list.add(get("iPhone"));
		list.add(get("Linux"));
		list.add(get("Windows7"));
		list.add(get("Suse"));
		list.add(get("Eclipse"));
		list.add(get("Ubuntu"));
		list.add(get("Solaris"));
		list.add(get("Android"));
		list.add(get("iPhone"));
		list.add(get("Linux"));
		list.add(get("Windows7"));
		list.add(get("Suse"));
		list.add(get("Eclipse"));
		list.add(get("Ubuntu"));
		list.add(get("Solaris"));
		list.add(get("Android"));
		list.add(get("iPhone"));
		list.add(get("Linux"));
		list.add(get("Windows7"));
		list.add(get("Suse"));
		list.add(get("Eclipse"));
		list.add(get("Ubuntu"));
		list.add(get("Solaris"));
		list.add(get("Android"));
		list.add(get("iPhone"));
		// Initially select one of the items
		list.get(1).setSelected(true);
		return list;
	}

	private Model get(String s) {
		return new Model(s);
	}
}
