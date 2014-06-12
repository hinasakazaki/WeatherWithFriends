package com.example.weatherwithfriends;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
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
		
		Log.v("HomeFragment", "Ok?");

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
		
		WeatherInfo w = null;
		
		if (location != null) {
			lat = location.getLatitude();
			lon = location.getLongitude();
		}
		
		final String request = "http://api.wunderground.com/api/86d6e9e9fcdda77c/conditions/q/" + lat + "," + lon + ".json";
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		
		Log.v("home url", request.toString());
		
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
				w = new WeatherInfo(null, "No Connection", "No Connection", "No Connection");
				response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
		
		
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
		JSONObject current_observation = null;
		String iconurl = null;
		String txt_forecast = null;
		String location = null;
		String temperature = null;
		WeatherInfo rWeatherInfo = null;
		
		
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
		Drawable icon= Drawable.createFromStream(content, "src");
		
	 	
		rWeatherInfo = new WeatherInfo(icon, temperature, location, txt_forecast);
		
		return rWeatherInfo;
		
	}
	

	class FindWeather extends AsyncTask <Location,WeatherInfo, WeatherInfo>{ 
		
		private Context mContext;
		private final String API_KEY = "86d6e9e9fcdda77c";
		
		
		@Override
		protected void onPreExecute() {
			Log.v("FindWeather", "PreExecute");
		}
		
		@Override
		protected WeatherInfo doInBackground(Location... params) {
				Log.v("FindWeather", "doInBackground");
				return HTTPRequest(here);
		}
		
        protected void onPostExecute(WeatherInfo result) {
			//display weather icon
        	
		 	ImageView iv = (ImageView) getView().findViewById(R.id.icon);
		 	iv.setImageDrawable(result.getIcon()); 
		 	
		 	TextView loc = (TextView)getView().findViewById(R.id.location);
		 	loc.setText(result.getLoc());
		 	
		 	TextView tv = (TextView)getView().findViewById(R.id.temperature);
		 	tv.setText(result.getTemperature());
		 
		 	TextView dv = (TextView)getView().findViewById(R.id.description);
		 	dv.setText(result.getText());
		 
		 	
        }
	}
	
}
