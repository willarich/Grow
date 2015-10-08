package com.example.grow;
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
import android.widget.Button;
import android.widget.TextView;

public class StudentEventInfo extends Activity {
	String host;
    String title;
    String time;
    String date;
    String numVolunteers;
    String description;
    ParseUser currentUser;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_event_info);
        Button button = (Button) findViewById(R.id.JoinEventButton);
		button.setText("Join This Event");
	final TextView hos = (TextView) findViewById(R.id.event_hostText);
	final TextView tit = (TextView) findViewById(R.id.event_titleText);
	final TextView ti = (TextView) findViewById(R.id.event_timeText);
	final TextView d = (TextView) findViewById(R.id.event_dateText);
	final TextView nv = (TextView) findViewById(R.id.event_numPplText);
	final TextView des = (TextView) findViewById(R.id.event_descriptionText);

	Intent intent = getIntent();
	
	host = intent.getStringExtra("host");
	title = intent.getStringExtra("title");
	time = intent.getStringExtra("time");
	date = intent.getStringExtra("date");
	numVolunteers = intent.getStringExtra("numVolunteers");
	description = intent.getStringExtra("description");
	
	hos.setText(host);
	tit.setText(title);
	ti.setText(time);
	d.setText(date);
	nv.setText(numVolunteers);
	des.setText(description);
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
    
    public void studentJoinEvent(View view){
    	currentUser = ParseUser.getCurrentUser();
    	String events = currentUser.getString("events");
    	currentUser.put("events", events + " " + title + " ");
    	currentUser.saveInBackground();
    	Intent intent = new Intent(this, StudentConfirmationPage.class);
    	startActivity(intent);
    }
    


}