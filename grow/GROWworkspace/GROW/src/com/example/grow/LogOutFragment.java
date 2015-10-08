

package com.example.grow;

import com.parse.ParseUser;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LogOutFragment extends Fragment {
	
	public LogOutFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		ParseUser.logOut();
		Toast.makeText(getActivity().getApplicationContext(), "Logged Out!", Toast.LENGTH_LONG).show();
        //View rootView = inflater.inflate(R.layout.org_home_page, container, false);
		Intent i = new Intent(getActivity(), MainActivity.class);
    	startActivity(i);
         
        //return rootView;
    	return null;
    }
}

