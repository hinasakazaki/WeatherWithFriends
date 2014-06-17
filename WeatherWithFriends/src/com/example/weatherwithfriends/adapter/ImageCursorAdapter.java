package com.example.weatherwithfriends.adapter;

import com.example.weatherwithfriends.R;
import com.example.weatherwithfriends.friends.database.FriendTable;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageCursorAdapter extends SimpleCursorAdapter{
	  private Cursor c;
      private Context context;

	public ImageCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to, 0);
		this.c = c;
		this.context = context;
	}

	public View getView(int pos, View inView, ViewGroup parent) {
	       View v = inView;
	       if (v == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = inflater.inflate(R.layout.friend_row, null);
	       }
	       this.c.moveToPosition(pos);		
	       
	       String friendName = this.c.getString(this.c.getColumnIndex(FriendTable.COLUMN_FRIEND));
	       String friendLocation = this.c.getString(this.c.getColumnIndex(FriendTable.COLUMN_CITY)) + ", " +  this.c.getString(this.c.getColumnIndex(FriendTable.COLUMN_STATE));
	       String friendTemperature = this.c.getString(this.c.getColumnIndex(FriendTable.COLUMN_TXT)) + " " + this.c.getString(this.c.getColumnIndex(FriendTable.COLUMN_TEMP));
	       
	       TextView fName =(TextView) v.findViewById(R.id.friend_name);
	       TextView fLoc = (TextView) v.findViewById(R.id.friend_location);
	       TextView fTemp = (TextView) v.findViewById(R.id.friend_temp);
	     
	       ImageView fIcon =  (ImageView)v.findViewById(R.id.friend_icon);
	       if (pos%2 == 0) {
	    	  fIcon.setImageResource(R.drawable.jenna);
	       } else if (pos%3 == 0) {
	    	   fIcon.setImageResource(R.drawable.mike);
	       } else {
	    	   fIcon.setImageResource(R.drawable.judy);
	       }
	       fName.setText(friendName);
	       fLoc.setText(friendLocation);
	       fTemp.setText(friendTemperature);
	       
	       byte[] wicon = this.c.getBlob(this.c.getColumnIndex(FriendTable.COLUMN_ICON));
	       if (wicon != null) {
		   ImageView iv = (ImageView) v.findViewById(R.id.friend_weather_icon);
	           iv.setImageBitmap(BitmapFactory.decodeByteArray(wicon, 0, wicon.length));
	       }
	       
	       
	       return(v);
	}
}
