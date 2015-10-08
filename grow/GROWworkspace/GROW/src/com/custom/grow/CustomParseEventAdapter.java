package com.custom.grow;

import java.util.List;

import com.example.grow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomParseEventAdapter extends BaseAdapter {

	Context context;
	private List<ParseEvent> parse = null;
	LayoutInflater inflater;
	
	public CustomParseEventAdapter(Context context, List<ParseEvent> parse) {
		super();
		this.context = context;
		this.parse = parse;
		inflater = LayoutInflater.from(context);
	}
	
	public class ViewHolder {
		TextView title;
		TextView time;
		TextView date;
		TextView numVolunteers;
		TextView description;
	}
	
	@Override
	public int getCount() {
		if (parse != null) {
			return parse.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return parse.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		View view = convertView;
		if (view == null) {
			// Create a new view if one didn't already exist
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.event_listrow, null);
			//holder.title = (TextView) view.findViewById(R.id.eventtitle);
			//holder.date = (TextView) view.findViewById(R.id.eventdate);
			//holder.time = (TextView) view.findViewById(R.id.eventtime);
			//holder.numVolunteers = (TextView) view.findViewById(R.id.numpeople);
			//holder.description = (TextView) view.findViewById(R.id.eventdescription);
			view.setTag(holder);
		}
		else {
			holder = (ViewHolder) view.getTag();
		}
		
		if (parse != null) {
			// Get event title
			if (parse.get(position).getTitle() != null) {
				holder.title.setText(parse.get(position).getTitle());
			}
			else {
				holder.title.setText("No Title Found");
			}
			
			// Get event date
			if (parse.get(position).getDate() != null) {
				holder.date.setText(parse.get(position).getDate().toString());
			}
			else {
				holder.date.setText("No Date Found");
			}
			
			// Get event time
			if (parse.get(position).getTime() != null) {
				holder.time.setText(parse.get(position).getTime());
			}
			else {
				holder.time.setText("No Time Found");
			}
			
			// Get event number of volunteers needed
			if (parse.get(position).getNumVolunteers() != null) {
				holder.numVolunteers.setText(parse.get(position).getNumVolunteers());
			}
			else {
				holder.numVolunteers.setText("No Number of Volunteers Found");
			}
			
			// Get event description
			if (parse.get(position).getDescription() != null) {
				holder.description.setText(parse.get(position).getDescription());
			}
			else {
				holder.description.setText("No Description Found");
			}
		}
		
		return view;
	}

}
