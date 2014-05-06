package com.sinf1225.whatdoyouwanttowatch;


import java.util.Hashtable;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.app.DialogFragment;

/**
 * Application
 * Sort of super class containing global data and user interaction
 * */
public class Application {
	private static User currentUser;
	
	/**
	 * Login the user onto the application
	 * @param name: name of the user
	 * @param password: password of the user
	 * @return true if the name-password combination is correct
	 */
	public static boolean login(Context context, String name, String password){
		Database db = new Database(context);
		boolean canLogin = db.login(
				name,
				password
				);
		if(canLogin){
			currentUser = new User( context, name );
		}
		return canLogin;
	}
	
	/**
	 * Logout the user from the application.
	 * Always succeeds.
	 */
	public static void logout(){
		currentUser = null;
		clearMovies();  // empty the database
		UserSuggestionGuesser.wipeGuesser();
	}
	
	/**
	 * Checks if a user is currently logged in
	 * @return true if a user is logged in
	 */
	public static boolean isUserLoggedIn(){
		return currentUser != null;
	}
	
	/**
	 * Asserts a user is logged in: if no user is logged in, the user is sent to login menu
	 * This allows for more security in the user system, and should be called in every onCreate method
	 * @param context: the current page
	 */
	public static void checkLogin(Context context){
		if(!isUserLoggedIn()){
			Intent intent = new Intent(context, MainActivity.class);
			context.startActivity(intent);
		}
	}
	
	
	/**
	 * Returns the current user of the application
	 * @return the current user, or null if no current user
	 */
	public static User getUser(){
		return currentUser;
	}
	
	
	// dictionary containing the pairs ID-movie object
	private static Hashtable<String, Movie> encounteredMovies = new Hashtable<String, Movie>();
	
	/**
	 * Returns a movie from given imdbID
	 * This makes sure Movie objects already encountered (and filled) are not lost! 
	 * @param imdbID: a String, holding the unique imdb id of the movie
	 * @return a Movie (that may or may not be filled) object
	 */
	public static Movie getMovie( String imdbID ){
		Movie res = encounteredMovies.get(imdbID);
		if(res == null){
			// build a new movie object
			res = new Movie( imdbID );
			encounteredMovies.put(imdbID, res);
		}
		return res;
	}
	
	/**
	 * Navigate to the page detailing a movie
	 * @param origin: The context where the call originates from (the page to navigate from)
	 * @param imdbID: The movie to open
	 */
	public static void openMovie( Context origin, String imdbID ){
		Intent intent = new Intent(origin, DisplayMovieActivity.class);
		intent.putExtra(DisplayMovieActivity.MOVIE_MESSAGE, imdbID);
		origin.startActivity(intent);
	}
	
	/**
	 * Navigate to the home page of this application
	 * @param origin: the page to navigate from
	 */
	public static void goHome( Context origin ){
		Intent intent = new Intent(origin, MainMenuActivity.class);
		origin.startActivity(intent);
	}
	
	/**
	 * Fully log out of the application, and send the user back to the login page
	 * @param origin: the page to navigate from
	 */
	public static void fullLogout( Context origin ){
		logout();
		Intent intent = new Intent(origin, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		origin.startActivity(intent);
	}
	
	/**
	 * Fully log out of the application, by showing a dialog to the user (OK --> logout)
	 * @param origin: the context to show the dialog in and navigate from
	 */
	public static void fullLogoutDialog( Activity origin ){
		DialogFragment dialog = new AskLogoutDialog();
		dialog.show(origin.getFragmentManager(), "logout");
	}
	
	/** 
	 * free the memory taken from all movies (usually on logout)
	 */
	public static void clearMovies(){
		encounteredMovies.clear();
	}
	
}
