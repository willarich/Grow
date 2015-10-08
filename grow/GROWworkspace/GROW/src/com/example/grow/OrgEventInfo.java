package com.example.grow;
import java.util.List;

import com.custom.grow.*;
import com.parse.ParseQuery;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import android.view.View.OnClickListener;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrgEventInfo extends Activity {
	boolean dateCorrect = true;
	boolean timeCorrect = true;
	boolean numVolunteersCorrect = true;
    boolean pushEvent;
    String previousTitle;
    String eventtitle;
    String eventtime;
    String eventdate;
    String numVolunteers;
    String eventdescription;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_event_info);
	final TextView tit = (TextView) findViewById(R.id.event_titleText);
	final TextView ti = (TextView) findViewById(R.id.event_timeText);
	final TextView d = (TextView) findViewById(R.id.event_dateText);
	final TextView nv = (TextView) findViewById(R.id.event_numPplText);
	final TextView des = (TextView) findViewById(R.id.event_descriptionText);

	Intent intent = getIntent();

	eventtitle = intent.getStringExtra("title");
	eventtime = intent.getStringExtra("time");
	eventdate = intent.getStringExtra("date");
	numVolunteers = intent.getStringExtra("numVolunteers");
	eventdescription = intent.getStringExtra("description");
	previousTitle = intent.getStringExtra("title");

	tit.setText(eventtitle);
	ti.setText(eventtime);
	d.setText(eventdate);
	nv.setText(numVolunteers);
	des.setText(eventdescription);

	submitEventInfoChangesListener();
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

    /**
     * Get content on the screen and change it in Parse
     */
    public void submitEventInfoChangesListener() {
		final Button submit = (Button) findViewById(R.id.EditEventButton);
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
		pushEvent = true;
		dateCorrect = true;
    	timeCorrect = true;
    	numVolunteersCorrect = true;
		
		// Get labels from view
		TextView titleLabel = (TextView) findViewById(R.id.event_titleText);
		TextView timeLabel = (TextView) findViewById(R.id.event_timeText);
		TextView dateLabel = (TextView) findViewById(R.id.event_dateText);
		TextView numLabel = (TextView) findViewById(R.id.event_numPplText);
		TextView desLabel = (TextView) findViewById(R.id.event_descriptionText);
		
		titleLabel.setTextColor(Color.BLACK);
		timeLabel.setTextColor(Color.BLACK);
		dateLabel.setTextColor(Color.BLACK);
		numLabel.setTextColor(Color.BLACK);
		desLabel.setTextColor(Color.BLACK);
	
		// Make sure fields aren't empty
		String title = titleLabel.getText().toString();
		String time = timeLabel.getText().toString();
		String date = dateLabel.getText().toString();
		String numVol = numLabel.getText().toString();
		String description = desLabel.getText().toString();
	
		if (title.equals("")) {
		    pushEvent = false;
		    titleLabel.setTextColor(Color.RED);
		}
		if (time.equals("")) {
		    pushEvent = false;
		    timeLabel.setTextColor(Color.RED);
		}
		if (date.equals("")) {
		    pushEvent = false;
		    dateLabel.setTextColor(Color.RED);
		}
		if (numVol.equals("")) {
		    pushEvent = false;
		    numLabel.setTextColor(Color.RED);
		}
		if (description.equals("")) {
		    pushEvent = false;
		    desLabel.setTextColor(Color.RED);
		}
		if(!RegexMatch.testDate(date)){
    		dateCorrect = false;
    	}
    	
    	if(!RegexMatch.testTime(time)){
    		timeCorrect = false;
    	}
    	
    	if(!RegexMatch.testNumVolunteers(numVol)) {
    		numVolunteersCorrect = false;
    	}
    	
		// Edit the event in Parse
		if (pushEvent == true) {
		    ParseQuery<ParseEvent> query = ParseQuery.getQuery("Event");
		    query.whereEqualTo("title", previousTitle);
		    
		    // Get match
		    query.findInBackground(new FindCallback<ParseEvent>() {
			    public void done(List<ParseEvent> events, ParseException e) {
				if (e == null) {
					TextView titleLabel = (TextView) findViewById(R.id.event_titleText);
					TextView timeLabel = (TextView) findViewById(R.id.event_timeText);
					TextView dateLabel = (TextView) findViewById(R.id.event_dateText);
					TextView numLabel = (TextView) findViewById(R.id.event_numPplText);
					TextView desLabel = (TextView) findViewById(R.id.event_descriptionText);
					String title = titleLabel.getText().toString();
					String time = timeLabel.getText().toString();
					String date = dateLabel.getText().toString();
					String numVol = numLabel.getText().toString();
					String description = desLabel.getText().toString();
				    // Set database fields
					ParseEvent event = events.get(0);
				    event.setTitle(title);
				    event.setTime(time);
				    event.setDate(date);
				    event.setNumVolunteers(numVol);
				    event.setDescription(description);
				    event.saveInBackground(new SaveCallback(){
		            	@Override
		                public void done(ParseException e) {
		                    if (e == null) {
		                    	// Event was successfully edited
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
				else {
				    TextView error = (TextView) findViewById(R.id.errorField);
				    error.setText("Couldn't find the event.");
				}
			    }
			});	    
		}
		else if (pushEvent == false) {
		    TextView error = (TextView) findViewById(R.id.errorField);
		    error.setText("Make sure all fields are filled!");
		}
		else {
    		if(!RegexMatch.testDate(date)){
        		dateCorrect = false;
        		dateLabel.setTextColor(Color.RED);
        	}
        	
        	if(!RegexMatch.testTime(time)){
        		timeCorrect = false;
        		timeLabel.setTextColor(Color.RED);
        		
        	}
        	
        	if(!RegexMatch.testNumVolunteers(numVol)) {
        		numVolunteersCorrect = false;
        		numLabel.setTextColor(Color.RED);
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

    private void saveAndFinish() {
    	Intent intent = new Intent(this, OrgMainNav.class);
    	startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    	saveAndFinish();
    	super.onBackPressed();
    }
}
