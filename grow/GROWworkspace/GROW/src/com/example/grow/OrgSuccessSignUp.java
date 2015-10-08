package com.example.grow;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class OrgSuccessSignUp extends Activity {
	
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		    setContentView(R.layout.org_successful_sign_up);
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
		 
	    public void goHome(View v){
	    	Intent i = new Intent(getApplicationContext(), MainActivity.class);
	    	startActivity(i);
	    	
	    }
	
	
}