

package com.example.grow;

import com.custom.grow.ParseEvent;
import com.custom.grow.RegexMatch;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateEventFragment extends Fragment {
	
	boolean pushEvent;
	boolean dateCorrect = true;
	boolean timeCorrect = true;
	boolean numVolunteersCorrect = true;
	ParseUser currentUser;
	
	public CreateEventFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.org_create_event, container, false);
         
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Spinner spinner = (Spinner) view.findViewById(R.id.orgCreateEventSpinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.SpinnerCategoryArray, android.R.layout.simple_spinner_item);
                
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Button createEventButton = (Button) view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				orgCreateEvent(v);
			}
		});
        ParseObject.registerSubclass(ParseEvent.class);
        
        currentUser = ParseUser.getCurrentUser();
	}
	
	/**
	 * check to see if a view has user input in it
	 * @param v
	 * @return
	 */
	   private boolean isEmpty(TextView v){
	    	return v.getText().toString().equals("");
	    }
	   
	   /**
	    * Check if editText fields have been filled by user 
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
	     * Organization creates the event and pushes it to the parse database
	     * @param v
	     */
	    public void orgCreateEvent(View v){
	    	pushEvent = true;
	    	dateCorrect = true;
	    	timeCorrect = true;
	    	numVolunteersCorrect = true;
	    	final EditText eventTitle = (EditText) getActivity().findViewById(R.id.titleText);
	    	final EditText eventDate = (EditText) getActivity().findViewById(R.id.dateText);
	    	final EditText eventTime = (EditText) getActivity().findViewById(R.id.timeText);
	    	final EditText numOfVolunteers = (EditText) getActivity().findViewById(R.id.numPplText);
	    	EditText eventDescription = (EditText) getActivity().findViewById(R.id.descriptionText);
	    	EditText[] editableTexts = {eventTitle, eventDate, eventTime, numOfVolunteers, eventDescription};
	    	
	    	//get the TextView Labels from the view
	    	TextView titleLabel = (TextView) getActivity().findViewById(R.id.orgCreateEventTitle);
	    	TextView dateLabel = (TextView) getActivity().findViewById(R.id.orgCreateEventDate);
	    	TextView timeLabel = (TextView) getActivity().findViewById(R.id.orgCreateEventTime);
	    	TextView numVolsLabel = (TextView) getActivity().findViewById(R.id.peopleNeeded);
	    	TextView descriptionLabel = (TextView) getActivity().findViewById(R.id.orgCreateEventDescription);
	    	
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
	                    	Intent i = new Intent(getActivity().getApplicationContext(), OrgMainNav.class);
	                    	startActivity(i);
	                    } 
	                    else {
	                    	// An error occurred that needs to be fixed. Properly display error.
	                    	TextView error = (TextView) getActivity().findViewById(R.id.errorField);
	                    	error.setText("Something went wrong");
	                    }
	                }
	            });
	          
	        }
	        
	        else if(pushEvent == false){
	        	
	        	
	        	if(anyFieldsEmpty(editableTexts)){
	        		TextView error = (TextView) getActivity().findViewById(R.id.errorField);
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
	            		TextView error = (TextView) getActivity().findViewById(R.id.errorField);
	            		error.setText("Please re-format the date and time!");
	            	}
	            	else {
	            		if(dateCorrect == false){
	            			TextView error = (TextView) getActivity().findViewById(R.id.errorField);
	            			error.setText("Please re-format the date!");
	            		}
	            		if(numVolunteersCorrect == false) {
	            			TextView error = (TextView) getActivity().findViewById(R.id.errorField);
	            			error.setText("Please re-format the number of volunteers!");
	            		}
	            		else{
	            			TextView error = (TextView) getActivity().findViewById(R.id.errorField);
	            			error.setText("Please re-format the time!");
	            		}
	            	}	
	        	}
	        }
	    }
	
	
	
	
	
	
}

