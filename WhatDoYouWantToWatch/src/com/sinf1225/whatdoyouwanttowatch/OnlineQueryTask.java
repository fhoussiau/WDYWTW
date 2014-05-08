package com.sinf1225.whatdoyouwanttowatch;

import java.util.ArrayList;

import android.os.AsyncTask;

public class OnlineQueryTask extends AsyncTask<String, Void, ArrayList<Movie>> {
	
	protected void onPreExecute(){
		// does nothing
	}

    protected void onPostExecute() {
        // does nothing
    }
    
    protected void onPostExecute(ArrayList<Movie> result){
    	// does nothing
    }

	@Override
	protected ArrayList<Movie> doInBackground(String... query) {
		for(String q: query){
			return InternetManager.GetMoviesOnline(q, InternetManager.lastContext); //TODO: not null
		}
		return null;
	}
}
