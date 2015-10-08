

package com.example.grow;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StudentAccountFragment extends Fragment {
	TextView emailView;
	TextView nameView;
	String userName;
	String email;
	boolean pushInfo;
	
	public StudentAccountFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.student_account_info, container, false);
         
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		nameView =  (EditText) getView().findViewById(R.id.nameText);
		emailView = (EditText) getView().findViewById(R.id.emailText);
		
		userName = ParseUser.getCurrentUser().getUsername();
		email = ParseUser.getCurrentUser().getEmail();

		nameView.setText(userName);
		emailView.setText(email);
		submitEventInfoChangesListener();
	}
	
	/**
     * Get content on the screen and change it in Parse
     */
    public void submitEventInfoChangesListener() {
		final Button submit = (Button) getView().findViewById(R.id.editInfoButton);
		submit.setOnClickListener(new OnClickListener() {
				@Override
			    public void onClick(View v) {
					submitEdits();
				}
		});
    }
    
    /** 
   * Submit info when event info is edited by user
   */
    public void submitEdits() {
		pushInfo = true;
		EditText userNameLabel = (EditText) getView().findViewById(R.id.nameText);
    	EditText emailLabel = (EditText) getView().findViewById(R.id.emailText);
    	
    	userNameLabel.setTextColor(Color.BLACK);
		emailLabel.setTextColor(Color.BLACK);
    	
    	String orgUserName = userNameLabel.getText().toString();
		String email = emailLabel.getText().toString();
	
		if (orgUserName.equals("")) {
			pushInfo = false;
		    userNameLabel.setTextColor(Color.RED);
		}
		if (email.equals("")) {
			pushInfo = false;
		    emailLabel.setTextColor(Color.RED);
		}
		if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
			pushInfo = false;
		    emailLabel.setTextColor(Color.RED);
    	}
		
		// Edit the event in Parse
		if (pushInfo == true) {
		    // Set database fields
			ParseUser.getCurrentUser().setUsername(orgUserName);
			ParseUser.getCurrentUser().setEmail(email);
			ParseUser.getCurrentUser().saveInBackground(new SaveCallback(){
            	@Override
                public void done(ParseException e) {
                    if (e == null) {
                    	// Event was successfully edited
                    	Intent i = new Intent(getActivity(), StudentMainNav.class);
                    	startActivity(i);
                    } 
                    else {
                    	// An error occurred that needs to be fixed. Properly display error.
                    	EditText error = (EditText) getView().findViewById(R.id.errorField);
                    	error.setText("Something went wrong");
                    }
            	}
            });
		}
	}
    
}


