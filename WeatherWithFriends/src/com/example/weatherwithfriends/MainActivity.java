package com.example.weatherwithfriends;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.example.weatherwithfriends.HomeFragment.FindWeather;
import com.example.weatherwithfriends.adapter.TabsPagerAdapter;

import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.os.Build;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener{

 	
	FindWeather task;
	Location here;
	String provider;
	LocationManager loc;

		
	private ArrayList<Friend> friends;
	
	ListView lv_home;
	ListView lv_add;
	ListView lv_social;
	

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Add", "Home", "Friends" };
	
	
	ArrayAdapter<String> arrayAdapter_friends;
	ArrayAdapter<String> arrayAdapter_location;
	
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

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		

		//let's figure out where I am!
	
		loc = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		if (loc.getAllProviders().size() > 0) {
			provider = loc.getAllProviders().get(0);
			here = loc.getLastKnownLocation(provider);
		}
		
        FindWeather task = new FindWeather();
        task.execute(here);
		
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private class FindWeather extends AsyncTask <Location, Void, String>{ 
		private Context mContext;
		private final String API_KEY = "86d6e9e9fcdda77c";
		double lat = 0;
		double lon = 0; 
		
		JSONObject jsonresult = null;
		
		@Override
		protected String doInBackground(Location... params) {
			if (params != null) {
				lat = ((Location)params[0]).getLatitude();
				lon = ((Location)params[0]).getLongitude();
			}
			
			final String request = "http://api.wunderground.com/api/86d6e9e9fcdda77c/geolookup/q/" + lat + "," + lon + ".json";
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			
			String responseString = null;
			
			try {
				response = httpclient.execute(new HttpGet(request));
				
				StatusLine statusLine = response.getStatusLine();
				
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					
				}
			}
		}

	}

}
