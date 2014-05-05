package com.sinf1225.whatdoyouwanttowatch;

import android.content.Context;

/**
 * Class holding data for a user
 */
public class User {
	private String name;
	private int age;
	public User(Context context, String username){
		name = username;
		Database db = new Database(context);
		age = db.getUserAge(username);
	}
	
	public int getAge(){
		return age;
	}
	public void setAge(int newAge){
		age = newAge;
		Database db = new Database(null);
		db.setUserAge(name, age);
	}
	public String getName(){
		return name;
	}
}
