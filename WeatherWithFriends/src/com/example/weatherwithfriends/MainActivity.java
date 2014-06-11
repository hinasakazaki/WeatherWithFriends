package com.example.weatherwithfriends;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.example.weatherwithfriends.adapter.TabsPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener{

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Add", "Home", "Friends" };
	
	Fragment HomeFragment;
	
	private static ArrayList<Friend> friendsList = new ArrayList<Friend>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs errors here!!!
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		
		Log.v("Added tabs", "Ok?");
	
		
		
//		/**
//		 * on swiping the viewpager make respective tab selected
//		 * */s
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		
			@Override
			public void onPageSelected(int position) {
//				 on changing the page
//				 make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});	

		//make sure friendslist isn't empty
		
		
		Friend j = new Friend("Chris", "Mountain View", "CA", "");
		addFriend(j);
		Log.v("Friendslist", friendsList.toString());
		
		Friend f = new Friend("Evan", "Hong Kong", "", "");
		addFriend(f);
		Log.v("Friendslist", friendsList.toString());
		
		changeTab(1);
		
    }


	@Override
	public void onTabSelected(Tab tab,
			android.app.FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}


	@Override
	public void onTabUnselected(Tab tab,
			android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabReselected(Tab tab,
			android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	public void addFriend(Friend f) {
		friendsList.add(f);

		
		/*
		String filename = "friends";
		//save to file
		FileOutputStream fos;
		
		try {
			File friendsFile = new File(Environment.getExternalStorageDirectory().getPath() + filename);
			friendsFile.createNewFile();FileOutputStream fOut = new FileOutputStream(friendsFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			//append new friend to list?
			myOutWriter.append(f.toString());
			myOutWriter.close();
			Log.v("File written", filename);
			fOut.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();}
				*/
	}
	
	public ArrayList<Friend> getFriendsList() {
		
		
		/*
		ArrayList<Friend> returnList = new ArrayList<Friend>();
		//read from file
		String filename = "friends";
		StringBuffer stringBuffer = new StringBuffer();
		String aDataRow = "";
		String aBuffer = "";
		
		try {
			File myFile = new File(Environment.getExternalStorageDirectory().getPath() + filename);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
				returnList.add(aDataRow);
				Log.v("Reading file", aDataRow);
			}
			myReader.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		*/
		return friendsList;
	}
	
	public static void removeFriend(int i) {
		friendsList.remove(i);
	}
	
	public void changeTab(Integer i) {
		viewPager.setCurrentItem(i);
	}
}
