package com.sinf1225.whatdoyouwanttowatch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "com.sinf1225.wdywtwt.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// see if no user was just created
		Intent intent = getIntent();
		String name; // default name to display
		if(intent!=null){
			name = intent.getStringExtra(EXTRA_MESSAGE);
			if(name!=null){
				// create a toast
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(context, "User "+name+" was created!", duration);
				toast.show();
			}
			else{
				// try and find the last user, insert his name
				Database db = new Database(this);
				name = db.getLastUserName();
			}
		}
		else{
			// try and find the last user, insert his name
			Database db = new Database(this);
			name = db.getLastUserName();
		}
		// display name if not null
		if(name!=null){
			// display the name of the user in the name field
			EditText nameField = (EditText) findViewById(R.id.editText1);
			nameField.setText(name);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	// called when the user clicks on the login button
	public void loginUserClick(View view){
		EditText nameField = (EditText) findViewById(R.id.editText1);
		EditText pswdField = (EditText) findViewById(R.id.editText2);
		String name = nameField.getText().toString();
		String password = pswdField.getText().toString();
		if(name.equals("") || password.equals("")){
			TextView warnuser = (TextView) findViewById(R.id.textView1);
			warnuser.setVisibility(TextView.VISIBLE);
			return;
		}
		// try to log in through the application
		boolean canLogin = Application.login(this, name, password);
		// respond accordingly
		if(canLogin){
			// hide the error message
			TextView warnuser = (TextView) findViewById(R.id.textView1);
			warnuser.setVisibility(TextView.GONE);
			// go to a new activity
			Intent intent = new Intent(this, MainMenuActivity.class);
			intent.putExtra(EXTRA_MESSAGE, name);
			startActivity(intent);
			return;
		}
		else{
			// warn user
			TextView warnuser = (TextView) findViewById(R.id.textView1);
			warnuser.setVisibility(TextView.VISIBLE);
		}
	}
	
	// called when the user clicks on the new user button
	public void newUserClick(View view){
		Intent intent = new Intent(this, NewUserActivity.class);
		startActivity(intent);
	}

}
