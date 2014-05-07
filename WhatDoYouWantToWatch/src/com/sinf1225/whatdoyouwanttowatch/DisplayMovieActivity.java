package com.sinf1225.whatdoyouwanttowatch;

import java.util.Hashtable;

import android.support.v7.app.ActionBarActivity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
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
	public static final int MAX_CAST = 10;
	public static final int MAX_AWARDS = 10;
	public static final int MAX_GENRE = 10;
	
	// movie to display
	private Movie movie;
	private Hashtable<View, Movie> moviesShowed;
	private DisplayMovieActivity current; // for global fields
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_movie);
		// essential for user security: guarantee a user is logged in
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
		TextView rating_tv = (TextView) findViewById(R.id.rating_text);
		RatingBar rating_rb = (RatingBar) findViewById(R.id.ratingbar_movie);
		// set description
		TextView description_tv = (TextView) findViewById(R.id.textview_description);
		director_tv.setText( movie.director );
		// set year (beware, easter egg here)
		String yeartext = Integer.toString(movie.year);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getBoolean("pref_binary", false)){
			yeartext = Integer.toBinaryString(movie.year);
		}
		year_tv.setText( yeartext );
		// put duration in the correct format and check if there is a duration to display (movie exists)
		if(movie.duration < 0){
			duration_tv.setText("Coming soon...");
		}
		else{
			int hour = movie.duration / 60,
					minutes = movie.duration % 60;
			String minutestext = Integer.toString(minutes);
			if(minutes<10){
				minutestext = "0" + minutestext;
			}
			duration_tv.setText( Integer.toString(hour)+"h"+minutestext );
		}
		description_tv.setText( movie.description );
		if(movie.rating < 0){
			rating_rb.setVisibility(TextView.GONE);
		}
		rating_rb.setRating( movie.rating/2 );
		// populate and color interest
		TextView interest_tv = (TextView) findViewById(R.id.textview_interest);
		interest_tv.setText(movie.InterestForUser.toString());
		interest_tv.setTextColor(movie.InterestForUser.toColor());
		// set age restriction and color accordingly
		rating_tv.setText("Age Restriction: "+Integer.toString(movie.ageRestrictions));
		if( Application.getUser().getAge() < movie.ageRestrictions ){
			// in red if you are too young!
			rating_tv.setTextColor(0xFFFF0000);
		}
		
		// then, populate awards and cast from lists
		// TODO: SWITCH TO LISTVIEWS
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
				String text = data.title + " (";
				if(pref.getBoolean("pref_binary", false)){
					text += Integer.toBinaryString(data.year) + ")";
				}
				else{
					text += Integer.toString(data.year) + ")";
				}
				link.setText(text);
				moviesShowed.put(link, related);
				link.setLayoutParams( new LayoutParams(LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT) );
				//link.setPadding(5, 1, 5, 1);
				link.setBackgroundColor(0x00000000);
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
		db.close();
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
			startActivity( new Intent(this, SettingsActivity.class) );
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
		if(id == R.id.action_logout){
			Application.fullLogoutDialog(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
