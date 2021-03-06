package com.example.weatherwithfriends;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

public class FindWeather extends AsyncTask<Location, Void, String[]> {
	CallMeBack mCallMeBack;
	
	public FindWeather(CallMeBack cmb) {
		mCallMeBack = cmb;
		/*
		mProgressDialog.setMessage("Counting sunrays...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show(); */
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//mProgressDialog.show();
	}

	/*
	protected void onProgressUpdate(String... progress) {
		mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	}
	*/
    
	@Override
	protected String[] doInBackground(Location... params) {
		return HTTPRequest(params);
	}
	
	protected void onPostExecute(String[] result) {
		Log.v("On post execute", "for Home fragment");
		mCallMeBack.onTaskDone(result);
	}
	
	private String[] HTTPRequest(Location[] location) {
	
		double lat = location[0].getLatitude();
		double lon= location[0].getLongitude();
		
		
		final String request = "http://api.wunderground.com/api/86d6e9e9fcdda77c/conditions/q/" + lat + "," + lon + ".json";
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		
		String[] ret = null;
				
		//location is problem
		Log.v("me_url", request.toString());
		
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
		
		Log.v("got to parse JSON!", "good");
		
		
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
		String[] returnSA = new String[] {today.toString(), temperature, txt_forecast, iconurl, location};
		
		return returnSA;
	}

	
}
