package com.sinf1225.whatdoyouwanttowatch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class NewUserActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void createUserClick(View view){
		TextView nameText = (TextView) findViewById(R.id.editText1);
		TextView pswdText = (TextView) findViewById(R.id.editText2);
		TextView  ageText = (TextView) findViewById(R.id.editText3);
		int age;
		try{
			age = Integer.parseInt(ageText.getText().toString());
		}
		catch(NumberFormatException e){
			errorText("Invalid age format");
			return;
		}
		String name = nameText.getText().toString();
		String pswd = pswdText.getText().toString();
		if(age<1 || age>150){
			errorText("Invalid age (too old or non-existent)");
			return;
		}
		if(nameText.equals("") || pswd.equals("")){
			errorText("Name or Password field empty");
			return;
		}
		// no error
		Database db = new Database(this);
		boolean success = db.newUser(name, pswd, age);
		if(!success){
			errorText("User already exists.");
		}
		else{
			// go back
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra(MainActivity.EXTRA_MESSAGE, name);
			startActivity(intent);
		}
		db.close();
	}
	
	private void errorText(String text){
		TextView redText = (TextView) findViewById(R.id.textViewRed);
		redText.setText(text);
		redText.setVisibility(TextView.VISIBLE);
	}

}
