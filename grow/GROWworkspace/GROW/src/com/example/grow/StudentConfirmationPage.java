package com.example.grow;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class StudentConfirmationPage extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_conf_page);
		TextView message = (TextView) findViewById(R.id.confirmation);
		message.setText("Thank you for signing up for this event, you should be receiving a confirmation email shortly!");
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds itehttp://stackoverflow.com/questions/16333754/how-to-customize-listview-using-baseadapterms to the action bar if it is present.
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
	

}
