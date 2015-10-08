package com.example.grow;

import com.parse.LogInCallback;
import com.parse.ParseException;
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
import android.widget.EditText;
import android.widget.TextView;

public class SignInPageStudent extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page_student);
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
    
    public void studentRegisterScreen(View view) {
    	Intent i = new Intent(getApplicationContext(), StudentRegisterScreen.class);
    	startActivity(i);
    }
    
    /**
     * Code to sign in an existing Organization (Or Super User)
     * @param view
     */
    public void studentSignIn(View view){
    	final EditText userName = (EditText) findViewById(R.id.StudentNameSignText);
    	final EditText password = (EditText) findViewById(R.id.StudentPasswordSignText);
    	final TextView error = (TextView) findViewById(R.id.ErrorStudentSignIn);
    	ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(),
                new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                        	ParseUser currentUser = ParseUser.getCurrentUser();
                        	boolean isOrg = (Boolean) currentUser.get("organization");
                            // Make sure it is not an organization signing in
                        	// If it is a student, then log them them in!!!!
                           	if(!isOrg){
                          		Intent i = new Intent(getApplicationContext(), StudentMainNav.class);
                            	startActivity(i);
                           	}
                           	//Actually an organization. Log them out!!!
                           	else{
                           		ParseUser.logOut();
                           		error.setText("Please log in as an organization!");
                           	}
                        } 
                        //Student user does not exist. retype name and password or create a new account
                        else {
                            if(e.getCode() == ParseException.CONNECTION_FAILED){
                            	 error.setText("Internet connection lost");
                            }
                            else if(e.getCode() == ParseException.ACCOUNT_ALREADY_LINKED){
                            	error.setText("Already logged in");
                            }
                            else{
                            	error.setText("Account information not recognized");
                            }
                            
                        }
                    }
                });
    }
}
