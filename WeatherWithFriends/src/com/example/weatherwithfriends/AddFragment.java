package com.example.weatherwithfriends;

import com.example.weatherwithfriends.friends.contentprovider.FriendContentProvider;
import com.example.weatherwithfriends.friends.database.FriendTable;

import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
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
	Button addButton = null;
	
	private Uri friendUri;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.add_fragment, container, false);
        addButton = (Button)rootView.findViewById(R.id.addButton);
       
        addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	Friend f;
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
    			
    			nameInput.setText("");
    			cityInput.setText("");
    			stateInput.setText("");
    			countryInput.setText("");
    			
    			
    			//go through friendcontroller to make a new entry in the database
    			FriendController fc = new FriendController();
    			
    			fc.addFriend(v.getContext(), name, city, state, country);
    			
    			
        		CharSequence text = "Added " + name + " to your friends list!";
        		
        		Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
        		
        
        return rootView;
    
	}
}
