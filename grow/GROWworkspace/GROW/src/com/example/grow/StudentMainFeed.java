package com.example.grow;

import java.util.ArrayList;
import java.util.List;

import com.custom.grow.ParseEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

public class StudentMainFeed extends Activity implements OnItemClickListener {

	ListView lv;
	List<ParseEvent> parse;
	ArrayList<String> EventTitle = new ArrayList<String>();


		@Override

		protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_student_main);
		ParseObject.registerSubclass(ParseEvent.class);
		getEvents();
		}
		
		
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
					TextView error = (TextView) findViewById(R.id.errorField);
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
			lv = (ListView) findViewById(R.id.list);
			ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.student_event_list_item, EventTitle);
			//CustomParseEventAdapter eventAdapter = new CustomParseEventAdapter(this, parse);
			lv.setAdapter(eventAdapter);
			lv.setOnItemClickListener(this);
			eventAdapter.notifyDataSetChanged();
		       

		}


		    @Override

		    public boolean onCreateOptionsMenu(Menu menu) {

		        // Inflate the menu; this adds items to the action bar if it is present.

		        getMenuInflater().inflate(R.menu.main, menu);

		        ActionBar bar = getActionBar();

		        int color = Color.parseColor("#038f0d");

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


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ParseEvent event = parse.get(position);
			Intent intent = new Intent(this, StudentEventInfo.class);
			intent.putExtra("title", event.getTitle());
			intent.putExtra("date", event.getDate());
			intent.putExtra("time", event.getTime());
			intent.putExtra("numVolunteers", event.getNumVolunteers());
			intent.putExtra("description", event.getDescription());
			startActivity(intent);
			
		}





		

}
