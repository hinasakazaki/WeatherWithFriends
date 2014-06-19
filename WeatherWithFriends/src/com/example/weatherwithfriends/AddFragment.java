package com.example.weatherwithfriends;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFragment extends Fragment {
	
	static String mName;
	static Context mContext;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.add_fragment, container, false);
        Button addButton = (Button)rootView.findViewById(R.id.addButton);
        mContext = rootView.getContext();
        
        addButton.setOnClickListener(new OnClickListener() {

 
    	@Override
    	public void onClick(View v) {
    		String name = null;
    		String city = null;
    		String state = null;
    		String country = null;

    		//inputs
    		EditText nameInput = (EditText)getView().findViewById(R.id.nameEdit);
    		EditText cityInput = (EditText)getView().findViewById(R.id.cityEdit);
    		EditText stateInput = (EditText)getView().findViewById(R.id.stateEdit);
    		EditText countryInput = (EditText)getView().findViewById(R.id.countryEdit);

    		name = nameInput.getText().toString();
    		city= cityInput.getText().toString();
    		state = stateInput.getText().toString();
    		country = countryInput.getText().toString();
    		mName = name;
    		
    		nameInput.setText("");
    		cityInput.setText("");
    		stateInput.setText("");
    		countryInput.setText("");

    		FriendController.addFriend(v.getContext(), name, city, state, country);
    		//go through friendcontroller to make a new entry in the database
        	}
        });
        return rootView;

	}
    public static void worked (Boolean b) {
    	CharSequence text;
    	if (b) {
    		text = "Added " + mName + " to your friends list!";
    	} else {
    		text =  "Please enter your friend again, with the correct city, state, and country!";
    	}
		Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }
    		
}
