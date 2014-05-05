package com.sinf1225.whatdoyouwanttowatch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		// essential security preliminary
		Application.checkLogin(this);
	}

	
	//menu items and handling
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		}
		return true;
	}
	
	private void openSearch(){
		onSearchRequested();
	}
	private void openSettings(){
		// TODO
	}
	
	
	// menu buttons
	public void onClickSurpriseMe(View v){
		Database db = new Database(this);
		Application.openMovie(this, db.getRandomMovie().getID());
	}
	public void onClickSearchMovie(View v){
		// it would be better to link better search here TODO!
		openSearch();
	}
	public void onClickDiscoverMovie(View v){
		Intent intent = new Intent(this, DiscoverMoviesActivity.class);
		startActivity(intent);
	}
	
}
