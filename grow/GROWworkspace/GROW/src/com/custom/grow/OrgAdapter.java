package com.custom.grow;

import java.util.List;

import com.example.grow.R;
import com.parse.ParseUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrgAdapter extends ArrayAdapter<ParseUser> {

	public OrgAdapter(Context context, List<ParseUser> objects) {
		super(context, R.layout.super_user_list_item, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ParseUser org = getItem(position);
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.super_user_list_item, parent, false);
		}
		
		TextView orgName = (TextView) convertView.findViewById(R.id.SupOrgListName);
		ImageView orgVerified = (ImageView) convertView.findViewById(R.id.SupOrgListVerified);
		
		orgName.setText(org.getUsername());
		if(org.getBoolean("superverified")==true){
			orgVerified.setImageResource(R.drawable.ic_action_navigation_check);
		}
		else{
			orgVerified.setImageResource(R.drawable.ic_action_navigation_close);
		}
		
		return convertView;
	
	}
	
}
