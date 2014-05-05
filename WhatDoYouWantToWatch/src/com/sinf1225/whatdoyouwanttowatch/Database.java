package com.sinf1225.whatdoyouwanttowatch;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Database extends SQLiteOpenHelper {

	// constants for database handling
	public static final String DATABASE_NAME = "WDYWTW.db";
	public static final int DATABASE_VERSION = 8;

	// users table
	public static final String TABLE_USERS = "users";
	public static final String USERS_NAME  = "name";
	public static final String USERS_AGE   = "age";
	public static final String USERS_PSWD  = "password";
	public static final String[] USERS_COLUMNS = new String[] {
		USERS_NAME, 
		USERS_AGE,
		USERS_PSWD
		};

	// movies table
	public static final String TABLE_MOVIES = "movies";
	public static final String MOVIES_ID = "imdbID";
	public static final String MOVIES_NAME = "name";
	public static final String MOVIES_DIRECTOR = "director";
	public static final String MOVIES_DURATION = "duration";
	public static final String MOVIES_YEAR = "year";
	public static final String MOVIES_RATING = "rating";
	public static final String MOVIES_DESCRIPTION = "description";
	public static final String MOVIES_AGERESTR = "agerestriction";
	public static final String[] MOVIES_COLUMNS = new String[] {
		MOVIES_ID,
		MOVIES_NAME,
		MOVIES_DIRECTOR,
		MOVIES_DURATION,
		MOVIES_YEAR,
		MOVIES_RATING,
		MOVIES_DESCRIPTION,
		MOVIES_AGERESTR
	};


	// coming soon table
	public static final String TABLE_COMINGSOON = "comingsoon";
	public static final String COMINGSOON_MOVIE = MOVIES_ID;
	public static final String[] COMINGSOON_COLUMNS = new String[] {COMINGSOON_MOVIE};

	// playing now table
	public static final String TABLE_PLAYINGNOW = "playingnow";
	public static final String PLAYINGNOW_MOVIE = MOVIES_ID;
	public static final String[] PLAYINGNOW_COLUMNS = new String[] {PLAYINGNOW_MOVIE};

	// most watched table
	public static final String TABLE_MOSTWATCHED = "mostwatched";
	public static final String MOSTWATCHED_MOVIE = MOVIES_ID;
	public static final String[] MOSTWATCHED_COLUMNS = new String[] {MOSTWATCHED_MOVIE};

	// genre table
	public static final String TABLE_GENRE = "genre";
	public static final String GENRE_MOVIE = MOVIES_ID;
	public static final String GENRE_GENRE = "genre";
	public static final String[] GENRE_COLUMNS = new String[] {GENRE_MOVIE, GENRE_GENRE};

	// related movies table
	public static final String TABLE_RELATED = "relatedmovies";
	public static final String RELATED_MOVIE1 = MOVIES_ID+"1";
	public static final String RELATED_MOVIE2 = MOVIES_ID+"2";
	public static final String[] RELATED_COLUMNS = new String[] {RELATED_MOVIE1, RELATED_MOVIE2};

	// awards table
	public static final String TABLE_AWARDS = "awards";
	public static final String AWARDS_MOVIE = MOVIES_ID;
	public static final String AWARDS_NAME  = "name";
	public static final String AWARDS_YEAR  = "year";
	public static final String[] AWARDS_COLUMNS = new String[]{ AWARDS_MOVIE, AWARDS_NAME, AWARDS_YEAR };

	// cast table
	public static final String TABLE_CAST = "cast";
	public static final String CAST_MOVIE = MOVIES_ID;
	public static final String CAST_ACTOR = "actor";
	public static final String CAST_CHARACTER = "character";
	public static final String[] CAST_TABLE = new String[] { CAST_MOVIE, CAST_ACTOR, CAST_CHARACTER };

	// interest table (!)
	public static final String TABLE_INTEREST = "interest";
	public static final String INTEREST_MOVIE = MOVIES_ID;
	public static final String INTEREST_USER  = USERS_NAME;
	public static final String INTEREST_INTEREST = "interest";
	public static final String[] INTEREST_COLUMNS = new String[] {
		INTEREST_MOVIE,
		INTEREST_USER,
		INTEREST_INTEREST
	};


	// constructor: wrap around the default constructor
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	// creation of the database: create the tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// users table
		db.execSQL("CREATE TABLE "+TABLE_USERS+ " (" +
				USERS_NAME + " TEXT NOT NULL PRIMARY KEY," +
				USERS_AGE  + " INTEGER," +
				USERS_PSWD + " TEXT NOT NULL);");
		// movies table
		db.execSQL("CREATE TABLE "+TABLE_MOVIES+" (" +
				MOVIES_ID + " TEXT NOT NULL PRIMARY KEY," +
				MOVIES_NAME + " TEXT NOT NULL," +
				MOVIES_DIRECTOR + " TEXT NOT NULL default 'Unknown', " +
				MOVIES_DURATION + " INTEGER NOT NULL," + // minutes
				MOVIES_YEAR + " INTEGER NOT NULL," +
				MOVIES_RATING + " FLOAT NOT NULL," +
				MOVIES_DESCRIPTION + " TEXT default \"No description available\"," +
				MOVIES_AGERESTR  + " INTEGER default -1);"
				);
		// coming soon
		db.execSQL("CREATE TABLE "+TABLE_COMINGSOON+" ("+ 
				COMINGSOON_MOVIE + " TEXT NOT NULL, " +
				"FOREIGN KEY ("+COMINGSOON_MOVIE+") REFERENCES "+
				TABLE_MOVIES+"("+MOVIES_ID+") );"
				);
		// playing now
		db.execSQL("CREATE TABLE "+TABLE_PLAYINGNOW+" ("+ 
				PLAYINGNOW_MOVIE + " TEXT NOT NULL, " +
				"FOREIGN KEY ("+PLAYINGNOW_MOVIE+") REFERENCES "+
				TABLE_MOVIES+"("+MOVIES_ID+") );"
				);
		// most watched 
		db.execSQL("CREATE TABLE "+TABLE_MOSTWATCHED+" ("+
				MOSTWATCHED_MOVIE + " TEXT NOT NULL, " +
				"FOREIGN KEY ("+MOSTWATCHED_MOVIE+") REFERENCES "+
				TABLE_MOVIES+"("+MOVIES_ID+") );"
				);
		// genre table
		db.execSQL("CREATE TABLE "+ TABLE_GENRE +" (" +
				GENRE_MOVIE + " TEXT NOT NULL, " +
				GENRE_GENRE + " INTEGER NOT NULL, " + // integer because part of an enum
				"FOREIGN KEY ("+ GENRE_MOVIE +") REFERENCES " +
				TABLE_MOVIES+"("+MOVIES_ID+") );"
				);
		// related table
		db.execSQL("CREATE TABLE "+ TABLE_RELATED +" (" +
				RELATED_MOVIE1 + " TEXT NOT NULL, " +
				RELATED_MOVIE2 + " TEXT NOT NULL, " +
				"FOREIGN KEY ("+ RELATED_MOVIE1 +") REFERENCES " +
				TABLE_MOVIES+"("+MOVIES_ID+"), " +
				"FOREIGN KEY ("+ RELATED_MOVIE2 +") REFERENCES " +
				TABLE_MOVIES+"("+MOVIES_ID+") );"
				);
		// awards table
		db.execSQL("CREATE TABLE "+ TABLE_AWARDS +" (" +
				AWARDS_MOVIE + " TEXT NOT NULL, " +
				AWARDS_NAME  + " TEXT NOT NULL, " +
				AWARDS_YEAR  + " INTEGER NOT NULL, " +
				"FOREIGN KEY ("+ AWARDS_MOVIE +") REFERENCES " +
				TABLE_MOVIES+"("+MOVIES_ID+") );"
				);
		// cast table
		db.execSQL("CREATE TABLE "+ TABLE_CAST +" (" +
				CAST_MOVIE + " TEXT NOT NULL, " +
				CAST_ACTOR + " TEXT NOT NULL, " +
				CAST_CHARACTER + " DEFAULT 'Unknown', " +
				" FOREIGN KEY ("+ CAST_MOVIE +") REFERENCES " +
				TABLE_MOVIES+"("+MOVIES_ID+") );"
				);
		// interest table
		db.execSQL("CREATE TABLE "+ TABLE_INTEREST +" (" +
				INTEREST_MOVIE + " TEXT NOT NULL, " +
				INTEREST_USER  + " TEXT NOT NULL, " +
				INTEREST_INTEREST + " INTEGER NOT NULL DEFAULT 0, " + // int because part of an enum
				"FOREIGN KEY ("+ INTEREST_MOVIE +") REFERENCES " +
				TABLE_MOVIES+"("+MOVIES_ID+"), " +
				"FOREIGN KEY ("+ INTEREST_USER + ") REFERENCES " +
				TABLE_USERS +"("+ USERS_NAME+ ") );"
				);
		// TODO: add some data into the database!
		fillFakeData( db );
	}
	
	private void fillFakeData(SQLiteDatabase db){
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"movie1\", \"Star Wars\","+
	"\"George Lucas\", 42, 1979, 9.9, \"Fighting in the stars\", 5);");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"movie1\");");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"movie1\", 4)");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"movie1\", \"Harrison Ford\", \"Han Solo\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"movie1\", \"Himself\", \"C3P0\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"movie1\", \"Michael Jackson\", \"Jabba the Hutt\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"movie1\", \"Michael Bane\", \"Batman\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"movie1\", \"Dr. Zoidberg\", \"Darth Vader\")");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"movie1\", \"Best movie ever\", 1994)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"movie1\", \"Best movie ever ever\", 1995)");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"movie2\", \"Star Trek\","+
				"\"Not George Lucas\", 142, 1969, 7.9, \"More fighting in the stars\", 15);");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"movie1\", \"movie2\")");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"movie7\", \"Star Trek 2\","+
				"\"Not George Lucas\", 142, 1971, 3.9, \"Pew Pew Pew! More fighting in the stars\", 15);");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"movie3\", \"The Holy Bible\","+
				"\"Jesus Christ\", 666, 33, 10, \"'Be blessed', my son, said Jesus, before"+"" +
						" he gave the Roman a taste of his boot, 'For I come in the name of my Father,"+"" +
								" and he is very, very angry.\n\nThere was the cross."+"" +
										" But now, he's back. And he's angry.\", 18);");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"movie4\", \"A Trek to the Stars\","+
				"\"Steven Spielberg\", 99, 2014, 6.9, \"No fighting in the stars: a peaceful walk amongst sentient creatures\", -1);");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"movie5\", \"Stars and Money\","+
				"\"Bill Gates\", 122, 1975, 3.9, \"for(Human you: TheWorld){\n\tMicrosoft.getMoney(you);\n}\", 15);");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"movie6\", \"Star Trek 3\","+
				"\"Steven Spielberg\", 142, 1981, 8.5, \"Such Fighting much stars wow.\", 12);");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"movie6\", \"Doge\", \"Han Solo\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"movie6\", \"Doge\", \"Jabba the Hutt\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"movie6\", \"Doge\", \"Batman\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"movie2\", \"movie7\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"movie2\", \"movie6\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"movie6\", \"movie7\")");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"movie3\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"movie5\");");
		db.execSQL("INSERT INTO "+TABLE_COMINGSOON+" VALUES (\"movie4\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"movie2\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"movie3\");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// nothing to do: this contains sensitive data (not changed by updates)
		String tables[] = {TABLE_USERS, TABLE_MOVIES, TABLE_COMINGSOON,
				TABLE_MOSTWATCHED, TABLE_PLAYINGNOW, TABLE_GENRE,
				TABLE_INTEREST, TABLE_RELATED, TABLE_CAST, TABLE_AWARDS
		};
		for(String table: tables){
			db.execSQL("DROP TABLE IF EXISTS " + table);
		}
		onCreate(db);
	}
	
	
	// methods used throughout the code
	// TODO: update to more secure password handling
	public boolean login(String userName, String password){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( true, 
				 TABLE_USERS, 
				 new String[] {USERS_NAME},
				 USERS_NAME+" = \""+userName+"\" AND "+USERS_PSWD+" = \""+password+"\";",
				 null, //new String[] {userName, password},
				 null, null, null, null
				 );
		return cursor.getCount()==1;
	}
	
	public boolean newUser(String userName, String password, int age){
		// check if name already exists
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( true, 
				 TABLE_USERS, 
				 new String[] {USERS_NAME},
				 USERS_NAME+" = \""+userName+"\";",
				 null, //new String[] {userName},
				 null, null, null, null
				 );
		if(cursor.getCount() > 0 ){
			return false; // user already exists
		}
		// user does not exist
		db = this.getWritableDatabase();
		db.execSQL("INSERT INTO "+TABLE_USERS+"("+USERS_NAME+", "+
				USERS_PSWD+", "+USERS_AGE+") VALUES (\""
				+userName+"\", \""+password+
				"\", "+Integer.toString(age)+");");
		return true;
	}
	
	public int getUserAge(String name){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( true,
				TABLE_USERS, 
				new String[] {USERS_AGE},
				USERS_NAME+" = \""+name+"\"",
				null, null, null, null, null);
		cursor.moveToFirst();
		if(cursor.getCount() < 1){
			return 0;
		}
		return cursor.getInt(0);
	}
	

	public void setUserAge(String name, int age){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE "+TABLE_USERS+" SET "+
		USERS_AGE+" = "+Integer.toString(age) + 
		" WHERE " + USERS_NAME + " = \""+name+"\";");
	}
	
	
	
	// movie part
	
	// fill the movie with data
	public void fillMovie( Movie movie ){
		// instantiate database, cursor and selection
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur, cur2;
		String[] selectionArgs = new String[] { movie.getID() };
		
		// execute statements
		// table movies
		cur = db.rawQuery("SELECT "+ 
				MOVIES_NAME + ", "+
				MOVIES_DIRECTOR + ", "+
				MOVIES_DURATION + ", "+
				MOVIES_YEAR + ", "+
				MOVIES_RATING + ", "+
				MOVIES_DESCRIPTION + ", "+
				MOVIES_AGERESTR + 
				" FROM "+TABLE_MOVIES+
				" WHERE "+MOVIES_ID+" = ?", 
				selectionArgs);
		cur.moveToFirst();
		// build values
		movie.title = cur.getString(0);
		movie.director = cur.getString(1);
		movie.duration = cur.getInt(2);
		movie.year = cur.getInt(3);
		movie.rating = cur.getFloat(4);
		movie.description = cur.getString(5);
		movie.ageRestrictions = cur.getInt(6);
		
		// boolean tables: information is presence in the table
		cur = db.rawQuery("SELECT * FROM "+TABLE_COMINGSOON+" WHERE "+
						   COMINGSOON_MOVIE + " = ?",
				selectionArgs);
		movie.isComingSoon = (cur.getCount() == 1);
		
		cur = db.rawQuery("SELECT * FROM "+TABLE_PLAYINGNOW+" WHERE "+
				   		   PLAYINGNOW_MOVIE + " = ?",
				selectionArgs);
		movie.isPlayingNow = (cur.getCount() == 1);	
		
		cur = db.rawQuery("SELECT * FROM "+TABLE_MOSTWATCHED+" WHERE "+
				   		    MOSTWATCHED_MOVIE + " = ?",
				selectionArgs);
		movie.isMostWatched = (cur.getCount() == 1);
		
		// many elements table
		int count;
		// genre, awards, cast, related
		cur = db.rawQuery("SELECT "+ AWARDS_NAME+", "+AWARDS_YEAR+" FROM "+ 
				TABLE_AWARDS + " WHERE " + AWARDS_MOVIE + " = ?",
				selectionArgs);
		count = cur.getCount();
		if(count > 0){
			movie.Awards = new Award[ count ];
			cur.moveToFirst();
			for(int i=0; !cur.isAfterLast(); i++){
				movie.Awards[i] = new Award(cur.getString(0), cur.getInt(1));
				cur.moveToNext();
			}
		}
		
		cur = db.rawQuery("SELECT "+CAST_ACTOR+", "+CAST_CHARACTER+" FROM "+
				TABLE_CAST +  " WHERE "+CAST_MOVIE+" = ?",
				selectionArgs);
		count = cur.getCount();
		if(count > 0){
			movie.Cast = new String[ count ][2];
			cur.moveToFirst();
			for(int i=0; !cur.isAfterLast(); i++){
				movie.Cast[i] = new String[] {cur.getString(0), cur.getString(1)};
				cur.moveToNext();
			}
		}
		
		cur = db.rawQuery("SELECT "+GENRE_GENRE+" FROM "+TABLE_GENRE + " WHERE "+
					GENRE_MOVIE + " = ?",
					selectionArgs);
		count = cur.getCount();
		if(count > 0){
			movie.MovieGenre = new Genre[ count ];
			cur.moveToFirst();
			for(int i=0; !cur.isAfterLast(); i++){
				movie.MovieGenre[i] = Genre.values()[cur.getInt(0)];
				cur.moveToNext();
			}
		}
		
		// related movies: there's a catch! The movie we're looking for
		// can be either movie1 or movie2!
		cur = db.rawQuery("SELECT "+RELATED_MOVIE1+" FROM "+TABLE_RELATED+
				" WHERE "+RELATED_MOVIE2+" = ?",
				selectionArgs);
		cur2= db.rawQuery("SELECT "+RELATED_MOVIE2+" FROM "+TABLE_RELATED+
				" WHERE "+RELATED_MOVIE1+" = ?",
				selectionArgs);
		count = cur.getCount();
		movie.RelatedMovies = new Movie[ count + cur2.getCount() ];
		cur.moveToFirst();
		for(int i=0; !cur.isAfterLast(); i++){
			movie.RelatedMovies[i] = Application.getMovie(cur.getString(0));
			cur.moveToNext();
		}
		cur2.moveToFirst();
		for(int i=count; !cur2.isAfterLast(); i++){
			movie.RelatedMovies[i] = Application.getMovie(cur2.getString(0));
			cur2.moveToNext();
		}
		
		// interest
		// this computes only for current user
		if(Application.isUserLoggedIn()){
			selectionArgs = new String[] {movie.getID(), Application.getUser().getName()};
			cur = db.rawQuery("SELECT "+INTEREST_INTEREST+" FROM "+TABLE_INTEREST+
					" WHERE "+INTEREST_MOVIE+" = ? AND "+INTEREST_USER+" = ?",
					selectionArgs);
			count = cur.getCount();
			if(count > 0){
				cur.moveToFirst();
				movie.InterestForUser = Interest.values()[cur.getInt(0)];
			}
			else{
				movie.InterestForUser = Interest.NOINTEREST;
			}
		}
		else{
			movie.InterestForUser = Interest.NOINTEREST;
		}
		// DONE
		
	}
	
	
	// get some info about the movie
	public MovieQuickData getMovieQuickData( Movie movie ){
		SQLiteDatabase db = this.getReadableDatabase();
		// execute a statement in the database
		Cursor cur = db.rawQuery("SELECT "+MOVIES_NAME+", "+
										   MOVIES_DIRECTOR+", "+
										   MOVIES_YEAR+
								 " FROM " + TABLE_MOVIES +
								 " WHERE "+MOVIES_ID+" = ?",
								 new String[] {movie.getID()}
				 );
		// this is an error! (normally, never happening)
		if(cur.getCount() < 1){
			return null;
		}
		// otherwise, build value
		cur.moveToFirst();
		MovieQuickData result =  new MovieQuickData(
									cur.getString(0),
									cur.getString(1),
									cur.getInt(2)
								);
		return result;
	}
	
	// movie options and bonus
	
	/**
	 * Return a random movie from the database
	 * @return a random movie, or null in case of error 
	 */
	public Movie getRandomMovie(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT "+MOVIES_ID+" FROM "+
								TABLE_MOVIES+" ORDER BY RANDOM() LIMIT 1", null);
		if(cur.getCount()>0){
			cur.moveToFirst();
			return Application.getMovie(cur.getString(0));
		}
		return null;
	}
	
	
	/**
	 * Change the current user's interest for a movie
	 * This is the only option available on movies!
	 * @param movie: the movie to change
	 */
	public void setMovieInterest(Movie movie, int i){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE "+TABLE_INTEREST+" SET "+INTEREST_INTEREST+
				" = "+Integer.toString(i)+" WHERE "+INTEREST_MOVIE+
				" = \""+movie.getID()+"\";");
	}

	 
	private static final int MAX_MOVIES_MATCH = 10;
	private static final int MAX_DIRECTOR_MATCH = 10;
	private static final int MAX_YEAR_MATCH = 10;
	
	
	// search
	
	/**
	 * Search the database with the given string as a query.
	 * This searches first for the name, then the year, then the director
	 * @param query: the query, that is (approximately) the title or the director, or (exactly) the year of the movie
	 * @return a list, possibly empty, of movies
	 * TODO: add features
	 */
	public ArrayList<Movie> Search_Movie(String query){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Movie> result = new ArrayList<Movie>();
		// first, check if movie has year-like format
		int year = -1;
		try{
			year = Integer.parseInt(query);
		}
		catch(NumberFormatException error){
		}
		if(year != -1){
			Cursor cur = db.rawQuery("SELECT "+MOVIES_ID+
					" FROM "+ TABLE_MOVIES+
					" WHERE "+MOVIES_YEAR+" = ?", 
					new String[] { query });
			cur.moveToFirst(); int iter =0;
			while(!cur.isAfterLast() && iter<MAX_YEAR_MATCH) {
				Movie mov = Application.getMovie(cur.getString(0));
				mov.getQuickData(this);
				result.add(mov);
				cur.moveToNext();
				iter++;
			}
		}
		// search for name
		Cursor cur = db.rawQuery("SELECT "+MOVIES_ID+
				   				 " FROM " + TABLE_MOVIES +
				   				 " WHERE "+MOVIES_NAME+" LIKE '%"+query+"%'",
				   				 null
								);
		// add movies found to value
		cur.moveToFirst(); int iter =0;
		while(!cur.isAfterLast() && iter<MAX_MOVIES_MATCH) {
			Movie mov = Application.getMovie(cur.getString(0));
			mov.getQuickData(this);
			result.add(mov);
			cur.moveToNext();
			iter++;
		}
		// search for director
		cur = db.rawQuery("SELECT "+MOVIES_ID+
  				 " FROM " + TABLE_MOVIES +" "+
  				 " WHERE "+MOVIES_DIRECTOR+ " LIKE '%"+query+"%'",
  				 null
				);
		// add movies found to value
		cur.moveToFirst(); iter =0;
		while(!cur.isAfterLast() && iter<MAX_DIRECTOR_MATCH) {
			Log.d("YEK YEK YEK", cur.getString(0));
			Movie mov = Application.getMovie(cur.getString(0));
			mov.getQuickData(this);
			result.add(mov);
			cur.moveToNext();
			iter++;
		}
		db.close();
		return result;
	}
	
	
	// TODO: SORT_RESULTS
	
	/**
	 * Returns n movies from a table
	 * @param n: the number of movies to return
	 * @param TABLE: the table from which to extract the movies
	 * @return: an array of at most n movies (less, if not enough movies are found)
	 */
	private Movie[] getNGenericTableMovies(int n, String TABLE){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT DISTINCT("+MOVIES_ID+") FROM "+TABLE+" LIMIT "+Integer.toString(n),
				null);
		cur.moveToFirst();
		int newN = cur.getCount();
		Movie[] res = new Movie[newN];
		cur.moveToFirst(); int iter=0;
		while(!cur.isAfterLast() && iter<newN){
			res[iter] = Application.getMovie( cur.getString(0) );
			iter++;
			cur.moveToNext();
		}
		return res;
	}
	
	public Movie[] getMostWatched(int n){
		return this.getNGenericTableMovies(n, TABLE_MOSTWATCHED);
	}
	public Movie[] getPlayingNow(int n){
		return this.getNGenericTableMovies(n, TABLE_PLAYINGNOW);
	}
	public Movie[] getComingSoon(int n){
		return this.getNGenericTableMovies(n, TABLE_COMINGSOON);
	}
	


}
