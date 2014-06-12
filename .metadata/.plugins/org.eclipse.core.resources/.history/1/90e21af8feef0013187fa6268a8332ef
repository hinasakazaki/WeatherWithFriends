package com.example.weatherwithfriends.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import com.example.weatherwithfriends.AddFragment;
import com.example.weatherwithfriends.HomeFragment;
import com.example.weatherwithfriends.SocialFragment;

import android.app.Fragment;
import android.app.Fragment.SavedState;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	public TabsPagerAdapter(android.support.v4.app.FragmentManager fm) {
	        super(fm);
	    }
	 
	    @Override
	    public android.support.v4.app.Fragment getItem(int index) {
	 
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
