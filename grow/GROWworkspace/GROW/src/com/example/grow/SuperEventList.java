package com.example.grow;

import java.util.ArrayList;
import java.util.List;

import com.custom.grow.ParseEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SuperEventList extends Activity implements OnItemClickListener{
	
	String orgName;
	String orgID;
	String eventName;
	ArrayList<String> EventTitle = new ArrayList<String>();
	ArrayAdapter<String> eventAdapter;
	List<ParseEvent> orgEvents;
	ListView eventList;
	boolean result;
	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.super_event_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		TextView title = (TextView) findViewById(R.id.SuperOrgEventListTitle);
		Intent intent = getIntent();	
		orgID = intent.getStringExtra("orgID");
		orgName = intent.getStringExtra("organization");
		title.setText(orgName);
		getEvents();
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
	 * Refresh display of the organization's events
	 */
	private void refreshDisplay(int count) {
		if(count == 0){
			TextView error = (TextView) findViewById(R.id.superOrgEventListError);
			error.setText("No Active Events!");
		}
		eventList = (ListView) findViewById(R.id.list);
		eventAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.event_listrow, EventTitle);
		eventList.setOnItemClickListener(this);
		eventList.setAdapter(eventAdapter);
		eventAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Get list of events for the organization that was clicked on
	 * 
	 */
	private void getEvents() {
		// Pull down the events from Parse that correspond to this specific user           
		ParseQuery<ParseEvent> query = ParseQuery.getQuery("Event");
		query.whereEqualTo("createdBy", orgName);
		query.findInBackground(new FindCallback<ParseEvent>() {
		  public void done(List<ParseEvent> objects, ParseException e) {
		     if (e==null) {
		    	 int count = 0;
		    	 for (ParseEvent o : objects) {
		    		 EventTitle.add(o.getTitle());
		    		 count++;
		    	 }
				 orgEvents = objects;
				 refreshDisplay(count);
		     }
		     else {
		    	 Toast.makeText(getApplicationContext(), "Something is wrong with list", Toast.LENGTH_LONG).show();
		     }
		  }	
	     });
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ParseEvent event = orgEvents.get(position);
		Intent intent = new Intent(this, SuperEditEvent.class);
		intent.putExtra("eventName", event.getTitle());
		intent.putExtra("organization", orgName);
		intent.putExtra("ID", orgID);
		intent.putExtra("eventID", event.getObjectId());
		intent.putExtra("position", position);
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
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
	 *After editing an event call this to clear the list
	 */
	public void removeList(){
		eventAdapter.clear();
		eventAdapter.notifyDataSetChanged();
	}
	
    /**
     * get new values once new event creation is finished
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
			final TextView orgTitle = (TextView) findViewById(R.id.SuperOrgEventListTitle);
			orgID = data.getStringExtra("ID");
			orgName = data.getStringExtra("organization");
			eventName = data.getStringExtra("eventName");
			orgTitle.setText(orgName);
			removeList();
			getEvents();
		}
		else{
			Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
		}
	}
	
	
}
