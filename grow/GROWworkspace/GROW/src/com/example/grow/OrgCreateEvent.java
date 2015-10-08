package com.example.grow;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.custom.grow.ParseEvent;
import com.custom.grow.RegexMatch;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class OrgCreateEvent extends Activity {
	
	boolean pushEvent;
	boolean dateCorrect = true;
	boolean timeCorrect = true;
	boolean numVolunteersCorrect = true;
	ParseUser currentUser;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_create_event);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.SpinnerCategoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        ParseObject.registerSubclass(ParseEvent.class);
        
        currentUser = ParseUser.getCurrentUser();
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private boolean isEmpty(TextView v){
    	return v.getText().toString().equals("");
    }
    
    private boolean anyFieldsEmpty(EditText[] text){
    	for(TextView v : text){
    		if(isEmpty(v)){
    			return true;
    		}
    	}
    	return false;
 
    }
    
    public void orgCreateEvent(View v){
    	pushEvent = true;
    	dateCorrect = true;
    	timeCorrect = true;
    	numVolunteersCorrect = true;
    	
    	final EditText eventTitle = (EditText) findViewById(R.id.titleText);
    	final EditText eventDate = (EditText) findViewById(R.id.dateText);
    	final EditText eventTime = (EditText) findViewById(R.id.timeText);
    	final EditText numOfVolunteers = (EditText) findViewById(R.id.numPplText);
    	EditText eventDescription = (EditText) findViewById(R.id.descriptionText);
    	EditText[] editableTexts = {eventTitle, eventDate, eventTime, numOfVolunteers, eventDescription};
    	
    	//get the TextView Labels from the view
    	TextView titleLabel = (TextView) findViewById(R.id.orgCreateEventTitle);
    	TextView dateLabel = (TextView) findViewById(R.id.orgCreateEventDate);
    	TextView timeLabel = (TextView) findViewById(R.id.orgCreateEventTime);
    	TextView numVolsLabel = (TextView) findViewById(R.id.peopleNeeded);
    	TextView descriptionLabel = (TextView) findViewById(R.id.orgCreateEventDescription);
    	
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
    	
    	if(!RegexMatch.testNumVolunteers(numOfVolunteers.getText().toString())) {
    		pushEvent = false;
    	}
    	
        if((pushEvent == true)){

        	//Create a new Parse user from the information given
            ParseEvent event = new ParseEvent();
            event.put("createdBy", currentUser.getUsername());
            event.setTitle(eventTitle.getText().toString());
            event.setDate(eventDate.getText().toString());
            event.setTime(eventTime.getText().toString());
            event.setNumVolunteers(numOfVolunteers.getText().toString());
            event.setDescription(eventDescription.getText().toString());
            
            event.saveInBackground(new SaveCallback(){
            	@Override
                public void done(ParseException e) {
                    if (e == null) {
                    	//Org was created and successfully pushed to parse
                    	Intent i = new Intent(getApplicationContext(), OrgMainNav.class);
                    	startActivity(i);
                    } 
                    else {
                    	// An error occurred that needs to be fixed. Properly display error.
                    	TextView error = (TextView) findViewById(R.id.errorField);
                    	error.setText("Something went wrong");
                    }
                }
            });
          
        }
        
        else if(pushEvent == false){
        	
        	
        	if(anyFieldsEmpty(editableTexts)){
        		TextView error = (TextView) findViewById(R.id.errorField);
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
            	
            	if(!RegexMatch.testNumVolunteers(numOfVolunteers.getText().toString())) {
            		numVolunteersCorrect = false;
            		numVolsLabel.setTextColor(Color.RED);
            	}
            	
            	if(dateCorrect == false & timeCorrect == false & numVolunteersCorrect == false){
            		TextView error = (TextView) findViewById(R.id.errorField);
            		error.setText("Please re-format the date and time!");
            	}
            	else {
            		if(dateCorrect == false){
            			TextView error = (TextView) findViewById(R.id.errorField);
            			error.setText("Please re-format the date!");
            		}
            		if(numVolunteersCorrect == false) {
            			TextView error = (TextView) findViewById(R.id.errorField);
            			error.setText("Please re-format the number of volunteers!");
            		}
            		else{
            			TextView error = (TextView) findViewById(R.id.errorField);
            			error.setText("Please re-format the time!");
            		}
            	}	
        	}
        }
    }
}