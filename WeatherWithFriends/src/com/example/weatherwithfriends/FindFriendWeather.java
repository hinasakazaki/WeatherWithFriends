package com.example.weatherwithfriends;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

public class FindFriendWeather extends AsyncTask <String, String[], String[]>{ 
	private final String API_KEY = "86d6e9e9fcdda77c";
	
	private AsyncTaskCompleteListener listener;
	Context mContext;
	Long id;
	String[] rsa;

	
	public FindFriendWeather(Context c, String id, AsyncTaskCompleteListener listener) {
		this.listener = listener;
		mContext = c;
		this.id = Long.valueOf(id);
	}
	
	/*
	public FindFriendWeather(Context c, ContentValues myEntry,
			FetchMyDataTaskCompleteListener fetchMyDataTaskCompleteListener) {
		// TODO Auto-generated constructor stub
	}*/

	@Override
	protected String[] doInBackground(String... params) {
		rsa = HTTPRequest(params);
		return HTTPRequest(params);
	}
	
	protected void onPostExecute() {
		FriendController fc = new FriendController();
		fc.updateFriendWeather();
	}


	
	
	private String[] HTTPRequest(String[] location) {
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
		
		String[] ret = null;
				
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
			ret = parseJSON(responseString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private String[] parseJSON (String rString) throws JSONException {
		
		JSONObject jsonresult = null;
		JSONObject current_observation = null;
		String iconurl = null;
		String txt_forecast = null;
		String location = null;
		String temperature = null;
		Uri friendsUri = FriendContentProvider.CONTENT_URI;
		
		
		if (rString != null) {
			//parse!
			jsonresult = (JSONObject) new JSONTokener(rString).nextValue();
			
			current_observation = jsonresult.getJSONObject("current_observation");
			
			location = current_observation.getJSONObject("display_location").getString("full");
		
			iconurl = (String) current_observation.getString("icon_url");
		
			temperature = (String) current_observation.getString("temp_f") + "F";
			
			txt_forecast = (String) current_observation.getString("weather");
			}
		
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		
		//time to update info!
		String[] returnSA = new String[] {today.toString(), temperature, txt_forecast, iconurl};
		
		return returnSA;
	}
	
}