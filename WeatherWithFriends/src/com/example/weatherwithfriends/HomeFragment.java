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

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		Log.d("FRAGMENT", "onAttach");
		super.onAttach(activity);
		
		//let's figure out where I am!
		
		loc = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		if (loc.getAllProviders().size() > 0) {
			provider = loc.getAllProviders().get(0);
			here = loc.getLastKnownLocation(provider);
		}
		
		else {
			Log.d("Couldn't find", loc.toString());
		}
		
     
		//execute the HTTP Request
        if (here != null) {
        	new FindWeather().execute(here);
        }
		
		
	}
	
	private WeatherInfo HTTPRequest(Location location) {
		final String API_KEY = "86d6e9e9fcdda77c";
		
		double lat = 0;
		double lon = 0;
		
		if (location != null) {
			lat = location.getLatitude();
			lon = location.getLongitude();
		}
		
		final String request = "http://api.wunderground.com/api/86d6e9e9fcdda77c/forecast/q/" + lat + "," + lon + ".json";
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		
		String responseString = null;
		
		try {
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
		
		WeatherInfo w = null;
		try {
			w = parseJSON(responseString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return w;
	}
	
	
	
	private WeatherInfo parseJSON (String rString) throws JSONException {
		JSONObject jsonresult = null;
		JSONObject forecast = null;
		JSONObject iconurl = null;
		JSONObject forecasttext = null;
		
		if (rString != null) {
			//parse!
			jsonresult = (JSONObject) new JSONTokener(rString).nextValue();
			
			forecast = jsonresult.getJSONObject("forecast");
			
			iconurl = forecast.getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("icon_url");
			
			forecasttext = forecast.getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("fcttxt");
			
			}
		return null;
		
	}
	

	class FindWeather extends AsyncTask <Location, Void, WeatherInfo>{ 
		
		private Context mContext;
		private final String API_KEY = "86d6e9e9fcdda77c";
		
		
		@Override
		protected void onPreExecute() {
			
		}
		
		@Override
		protected WeatherInfo doInBackground(Location... params) {
			
			Log.d("Okay got to background thread", null);
				return HTTPRequest(here);
		}
		
        protected void onPostExecute(Void result) {
			
				
			}
	
        }
		
	
}
