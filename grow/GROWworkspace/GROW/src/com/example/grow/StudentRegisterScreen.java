package com.example.grow;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

public class StudentRegisterScreen extends Activity {
	boolean pushUser;
	boolean isConfirmed;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_screen);
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
     * Code to register a new student user account
     * @param v
     */
    public void studentRegister(View v){
    	
    	pushUser = true;
    	isConfirmed = true;
    	
    	//get the EditText values from view
    	final EditText userName = (EditText) findViewById(R.id.StudentName);
    	final EditText password = (EditText) findViewById(R.id.StudentPassword);
    	final EditText email = (EditText) findViewById(R.id.StudentEmail);
    	final EditText confPassword = (EditText) findViewById(R.id.stuConfPassText);
    	
    	//get the label values from the view
    	TextView stuPassLabel = (TextView) findViewById(R.id.PasswordStuLabel);
    	TextView stuNameLabel = (TextView) findViewById(R.id.NameStuLabel);
    	TextView stuEmailLabel = (TextView) findViewById(R.id.EmailStuLabel);
    	TextView stuConfPassLabel = (TextView) findViewById(R.id.stuConfPassLabel);
    	TextView error = (TextView) findViewById(R.id.stuErrorField);
    	
    	//Make sure the labels and text are black
    	stuNameLabel.setTextColor(Color.BLACK);
    	stuPassLabel.setTextColor(Color.BLACK);
    	stuEmailLabel.setTextColor(Color.BLACK);
    	email.setTextColor(Color.BLACK);
    	password.setTextColor(Color.BLACK);
    	userName.setTextColor(Color.BLACK);
    	confPassword.setTextColor(Color.BLACK);
    	stuConfPassLabel.setTextColor(Color.BLACK);
    	
    	//Check to see if the fields have values
    	if(password.getText().toString().equals("")){
    		pushUser = false;
    		stuPassLabel.setTextColor(Color.RED);
    	}
    	if(userName.getText().toString().equals("")){
    		pushUser = false;
    		stuNameLabel.setTextColor(Color.RED);
    	}
    	if(email.getText().toString().equals("")){
    		pushUser = false;
    		stuEmailLabel.setTextColor(Color.RED);
    	}
    	if(confPassword.getText().toString().equals("")){
    		pushUser = false;
    		stuConfPassLabel.setTextColor(Color.RED);
    	}
    	//Even if all fields are filled, check to see if Password and confirmation are the same
    	if(pushUser == true){
    		if(!password.getText().toString().equals(confPassword.getText().toString())){
    			isConfirmed = false;
    		}
    	}
    	// Attempt to sign up the org account if all fields are filled
        if((pushUser == true) && (isConfirmed == true)){
        	//Create a new Parse user from the information given
            ParseUser user = new ParseUser();
            user.put("organization", false);
            user.put("superverified", false);
            user.put("super", false);
            user.setUsername(userName.getText().toString());
            user.setPassword(password.getText().toString());
            user.setEmail(email.getText().toString());
        	user.signUpInBackground(new SignUpCallback() {
                 @Override
                 public void done(ParseException e) {
                     if (e == null) {
                     	//Student user was created, signed in, and successfully pushed to parse
                      	Intent i = new Intent(getApplicationContext(), StudentMainNav.class);
                      	startActivity(i);
                     } 
                     else {
                     	// An error occurred that needs to be fixed. Properly display error.
                     	TextView error = (TextView) findViewById(R.id.stuErrorField);
                     	if(e.getCode() == ParseException.INVALID_EMAIL_ADDRESS){
                     		email.setTextColor(Color.RED);
                     		error.setText("Invalid Email Address");
                     	}
                     	else if(e.getCode() == ParseException.EMAIL_TAKEN ){
                     		email.setTextColor(Color.RED);
                     		error.setText("Email is already taken");
                     	}
                     	else if(e.getCode() == ParseException.USERNAME_TAKEN){
                     		userName.setTextColor(Color.RED);
                     		error.setText("Username already taken!");
                     	}
                     	else if(e.getCode() == ParseException.CONNECTION_FAILED){
                     		error.setText("Internet connection failed!");
                     	}
                     }
                 }
             });
        }
        // All fields were not properly filled
        else if(pushUser == false){
        	error.setText("Make sure all fields are filled!");
        }
        else{
        	stuConfPassLabel.setTextColor(Color.RED);
        	stuPassLabel.setTextColor(Color.RED);
        	error.setText("Password and confirmation not equal");
        }
    }
}
