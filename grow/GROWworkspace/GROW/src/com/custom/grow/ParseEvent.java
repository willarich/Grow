package com.custom.grow;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Event")
public class ParseEvent extends ParseObject {

	public String getTitle() {
		return getString("title");
	}
	
	public void setTitle(String title) {
		put("title", title);
	}
	
	public String getDate() {
		return getString("date");
	}
	
	public void setDate(String date){
		put("date", date);
	}
	
	public String getTime() {
		return getString("time");
	}
	
	public void setTime(String time){
		put("time", time);
	}
	
	public String getCreatedBy() {
		return getString("createdBy");
	}
	
	public String getNumVolunteers() {
		return getString("numVolunteers");
	}
	
	public void setNumVolunteers(String volunteers){
		put("numVolunteers", volunteers);
	}
	
	public String getDescription() {
		return getString("EventDescription");
	}
	
	public void setDescription(String description){
		put("EventDescription", description);
	}
}
	
