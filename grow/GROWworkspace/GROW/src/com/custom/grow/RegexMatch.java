package com.custom.grow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatch {
	
	public static boolean testDate(String date){
		String pattern = "^(0?[1-9]|1[012])[-/.](0?[1-9]|[12][0-9]|3[01])[-/.](19|20)?\\d\\d$";
		
		Pattern r = Pattern.compile(pattern);
		
		Matcher m = r.matcher(date);
		if(m.matches()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean testTime(String time){
		String pattern = "(1[012]|0?[1-9]):[0-5][0-9](am|pm)";
		
		Pattern r = Pattern.compile(pattern);
		
		Matcher m = r.matcher(time);
		if(m.matches()){
			return true;
		}
		else{
			return false;
		}
	}

	public static boolean testNumVolunteers(String num) {
		String pattern = "[0-9]+";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(num);
		if (m.matches()) {
			return true;
		}
		else {
			return false;
		}
	}

}
