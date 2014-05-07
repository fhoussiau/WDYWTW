package com.sinf1225.whatdoyouwanttowatch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This class, heavily linked to Database, guesses the user's favorite genre, director and actor
 * and then guesses the "best" movie for the user.
 */
public class UserSuggestionGuesser {
	
	// data for the user
	private static Genre UserGenre;
	private static String UserDirector;
	private static String UserActor;
	
	/**
	 * Guess the favorite genre, director and actor of the current user, loading from database
	 * @param context: the context initiating the request to init
	 * TODO: add director and actor guessing
	 */
	public static void initGuesser(Context context){
		Database db = new Database( context );
		SQLiteDatabase rdb = db.getReadableDatabase();
		Cursor cur = rdb.rawQuery("SELECT "+Database.TABLE_GENRE+"."+Database.GENRE_GENRE+
								" FROM "+Database.TABLE_INTEREST+", "+Database.TABLE_GENRE+
								" WHERE "+Database.TABLE_INTEREST+"."+Database.INTEREST_MOVIE+" = "+
								Database.TABLE_GENRE +"."+ Database.GENRE_MOVIE+" AND "+
								Database.TABLE_INTEREST+"."+Database.INTEREST_INTEREST+" >= ? "+
								" GROUP BY "+Database.TABLE_INTEREST+"."+Database.INTEREST_INTEREST+
								" ORDER BY COUNT("+
								Database.TABLE_INTEREST+"."+Database.INTEREST_MOVIE+") DESC LIMIT 1" , 
							new String[] {Integer.toString(Interest.NOINTEREST.ordinal())} );
		if(cur.getCount()>0 ){
			cur.moveToFirst();
			int genre = cur.getInt(0);
			UserGenre = Genre.values()[ genre ];
		}
		Log.d("GUESSER", "Your favorite genre: "+UserGenre.toString());
	}
	
	/**
	 * Get the user's favorite genre
	 * @return
	 */
	public static Genre getUserGenre(){
		return UserGenre;
	}
	
	public static void wipeGuesser(){
		UserGenre = null;
		UserDirector = null;
		UserActor = null;
	}
}
