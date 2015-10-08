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
import android.widget.Toast;

public class SignInPageOrg extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page_org);
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
    
    /**
     * Start organization registration page
     * @param view
     */
    public void orgRegisterScreen(View view) {
    	Intent i = new Intent(getApplicationContext(), OrgRegisterScreen.class);
    	startActivity(i);
    }
    
    /**
     * Code to sign in an existing Organization (Or Super User)
     * @param view
     */
    public void orgSignIn(View view){
    	final EditText orgName = (EditText) findViewById(R.id.nameText);
    	final EditText orgPassword = (EditText) findViewById(R.id.passwordText);
    	final TextView error = (TextView) findViewById(R.id.errorMessage);
    	ParseUser.logInInBackground(orgName.getText().toString(), orgPassword.getText().toString(),
                new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                        	ParseUser currentUser = ParseUser.getCurrentUser();
                        	boolean isSuper = (Boolean) currentUser.get("super");
                        	// Check to see if Super User logs in
                        	if(isSuper){
                        		Intent i = new Intent(getApplicationContext(), SuperUserMain.class);
                        		startActivity(i);
                        	}
                        	//Check regular org sign in
                        	else{
                        		boolean isOrg = (Boolean) currentUser.get("organization");
                            	// Make sure an organization is being checked
                            	if(isOrg){
                            		boolean isVerified = (Boolean) currentUser.get("superverified");
                                	// Has the organization been verified by the superuser?
                                	if (isVerified){
                                		Toast.makeText(getApplicationContext(), "Welcome " + currentUser.getUsername(), Toast.LENGTH_LONG).show();
                                		Intent i = new Intent(getApplicationContext(), OrgMainNav.class);
                                		startActivity(i);
                                	}
                                	else{
                                		ParseUser.logOut();
                                		Toast.makeText(getApplicationContext(), "Not Verified!", Toast.LENGTH_LONG).show();
                                	}
                            	}
                            	//Actually a student user. Log them out!!!
                            	else{
                            		ParseUser.logOut();
                            		Toast.makeText(getApplicationContext(), "Please log in as a student user!", 
                            				Toast.LENGTH_LONG).show();
                            	}
                        	}
                        } 
                        //Org User does not exist. retype name and password or create a new account
                        else {
                            if(e.getCode() == ParseException.CONNECTION_FAILED){
                            	 error.setText("Internet connection lost");
                            }
                            else if(e.getCode() == ParseException.ACCOUNT_ALREADY_LINKED){
                            	error.setText("Organization already logged in");
                            }
                            else{
                            	error.setText("Organization information not recognized");
                            }
                            
                        }
                    }
                });
    }
}
