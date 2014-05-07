package com.sinf1225.whatdoyouwanttowatch;

import java.util.ArrayList;
import java.util.Arrays;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Build;

public class WatchListActivity extends ActionBarActivity {

	private Movie[] WatchList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_list);

		Database db = new Database (this);
		WatchList = db.getWatchList();
		
		ListView lv_watchlist = (ListView) findViewById(R.id.container_watchlist);
		
		MovieAdapter ma_wl = new MovieAdapter(this, 
				new ArrayList<Movie>(Arrays.asList( WatchList ) ) );
		lv_watchlist.setAdapter(ma_wl);
		// ajout d'un listener a la liste
		lv_watchlist.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> Parent, View view, int pos, long id){
				// affiche le film
				Application.openMovie(view.getContext(), WatchList[pos].getID());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity( new Intent(this, SettingsActivity.class) );
			return true;
		}
		if(id == R.id.action_search){
			onSearchRequested();
			return true;
		}
		if(id == R.id.action_back){
			Application.goHome(this);
			return true;
		}
		if(id == R.id.action_logout){
			Application.fullLogoutDialog(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
