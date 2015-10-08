package com.example.grow;

import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SuperOrgEditor extends Activity{
	String orgID;
	String userName;
	HashMap<String, Object> params  = new HashMap<String, Object>();
	boolean result;
	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	Activity activity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.super_org_editor);
		final TextView orgName = (TextView) findViewById(R.id.SupEditOrgName);
		Intent intent = getIntent();	
		orgID = intent.getStringExtra("ID");
		userName = intent.getStringExtra("organization");
		orgName.setText(intent.getStringExtra("organization"));
		//Parameters for the cloud code
		params.put("org", orgID);
		params.put("orgName", userName);
		updateVerified();
		switchVerificationListener();
		newEventListener();
		allEventsListener();
		orgInformationListener();
		deleteOrganizationListener();
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
		return super.onOptionsItemSelected(item);
	}
	
    /**
     * Code to make an organization superverified
     */
    public void changeSuperVerified(){
		ParseCloud.callFunctionInBackground("superverified", params, new FunctionCallback<String>() {
			   public void done(String success, ParseException e) {
			       if (e == null) {
			    	   Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
			       }
			       else{
			    	   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			       }
			   }
			});
    }
	
   /**
    * Check to see if organization is already superverified
    */
    public boolean updateVerified(){
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	query.whereEqualTo("objectId", orgID);
    	
    	query.findInBackground(new FindCallback<ParseUser>() {
    		  public void done(List<ParseUser> users, ParseException e) {
    		    if (e == null) {
    		    	ParseUser org = users.get(0);
    		    	result = org.getBoolean("superverified");
    		    	CheckBox verified =(CheckBox) findViewById(R.id.VerifiedCheckBox);
    		    	verified.setChecked(result);
    		    	
    		    } else {
    		    	Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
    		    }
    		 }
    	});
    	return true;
    }
    
    /**
     * Update state of the verified checkbox
     */
    public void switchVerificationListener(){
    	final CheckBox verified =(CheckBox) findViewById(R.id.VerifiedCheckBox);
    	verified.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSuperVerified();
			}
		});
    }
    
    /**
     * Go to Screen to create new Event for the Organization
     */
    public void newEventListener(){
    	final ImageButton newEvent = (ImageButton) findViewById(R.id.SuperCreateEventButton);
    	newEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), SuperOrgCreateEvent.class);
				i.putExtra("organization", userName);
				i.putExtra("orgID", orgID);
				startActivityForResult(i, EDITOR_ACTIVITY_REQUEST);
			}
		});
    }
    
    /**
     * get new values once new event creation is finished
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
			final TextView orgName = (TextView) findViewById(R.id.SupEditOrgName);
			orgID = data.getStringExtra("ID");
			userName = data.getStringExtra("organization");
			orgName.setText(data.getStringExtra("organization"));
			//Parameters for the cloud code
			params.put("org", orgID);
		}
		else{
			Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Listener to be placed on button to display list of all of an organization's events
	 */
	public void allEventsListener(){
		final Button AllEventButton = (Button) findViewById(R.id.SuperListEvents);
		AllEventButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), SuperEventList.class);
				i.putExtra("organization", userName);
				i.putExtra("orgID", orgID);
				startActivityForResult(i, EDITOR_ACTIVITY_REQUEST);
			}
		});
	}
	
	/**
	 * listener for button to go to edit organization information page
	 */
	public void orgInformationListener(){
		final Button information = (Button) findViewById(R.id.SuperEditOrgInformation);
		information.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), SuperEditOrgInformation.class);
				i.putExtra("organization", userName);
				i.putExtra("orgID", orgID);
				startActivityForResult(i, EDITOR_ACTIVITY_REQUEST);
			}
		});
	}
	
	/**
	 * Delete an Organization!!!!
	 */
	public void deleteOrganizationListener(){
		final Button delete = (Button) findViewById(R.id.SuperDeleteOrg);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(activity).setTitle("Delete")
		        .setMessage("Are you sure you want to delete " + userName + " ?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			        	ParseCloud.callFunctionInBackground("deleteOrganization", params, new FunctionCallback<String>() {
							   public void done(String success, ParseException e) {
							       if (e == null) {
							    	   Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
							    	   Intent i = new Intent(getApplicationContext(), SuperUserMain.class);
							    	   startActivity(i);
							       }
							       else{
							    	   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
							       }
							   }
							});
		        }
		    })
		    .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       })
		    .show();
			}
		});
	}
}

