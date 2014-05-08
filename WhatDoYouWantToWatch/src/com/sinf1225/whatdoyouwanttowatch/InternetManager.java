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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;



public class InternetManager extends AsyncTask<InternetTaskArgument, Movie, ArrayList<Movie>> {

	// static part
	public static String DATABASE_URL = "http://www.omdbapi.com/?s=";
	public static String DATABASE_ID_URL = "http://www.omdbapi.com/?i=";

	public static boolean CheckNet(Context context){
		ConnectivityManager cm = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	
	
	
	// object part
	SearchActivity context;
	
	protected void onPreExecute(){
		
	}

    protected void onProgressUpdate(Movie... movies){
        context.addMovie(movies[0]);
    }
    
    protected void onPostExecute(ArrayList<Movie> result){
    	context.notifyDone();
    }




	/**
	 * Get movies online, from the OMDBapi website, online. This downloads movies in the Database
	 * This cannot be used on main thread, see getMoviesOnlineAsync for this
	 * @param name: the name to look for
	 * @param context: the context initiating the request
	 * @return a list of movies, possibly empty, filled in the Database. Null will be returned if an exception occurs.
	 */
	
    @Override
	protected ArrayList<Movie> doInBackground(InternetTaskArgument...  args) {
		// first, check if there is wifi
    	context = args[0].context;
    	String name = args[0].query;
    	context.notifyLoading();
		if(CheckNet(context))
		{
			// if yes, build return Value
			ArrayList<Movie> movieslist = new ArrayList<Movie>();
			// Query on the URL
			String Query = DATABASE_URL+name+"", FullUrl = "";
			for(int i=0; i<Query.length(); i++){
				if(Query.charAt(i) == ' '){
					FullUrl += "%20";
				}
				else{
					FullUrl += Query.charAt(i);
				}
			}
			DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
			HttpGet httppost = new HttpGet(FullUrl);
			httppost.setHeader("Content-type", "application/json");
			InputStream inputStream = null;
			String result = null;
			// Try and parse the results
			try {
				HttpResponse response = httpclient.execute(httppost);           
				HttpEntity entity = response.getEntity();

				inputStream = entity.getContent();
				// json is UTF-8 by default
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				// Read content from request
				String line = null;
				while ((line = reader.readLine()) != null)
				{
					sb.append(line + "\n");
				}
				result = sb.toString();
				JSONObject jObject = new JSONObject(result);

				
				// Parse the Search Array
				JSONArray jArray;
				jArray = jObject.getJSONArray("Search");
				// if no search is found, an error will be caught by the uppermost catch clause (and null is returned)
				
				// let's get the maximum number of movies to treat in the query
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
				int toSearch = Integer.parseInt(pref.getString("pref_nsearch", "10"));
				
				// At this point, we have the movies' array, let's read its content
				int movieCount=0;
				for (int i=0; i < jArray.length() && movieCount < toSearch; i++)
				{
					try {
						JSONObject oneObject = jArray.getJSONObject(i);
						// Proceed the item, using the 
						if(oneObject.getString("Type").equals("movie")){
							movieCount++;
							Movie mov = FillMovie(oneObject.getString("imdbID"), context);
							if(mov != null){
								movieslist.add(mov);
								this.publishProgress( new Movie[] {mov} );
							}
						}
					} catch (JSONException e) {
						//TODO Handle exception: what to do if no movie was found?
						// note that the entry is ignored, simply
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				// if an exception is caught here, then it was not handled and the program meant for us to intercept it
				// the behaviour is to return null (empty list ==> error )
				e.printStackTrace();
				return null;
			}
			finally {
				try{
					// try and close the input stream
					if(inputStream != null)
						inputStream.close();
					}catch(Exception squish){}
			}

			return movieslist;
		}
		else{
			return null;
		}
	}

	
	

	
	// not changed
	/**
	 * Fill a movie from the OMDBapi into the Database
	 * @param imdbID: the ID of the movie to fill (extracted from the database)
	 * @param context: the context initiating the request
	 * @return a Movie object from the ID, or null if an error occurs
	 */
	public static Movie FillMovie (String imdbID, Context context){
		Movie movie = Application.getMovie(imdbID);
		// first, check if the movie is already in the database
		Database db = new Database(context);
		if(db.movieAlreadyExists(imdbID)){
			return movie;
		}
		
		// else, fetch data from the omdbAPI website
		DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
		String Query = DATABASE_ID_URL+imdbID, FullUrl = "";
		for(int i=0; i<Query.length(); i++){
			if(Query.charAt(i) == ' '){
				FullUrl += "%20";
			}
			else{
				FullUrl += Query.charAt(i);
			}
		}
		HttpGet httppost = new HttpGet(FullUrl);
		httppost.setHeader("Content-type", "application/json");
		InputStream inputStream = null;
		String result = null;

		// the query code is very similar to the one from above
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
				//String Released = jObject.getString("Released");
				String Runtime = jObject.getString("Runtime");
				String GenreS = jObject.getString("Genre");
				String Director = jObject.getString("Director");
				// String Writer = jObject.getString("Writer");
				String Actors = jObject.getString("Actors");
				String Plot = jObject.getString("Plot");
				String Awards = jObject.getString("Awards");
				//String Poster = jObject.getString("Poster");
				//String Metascore = jObject.getString("Metascore");
				String imdbRating = jObject.getString("imdbRating");
				//String imdbVotes = jObject.getString("imdbVotes");

				// fill data in the database
				SQLiteDatabase wdb = db.getWritableDatabase();
				wdb.execSQL("INSERT INTO "+Database.TABLE_MOVIES+" VALUES ("+
						"\""+imdbID+"\", "+
						"\""+Title +"\", "+
						"\""+Director+"\", "+
						Runtime.substring(0, Runtime.length()-4)+", "+ // skip " min"
						Year.substring(Year.length()-4)+", "+
						imdbRating+", "+
						"\""+Plot+"\", "+
						fromRatedToAgeRestr(Rated) + ");"
						);
				
				String[] genres = GenreS.split(", ");
				for(String genre: genres){
					Genre value = fromTextToGenre( genre );
					if(value != null){
						wdb.execSQL("INSERT INTO "+Database.TABLE_GENRE+" VALUES("+
								"\""+imdbID+"\", "+
								""+Integer.toString(value.ordinal())+");");
					}
				}
				
				String[] cast   = Actors.split(", ");
				for(String actor: cast){
					wdb.execSQL("INSERT INTO "+Database.TABLE_CAST+" VALUES("+
								"\""+imdbID+"\", "+
								"\""+actor+"\", \"N/A\");");
				}
				wdb.execSQL("INSERT INTO "+Database.TABLE_AWARDS+" VALUES ("+
								"\""+imdbID+"\", \""+Awards+"\", "+
								Year.substring(Year.length()-4)+");");
				
				// finally
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
	
	
	// utilities
	/**
	 * Transform a rated symbol into an age restriction (an integer into a string)
	 * @param Rated the String
	 * @return a String representing an integer (age)
	 */
	private static String fromRatedToAgeRestr( String rating ){
		int ageRestrictions; 
        if(rating.equals("N/A") || rating.equals("G")) 
            ageRestrictions = 0; 
        else if(rating.equals("PG")) 
            ageRestrictions = 10; 
        else if(rating.equals("PG-13")) 
            ageRestrictions = 13; 
        else if(rating.equals("Approved")) 
            ageRestrictions = 15; 
        else if(rating.equals("R")|| rating.equals("M")) 
            ageRestrictions = 16; 
        else if(rating.equals("NC-17") || rating.equals("X")) 
            ageRestrictions = 18; 
        else
            ageRestrictions = 12; 
        return Integer.toString(ageRestrictions); 
	}
	
	private static Genre fromTextToGenre( String text ){
		try{
			if(text.equals("Sci-Fi")){
				return Genre.SCIENCEFICTION;
			}
			return Genre.valueOf(Genre.class, text.toUpperCase() );
		}
		catch(Exception e){
			return null;
		}
	}

}
