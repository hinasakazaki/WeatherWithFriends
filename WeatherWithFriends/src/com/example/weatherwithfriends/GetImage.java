package com.example.weatherwithfriends;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

class GetImage extends AsyncTask<String, Void, byte[]> {
		//takes in 
		CallMeBack mCallMeBack; 
		public GetImage (CallMeBack cmb) {
			mCallMeBack = cmb;
		}
		@Override
		protected byte[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			return getImage(params[0]);
		}
		
		protected void onPostExecute(byte[] result) {
			Log.v("onPostexecute", "forGetImage" );
			mCallMeBack.onTaskFinished(result);
		}
	
	private static byte[] getImage(String iconurl) {
		//get image stuff
		URL iUrl = null;
		try {
			iUrl = new URL(iconurl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			URLConnection ucon = iUrl.openConnection();
			
			InputStream is = ucon.getInputStream();
			
			BufferedInputStream bis = new BufferedInputStream(is);
			
			ByteArrayBuffer baf = new ByteArrayBuffer(500);
			
			int current = 0;
			
			while((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			
			return baf.toByteArray();
		} catch (Exception e) { //image manager error
			Log.d("ImageManager", "Error" +e.toString());
		}

		return null;

	}
}