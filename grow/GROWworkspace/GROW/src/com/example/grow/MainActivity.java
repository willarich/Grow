package com.example.grow;


import com.custom.grow.ParseEvent;
import com.parse.ParseObject;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.parse.Parse;


public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this, "urYLFgtcNa4DQlNWCLUvAS5CtcvlggzEIWyW3xV0", "TFYMNDNE2uaF4IWaqMKO9ntuNgPaIQiVhROQf1qm");
        ParseObject.registerSubclass(ParseEvent.class);
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
    	
        // Handle action bar actions click
    	int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    public void orgFirstScreen(View view) {
    	// Function from first screen when registers organization click
    	Intent i = new Intent(getApplicationContext(), SignInPageOrg.class);
    	startActivity(i);
    }
    
    public void studentFirstScreen(View view) {
    	// Function from first screen when registers student click
    	Intent i = new Intent(getApplicationContext(), SignInPageStudent.class);
    	
    	startActivity(i);
    }
    
	@Override
	public void onBackPressed(){
		//DO NOTHING! =)
	}

}
