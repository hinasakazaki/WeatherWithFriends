package com.example.weatherwithfriends;

import java.io.FileOutputStream;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFragment extends Fragment {
	Button addButton = null;
	
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
        		String location = null;
        		
        		//inputs
        		EditText nameInput = (EditText)getView().findViewById(R.id.nameEdit);
        		EditText cityInput = (EditText)getView().findViewById(R.id.cityEdit);
        		
        		if (nameInput.getText().toString().length() > 0 && cityInput.getText().toString().length() > 0) {
        			name = nameInput.getText().toString();
        			location = cityInput.getText().toString();
        		}
        		
        		//add friend in list
        		
        		Activity mActivity = getActivity();
        		
        		f = new Friend(name, location, "", "");
        		
        		if (mActivity instanceof MainActivity) {
        			((MainActivity)mActivity).addFriend(f);
        		}
        		
        		// Create new fragment and transaction
        		Fragment newFragment = new SocialFragment();
        		FragmentTransaction transaction = getFragmentManager().beginTransaction();

        		// Replace whatever is in the fragment_container view with this fragment,
        		// and add the transaction to the back stack
        		transaction.replace(R.id.pager, newFragment);
        		transaction.addToBackStack(null);

        		// Commit the transaction
        		transaction.commit();            
        		}

        });
        
        return rootView;
    
    }
	
}
