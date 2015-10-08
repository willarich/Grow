

package com.example.grow;

import java.util.ArrayList;
import java.util.List;

import com.custom.grow.ParseEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StudentHomeFragment extends Fragment implements OnItemClickListener {
	
	ListView lv;
	List<ParseEvent> parse;
	ArrayList<String> EventTitle = new ArrayList<String>();
	
	public StudentHomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.student_event_list, container, false);
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//setContentView(R.layout.activity_student_main);
	ParseObject.registerSubclass(ParseEvent.class);
	getEvents();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ParseEvent event = parse.get(position);
		Intent intent = new Intent(getActivity().getApplicationContext(), StudentEventInfo.class);
		intent.putExtra("host", event.getCreatedBy());
		intent.putExtra("title", event.getTitle());
		intent.putExtra("date", event.getDate());
		intent.putExtra("time", event.getTime());
		intent.putExtra("numVolunteers", event.getNumVolunteers());
		intent.putExtra("description", event.getDescription());
		startActivity(intent);
	}
	
	/**
	 * get events and refresh the display
	 */
	private void getEvents() {
		// Pull down the events from Parse that correspond to this specific user
		ParseQuery<ParseEvent> query = ParseQuery.getQuery("Event");
		query.findInBackground(new FindCallback<ParseEvent>() {
			
		  public void done(List<ParseEvent> objects, ParseException e) {
		     if (e==null) {
		    	 for (ParseEvent o : objects) {
		    		 EventTitle.add(o.getTitle());
		    	 }
				 parse = objects;
				 refreshDisplay();
		     }
		     else {
				TextView error = (TextView) getActivity().findViewById(R.id.errorField);
				error.setText("Something went wrong");
		     }
		  }	
	     });
	}
	/**
	 * refresh the events displayed
	 */
	private void refreshDisplay(){
		lv = (ListView) getActivity().findViewById(R.id.list);
		ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.student_event_list_item, EventTitle);
		//CustomParseEventAdapter eventAdapter = new CustomParseEventAdapter(this, parse);
		lv.setOnItemClickListener(this);
		lv.setAdapter(eventAdapter);
		eventAdapter.notifyDataSetChanged();
	}


}

