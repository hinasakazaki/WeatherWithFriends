package com.example.weatherwithfriends;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherInfo  {
	private String state;
	private String city;
	private String country;
	private Drawable icon;
	private String forecasttext;
	private String location;
	private String temperature;
	
	public WeatherInfo(String t, String l, String f){
		this.forecasttext = f;
		this.temperature = t;
		this.location = l;
	}
	
	public Drawable getIcon(){
		return icon;
	}
	
	public String getTemperature() {
		return temperature;
	}
	
	public String getText(){
		return forecasttext;
	}
	
	public String getLoc(){
		return location;
	}
	
	class findWeather extends AsyncTask <String, Void, WeatherInfo>{ 
//		private final WeakReference imageViewReference;
//		private final WeakReference textViewReference;
		private final String API_KEY = "86d6e9e9fcdda77c";
		
		
		public findWeather (ImageView imageView, TextView textView) {
			imageViewReference = new WeakReference(imageView);
			textViewReference = new WeakReference(textView);
		}
		
		
		@Override
		protected void onPreExecute() {
			Log.v("Finding weather in weatherinfo class", "PreExecute");
		}

		
        protected void onPostExecute(WeatherInfo result) {
//        	ImageView imageView = (ImageView) imageViewReference.get();
//        	TextView textView = (TextView) textViewReference.get();
//        	
//			imageView.setImageDrawable(result.getIcon());
//			textView.setText((result.getText()) + " "+ result.getTemperature());
        }


		@Override
		protected WeatherInfo doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HTTPRequest(params);
		}
	}
	
	private WeatherInfo HTTPRequest(String[] location) {
		final String API_KEY = "86d6e9e9fcdda77c";
		
		String city = location[0];
		String state = location[1];
		String country = location[2];
		
		WeatherInfo w = null;
		
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
	
}
