package com.sinf1225.whatdoyouwanttowatch;


import java.util.Comparator;

import android.content.Context;
import android.util.Log;

public class Movie implements Comparable<Movie> {

	// data
	private String imdbID;
	public String title;
	public String director;
	public int year;
	public int duration; // minutes
	public float rating; // 0 <= rating <= 10
	public String description;
	public int ageRestrictions;

	public boolean isComingSoon;
	public boolean isMostWatched;
	public boolean isPlayingNow;

	public Movie[] RelatedMovies;
	public Award[] Awards;
	public String[][] Cast; // Cast[ numero_film ][ i ] gives the actor if i==0, and the character if i==1

	public Genre MovieGenre[];

	public Interest InterestForUser;

	private MovieQuickData quickData;
	private boolean filled;


	// constructor: takes as argument the ID of the movie
	public Movie( String imdbID ) {
		this.imdbID = imdbID;
	}

	// getter for the IMDB ID: no setter! (of course not!)
	public String getID(){
		return imdbID;
	}

	// fill all the movie's data
	// This gets "a lot of" information from the database
	// so it is to be used only if the movie needs filling
	// the context argument is used to create a database
	// In fact, it is a good practice to use movie.fillData(...) every time you
	//  access a movie in the program
	public void fillData( Context context ){
		if(filled){
			return;
		}
		Database db = new Database( context );
		this.fillData( db );
		db.close();
	}

	// fill all the movie's data using a pre-instantiated database
	public void fillData( Database db ){
		if(filled){
			return;
		}

		// this is put here in order to avoid infinite loops during
		// the fillMovie (calls to Application.getMovie)
		filled = true;

		db.fillMovie(this); // what did you expect?

		// also, add quick data...
		if(quickData == null){
			quickData = new MovieQuickData(title, director, year, InterestForUser);
		}
	}

	// get some quick data from the Movie, for display for example
	public MovieQuickData getQuickData( Context context ){
		if(this.quickData != null){
			return this.quickData;
		}
		Database db = new Database( context );
		MovieQuickData res = this.getQuickData(db);
		db.close();
		// return the quick data
		return res;
	}

	// get some quick data from the movie, using an existing database
	public MovieQuickData getQuickData( Database db ){
		if(this.quickData != null){
			return this.quickData;
		}
		quickData = db.getMovieQuickData(this);
		// fill the movie's data from the quick data
		this.title = quickData.title;
		this.director = quickData.director;
		this.year = quickData.year;
		this.InterestForUser = quickData.interest;
		return quickData;
	}

	// comparison method
	public int compareTo(Movie movie){
		return this.year - movie.year;
	}

	// two comparators
	
	// sort by title
	public static Comparator<Movie> MovieTitleComparator  = new Comparator<Movie>() {
		public int compare(Movie movie1, Movie movie2) {
			return movie1.title.compareTo(movie2.title);
		}
	};


	//sort by director
	public static Comparator<Movie> MovieDirectorComparator = new Comparator<Movie>() {
		public int compare(Movie movie1, Movie movie2) {
			return movie1.director.compareTo(movie2.director);
		}
	};
	
	// sort by interest
	public static Comparator<Movie> MovieInterestComparator = new Comparator<Movie>() {
		public int compare(Movie movie1, Movie movie2) {
			return movie1.InterestForUser.ordinal() - movie2.InterestForUser.ordinal();
		}
	};


}
