package com.sinf1225.whatdoyouwanttowatch;

import java.util.ArrayList;
import android.provider.SearchRecentSuggestions;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.app.ListActivity;
import android.app.SearchManager;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.MenuItem;
import android.view.View;

public class SearchActivity extends ListActivity {

	// liste des films affiches dans la liste
	private ArrayList<Movie> listM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
					MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			Database db = new Database( this );
			// Recuperation de la liste des films
			listM = db.Search_Movie(query);

			if(listM.size()> 0){
				// Creation et initialisation de l'Adapter pour les personnes
				MovieAdapter adapter = new MovieAdapter(this, listM);

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
			}
			else{
				// No result found...
				LinearLayout errorDisp = (LinearLayout) findViewById(R.id.related_container);
				errorDisp.setVisibility(LinearLayout.VISIBLE);
			}
		}

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

}

