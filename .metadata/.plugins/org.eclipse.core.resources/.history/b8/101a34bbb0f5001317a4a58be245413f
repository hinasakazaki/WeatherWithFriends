package com.example.weatherwithfriends;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class FindWeather extends AsyncTask <String, Void, Void>{ 
	private final String API_KEY = "86d6e9e9fcdda77c";
	
	@Override
	protected void onPreExecute() {
		Log.v("Finding weather in weatherinfo class", "PreExecute");
	}

	
    protected void onPostExecute(Void result) {
    }


	@Override
	protected void doInBackground(String... params) {
		HTTPRequest(params);
	}

	private void HTTPRequest(String[] location) {
		final String API_KEY = "86d6e9e9fcdda77c";
		
		String city = location[0];
		String state = location[1];
		String country = location[2];
	
		
		//replace all spaces with _
		String newCity = city.replaceAll(" ",  "_").toLowerCase();
		String newCountry = country.replaceAll(" ", "_").toLowerCase();
		
		
		final String request = "http://api.wunderground.com/api/86d6e9e9fcdda77c/conditions/q/" + newCountry + "/" + state + "/" + newCity + ".json";
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		
		//location is problem
		Log.v("friend_url", request.toString());
		
		String responseString = null;
		
		try {
			//next line throws error - solved by allowing internet in manifest. lol.
			response = httpclient.execute(new HttpGet(request));
			
			StatusLine statusLine = response.getStatusLine();
			
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
	            response.getEntity().writeTo(out);
	            out.close();
	            responseString = out.toString();
			} else {
				//close connection 
				response.getEntity().getContent().close();
	            throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
	        //TODO Handle problems..
	    } catch (IOException e) {
	        //TODO Handle problems..
	    }
		
		try {
			parseJSON(responseString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* PARSON JSON FOR BOTH WEATHER FORMATS */
	
	private void parseJSON (String rString) throws JSONException {
		
		JSONObject jsonresult = null;
		JSONObject current_observation = null;
		String iconurl = null;
		String txt_forecast = null;
		String location = null;
		String temperature = null;
		
		if (rString != null) {
			//parse!
			jsonresult = (JSONObject) new JSONTokener(rString).nextValue();
			
			current_observation = jsonresult.getJSONObject("current_observation");
			
			location = current_observation.getJSONObject("display_location").getString("full");
		
			iconurl = (String) current_observation.getString("icon_url");
		
			temperature = (String) current_observation.getString("temp_f") + "F";
			
			txt_forecast = (String) current_observation.getString("weather");
			}
		
		//get image stuff
		URL iUrl = null;
		try {
			iUrl = new URL(iconurl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream content = null;
		try {
			content = (InputStream) iUrl.getContent();
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 //Cursor shit
    	Uri friendsUri = FriendContentProvider.CONTENT_URI;
		//An array specifying which columns to return
		String[] projection = new String[]{FriendTable.COLUMN_FRIEND, FriendTable.COLUMN_CITY, FriendTable.COLUMN_STATE, FriendTable.COLUMN_COUNTRY};
		
        Cursor cur = getContentResolver().query(friendsUri, 
				projection, 
				null,
				null,
				null);
        cur.moveToFirst();
        
		int nameColumn = cur.getColumnIndex(FriendTable.COLUMN_FRIEND);
		int cityColumn = cur.getColumnIndex(FriendTable.COLUMN_CITY);
		int stateColumn = cur.getColumnIndex(FriendTable.COLUMN_STATE);
		int tempColumn = cur.getColumnIndex(FriendTable.COLUMN_TEMP);
		int txtColumn = cur.getColumnIndex(FriendTable.COLUMN_TXT);
		int iconColumn = cur.getColumnIndex(FriendTable.COLUMN_ICON);
		int timeColumn = cur.getColumnIndex(FriendTable.COLUMN_TIME);
		
		//load from table, update
		if (cur.getString(nameColumn) == "ME" && UpdateOk(today, cur.getString(timeColumn))) {
			//UIs
			//image view -- where to construct?
			 ImageView iv = (ImageView) getView().findViewById(R.id.icon);
			 iv.setImageDrawable(cur.getString(iconColumn)); 
		
			 TextView loc = (TextView)getView().findViewById(R.id.location);
			 loc.setText(cur.getString(stateColumn) + ", " + cur.getString(stateColumn));
		
			 TextView tv = (TextView)getView().findViewById(R.id.temperature);
			 tv.setText(cur.getString(tempColumn));
		
			 TextView dv = (TextView)getView().findViewById(R.id.description);
			 dv.setText(cur.getString(txtColumn));
		}
		//if entry not match, then create	
	    ContentValues myEntry = new ContentValues();
	    myEntry.put(FriendTable.COLUMN_FRIEND, "ME");
	    myEntry.put(FriendTable.COLUMN_CITY, "San Francisco");
	    myEntry.put(FriendTable.COLUMN_COUNTRY, "");
	    myEntry.put(FriendTable.COLUMN_TIME, today.toString());
	    friendsUri = getActivity().getContentResolver().insert(FriendContentProvider.CONTENT_URI, myEntry);
	    
		cur.close();
	}
}