

package com.example.grow;

import java.util.ArrayList;
import java.util.List;

import com.custom.grow.ParseEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

public class OrgHomeFragment extends Fragment implements OnItemClickListener {
	
	ListView lv;
	List<ParseEvent> parse;
	ParseUser currentUser;
	ArrayList<String> EventTitle = new ArrayList<String>();

	public OrgHomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.org_home_page, container, false);
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        TextView orgTitle = (TextView) view.findViewById(R.id.organizationName);
        currentUser = ParseUser.getCurrentUser();
		orgTitle.setText(currentUser.getUsername());
//		// Have to populate parse with the actual events
		getEvents();
	}
	
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ParseEvent event = parse.get(position);
		Intent intent = new Intent(getActivity().getApplicationContext(), OrgEventInfo.class);
		intent.putExtra("title", event.getTitle());
		intent.putExtra("date", event.getDate());
		intent.putExtra("time", event.getTime());
		intent.putExtra("numVolunteers", event.getNumVolunteers());
		intent.putExtra("description", event.getDescription());
		startActivity(intent);
    }

	private void getEvents() {
		// Pull down the events from Parse that correspond to this specific user
		ParseQuery<ParseEvent> query = ParseQuery.getQuery("Event");
		query.whereEqualTo("createdBy", ParseUser.getCurrentUser().getUsername());
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
				TextView error = (TextView) getView().findViewById(R.id.errorField);
				error.setText("Something went wrong");
		     }
		  }	
	     });
	}

	private void refreshDisplay() {
		lv = (ListView) getView().findViewById(R.id.list);
		ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.event_listrow, EventTitle);
		//CustomParseEventAdapter eventAdapter = new CustomParseEventAdapter(this, parse);
		lv.setOnItemClickListener(this);
		lv.setAdapter(eventAdapter);
		eventAdapter.notifyDataSetChanged();
	}
	
}

