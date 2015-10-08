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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class OrgRegisterScreen extends Activity {
	
	boolean pushUser;
	boolean isConfirmed;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_register_screen);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.SpinnerCategoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
     * Code to sign an organization up in parse
     * @param v
     */
    public void orgSignUp(View v){
    	// Get all fields from the org sign up screen
    	pushUser = true;
    	isConfirmed = true;
    	
    	//get the EditText values from view
    	final EditText orgName = (EditText) findViewById(R.id.NameTextField);
    	final EditText orgPassword = (EditText) findViewById(R.id.PasswordTextField);
    	final EditText orgEmail = (EditText) findViewById(R.id.EmailTextField);
    	EditText orgDescription = (EditText) findViewById(R.id.DescriptionTextField);
    	EditText confirmPassword = (EditText) findViewById(R.id.ConfPassText);
    	//get the TextView Labels from the view
    	TextView passWordLabel = (TextView) findViewById(R.id.PasswordLabel);
    	TextView orgNameLabel = (TextView) findViewById(R.id.NameLabel);
    	TextView emailLabel = (TextView) findViewById(R.id.EmailLabel);
    	TextView descriptionLabel = (TextView) findViewById(R.id.DescriptionLabel);
    	TextView confPassLabel = (TextView) findViewById(R.id.ConfPassLabel);
    	
    	//Make sure labels are reset to the correct color
    	orgNameLabel.setTextColor(Color.BLACK);
    	passWordLabel.setTextColor(Color.BLACK);
    	emailLabel.setTextColor(Color.BLACK);
    	descriptionLabel.setTextColor(Color.BLACK);
    	orgEmail.setTextColor(Color.BLACK);
    	orgPassword.setTextColor(Color.BLACK);
    	orgName.setTextColor(Color.BLACK);
    	confPassLabel.setTextColor(Color.BLACK);
    	        
        //Check to see if the fields have values
    	if(orgPassword.getText().toString().equals("")){
    		pushUser = false;
    		passWordLabel.setTextColor(Color.RED);
    	}
    	if(orgName.getText().toString().equals("")){
    		pushUser = false;
    		orgNameLabel.setTextColor(Color.RED);
    	}
    	if(orgEmail.getText().toString().equals("")){
    		pushUser = false;
    		emailLabel.setTextColor(Color.RED);
    	}
    	if(confirmPassword.getText().toString().equals("")){
    		pushUser = false;
    		confPassLabel.setTextColor(Color.RED);
    	}
    	if(orgDescription.getText().toString().equals("")){
    		pushUser = false;
    		descriptionLabel.setTextColor(Color.RED);
    	}
    	
    	//Even if all fields are filled, check to see if Password and confirmation are the same
    	if(pushUser == true){
    		if(!orgPassword.getText().toString().equals(confirmPassword.getText().toString())){
    			isConfirmed = false;
    		}
    	}
	
        // Attempt to sign up the org account if all fields are filled
        if((pushUser == true) && (isConfirmed == true)){
        	//Create a new Parse user from the information given
            ParseUser user = new ParseUser();
            user.put("organization", true);
            user.put("description", orgDescription.getText().toString());
            user.put("superverified", false);
            user.put("super", false);
            user.setUsername(orgName.getText().toString());
            user.setPassword(orgPassword.getText().toString());
            user.setEmail(orgEmail.getText().toString());
            
        	user.signUpInBackground(new SignUpCallback() {
                 @Override
                 public void done(ParseException e) {
                     if (e == null) {
                     	//Org was created and successfully pushed to parse
                     	Intent i = new Intent(getApplicationContext(), OrgSuccessSignUp.class);
                     	startActivity(i);
                     } 
                     else {
                     	// An error occurred that needs to be fixed. Properly display error.
                     	TextView error = (TextView) findViewById(R.id.errorField);
                     	if(e.getCode() == ParseException.INVALID_EMAIL_ADDRESS){
                     		orgEmail.setTextColor(Color.RED);
                     		error.setText("Invalid Email Address");
                     	}
                     	else if(e.getCode() == ParseException.EMAIL_TAKEN ){
                     		orgEmail.setTextColor(Color.RED);
                     		error.setText("Email is already taken");
                     	}
                     	else if(e.getCode() == ParseException.USERNAME_TAKEN){
                     		orgName.setTextColor(Color.RED);
                     		error.setText("Organization name already taken!");
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
        	TextView error = (TextView) findViewById(R.id.errorField);
        	error.setText("Make sure all fields are filled!");
        }
        else{
        	TextView error = (TextView) findViewById(R.id.errorField);
        	confPassLabel.setTextColor(Color.RED);
        	passWordLabel.setTextColor(Color.RED);
        	error.setText("Password and confirmation not equal");
        }
    }
}
