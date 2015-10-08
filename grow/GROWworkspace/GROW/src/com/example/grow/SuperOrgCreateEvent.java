package com.example.grow;

import com.custom.grow.ParseEvent;
import com.custom.grow.RegexMatch;
import com.parse.ParseException;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SuperOrgCreateEvent extends Activity {
	
	boolean pushEvent;
	boolean dateCorrect = true;
	boolean timeCorrect = true;
	String orgName;
	String orgID;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_create_event);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        orgID = intent.getStringExtra("orgID");
        orgName = intent.getStringExtra("organization");
        TextView title = (TextView) findViewById(R.id.SupNewEventLabel);
        title.setText("New Event for "+ orgName);
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
     * Create new event for an organization as while in super mode
     * @param v
     */
    public void superOrgCreateEvent(View v){
    	pushEvent = true;
    	dateCorrect = true;
    	timeCorrect = true;
    	
    	final EditText eventTitle = (EditText) findViewById(R.id.SupEventTitleText);
    	final EditText eventDate = (EditText) findViewById(R.id.SupEventDateText);
    	final EditText eventTime = (EditText) findViewById(R.id.SupEventTimeText);
    	final EditText numOfVolunteers = (EditText) findViewById(R.id.SupEventNumPplText);
    	EditText eventDescription = (EditText) findViewById(R.id.SupEventDescriptionText);
    	EditText[] editableTexts = {eventTitle, eventDate, eventTime, numOfVolunteers, eventDescription};
    	
    	//get the TextView Labels from the view
    	TextView titleLabel = (TextView) findViewById(R.id.SupEventTitle);
    	TextView dateLabel = (TextView) findViewById(R.id.SupEventDate);
    	TextView timeLabel = (TextView) findViewById(R.id.SupEventTime);
    	TextView numVolsLabel = (TextView) findViewById(R.id.SupEventPeopleNeeded);
    	TextView descriptionLabel = (TextView) findViewById(R.id.SupEventDescription);
    	
    	titleLabel.setTextColor(Color.BLACK);
    	dateLabel.setTextColor(Color.BLACK);
    	timeLabel.setTextColor(Color.BLACK);
    	descriptionLabel.setTextColor(Color.BLACK);
    	numVolsLabel.setTextColor(Color.BLACK);

    	if(eventTitle.getText().toString().equals("")){
    		pushEvent = false;
    		titleLabel.setTextColor(Color.RED);
    	}
    	if(eventDate.getText().toString().equals("")){
    		pushEvent = false;
    		dateLabel.setTextColor(Color.RED);
    	}
    	if(eventTime.getText().toString().equals("")){
    		pushEvent = false;
    		timeLabel.setTextColor(Color.RED);
    	}
    	if(numOfVolunteers.getText().toString().equals("")){
    		pushEvent = false;
    		numVolsLabel.setTextColor(Color.RED);
    	}
    	if(eventDescription.getText().toString().equals("")){
    		pushEvent = false;
    		descriptionLabel.setTextColor(Color.RED);
    	}
    	
    	if(!RegexMatch.testDate(eventDate.getText().toString())){
    		pushEvent = false;
    	}
    	
    	if(!RegexMatch.testTime(eventTime.getText().toString())){
    		pushEvent = false;
    	}
    	
    
        if((pushEvent == true)){
        	
        	//Create a new Parse user from the information given
            ParseEvent event = new ParseEvent();
            event.put("createdBy",orgName);
            event.setTitle(eventTitle.getText().toString());
            event.setDate(eventDate.getText().toString());
            event.setTime(eventTime.getText().toString());
            event.setNumVolunteers(numOfVolunteers.getText().toString());
            event.setDescription(eventDescription.getText().toString());
            
            event.saveInBackground(new SaveCallback(){
            	@Override
                public void done(ParseException e) {
                    if (e == null) {
                    	//Event was created and successfully pushed to parse
                    	Toast.makeText(getApplicationContext(), "Succesfully Created Event!", Toast.LENGTH_SHORT).show();
                    	saveAndFinish();
                    } 
                    else {
                    	// An error occurred that needs to be fixed. Properly display error.
                    	TextView error = (TextView) findViewById(R.id.SupEventErrorField);
                    	error.setText("Something went wrong");
                    }
                }
            });
          
        }
    
        else if(pushEvent == false){
        	if(anyFieldsEmpty(editableTexts)){
        		TextView error = (TextView) findViewById(R.id.SupEventErrorField);
        		error.setText("Make sure all fields are filled!");
        	}
        	
        	else{
        		if(!RegexMatch.testDate(eventDate.getText().toString())){
            		dateCorrect = false;
            		dateLabel.setTextColor(Color.RED);
            	}
            	
            	if(!RegexMatch.testTime(eventTime.getText().toString())){
            		timeCorrect = false;
            		timeLabel.setTextColor(Color.RED);
            		
            	}
            	
            	if(dateCorrect == false & timeCorrect == false){
            		TextView error = (TextView) findViewById(R.id.SupEventErrorField);
            		error.setText("Please re-format the date and time!");
            	}
            	else {
            		if(dateCorrect == false){
            			TextView error = (TextView) findViewById(R.id.SupEventErrorField);
            			error.setText("Please re-format the date!");
            		}
            		else{
            			TextView error = (TextView) findViewById(R.id.SupEventErrorField);
            			error.setText("Please re-format the time!");
            		}
            	}
        	}
        }
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
	
}
