package com.example.weatherwithfriends.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

public class TabsPagerAdapter {
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
