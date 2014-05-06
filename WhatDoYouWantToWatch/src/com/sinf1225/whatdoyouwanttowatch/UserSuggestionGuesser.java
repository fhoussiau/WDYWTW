package com.sinf1225.whatdoyouwanttowatch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class, heavily linked to Database, guesses the user's favorite genre, director and actor
 * and then guesses the "best" movie for the user.
 */
public class UserSuggestionGuesser {
	
	// data for the user
	private static Genre UserGenre;
	private static String UserDirector;
	private static String UserActor;
	
	// currently, only fills genre
	public void initGuesser(Context context){
		Database db = new Database( context );
		SQLiteDatabase rdb = db.getReadableDatabase();
		Cursor cur = rdb.rawQuery("SELECT G."+Database.GENRE_GENRE+" COUNT(M."+Database.MOVIES_ID+")"+
								" FROM TABLES "+Database.TABLE_INTEREST+" M, "+Database.TABLE_GENRE+" G "+
								"WHERE M."+Database.INTEREST_MOVIE+" = "+Database.GENRE_MOVIE+" AND "+
								"M."+Database.INTEREST_INTEREST+" >= ? ORDER BY COUNT(M."+Database.MOVIES_ID+") DESC LIMIT 1" , 
							new String[] {Integer.toString(Interest.NOINTEREST.ordinal())} );
		if(cur.getCount()>0 ){
			int genre = cur.getInt(0);
			UserGenre = Genre.values()[ genre ];
		}
	}
	
	public void wipeGuesser(){
		UserGenre = null;
		UserDirector = null;
		UserActor = null;
	}
}
