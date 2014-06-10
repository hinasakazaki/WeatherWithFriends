package com.example.weatherwithfriends.adapter;

import com.example.weatherwithfriends.AddFragment;
import com.example.weatherwithfriends.HomeFragment;
import com.example.weatherwithfriends.SocialFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	public TabsPagerAdapter(FragmentManager fm) {
	        super(fm);
	    }
	 
	    @Override
	    public Fragment getItem(int index) {
	 
	        switch (index) {
	        case 0:
	            return new AddFragment();
	        case 1:
	            return new HomeFragment();
	        case 2:
	            return new SocialFragment();
	        }
	 
	        return null;
	    }
	 
	    @Override
	    public int getCount() {
	        // get item count - equal to number of tabs
	        return 3;
	    }
	    
}
