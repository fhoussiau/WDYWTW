package com.sinf1225.whatdoyouwanttowatch;

import java.util.ArrayList;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.ListActivity;
import android.app.SearchManager;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;

import java.util.Collections;

public class SearchActivity extends ListActivity {

	// liste des films affiches dans la liste
	private ArrayList<Movie> listM = new ArrayList<Movie>();
	private MovieAdapter adapter;
	
	// la requete de recherche sur Internet
	AsyncTask<InternetTaskArgument, Movie, ArrayList<Movie>> taskQuery;
	String query;
	
	// mode d'affichage des films (ordre d'affichage)
	private int mode = 0; // 0:name, 1:year, 2:director
	private static final String[] buttonsName = {"name", "year", "director", "interest"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
					MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);

			// Creation et initialisation de l'Adapter pour les personnes
			adapter = new MovieAdapter(this, listM);

			//Recuperation du composant ListView
			ListView list = (ListView)findViewById(android.R.id.list);

			//Initialisation de la liste avec les donnees
			list.setAdapter(adapter);

			// ajout d'un listener a la liste
			list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick( AdapterView<?> Parent, View view, int pos, long id){
					// affiche le film
					Application.openMovie(view.getContext(), listM.get(pos).getID());
				}
			});
	
			setButtonText();
			
			// voir si l'utilisateur a autorise la recherche en ligne
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			boolean okSearchInternet = pref.getBoolean("pref_wifi", true);
			
			if(okSearchInternet){
				fillDisplayInternet(query);
			}
			else{
				fillDisplayDatabase(query);
			}

		}

	}
	
	private void fillDisplayInternet(String query){
		taskQuery = new InternetManager().execute( new InternetTaskArgument(this, query) );
	}
	
	
	public void addMovie(Movie mov){
		listM.add(mov);
		updateDisplay();
	}
	
	public void updateDisplay(){
		// pour le tri, il faut s'assurer que tous les films ont ete rempli
		if(listM==null || listM.isEmpty()){
			return;
		}
		listM.removeAll(Collections.singleton(null));
		Database db = new Database(this);
		for(Movie movie: listM){
			movie.getQuickData(db);
		}
		sortEntries();
	}
	
	private void fillDisplayDatabase(String query){
		Database db = new Database( this );
		// recuperer dans les preferences le nombre de films a chercher
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		int toSearch = Integer.parseInt(pref.getString("pref_nsearch", "10"));
		listM.addAll(db.Search_Movie(query, toSearch));
		if(listM == null || listM.isEmpty()){
			LinearLayout errorDisp = (LinearLayout) findViewById(R.id.related_container);
			errorDisp.setVisibility(LinearLayout.VISIBLE);
		}
		else{
			updateDisplay();
		}
	}
	
	public void notifyLoading(){
		notifyText("Loading...");
	}
	
	public void notifyDone(){
		notifyText("No result found...");
		LinearLayout errorDisp = (LinearLayout) findViewById(R.id.related_container);
		errorDisp.setVisibility(LinearLayout.GONE);
		// checker la valeur de retour
		try{
			ArrayList<Movie> results = taskQuery.get();
			if(results == null || results.isEmpty()){
				fillDisplayDatabase( query );
			}
		}
		catch(Exception e){
			fillDisplayDatabase( query );
		}
		
	}
	
	private void notifyText(String text){
		LinearLayout errorDisp = (LinearLayout) findViewById(R.id.related_container);
		errorDisp.setVisibility(LinearLayout.VISIBLE);
		TextView notifier = (TextView) findViewById(R.id.text_notifier);
		notifier.setText(text);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear_recent_suggestions:
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
					MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
			suggestions.clearHistory();
			return true;
		case R.id.action_back:
			Application.goHome(this);
			return true;
		case R.id.action_settings:
			startActivity( new Intent(this, SettingsActivity.class) );
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search_widget).getActionView();

		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(true); 
		searchView.setQueryRefinementEnabled(true);

		return true;
	}
	
	/**
	 * Update the text on the button so that it matches the current search mode
	 */
	private void setButtonText(){
		Button button = (Button) findViewById(R.id.button_sort_search);
		button.setText( "Sort by: "+ buttonsName[ mode ] );
	}
	
	/**
	 * Sort the entries in the movies' list
	 */
	private void sortEntries(){
		if(listM==null || listM.isEmpty()){
			return;
		}
		switch(mode){
		case 0: // name
			Collections.sort( listM, Movie.MovieTitleComparator );
			break;
		case 1: // year
			Collections.sort( listM );
			break;
		case 2: // director
			Collections.sort( listM, Movie.MovieDirectorComparator );
			break;
		case 3: // interest
			Collections.sort( listM, Movie.MovieInterestComparator );
			break;
		}
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * Called when the button is clicked: change mode (and do all changes)
	 * @param v
	 */
	public void onModeButtonClick(View v){
		mode = (mode + 1) % buttonsName.length;
		sortEntries();
		setButtonText();
	}

}

