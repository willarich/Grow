

package com.example.grow;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.custom.grow.ParseEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StudentMyEvents extends Fragment {
	ListView lv;
	List<ParseEvent> parse;
	ArrayList<String> EventTitle = new ArrayList<String>();
	ParseUser currentUser;

	
	public StudentMyEvents(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.student_my_events, container, false);
         
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ParseObject.registerSubclass(ParseEvent.class);
		currentUser  = ParseUser.getCurrentUser();
		getEvents();
	}
	
	private void getEvents() {
		ParseQuery<ParseEvent> query = ParseQuery.getQuery("Event");
		
		
		query.findInBackground(new FindCallback<ParseEvent>() {

		  public void done(List<ParseEvent> objects, ParseException e) {
		     if (e==null) {
		    	 if (currentUser.getString("events") != null){
		    	
		    		 for (ParseEvent o : objects) {
		    			 Pattern pattern = Pattern.compile(o.getTitle());
		    			 Matcher matcher = pattern.matcher(currentUser.getString("events"));
		    			 if(matcher.find()){
		    				 EventTitle.add(o.getTitle());
		    			 }
		    		 }
		    	 }
				 parse = objects;
				 refreshDisplay();
		     }
		     else {
				TextView error = (TextView) getView().findViewById(R.id.errorField);
				error.setText("Something went wrong");
		     }
		  }	
	     });
	}
	
	/**

	* Refresh the listView with list of unverified organizations

	* @param orgList

	*/

	private void refreshDisplay(){
		lv = (ListView) getView().findViewById(R.id.list);
		ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.student_my_event_list_item, EventTitle);
		//CustomParseEventAdapter eventAdapter = new CustomParseEventAdapter(this, parse);
		lv.setAdapter(eventAdapter);
		eventAdapter.notifyDataSetChanged();
	       

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

}

