package com.example.grow;

import java.util.ArrayList;
import java.util.List;

import com.custom.grow.OrgAdapter;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class SuperUserMain extends Activity implements OnItemClickListener {
	
	List<ParseUser> organizations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_super_user_main);
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("organization", true);
		query.findInBackground(new FindCallback<ParseUser>() {
		    public void done(List<ParseUser> orgList, ParseException e) {
		        if (e == null) {
		            ArrayList<String>OrgNames = new ArrayList<String>();
		            for (ParseUser org : orgList) {
		            	OrgNames.add(org.getUsername());
		            }
		            organizations = orgList;
		            refreshDisplay(OrgNames);
		        } else {
		            
		        }
		    }
		});
	}
	/**
	 * Refresh the listView with list of unverified organizations
	 * @param orgList
	 */
	private void refreshDisplay(ArrayList<String> OrgNames){
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.super_user_list_item, OrgNames);
		OrgAdapter adapter = new OrgAdapter(getApplicationContext(), organizations);   
        ListView orgListView = (ListView) findViewById(R.id.list);
        orgListView.setOnItemClickListener(this);
    	orgListView.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
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
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		ParseUser org = organizations.get(position);
		Intent intent = new Intent(this, SuperOrgEditor.class);
		intent.putExtra("organization", org.getUsername());
		intent.putExtra("ID", org.getObjectId());
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed(){
		//DO NOTHING! =)
	}
	
	
	
}