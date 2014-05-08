/**
 * Besoin de android.permission.INTERNET et android.permission.ACCESS_NETWORK_STATE
 * Je les ai rajouté dans le manifest
 */
package com.sinf1225.whatdoyouwanttowatch;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class InternetManager {

	//CheckNet seems to be ok
	boolean CheckNet(Context context){
		ConnectivityManager cm = (ConnectivityManager)
	    		context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	
	/*
	 * Shcéma Fonctionnel:
	 * 
	 * ArrayList<Movie> GetQuery(String name, Context context)
	 * 	CheckNet();
	 * 	Request url();
	 * 	Parse Json();
	 * 	Make ArrayList();
	 * 	for each imdbID found
	 * 		Add FillMovie(imdbID) to ArrayList;
	 * 	return ArrayList;
	 * end of GetQuery
	 */
	
	ArrayList<Movie> GetQuery(String name, Context context){
		if(CheckNet(context))
		{
			ArrayList<Movie> movieslist = new ArrayList<Movie>();
			//New Version importated from http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
			DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
			HttpPost httppost = new HttpPost("http://www.omdbapi.com/?s="+name+"");
			httppost.setHeader("Content-type", "application/json");
			InputStream inputStream = null;
			String result = null;
			
			try {
				HttpResponse response = httpclient.execute(httppost);           
			    HttpEntity entity = response.getEntity();

			    inputStream = entity.getContent();
			    // json is UTF-8 by default
			    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			    StringBuilder sb = new StringBuilder();

			    String line = null;
			    while ((line = reader.readLine()) != null)
			    {
			        sb.append(line + "\n");
			    }
			    result = sb.toString();
			    JSONObject jObject = new JSONObject(result);
			    //TODO Check for Response here to avoid exception further?
			    	JSONArray jArray = jObject.getJSONArray("Search");
			    	for (int i=0; i < jArray.length(); i++)
			    	{
			    		try {
			    			JSONObject oneObject = jArray.getJSONObject(i);
			    			// Pulling items from the array
			    			if(oneObject.getString("Type").equals("movie"))
			    				movieslist.add(FillMovie(oneObject.getString("imdbID"), context));
			    		} catch (JSONException e) {
			    			//TODO Handle exception: what to do if no movie was found?
			    			e.printStackTrace();
			    		}
			    	}
			    
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
			    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
			}
			
			return movieslist;
		}
		else
			return null;
	}
	

	/*Schéma Fonctionnel:
	 * 
	 * Movie FillMovie(String imdbID, Context context)
	 * 	New Movie();
	 * 	Request url();
	 * 	Parse Json();
	 * 	FillDatabase (! Instancier la Database ?)
	 * 				 (! New Database ?)
	 * 				 (! Database.TableMovies ?)
	 * 	application.getMovie(imdbID);
	 * 	movie.fillData(context);
	 * 	return movie;
	 * end of FillMovie
	 */
	
	Movie FillMovie (String imdbID, Context context){
		Movie movie = new Movie(imdbID);
		DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpPost httppost = new HttpPost("http://www.omdbapi.com/?i="+imdbID+"");
		httppost.setHeader("Content-type", "application/json");
		InputStream inputStream = null;
		String result = null;
		
		try {
			HttpResponse response = httpclient.execute(httppost);           
		    HttpEntity entity = response.getEntity();

		    inputStream = entity.getContent();
		    // json is UTF-8 by default
		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		    while ((line = reader.readLine()) != null)
		    {
		        sb.append(line + "\n");
		    }
		    result = sb.toString();
		    
		    JSONObject jObject = new JSONObject(result);
		    if(jObject.getString("Response").equals("True")){
		    	
		    	String Title = jObject.getString("Title");
		    	String Year = jObject.getString("Year");
		    	String Rated = jObject.getString("Rated");
		    	String Released = jObject.getString("Released");
		    	String Runtime = jObject.getString("Runtime");
		    	String Genre = jObject.getString("Genre");
		    	String Director = jObject.getString("Director");
		    	String Writer = jObject.getString("Writer");
		    	String Actors = jObject.getString("Actors");
		    	String Plot = jObject.getString("Plot");
		    	String Poster = jObject.getString("Poster");
		    	String Metascore = jObject.getString("Metascore");
		    	String imdbRating = jObject.getString("imdbRating");
		    	String imdbVotes = jObject.getString("imdbVotes");
		    
		    	//TODO Gestion zarbi du Movie et de la Database. How to?
		    	Database db = new Database(context);
		    	db.fillMovie(movie);
		    	Application.getMovie(imdbID);
		    	movie.fillData(db);
		    	return movie;
		    }
		    else
		    	return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
