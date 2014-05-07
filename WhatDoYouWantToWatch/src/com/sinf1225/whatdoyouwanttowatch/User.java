package com.sinf1225.whatdoyouwanttowatch;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Class holding data for a user
 */
public class User {
	private String name;
	private Date birthday;
	private int age;
	public User(Context context, String username){
		name = username;
		Database db = new Database(context);
		birthday = db.getUserBirthday(username);
		computeAge();
	}
	
	public int getAge(){
		return age;
	}
	public void setBirthday(Date birthday, Context context){
		this.birthday = birthday;
		Database db = new Database(context);
		db.setUserAge(name, birthday);
		computeAge();
	}
	public String getName(){
		return name;
	}
	
	private void computeAge(){
		Calendar today = new GregorianCalendar();
		Calendar dob= new GregorianCalendar();
		if(birthday != null){
			dob.setTime(birthday);
			age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR); 
			if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
				age--;  
			} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
					&& today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
				age--;  
			}
		}
		else{
			age = -1;
		}
	}
}
