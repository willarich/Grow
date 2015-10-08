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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SuperEditOrgInformation extends Activity {
	String orgName;
	String orgID;
	
	EditText name;
	EditText email;
	EditText description;
	
	ParseUser organization;
	
	boolean pushUser;
	
	HashMap<String, Object> params  = new HashMap<String, Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.super_edit_org_information);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		TextView title = (TextView) findViewById(R.id.SuperOrgInfoTitle);
		Intent intent = getIntent();
		
		orgName = intent.getStringExtra("organization");
		orgID = intent.getStringExtra("ID");
		title.setText("Edit Information for " + orgName);
		getOrg();
		submitInfoChangesListener();
		
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.super_user_main, menu);
	        ActionBar bar = getActionBar();
	        int color = Color.parseColor("#FF038f0d");
	        bar.setBackgroundDrawable(new ColorDrawable(color));
	        return true;
	    }
	    
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				ParseUser.logOut();
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
			}
			else{
				saveAndFinish();
			}
			return super.onOptionsItemSelected(item);
		}
		
	    /**
	     * End the activity and go back to Super Org Editor screen
	     */
		private void saveAndFinish() {
			Intent intent = new Intent();
			intent.putExtra("organization", orgName);
			intent.putExtra("ID", orgID);
			setResult(RESULT_OK, intent);
			finish();
		}
		
		@Override
		public void onBackPressed() {
			saveAndFinish();
			super.onBackPressed();
		}
		
		/**
		 * display all org information using edit text fields
		 */
		public void displayOrgInformation(ParseUser org){
			name = (EditText) findViewById(R.id.NameEditTextField);
			email = (EditText) findViewById(R.id.EmailEditTextField);
			description = (EditText) findViewById(R.id.DescriptionEditTextField);
			name.setText(org.getUsername());
			email.setText(org.getEmail());
			description.setText(org.getString("description"));
		}
		
		/**
		 * Query the org from the parse database
		 */
		public void getOrg(){
			ParseQuery<ParseUser> query = ParseUser.getQuery();
	    	query.whereEqualTo("username", orgName);
	    	query.findInBackground(new FindCallback<ParseUser>() {
	    		  public void done(List<ParseUser> users, ParseException e) {
	    		    if (e == null) {
	    		    	ParseUser org = users.get(0);
	    		    	displayOrgInformation(org);
	    		    } else {
	    		    	Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
	    		    }
	    		 }
	    	});
		}
		

		
		/**
		 * function to submit edited org information
		 */
		public void submitEditedInformation(){
			pushUser = true;
			
			//get all labels from the view
			
			TextView orgNameLabel = (TextView) findViewById(R.id.NameEditLabel);
	    	TextView emailLabel = (TextView) findViewById(R.id.EmailEditLabel);
	    	TextView descriptionLabel = (TextView) findViewById(R.id.DescriptionEditLabel);
			
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
	    	
	    	// Attempt to sign up the org account if all fields are filled
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
	 			    	   Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
	 			    	   
	 			    	   if(name.getText().toString() != orgName){
	 			    		  updateEventRelations();
	 			    		  orgName = name.getText().toString();
	 			    	   }
	 			    	  saveAndFinish();
	 			       }
	 			       else{
	 			    	   TextView error = (TextView) findViewById(R.id.errorEditField);
	 			    	   error.setText("Make sure the email is valid!");
	 			       }
	 			   }
	 			});
	        }
	        // All fields were not properly filled
	        else if(pushUser == false){
	        	TextView error = (TextView) findViewById(R.id.errorEditField);
	        	error.setText("Make sure all fields are filled!");
	        }
	    }
		
		/**
		 * Get content on the screen and change the content for 
		 */
		public void submitInfoChangesListener(){
			final Button submit = (Button) findViewById(R.id.SignEditUpButton);
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
									Toast.makeText(getApplicationContext(), "Could not update event relations", Toast.LENGTH_LONG).show();
								}
							}
						});
			    	 }
				
			     }
			     else {
			    	 Toast.makeText(getApplicationContext(), "Could not get events to update with new relation", Toast.LENGTH_LONG).show();
			     }
			  }	
		     });
		}
	
}
