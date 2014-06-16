package com.example.weatherwithfriends;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

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

import com.example.weatherwithfriends.friends.contentprovider.FriendContentProvider;
import com.example.weatherwithfriends.friends.database.FriendTable;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment{
	
	Location here;
	String provider;
	LocationManager loc;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		return rootView;
	}
	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		
		//let's figure out where I am!
		loc = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		if (loc.getAllProviders().size() > 0) {
			provider = loc.getAllProviders().get(0);
			here = loc.getLastKnownLocation(provider);
		}
		
		else {
			Log.d("Couldn't find current location", loc.toString());
		}
		
		
        
		
		/* 
        //Cursor shit
    	Uri friendsUri = FriendContentProvider.CONTENT_URI;
		//An array specifying which columns to return
		String[] projection = new String[]{FriendTable.COLUMN_FRIEND, FriendTable.COLUMN_CITY, FriendTable.COLUMN_STATE, FriendTable.COLUMN_COUNTRY};
		
        Cursor cur = getActivity().getContentResolver().query(friendsUri, 
				projection, 
				null,
				null,
				null);
        cur.moveToFirst();
        
		
		
		*/
		
	
		FriendController fc = new FriendController();
		Cursor cur = fc.getFriends();
		cur.moveToFirst();
		
		int nameColumn = cur.getColumnIndex(FriendTable.COLUMN_FRIEND);
		int cityColumn = cur.getColumnIndex(FriendTable.COLUMN_CITY);
		int stateColumn = cur.getColumnIndex(FriendTable.COLUMN_STATE);
		int tempColumn = cur.getColumnIndex(FriendTable.COLUMN_TEMP);
		int txtColumn = cur.getColumnIndex(FriendTable.COLUMN_TXT);
		int iconColumn = cur.getColumnIndex(FriendTable.COLUMN_ICON);
		int timeColumn = cur.getColumnIndex(FriendTable.COLUMN_TIME);
		
		
		//load from table, update
		if (cur.getString(nameColumn) == "ME") {
			//UIs
			//image view -- where to construct?
			 ImageView iv = (ImageView) getView().findViewById(R.id.icon);
			 iv.setImageBitmap(BitmapFactory.decodeByteArray(cur.getBlob(iconColumn), 0, cur.getBlob(iconColumn).length)); 
		
			 TextView loc = (TextView)getView().findViewById(R.id.location);
			 loc.setText(cur.getString(stateColumn) + ", " + cur.getString(stateColumn));
		
			 TextView tv = (TextView)getView().findViewById(R.id.temperature);
			 tv.setText(cur.getString(tempColumn));
		
			 TextView dv = (TextView)getView().findViewById(R.id.description);
			 dv.setText(cur.getString(txtColumn));
		} else {
			fc.addSelf("San Francisco", "CA", null);
		}
	
       
	}
	

}
