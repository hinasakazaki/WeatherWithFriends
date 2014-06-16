package com.example.weatherwithfriends;

import java.util.ArrayList;

import com.example.weatherwithfriends.friends.contentprovider.FriendContentProvider;
import com.example.weatherwithfriends.friends.database.FriendTable;

import android.support.v4.app.Fragment;
import android.widget.SimpleCursorAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SocialFragment extends Fragment{
	
	private SimpleCursorAdapter adapter;
	ListView list;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.social_fragment, container, false);
 	
		fillData(rootView);
		
        return rootView;
    }

	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
	}
	
	
	private void fillData(View v) {
		
		FriendController fc = new FriendController();
		Cursor cur = fc.getFriends();
		
		ListView list = (ListView) v.findViewById(R.id.list);
		String[] from = new String[] { FriendTable.COLUMN_FRIEND, FriendTable.COLUMN_CITY, FriendTable.COLUMN_TXT };
		
		int[] to = new int[] {R.id.friend_name, R.id.friend_location, R.id.friend_temp};
		adapter = new SimpleCursorAdapter(this.getActivity(), R.layout.friend_row, cur, from, to, 0);
			
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				DeleteItem(view, position, id);
			}
		});
		
	}
	
	
	/*Deleting functionality on press */
	
	private void DeleteItem(View v, int position, long id){

		ImageView fView = (ImageView) v.findViewById(R.id.friend_icon);
		
		TextView fText = (TextView) v.findViewById(R.id.friend_name);
		
		String friendname = fText.getText().toString();
		
		TextView lText = (TextView) v.findViewById(R.id.friend_location);
		
		TextView tText = (TextView) v.findViewById(R.id.friend_temp);
		
		ImageView wView = (ImageView) v.findViewById(R.id.friend_weather_icon);
	    
		fView.setVisibility(View.GONE);
		
		fText.setVisibility(View.GONE);
		
		lText.setVisibility(View.GONE);
		
		tText.setVisibility(View.GONE);
		
		wView.setVisibility(View.GONE);
		
		TextView rFriend = (TextView) v.findViewById(R.id.remove_friend);
		
		Button yes = (Button)v.findViewById(R.id.yesButton);
		
		Button no = (Button)v.findViewById(R.id.noButton);
		
		rFriend.setText("Remove " + friendname + " ?");
		
		yes.setVisibility(View.VISIBLE);
		yes.setTag(id);
		
		no.setVisibility(View.VISIBLE);
		
        yes.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	FriendController fc = new FriendController();
            	Long tid = (Long)v.getTag();
            	fc.deleteFriend(v, tid);
            }
           });
        
        no.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
            	Cancel((View)v.getParent());
            }
        });		
	}
	
	private void Cancel(View v) {
		ImageView fView = (ImageView) v.findViewById(R.id.friend_icon);
		
		TextView fText = (TextView) v.findViewById(R.id.friend_name);
		
		TextView lText = (TextView) v.findViewById(R.id.friend_location);
		
		TextView tText = (TextView) v.findViewById(R.id.friend_temp);
		
		ImageView wView = (ImageView) v.findViewById(R.id.friend_weather_icon);
	    
		fView.setVisibility(View.VISIBLE);
		
		fText.setVisibility(View.VISIBLE);
		
		lText.setVisibility(View.VISIBLE);
		
		tText.setVisibility(View.VISIBLE);
		
		wView.setVisibility(View.VISIBLE);

		Button yes = (Button)v.findViewById(R.id.yesButton);
		
		Button no = (Button)v.findViewById(R.id.noButton);
		
		TextView rFriend = (TextView) v.findViewById(R.id.remove_friend);
		
		rFriend.setText("");
		
		yes.setVisibility(View.GONE);
		
		no.setVisibility(View.GONE);
	}
	
	
	
	
}
