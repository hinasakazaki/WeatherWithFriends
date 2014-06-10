package com.example.weatherwithfriends;

import java.io.FileOutputStream;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.add_fragment, container, false);
         
        return rootView;
    }
	
	//on click for add
	public void add(View v) {
		Friend f;
		String name = null;
		String location = null;
		
		//inputs
		EditText nameInput = (EditText)getView().findViewById(R.id.nameEdit);
		EditText cityInput = (EditText)getView().findViewById(R.id.cityEdit);
		
		if (nameInput.getText().toString().length() > 0 && cityInput.getText().toString().length() > 0) {
			name = nameInput.getText().toString();
			location = nameInput.getText().toString();
		}
		
		//add friend in list
		
		Activity mActivity = getActivity();
		
		f = new Friend(name, location, null, null);
		
		if (mActivity instanceof MainActivity) {
			((MainActivity)mActivity).addFriend(f);
		}
		
	}
}
