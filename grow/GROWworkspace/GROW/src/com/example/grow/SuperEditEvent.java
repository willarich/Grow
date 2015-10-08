package com.example.grow;

import java.util.HashMap;

import com.custom.grow.ParseEvent;
import com.custom.grow.RegexMatch;
import com.parse.GetCallback;
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

public class SuperEditEvent extends Activity {
	EditText title;
	EditText date;
	EditText time;
	EditText numPeople;
	EditText description;
	
	String orgID;
	String orgName;
	String eventName;
	String eventID;
	ParseEvent event;
	HashMap<String, Object> params  = new HashMap<String, Object>();
	
	boolean pushEvent;
	boolean dateCorrect = true;
	boolean timeCorrect = true;
	boolean result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.super_edit_event);
		TextView title = (TextView) findViewById(R.id.SuperEditEventTitle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		orgName = intent.getStringExtra("organization");
		orgID = intent.getStringExtra("ID");
		eventName = intent.getStringExtra("eventName");
		eventID = intent.getStringExtra("eventID");
		title.setText("Edit Event for " + orgName);
		submitChangeListener();
		getEvent();
		
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
		intent.putExtra("eventName", eventName);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		saveAndFinish();
		super.onBackPressed();
	}
	
	/**
	 * get the event object from parse
	 */
	public void getEvent(){
		ParseQuery<ParseEvent> query = ParseQuery.getQuery("Event");
		 query.getInBackground(eventID, new GetCallback<ParseEvent>() {
		     public void done(ParseEvent o, ParseException e) {
		         if (e == null) {
		             event = o;
		             displayEventDetails();
		         } else {
		             Toast.makeText(getApplicationContext(), "Could not get event details!", Toast.LENGTH_LONG).show();
		         }
		     }
		 });
	}
	
	/**
	 * function to display all of the event details that were pulled from parse
	 */
	public void displayEventDetails(){
		title = (EditText) findViewById(R.id.SupEditEventTitleText);
		date = (EditText) findViewById(R.id.SupEditEventDateText);
		time = (EditText) findViewById(R.id.SupEditEventTimeText);
		numPeople = (EditText) findViewById(R.id.SupEditEventNumPplText);
		description = (EditText) findViewById(R.id.SupEditEventDescriptionText);
		title.setText(event.getTitle());
		date.setText(event.getDate());
		time.setText(event.getTime());
		numPeople.setText(event.getNumVolunteers());
		description.setText(event.getDescription());
	}
	
	/**
	 * Listener for the submit change button
	 */
	public void submitChangeListener(){
		final Button eventEdit = (Button) findViewById(R.id.SupEditEventButton);
		eventEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				submitEditedEvent();
			}
		});
	}
	
	
    /**
     * Check to see if a field is empty 
     * @param v
     * @return
     */
    private boolean isEmpty(TextView v){
    	return v.getText().toString().equals("");
    }
    
    /**
     * check to see if any fields are empty
     * @param text
     * @return
     */
    private boolean anyFieldsEmpty(EditText[] text){
    	for(TextView v : text){
    		if(isEmpty(v)){
    			return true;
    		}
    	}
    	return false;
    }
    
	/**
	 * Function to submit the edited event to the parse database
	 */
	public void submitEditedEvent(){
		pushEvent = true;
    	dateCorrect = true;
    	timeCorrect = true;
		EditText[] editableTexts = {title, date, time, numPeople, description};
		
		TextView titleLabel = (TextView) findViewById(R.id.SupEditEventTitle);
		TextView dateLabel = (TextView) findViewById(R.id.SupEditEventDate);
		TextView timeLabel = (TextView) findViewById(R.id.SupEditEventTime);
		TextView numVolsLabel = (TextView) findViewById(R.id.SupEditEventPeopleNeeded);
		TextView descriptionLabel = (TextView) findViewById(R.id.SupEditEventDescription);
		
		// Make sure all labels are initialized to be black
		
		titleLabel.setTextColor(Color.BLACK);
    	dateLabel.setTextColor(Color.BLACK);
    	timeLabel.setTextColor(Color.BLACK);
    	descriptionLabel.setTextColor(Color.BLACK);
    	numVolsLabel.setTextColor(Color.BLACK);
		
    	if(title.getText().toString().equals("")){
    		pushEvent = false;
    		titleLabel.setTextColor(Color.RED);
    	}
    	if(date.getText().toString().equals("")){
    		pushEvent = false;
    		dateLabel.setTextColor(Color.RED);
    	}
    	if(time.getText().toString().equals("")){
    		pushEvent = false;
    		timeLabel.setTextColor(Color.RED);
    	}
    	if(numPeople.getText().toString().equals("")){
    		pushEvent = false;
    		numVolsLabel.setTextColor(Color.RED);
    	}
    	if(description.getText().toString().equals("")){
    		pushEvent = false;
    		descriptionLabel.setTextColor(Color.RED);
    	}
    	if(!RegexMatch.testDate(date.getText().toString())){
    		pushEvent = false;
    	}
    	if(!RegexMatch.testTime(time.getText().toString())){
    		pushEvent = false;
    	}
    	if((pushEvent == true)){
        	
        	//Edit the event from the information given
       
    		event.setTitle(title.getText().toString());
    		event.setDate(date.getText().toString());
    		event.setTime(time.getText().toString());
    		event.setDescription(description.getText().toString());
    		//event.setNumVolunteers(numPeople.getText().toString());
            
            event.saveInBackground(new SaveCallback(){
            	@Override
                public void done(ParseException e) {
                    if (e == null) {
                    	//Event was edited and successfully pushed to parse
                    	eventName = title.getText().toString();
                    	Toast.makeText(getApplicationContext(), "Succesfully Edited Event!", Toast.LENGTH_SHORT).show();
                    	saveAndFinish();
                    } 
                    else {
                    	// An error occurred that needs to be fixed. Properly display error.
                    	TextView error = (TextView) findViewById(R.id.SupEditEventErrorField);
                    	error.setText("Something went wrong");
                    }
                }
            });
          
        }
    
        else if(pushEvent == false){
        	if(anyFieldsEmpty(editableTexts)){
        		TextView error = (TextView) findViewById(R.id.SupEditEventErrorField);
        		error.setText("Make sure all fields are filled!");
        	}
        	
        	else{
        		if(!RegexMatch.testDate(date.getText().toString())){
            		dateCorrect = false;
            		dateLabel.setTextColor(Color.RED);
            	}
            	if(!RegexMatch.testTime(time.getText().toString())){
            		timeCorrect = false;
            		timeLabel.setTextColor(Color.RED);
            		
            	}
            	if(dateCorrect == false & timeCorrect == false){
            		TextView error = (TextView) findViewById(R.id.SupEditEventErrorField);
            		error.setText("Please re-format the date and time!");
            	}
            	else {
            		if(dateCorrect == false){
            			TextView error = (TextView) findViewById(R.id.SupEditEventErrorField);
            			error.setText("Please re-format the date!");
            		}
            		else{
            			TextView error = (TextView) findViewById(R.id.SupEditEventErrorField);
            			error.setText("Please re-format the time!");
            		}
            	}
        	}
        }
	}
}
