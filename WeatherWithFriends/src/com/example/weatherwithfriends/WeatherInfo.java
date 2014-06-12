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

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class WeatherInfo  {
	private Drawable icon;
	private String forecasttext;
	private String location;
	private String temperature;
	
	public WeatherInfo(String city, String state, String country){
		String[] here = new String[] {city, state, country};
		new FindWeather().execute(here);
	}
	
	public WeatherInfo(Location loc, Context context) {
		new FindWeather2(context).execute(loc);
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
	
	class FindWeather extends AsyncTask <String, Void, WeatherInfo>{ 
		private final String API_KEY = "86d6e9e9fcdda77c";
		
		@Override
		protected void onPreExecute() {
			Log.v("Finding weather in weatherinfo class", "PreExecute");
		}

		
        protected void onPostExecute(WeatherInfo result) {
        }


		@Override
		protected WeatherInfo doInBackground(String... params) {
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
			parseJSON(responseString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return w;
	}

	/* PARSON JSON FOR BOTH WEATHER FORMATS */
	
	private void parseJSON (String rString) throws JSONException {
		
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
		this.icon= Drawable.createFromStream(content, "src");
		this.temperature = temperature;
		this.location = location;
		this.forecasttext = txt_forecast;
	}
	
/* PLACE FOR HOME FRAGMENT CALLING FINDWEATHER2 */
	
	class FindWeather2 extends AsyncTask <Location, String, WeatherInfo>{ 

		private Context mContext;
		private final String API_KEY = "86d6e9e9fcdda77c";
		
		public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
		private ProgressDialog mProgressDialog;

		public FindWeather2(Context context) {
			this.mContext = context;
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Counting sunrays...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected WeatherInfo doInBackground(Location... params) {
				Log.v("FindWeather", "doInBackground");
				return HTTPRequest2(params[0]);
		}

		protected void onProgressUpdate(String... progress) {
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}
        protected void onPostExecute(WeatherInfo result) {
        	mProgressDialog.dismiss();
        }
	}
        
    private WeatherInfo HTTPRequest2(Location location) {
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
		return w;
	}
	
}
