package com.sinf1225.whatdoyouwanttowatch;

import java.util.Hashtable;

import android.support.v7.app.ActionBarActivity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;


public class DisplayMovieActivity extends ActionBarActivity {

	public static final String MOVIE_MESSAGE = "com.sinf1225.wdywtw.movie.message";
	public static final int MAX_CAST = 5;
	public static final int MAX_AWARDS = 5;
	public static final int MAX_GENRE = 5;
	
	// movie to display
	private Movie movie;
	private Hashtable<View, Movie> moviesShowed;
	private DisplayMovieActivity current; // for global fields
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_movie);
		// essentiel for user secutiry: garantee a user is logged in
		Application.checkLogin(this);
		// populate
		current = this;
		// this is passed a movie id through its intent
		Intent intent = getIntent(); 
		if(intent != null){
			String imdbID = intent.getStringExtra( MOVIE_MESSAGE );
			if(imdbID != null){
				this.movie = Application.getMovie(imdbID);
				try{
					movie.fillData(this);
					this.displaySuccess();
					return;
				}
				catch(Exception error){
				}
			}
		}
		// if this point is reached, either no movie ID was sent, or an invalid one
		this.movie = null;
		this.displayError();
	}
	
	private void displayError(){
		this.setTitle("No movie found");
		LinearLayout successframe = (LinearLayout) findViewById(R.id.success_frame);
		successframe.setVisibility(LinearLayout.GONE);
		LinearLayout failureframe = (LinearLayout) findViewById(R.id.failure_frame);
		failureframe.setVisibility(LinearLayout.VISIBLE);
	}
	
	private void displaySuccess(){
		this.setTitle(movie.title);
		// populate the activity with content
		// first, select widgets to be filled very simply
		TextView director_tv = (TextView) findViewById(R.id.textview_director);
		TextView year_tv = (TextView) findViewById(R.id.textview_year);
		TextView duration_tv = (TextView) findViewById(R.id.textview_duration);
		RatingBar rating_rb = (RatingBar) findViewById(R.id.ratingbar_movie);
		TextView description_tv = (TextView) findViewById(R.id.textview_description);
		director_tv.setText( movie.director );
		year_tv.setText( Integer.toString(movie.year));
		int hour = movie.duration / 60,
			minutes = movie.duration % 60;
		duration_tv.setText( Integer.toString(hour)+"h"+Integer.toString(minutes) );
		description_tv.setText( movie.description );
		rating_rb.setRating( movie.rating/2 );
		// populate and color interest
		TextView interest_tv = (TextView) findViewById(R.id.textview_interest);
		interest_tv.setText(movie.InterestForUser.toString());
		interest_tv.setTextColor(movie.InterestForUser.toColor());
		// then, populate awards and cast from lists
		if(movie.Awards != null){
			// programmatically add widgets
			LinearLayout awardsLayout = (LinearLayout) findViewById(R.id.awards_container);
			for(int i=0; i<MAX_AWARDS && i<movie.Awards.length; i++){
				Award award = movie.Awards[i];
				addEntry(awardsLayout, award.name+", "+Integer.toString(award.year));
			}
		}
		if(movie.Cast != null){
			LinearLayout castLayout = (LinearLayout) findViewById(R.id.cast_container);
			for(int i=0; i<MAX_CAST && i< movie.Cast.length; i++){
				String[] castmember = movie.Cast[i];
				addEntry(castLayout, castmember[1]+" ("+castmember[0]+")");
			}
		}
		// now, populate entries for genre and related movies
		if(movie.MovieGenre != null){
			LinearLayout genreLayout = (LinearLayout) findViewById(R.id.genre_container);
			for(int i=0; i<MAX_GENRE && i<movie.MovieGenre.length; i++){
				addEntry(genreLayout, movie.MovieGenre[i].toString());
			}
		}
		// finally, populate with movies (a bit different)
		if(movie.RelatedMovies != null){
			LinearLayout relatedLayout = (LinearLayout) findViewById(R.id.related_container);
			moviesShowed = new Hashtable<View, Movie>();
			for(Movie related: movie.RelatedMovies){
				MovieQuickData data = related.getQuickData(this);
				Button link = new Button(this);
				link.setText(data.title+" ("+data.year+")");
				moviesShowed.put(link, related);
				link.setLayoutParams( new LayoutParams(LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT) );
				link.setPadding(5, 1, 5, 1);
				link.setBackgroundColor(0xFFFFFFFF);
				relatedLayout.addView(link);
				// add an action (browse the movie when clicked
				link.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						Movie toBrowse = moviesShowed.get(v);
						if(v==null){
							return; // error case, ignored
						}
						Application.openMovie(current, toBrowse.getID());
					}
				});
			}
		}		
	} // end of DisplaySuccess
	
	
	// add an entry to a linear layout
	private void addEntry(LinearLayout target, String text){
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setPadding(5, 0, 0, 0);
		tv.setLayoutParams( new LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT) );
		target.addView(tv);
	}
	
	// handler for a click on the interest entry
	public void onClickInterest(View v){
		DialogFragment dialog = new SetInterestDialog();
		dialog.show(getFragmentManager(), "interest");
	}
	
	// set the interest of the current movie (part of API)
	public void setMovieInterest(Interest i){
		this.setMovieInterest( i.ordinal() );
	}
	
	// set the interest of the current movie (the interest is the ith value)
	public void setMovieInterest(int i){
		Database db = new Database( this );
		db.setMovieInterest(this.movie, i);
		// update movie object
		Interest it = Interest.values()[i];
		this.movie.InterestForUser = it;
		// update display
		TextView interest_tv = (TextView) findViewById(R.id.textview_interest);
		interest_tv.setText(it.toString());
		interest_tv.setTextColor(it.toColor());
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_movie, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if(id == R.id.action_interest){
			this.onClickInterest(null); // simulate click on the interest
			return true;
		}
		if(id == R.id.action_search){
			onSearchRequested();
		}
		if(id == R.id.action_back){
			Application.goHome(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
