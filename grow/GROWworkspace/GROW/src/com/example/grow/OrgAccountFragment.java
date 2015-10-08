

package com.example.grow;

import java.util.HashMap;
import java.util.List;

import com.custom.grow.ParseEvent;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OrgAccountFragment extends Fragment {
	
	String orgName;
	String orgID;
	
	EditText name;
	EditText email;
	EditText description;
	
	ParseUser organization;
	
	boolean pushUser;
	
	HashMap<String, Object> params  = new HashMap<String, Object>();
	public OrgAccountFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.super_edit_org_information, container, false);
         
        return rootView;
    }
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView title = (TextView) getActivity().findViewById(R.id.SuperOrgInfoTitle);
		title.setText("Edit Account Information");
		organization = ParseUser.getCurrentUser();
		orgName = organization.getUsername();
		orgID = organization.getObjectId();
		displayOrgInformation();
		submitInfoChangesListener();
		
	}
	
	/**
	 * display all org information using edit text fields
	 */
	public void displayOrgInformation(){
		name = (EditText) getActivity().findViewById(R.id.NameEditTextField);
		email = (EditText) getActivity().findViewById(R.id.EmailEditTextField);
		description = (EditText) getActivity().findViewById(R.id.DescriptionEditTextField);
		name.setText(organization.getUsername());
		email.setText(organization.getEmail());
		description.setText(organization.getString("description"));
	}
	
	
	/**
	 * function to submit edited org information
	 */
	public void submitEditedInformation(){
		pushUser = true;
		
		//get all labels from the view
		
		TextView orgNameLabel = (TextView) getActivity().findViewById(R.id.NameEditLabel);
    	TextView emailLabel = (TextView) getActivity().findViewById(R.id.EmailEditLabel);
    	TextView descriptionLabel = (TextView) getActivity().findViewById(R.id.DescriptionEditLabel);
		
    	// Make sure labels are set to the correct color
    	orgNameLabel.setTextColor(Color.BLACK);
    	emailLabel.setTextColor(Color.BLACK);
    	descriptionLabel.setTextColor(Color.BLACK);
    	email.setTextColor(Color.BLACK);
    	name.setTextColor(Color.BLACK);
    	
		//Check to see if the fields have values
		
    	//Check to see if the fields have values
    	if(name.getText().toString().equals("")){
    		pushUser = false;
    		orgNameLabel.setTextColor(Color.RED);
    	}
    	if(email.getText().toString().equals("")){
    		pushUser = false;
    		emailLabel.setTextColor(Color.RED);
    	}
    	if(description.getText().toString().equals("")){
    		pushUser = false;
    		descriptionLabel.setTextColor(Color.RED);
    	}
    	
    	// Attempt to edit the org account
        if(pushUser == true){
        	//Edit organization from the information given
        	params.put("orgName", orgName);
        	params.put("newName", name.getText().toString());
        	params.put("newEmail", email.getText().toString());
        	params.put("newDescription", description.getText().toString());
        	
        	//Edit organization information by calling cloud code
        	ParseCloud.callFunctionInBackground("updateOrgInfo", params, new FunctionCallback<String>() {
 			   public void done(String success, ParseException e) {
 			       if (e == null) {
 			    	   Toast.makeText(getActivity().getApplicationContext(), success, Toast.LENGTH_LONG).show();
 			    	   
 			    	   if(name.getText().toString() != orgName){
 			    		  updateEventRelations();
 			    		  orgName = name.getText().toString();
 			    	   }
 			    	  Fragment fragment = new OrgHomeFragment();

 			   			FragmentManager fragmentManager = getFragmentManager();
 			   			fragmentManager.beginTransaction()
 			   					.replace(R.id.frame_container, fragment).commit();
 			    	  
 			       }
 			       else{
 			    	   TextView error = (TextView) getActivity().findViewById(R.id.errorEditField);
 			    	   error.setText("Make sure the email is valid!");
 			       }
 			   }
 			});
        }
        // All fields were not properly filled
        else if(pushUser == false){
        	TextView error = (TextView) getActivity().findViewById(R.id.errorEditField);
        	error.setText("Make sure all fields are filled!");
        }
    }
	
	/**
	 * Get content on the screen and change the content for 
	 */
	public void submitInfoChangesListener(){
		final Button submit = (Button) getActivity().findViewById(R.id.SignEditUpButton);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submitEditedInformation();
				
			}
		});
	}
	
	/**
	 * If the organization name was changed then 
	 */
	public void updateEventRelations(){
		ParseQuery<ParseEvent> query = ParseQuery.getQuery("Event");
		query.whereEqualTo("createdBy", orgName);
		query.findInBackground(new FindCallback<ParseEvent>() {
		  public void done(List<ParseEvent> objects, ParseException e) {
		     if (e==null) {
		    	 //Find every event created by the organization and update the relation
		    	 for (ParseEvent o : objects) {
		    		 o.put("createdBy", name.getText().toString());
		    		 o.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if(e==null){
								
							}
							else{
								Toast.makeText(getActivity().getApplicationContext(), "Could not update event relations", Toast.LENGTH_LONG).show();
							}
						}
					});
		    	 }
			
		     }
		     else {
		    	 Toast.makeText(getActivity().getApplicationContext(), "Could not get events to update with new relation", Toast.LENGTH_LONG).show();
		     }
		  }	
	     });
	}
}

