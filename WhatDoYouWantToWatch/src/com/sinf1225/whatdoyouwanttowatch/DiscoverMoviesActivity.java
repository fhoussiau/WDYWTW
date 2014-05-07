package com.sinf1225.whatdoyouwanttowatch;

import java.util.ArrayList;
import java.util.Arrays;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DiscoverMoviesActivity extends ActionBarActivity {

	private static final int N_MOVIES_PER_CATEGORY = 5;
	private Movie[] MostWatched;
	private Movie[] PlayingNow;
	private Movie[] ComingSoon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover_movies);
		
		// populate the view with elements from the database
		Database db = new Database( this );
		MostWatched = db.getMostWatched(N_MOVIES_PER_CATEGORY);
		PlayingNow  = db.getPlayingNow(N_MOVIES_PER_CATEGORY);
		ComingSoon  = db.getComingSoon(N_MOVIES_PER_CATEGORY);
		
		ListView lv_mostwatched = (ListView) findViewById(R.id.container_mostwatched);
		
		MovieAdapter mv_mw = new MovieAdapter(this, 
				new ArrayList<Movie>(Arrays.asList( MostWatched ) ) );
		lv_mostwatched.setAdapter(mv_mw);
		// ajout d'un listener a la liste
		lv_mostwatched.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> Parent, View view, int pos, long id){
				// affiche le film
				Application.openMovie(view.getContext(), MostWatched[pos].getID());
			}
		});
		
		// same for pn
		ListView lv_playingnow = (ListView) findViewById(R.id.container_playingnow);
		MovieAdapter mv_pn = new MovieAdapter(this, 
				new ArrayList<Movie>(Arrays.asList( PlayingNow ) ) );
		lv_playingnow.setAdapter(mv_pn);
		// ajout d'un listener a la liste
		lv_playingnow.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> Parent, View view, int pos, long id){
				// affiche le film
				Application.openMovie(view.getContext(), PlayingNow[pos].getID());
			}
		});
		
		
		// same for cs
		ListView lv_comingsoon = (ListView) findViewById(R.id.container_comingsoon);
		MovieAdapter mv_cs = new MovieAdapter(this, 
				new ArrayList<Movie>(Arrays.asList( ComingSoon ) ) );
		lv_comingsoon.setAdapter(mv_cs);
		// ajout d'un listener a la liste
		lv_comingsoon.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> Parent, View view, int pos, long id){
				// affiche le film
				Application.openMovie(view.getContext(), ComingSoon[pos].getID());
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
