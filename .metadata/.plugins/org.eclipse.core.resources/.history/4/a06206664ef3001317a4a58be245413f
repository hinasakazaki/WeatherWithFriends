package com.example.weatherwithfriends.adapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.weatherwithfriends.Friend;
import com.example.weatherwithfriends.MainActivity;
import com.example.weatherwithfriends.R;
import com.example.weatherwithfriends.SocialFragment;
import com.example.weatherwithfriends.WeatherInfo;
import com.example.weatherwithfriends.friends.contentprovider.FriendContentProvider;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendArrayAdapter extends ArrayAdapter<Friend> {
	private final Context context;
	private final ArrayList<Friend> friends;
	protected int position = 0;
	private WeatherInfo thisWeather = null;

	public FriendArrayAdapter(Context context, ArrayList<Friend> friends) {
		super(context, R.layout.friend_row, friends);
		this.context = context;
		this.friends = friends;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		this.position = position;
		View v = convertView;
		
       if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.friend_row, null, false);
        }
       
	   ImageView fView = (ImageView) v.findViewById(R.id.friend_icon);
	   if (position == 0) {
		   fView.setImageResource(R.drawable.jenna);
	   }
	   if (position == 1) {
		   fView.setImageResource(R.drawable.judy);
	   }
	   else {
		   fView.setImageResource(R.drawable.mike);
	   }
	   
	    
	   TextView fText = (TextView) v.findViewById(R.id.friend_name);
	   fText.setText(friends.get(position).getName());
	    
	   TextView lText = (TextView) v.findViewById(R.id.friend_location);
	   lText.setText(friends.get(position).getCity());
	    
	   TextView tText = (TextView) v.findViewById(R.id.friend_temp);
     //  tText.setText(result.getLoc());

       ImageView wView = (ImageView) v.findViewById(R.id.friend_weather_icon);
     //  wView.setImageDrawable(result.getIcon());
       
	   String city = friends.get(position).getCity();
	   String state = friends.get(position).getState();
	   String country = friends.get(position).getCountry();
      
	   thisWeather = new WeatherInfo(city, state, country);
     
      //swipe delete feature
       v.setOnTouchListener(new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				//add delete button, get rid of other stuff
				DeleteButton(v, position);
				return true;
			}
			return false;
		}
    	   
       });
       return v;
    }
	
	private void Cancel(View v) {
		ImageView fView = (ImageView) v.findViewById(R.id.friend_icon);
		
		TextView fText = (TextView) v.findViewById(R.id.friend_name);
		
		TextView lText = (TextView) v.findViewById(R.id.friend_location);
		
		TextView tText = (TextView) v.findViewById(R.id.friend_temp);
		
		ImageView wView = (ImageView) v.findViewById(R.id.friend_weather_icon);
	    
		fView.setVisibility(View.VISIBLE);
		
		fText.setVisibility(View.VISIBLE);
		
		lText.setVisibility(View.VISIBLE);
		
		tText.setVisibility(View.VISIBLE);
		
		wView.setVisibility(View.VISIBLE);

		Button yes = (Button)v.findViewById(R.id.yesButton);
		
		Button no = (Button)v.findViewById(R.id.noButton);
		
		TextView rFriend = (TextView) v.findViewById(R.id.remove_friend);
		
		rFriend.setText("");
		
		yes.setVisibility(View.GONE);
		
		no.setVisibility(View.GONE);
	}
	

	private void DeleteButton(View v, int p){
		//Log.v("DeleteButton", "was called!");
		
		ImageView fView = (ImageView) v.findViewById(R.id.friend_icon);
		
		TextView fText = (TextView) v.findViewById(R.id.friend_name);
		
		TextView lText = (TextView) v.findViewById(R.id.friend_location);
		
		TextView tText = (TextView) v.findViewById(R.id.friend_temp);
		
		ImageView wView = (ImageView) v.findViewById(R.id.friend_weather_icon);
	    
		fView.setVisibility(View.GONE);
		
		fText.setVisibility(View.GONE);
		
		lText.setVisibility(View.GONE);
		
		tText.setVisibility(View.GONE);
		
		wView.setVisibility(View.GONE);
		
		TextView rFriend = (TextView) v.findViewById(R.id.remove_friend);
		
		Button yes = (Button)v.findViewById(R.id.yesButton);
		
		Button no = (Button)v.findViewById(R.id.noButton);
		
		rFriend.setText("Remove " + friends.get(p).getName() + " ?");
		
		yes.setVisibility(View.VISIBLE);
		yes.setTag(position);
		
		no.setVisibility(View.VISIBLE);
	     
        yes.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	int pos = (Integer) v.getTag();
            //	SocialFragment.deleteItem();
            }
            	
           });
        
        no.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
            	Cancel((View)v.getParent());
            	//view is button
	    	       
            }
        });		
		
	}
}