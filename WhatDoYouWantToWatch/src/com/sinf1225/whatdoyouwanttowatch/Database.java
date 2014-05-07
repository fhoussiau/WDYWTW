package com.sinf1225.whatdoyouwanttowatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Database extends SQLiteOpenHelper {

	// constants for database handling
	public static final String DATABASE_NAME = "WDYWTW.db";
	public static final int DATABASE_VERSION = 13;
	private static final String DATE_FORMAT = "dd/mm/yyyy";
	public static final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
	public static final Calendar currentTime = Calendar.getInstance();

	// users table
	public static final String TABLE_USERS = "users";
	public static final String USERS_NAME  = "name";
	public static final String USERS_BIRTHDAY   = "birthday";
	public static final String USERS_PSWD  = "password";
	public static final String USERS_LASTACCESS = "lastaccess"; // this field is a long (s since Epoch)
	public static final String[] USERS_COLUMNS = new String[] {
		USERS_NAME, 
		USERS_BIRTHDAY,
		USERS_PSWD,
		USERS_LASTACCESS
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
				USERS_BIRTHDAY  + " TEXT NOT NULL," +
				USERS_PSWD + " TEXT NOT NULL, "+
				USERS_LASTACCESS+ " INTEGER NOT NULL);");
		// movies table
		db.execSQL("CREATE TABLE "+TABLE_MOVIES+" (" +
				MOVIES_ID + " TEXT NOT NULL PRIMARY KEY," +
				MOVIES_NAME + " TEXT NOT NULL," +
				MOVIES_DIRECTOR + " TEXT NOT NULL default 'Unknown', " +
				MOVIES_DURATION + " INTEGER default -1," + // minutes
				MOVIES_YEAR + " INTEGER NOT NULL," +
				MOVIES_RATING + " FLOAT default -1," +
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
		/*
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
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"movie3\");");*/

		// Mike :D
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"001\", \"Star Wars I\", \" George Lucas\", 136, 1999, 6.6, \"The First Adventure of Yedi Anakin\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"002\", \"Star Wars II\", \" George Lucas\", 142, 2002, 6.8, \"The Second Adventure of Yedi Anakin\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"003\", \"Star Wars III\", \" George Lucas\", 140, 2005, 7.7, \"The Third Adventure of Yedi Anakin\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"004\", \"Star Trek\", \" J.J. Abrahams\", 127, 2009, 8.1, \"Captain Kirk and Mr.Spock voyage cross the Space\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"005\", \"Star Trek Into Darkness\", \" J.J. Abrahams\", 132, 2013, 7.9, \"The Prolog to Star Trek, Adventure of T.Kirk becoming a Captain\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"006\", \"Hunger Games\", \" Gary Ross\", 142, 2012, 7.3, \"Katniss Everdeen in a deadly tournament \", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"007\", \"Hunger Games Catching Fire\", \" Francis Lawrence\", 146, 2013, 7.8, \"Katniss Everdeen is back in the deadly tournament andbecomes a symbol of Hope\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"008\", \"The Intouchables\", \"Olivier Nakache\", 112, 2011, 8.6, \"Biography of a disabled Wheelchair driver and his friend\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"009\", \"The Ring\", \" Gore Verbinski\", 115, 2002, 7.1, \"A journalist investigates a videotape, which kills everyone who looked it\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"010\", \"Nothing to declare\", \" Dany Boon\", 108, 2010, 6.3, \"A belgian-French comedy about the customs\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"011\", \"Maleficent\", \" Robert Stromberg\", 98, 2014, NULL, \"The History of the evil Fairy Maleficent\", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"012\", \"Tron\", \" Steven Lisberger\", 96, 1982, 6.8, \"A Computer hacker finds himself inside the digital World and has to save it from the MCP\", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"013\", \"Tron Legacy\", \"Joseph Kosinski\", 125, 2010, 6.9, \"The son of a virtual World designer ends up in the virtual World while searching his father \", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"014\", \"Saw\", \"James Wan\", 103, 2004, 7.7, \"Two men awake in the nightmare of Jigsaw's deadly games\", 18 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"015\", \"SawII\", \" Darren Lynn Bousman\", 93, 2005, 6.6, \"Imprisoned by jigsaw, a few people have to escape before lethal nerve gas kill them\", 18 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"016\", \"Saw III\", \"Darren Lynn Bousman\", 108, 2006, 6.2, \"The apprentice of jigsaw plays with a unlucky citizen \", 18 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"017\", \"Final Destination\", \" James Wong\", 98, 2000, 6.8, \" Preventing his friends from death, he and his Friends are hunted to death by the Death itself\", 18 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"018\", \"Final Destination 2\", \" David R.Ellis \", 90, 2003, 6.2, \"Kimberly has a vision of highway Pilup and saves some people, but for how long?\", 18 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"019\", \"Final Destination 3\", \" James Wong\", 93, 2006, 5.8, \"A young student saves his friends from a Rollercoaster death, but Death is not pleased\", 18 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"020\", \"Pretty Woman\", \" Garry Marshall\", 119, 1990, 6.9, \"A buisness Man hires a prostitute to play his girlfriend, just to fall in love with her\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"021\", \"The Bodyguard \", \"Mick Jackson\", 129, 1992, 6.0, \" A old Secret Service Agent, takes a Job as Bodyguard for a pop singer\", 6 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"022\", \"Love actually\", \"Richard Curtis\", 135, 2003, 7.7, \"The History of 8 couples and their Love live\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"023\", \"Dirty Dancing\", \"Emile Ardolino\", 100, 1987, 6.8, \"Johnny Castle teaches 'Baby'Houseman and falls in love with her\", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"024\", \"Grease\", \"Randal Kaiser\", 110, 1978, 7.2, \"Good girl Sandy and greaser danny fell in love over summer and discover they're in the same school\", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"025\", \"High School Musical\", \"Kenny Ortega\", 98, 2006, 5.1, \"Troy and Gebriella discover their love to music and to ach other\", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"026\", \"High School Musical 2\", \" Kenny Ortega\", 104, 2007, 4.6, \"A local Talent show is held in a country club and Troy, Gabriella and the club has to be part of it \", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"027\", \"Camp Rock\", \"Matthew Diamond\", 94, 2008, 4.8, \"At a music Camp The jonas brothers and a kitchen Worker rock the camp\", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"028\", \"Camp Rock 2\", \"Paul Hoen\", 97, 2010, 5.0, \"Camp Star tries to ruin Camp Rock and so a Music battle begins\", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"029\", \" Moulin Rouge\", \" Baz Luhrmann\", 127, 2001, 7.7, \"Satine, star of the Moulin Rouge, falls in love with a poet, but a duke want her for hiself\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"030\", \"The Great Gatsby\", \"Baz Luhrman\", 143, 2013, 7.3, \"A Midwestern war veteran finds himself back in the past of his millionaire Neighbor\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"031\", \"Some like it hot\", \"Billy Wilder\", 120, 1959, 8.4, \" Two musicians run from the mafia and hide in a girl group\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"032\", \"Lone Ranger\", \" Gore Verbinski\", 149, 2013, 6.6, \"History of a Native American Warrior who becomes a legend of Justice\", 16 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"033\", \"Once upon a time in the West\", \" Sergio Leone\", 175, 1968, 8.7, \"Story of a mysterious Stranger who joins with a desperado to protect a beautiful widow\", 16 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"034\", \"Apache Gold\", \" Harald Reinl\", 101, 1963, 7.0, \"Winnetou and Old Schatterhand try to settle a violent conflict and become bloodbrothers\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"035\", \"Last of the Renegades \", \"Harald Reinl\", 94, 1964, 6.6, \"A ruthless Oil baron want to create a bloody war betwenn natives and settlers and it is to the bloodbrothers again to settle it \", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"036\", \"Avatar\", \"James Cameron\", 162, 2009, 7.9, \"A paraplegic Marine between his orders and the world he feels home \", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"037\", \"Avatar 2\", \"James Cameron\", NULL, 2016, NULL, \"a sequel to the 2009 film Avatar\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"038\", \"Avatar 3\", \"James Cameron\", NULL, 2017, NULL, \"No Description for the Moment\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"039\", \"Spirited away\", \"Hayao Miyazaki\", 125, 2001, 8.6, \"A 10 year old Girl wanders in a world ruled by gods, witches and monsters, and where human a transformed in animals\", 6 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"040\", \"Princess Mononoke\", \"Hayao Miyazaki\", 134, 1997, 8.5, \"On a JOurney for a curse of a friend's curse, Ashitaka finds himself in a war between forest gods and settlers\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"041\", \"Tales from Earthsea\", \"Goro Miyazaki\", 115, 2006, 6.5, \"In a  Kindom people start to become bizarre, and everyone swear to have suddenly seen Dragons around the Land\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"042\", \"The King's Speech\", \" Tom Hooper\", 118, 2010, 8.1, \"The Story of King George VI and his Speech Therapist Lionel Logue\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"043\", \"Godzilla\", \"Gareth Edwards\", 123, 2014,NULL, \"Godzilla fight against some Monsters who brings HUman life near to extintion\", 16 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"044\", \"X-Men Days of Future Past\", \"Byran Singer\", 130, 2014, NULL, \"The X-men send Wolverine in th past to save the future of human and mutants\", 16 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"045\", \"Captain America: The First Avenger\", \"Joe Johnston\", 124, 2011, 6.8, \"After being deemed unfit for the army, Steve Rogers takes Part of a secret operations who gives him supernatural strengh making him Captain America\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"046\", \"Captain America: The Winter Soldier\", \" Joe Russo \", 136, 2014, 8.2, \"Captain America struggels in the modern World but he is needed to fight against the winter Soldier \", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"047\", \"The Incredibles\", \"Brad Bird\", 115, 2004, 8.0, \" A family of undercover Heroes are forced into saving the World\", 0 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"048\", \"Diplomatie\", \"Voler Schlöndorff\", 88, 2014, 7.1, \"historical Drama that despicts the relationship between Dietrich von Cholitz and Raoul Nordling\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"049\", \"Mad Ship\", \"David Mortin\", 90, 2013, 6.5, \"A poor Scandinavian couple winds up in Canada but The Great Depression takes a toll in a way the never would have feared\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"050\", \"Les Misérables\", \"Tom Hooper\", 158, 2012, 7.7, \"Jean Valjean, pused for decades by policeman Javert for breaking parole, agrees to take care of a factory worker's daughter\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"051\", \"Kabhi Khushi Kabhie Gham\", \"Karan Johar\", 210, 2001, 7.5, \" A family Drama where Rahul adopted son of the family don't want to marry the wife chosen by his father but to live his life\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"052\", \"New Jork Massala\", \" Nikihl Advani\", 186, 2003, 8.1, \" Life of Naina who changes after Aman teaches her the new way of Live\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"053\", \"X-Men First Class\", \" Matthew Vaughn \", 132, 2011, 7.8, \"The american gouverment ask 1962 the mutants to help detroning a dictator who wants the World War III\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"054\", \"X-Men Origins: Wolverine\", \" Byran Singer\", 107, 2009, 6.8, \"A look in Wolverine's early life and the influence on him from the gouvernement\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"055\", \"X-Men 2\", \"Byran Singer\", 133, 2003, 7.5, \"The mutants try to find a mutant assassin while the mutants academy ist attacked by the gourvernement\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"056\", \"X-Men\", \" Bryan Singer\", 104, 2000, 7.4, \"Two mutants came to mutants academy and are involved in a battle between the academy an a terror organisation with similar Powers \", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"057\", \"Hercules\", \"Brett Radner \", NULL, 2014, NULL, \"After the twelve labors the king of Thrace and his daughter seek Hercules aid to defead a tyrannical Warlord\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"058\", \"X-Men The Last Stand\", \"Bratt Radner\", 104, 2006, 6.8, \"When a cure is found for mutations a line is dran between Magneto an Xavier \", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"059\", \"Oblivion\", \"Joseph Kosinski\", 124, 2013, 7.0, \"A veteran assigned toextract remaining earth ressources start to question his mission and life\", 12 );");
		db.execSQL("INSERT INTO "+TABLE_MOVIES+" VALUES (\"060\", \"Looper\", \"Rian Johnson\", 119, 2012, 7.5, \"In 2074 people are send back in past where they are killed by sell-swords, if the mob want to get rid of them. One Day they want to 'close the loop' sending Joe, one of the sellswords back\", 12 );");

		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"001\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"004\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"005\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"007\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"008\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"013\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"014\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"020\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"023\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"029\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"031\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"032\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"036\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"042\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"046\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"059\");");
		db.execSQL("INSERT INTO "+TABLE_MOSTWATCHED+" VALUES (\"060\");");

		db.execSQL("INSERT INTO "+TABLE_COMINGSOON+" VALUES (\"011\");");
		db.execSQL("INSERT INTO "+TABLE_COMINGSOON+" VALUES (\"037\");");
		db.execSQL("INSERT INTO "+TABLE_COMINGSOON+" VALUES (\"038\");");
		db.execSQL("INSERT INTO "+TABLE_COMINGSOON+" VALUES (\"043\");");
		db.execSQL("INSERT INTO "+TABLE_COMINGSOON+" VALUES (\"044\");");
		db.execSQL("INSERT INTO "+TABLE_COMINGSOON+" VALUES (\"057\");");

		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"006\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"009\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"020\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"024\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"026\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"041\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"048\");");
		db.execSQL("INSERT INTO "+TABLE_PLAYINGNOW+" VALUES (\"060\");");

		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"001\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"001\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"002\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"002\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"003\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"003\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"004\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"004\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"004\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"005\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"005\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"005\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"006\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"006\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"007\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"007\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"008\", 20)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"008\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"009\", 5)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"009\", 0)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"010\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"011\", 8 )");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"011\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"012\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"012\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"013\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"013\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"014\", 5)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"014\", 10)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"014\", 0)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"015\", 5)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"015\", 10)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"015\", 0)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"016\", 5)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"016\", 10)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"016\", 0)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"017\", 5)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"017\", 0)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"018\", 5)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"018\", 0)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"019\", 5)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"019\", 0)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"020\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"020\", 11)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"021\", 11 )");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"021\", 12)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"022\", 11)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"022\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"023\", 11)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"023\", 13)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"024\", 11)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"024\", 13)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"025\", 13)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"025\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"026\", 13)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"026\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"027\", 13)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"027\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"028\", 13)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"028\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"029\", 12)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"029\", 11 )");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"030\", 12)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"030\", 11)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"031\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"032\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"032\", 14)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"033\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"033\", 14)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"034\", 14)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"035\", 14)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"036\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"036\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"037\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"037\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"038\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"038\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"039\", 15)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"039\", 3)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"040\", 15)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"040\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"041\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"041\", 15)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"041\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"042\", 17)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"042\", 1)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"043\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"043\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"044\", 7 )");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"044\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"045\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"045\", 16)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"046\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"046\", 16)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"047\", 15)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"047\", 16)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"047\", 3)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"048\", 17)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"048\", 12)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"049\", 17)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"049\", 9)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"050\", 17)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"050\", 12)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"051\", 21)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"051\", 19)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"052\", 19)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"052\", 11)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"053\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"053\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"054\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"054\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"055\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"055\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"056\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"056\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"057\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"057\", 8)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"058\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"058\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"059\", 7)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"059\", 10)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"060\", 18)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"060\", 4)");
		db.execSQL("INSERT INTO "+TABLE_GENRE+" VALUES (\"060\", 7)");


		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"001\", \" Anthony Daniels\", \" C3P0 (voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"001\", \" Jack Lloyd\", \" Anakin Skywalker\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"001\", \" Frank Oz \", \" Yoda (voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"001\", \" Ewan McGregor \", \" Obi-Wan Kenobi\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"002\", \" Ewan McGregor \", \" Obi-Wan Kenobi\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"002\", \" Anthony Daniels\", \" C3P0(voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"002\", \" Hayden Christensen\", \" Anakin Skywalker\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"002\", \" Christopher Lee \", \" Count Doku\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"002\", \" Frank Oz\", \" Yoda (voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"003\", \" Anthony Daniels\", \" C3P0(voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"003\", \" Hayden Christensen\", \" Anakin Skywalker\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"003\", \" Christopher Lee \", \" Count Doku\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"003\", \" Frank Oz\", \" Yoda (voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"003\", \" Ewan McGregor \", \" Obi-Wan Kenobi\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"004\", \" Chris Pine\", \" T.Kirk\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"004\", \" Zachary Quinto\", \" Spock\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"004\", \" Leonard Nimoy\", \" Spock Prime\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"004\", \" Erik Bana \", \" Nero\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"005\", \" Chris Pine\", \" T.Kirk\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"005\", \" Zachary Quinto\", \" Spock\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"005\", \" Benedict Cumberbatch\", \" Khan \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"006\", \" Jennifer Lawrence \", \"Kathniss Everdeen\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"006\", \" Wes Bentley\", \" Seneca Crane\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"006\", \" Josh Hutcherson\", \" Peeta Mellark\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"006\", \" Donald Sutherland\", \" President Snow\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"006\", \" Liam Hemsworth \", \" Gale Hawthrone \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"007\", \" Jennifer Lawrence \", \"Kathniss Everdeen\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"007\", \" Sam Clafin\", \" Finnick Odair\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"007\", \" Josh Hutcherson\", \" Peeta Mellark\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"007\", \" Donald Sutherland\", \" President Snow\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"007\", \" Liam Hemsworth \", \" Gale Hawthrone \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"008\", \" Omar Sy\", \" Driss\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"008\", \" François Cluzet\", \" Philippe\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"008\", \" Audrey Fleurot\", \" Magalie\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"009\", \" Naomi Watts \", \" Rachel\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"009\", \" Martin Henderson\", \" Noah\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"009\", \" David Dorfman\", \" Aidan\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"010\", \" Benoit Poelvoorde\", \" Ruben Vandevoorde\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"010\", \" Dany Boon \", \" Mathias Ducatel\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"010\", \" Julie Bernard\", \" Louise Vandervoorde\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"011\", \" Angelina Jolie\", \" Maleficent\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"011\", \" Juno Temple\", \" Thistletwit\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"011\", \" Elle Fanning\", \" Princess Aurora\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"012\", \" Jeff Bringes\", \"Clu\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"012\", \" Jeff Bringes\", \" Kevin Flynn\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"012\", \" Bruce Boxleitner\", \"Tron\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"012\", \" Bruce Boxleitner\", \" Alan Bradley\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"012\", \" David Warner\", \"MCP\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"013\", \" Jeff Bringes\", \"Clu\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"013\", \" Jeff Bringes\", \" Kevin Flynn\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"013\", \" Bruce Boxleitner\", \"Tron\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"013\", \" Bruce Boxleitner\", \" Alan Bradley\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"013\", \" Garett Hedlund\", \"Sam Flynn \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"014\", \" Danny Glover\", \" Detective David Tapp\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"014\", \" Ken Leung \", \" Detective Steven Sing \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"014\", \" Leigh Whannell\", \" Adam Faulkner-Stanheight\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"014\", \" Dina Meyer\", \"Alison Kerry\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"015\", \" Dina Meyer\", \"Alison Kerry\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"015\", \" Tobin Bell \", \" John Kramer\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"015\", \" Tobin Bell\", \" Jigsaw\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"016\", \" Dina Meyer\", \"Alison Kerry\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"016\", \" Tobin Bell \", \" John Kramer\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"016\", \" Tobin Bell\", \" Jigsaw\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"017\", \" Devon Sawa\", \" Alex Browning\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"017\", \" Ali Larter\", \" Clear Rivers\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"017\", \" Kerr Smith\", \" Carter Horton\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"018\", \" Ali Carter \", \" Clear Rivers \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"018\", \" A.J. Cook \", \" Kimberly Corman \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"018\", \" James Kirk \", \" Tom Carpenter\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"019\", \" Ryan Merriman\", \" Kevin Fisher\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"019\", \" Jesse Moss\", \" Jason Wise\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"019\", \" Kris Lemche \", \"Ian McKinley\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"020\", \" Richard Gere \", \" Edward Lewis\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"020\", \" Julia Roberts\", \" Vivian Ward\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"020\", \" Ralph Bellamy\", \" James Morse\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"021\", \" Kevin Costner\", \" Frank Farmer\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"021\", \" Whitney Houston\", \" Rachel Marron \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"021\", \" Gary Kemp\", \" Sy Spector\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"022\", \" Liam Neeson\", \"Daniel\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"022\", \" Hugh Grant\", \"The Prime Minister\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"022\", \" Martine McCutcheron\", \"Natalie\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"022\", \" Keira Knightley\", \"Juliet\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"023\", \" Jennifer Grey\", \" Baby Houseman\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"023\", \" Patrick Swayze\", \"Johnny Castle\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"023\", \" Jerry Orbach\", \" Jake Houseman\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"024\", \" John Travolta\", \"Danny\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"024\", \" Olivia Newton-John\", \"Sandy\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"024\", \" Stockard Channing\", \"Rizzo\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"025\", \" Zak Efron \", \" Troy Bolton\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"025\", \" Vanessa Hughes\", \" Gabriella Montez\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"025\", \" Ashley Tisdale\", \"Sharpay Evans\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"026\", \" Zak Efron \", \" Troy Bolton\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"026\", \" Vanessa Hughes\", \" Gabriella Montez\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"026\", \" Ashley Tisdale\", \"Sharpay Evans\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"027\", \" Demi Lovato\", \"Mitchie Torres\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"027\", \" Joe Jonas\", \" Shane Gray\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"027\", \" Kevin Jonas\", \"Jason Gray\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"028\", \" Demi Lovato\", \"Mitchie Torres\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"028\", \" Joe Jonas\", \" Shane Gray\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"028\", \" Kevin Jonas\", \"Jason Gray\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"029\", \" Evan McGregor\", \"Christian\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"029\", \" Nicole Kidman\", \"Satine\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"029\", \" Jim Broadbent\", \"Harold Ziedler\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"029\", \" Richard Roxburgh\", \"The Duke\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"030\", \" Jason Clarke\", \" George Wilson \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"030\", \" Elizabeth Debicki\", \" Jordan Baker\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"030\", \" Leonardo Di Caprio \", \"Jan Gatsby\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"031\", \" Marilyn Monroe\", \"Sugar Kane Kowalczyk \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"031\", \" Tony Curtis\", \" Joe\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"031\", \" Jack Lemmon\", \" Jerry\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"032\", \" Johnny Depp \", \" Tonto\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"032\", \" Armin Hammer\", \" John Reid\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"032\", \" William Fichtner\", \" Butch Cavendish\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"033\", \" Claudia Cardinale\", \" Jill McBain\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"033\", \" Herny Fonda\", \" Frank\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"033\", \" Jason Robards\", \" Cheyenne\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"033\", \" Charles Bronson\", \" Harmonica\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"034\", \" Lex Barter\", \" Old Schatterhand\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"034\", \" Pierre Brice\", \"Winnetou \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"034\", \" Marie Versini\", \"Nscho-tschi\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"035\", \" Lex Barter\", \" Old Schatterhand\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"035\", \" Pierre Brice\", \"Winnetou \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"035\", \" Anthony Steel\", \" Bud Forrester\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"036\", \" Sam Worthington\", \" Jake Sully\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"036\", \" Zoe Saldana\", \" Neytri\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"036\", \" Sigourney Weaver\", \" Grace\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"036\", \" Wes Studi\", \" Eytukan\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"037\", \" Sam Worthington\", \" Jake Sully\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"037\", \" Zoe Saldana\", \" Neytri\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"037\", \" Sigourney Weaver\", \" Grace\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"038\", \" Sam Worthington\", \" Jake Sully\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"038\", \" Zoe Saldana\", \" Neytri\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"039\", \" Rumi Hiiragi\", \" Chihiro(voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"039\", \" Miyu Irino\", \"Haku(voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"039\", \" Bunta Sugawara\", \"Kamajî (voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"040\", \" Bily Crudup\", \"Ashitaka(voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"040\", \" Billy Bob Thronton\", \"Jigo (voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"040\", \" Minnie Driver\", \"Lady Eboshi\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"041\", \" Jun'ichi Okada\", \" Arren(voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"041\", \" Aoi Teshima\", \" Theru(voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"041\", \" Bunta Sugawama\", \"Haitaka(voice)\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"042\", \" Colin Firth\", \" King George VI\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"042\", \" Geoffrey Rush\", \"Lionel Rogue\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"042\", \" Helena Bohnham Carter\", \" Queen Elisabeth\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"043\", \" Aaron Taylor-Johnson\", \" Ford Brody\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"043\", \" Elisabeth Olsen\", \"Elle Brody\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"043\", \" Bryan Cranston\", \" Joe Brody\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"044\", \" Jennifer Lawrence\", \"Mystique\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"044\", \" Ian McKellen\", \"Magneto\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"044\", \" Hugh Jackman\", \" Wolverine\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"044\", \" James McAvoy\", \"Charles Xavier \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"045\", \" Chris Evans\", \"Captain America/ Steve Rogers\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"045\", \" Sebastian Stan\", \"James Buchanan 'Bucky' Barnes\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"045\", \" Hugo Weaving\", \"Johann Schmidt\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"046\", \" Chris Evans\", \"Captain America/ Steve Rogers\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"046\", \" Sebastian Stan\", \"James Buchanan 'Bucky' Barnes\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"046\", \" Anthony Mackie\", \" Sam Wilson\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"047\", \" Samuel L.Jackson\", \"Frozone\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"047\", \" Craig T.Nelson \", \" Mr. Incredible\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"047\", \" Holly Hunter\", \"Elastigirl\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"048\", \" Andre Dussollier\", \" Raoul Nordling\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"048\", \" Niels Arestrup \", \" General von Choliz\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"049\", \" Gil Bellows\", \"Cameron\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"049\", \" Rachel Blacnhard\", \"Adeline\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"050\", \" Hugh Jackman\", \"Jean Valjean\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"050\", \" Russel Crowe\", \"Javert\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"050\", \" Amanda Seyfried\", \"Cosette\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"051\", \" Amitabh Bachchan\", \" Yashvardhan Raichand\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"051\", \" Shah Rukh Khan\", \"Rahul Raichand\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"051\", \" Kajol\", \"Anjali Sharma\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"052\", \" Shah Rukh Khan\", \"Aman Mathur\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"052\", \" Preity Zinta \", \" Naina Catherine Capur\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"052\", \" Saif Ali Khan\", \"Rohit\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"053\", \" Jennifer Lawrence\", \"Mystique\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"053\", \" Michael Fassbender\", \"Erik Lehnsherr\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"053\", \" James McAvoy\", \"Charles Xavier \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"053\", \" Nicholas Hoult\", \"Hank/Beast\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"054\", \" Ryan Reynolds\", \"Wade Wilson\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"054\", \" Liev Schreiber\", \"Victor Creed\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"054\", \" Hugh Jackman\", \" Wolverine\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"055\", \" Rebecca Romijn\", \"Mystique\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"055\", \" Ian McKellen\", \"Magneto\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"055\", \" Hugh Jackman\", \" Wolverine\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"055\", \" Patrick Stewart\", \"Charles Xavier \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"056\", \" Rebecca Romijn\", \"Mystique\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"056\", \" Ian McKellen\", \"Magneto\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"056\", \" Hugh Jackman\", \" Wolverine\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"056\", \" Patrick Stewart\", \"Charles Xavier \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"057\", \" Dwayne Johnson\", \" Herkules\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"057\", \" Irina Shayk\", \"Megara\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"057\", \" John Hurt\", \"Cotys\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"058\", \" Rebecca Romijn\", \"Mystique\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"058\", \" Ian McKellen\", \"Magneto\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"058\", \" Hugh Jackman\", \" Wolverine\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"058\", \" Patrick Stewart\", \"Charles Xavier \")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"059\", \" Tom Cruise\", \"Jack\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"059\", \" Morgan Freeman\", \"Beech\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"059\", \" Olga Kurylenko\", \"Julia\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"060\", \" Bruce Willis \", \"Old Joe\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"060\", \" Joseph Gordon-Levitt\", \"Joe\")");
		db.execSQL("INSERT INTO "+TABLE_CAST+" VALUES (\"060\", \" Paul Dano\", \"Seth\")");

		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"001\", \"Saturn Award Best Special Effects\", 2000)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"001\", \"Satrun Award Best Costumes\", 2000)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"002\", \"Saturn Award Best Special Effects\", 2003)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"002\", \"Saturn Award Best Costumes\", 2003)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"003\", \"Saturn Award Best science Fiction Film\", 2006)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"003\", \"Saturn Award Best Music\", 2006)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"004\", \"Oscar Best archievement in Makeup\", 2010)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"004\", \"Saturn Award Best Makeup\", 2010)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"005\", \"Britannia Award Best British Actor of the Year\", 2013)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"006\", \"Saturn Award Best Actress\", 2013)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"007\", \"Saturn Award Best science Fiction Film\", 2014)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"007\", \"Saturn Award Best Director \", 2014)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"008\", \"Czech Lion for Best Foreign Film \", 2013)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"008\", \"Japanese Best Foreign Film\", 2013)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"009\", \"Saturn Award Best Horror Film\", 2003)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"009\", \"Saturn Award Best Actress\", 2003)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"012\", \"Saturn Award Best Costumes\", 1983)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"013\", \"Saturn Award Best Production Design\", 2011)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"013\", \"Saturn Award Best Actor\", 2011)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"017\", \"Saturn Award Best Horror Film\", 2001)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"020\", \"Golden Globe Best Performance by a Actress in a Motion Picture\", 1991)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"023\", \"Oscar Best Music, Original Song\", 1988)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"023\", \"Golden Globe Best Original Song \", 1988)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"029\", \"Oscar Best Art Direction-Set Decoration\", 2002)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"029\", \"Oscar Best Costume Design\", 2002)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"029\", \"Golden Globe Best motion Picture \", 2002)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"030\", \"Oscar Best Achievement in Production Design\", 2014)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"030\", \"Oscar Best Achievement in Costume Design\", 2014)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"031\", \"Oscar Best Costume Design - Black and White\", 1960)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"031\", \"Golden Globe Best Motion Picture - Comedy\", 1960)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"031\", \"Golden Globe Best Motion Picture Actress/Actor - Musical/Comedy\", 1960)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"035\", \"Bambi Best Actor-International\", 1967)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"036\", \"Oscar Best Achievement in Cinematography \", 2010)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"036\", \"Oscar Best Achievement in Visual Effects\", 2010)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"036\", \"Oscar Best Achievement in Art Direction\", 2010)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"036\", \"Golden Globe Best Director - Motion Picture\", 2010)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"036\", \"Golden Globe Best Motion Picture - Drama\", 2010)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"039\", \"Oscar Best Animated Feature\", 2003)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"039\", \"Saturn Award Best Animated Film\", 2003)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"040\", \"Saturn Award Best Home Video Release\", 1997)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"042\", \"Oscar Best Motion Picture of the Year\", 2011)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"042\", \"Oscar Best Performance by an Actor in a Leading Role\", 2011)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"042\", \"Oscar Best Achievement in Directing\", 2011)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"042\", \"Oscar Best Writing, Original Screenplay\", 2011)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"047\", \"Oscar Best Achievement in Sound Editing\", 2005)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"047\", \"Oscar Best Animated Feature Film of the Year\", 2005)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"047\", \"Saturn Award Best Animated Film\", 1994)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"050\", \"Oscar Best Performance by an Actress in a Supporting Role\", 2013)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"050\", \"Oscar Best Achievement in Makeup and Hairstyling\", 2013)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"050\", \"Oscar Best Achievement in Sound Mixing\", 2013)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"053\", \"Saturn Award Best Make-Up\", 2012)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"055\", \"Saturn Best Science Fiction Film\", 2003)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"056\", \"Saturn Award Best Science Fiction Film\", 2001)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"056\", \"Saturn Award Best Writing\", 2001)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"056\", \"Saturn Award Best movie ever\", 2001)");
		db.execSQL("INSERT INTO "+TABLE_AWARDS+" VALUES (\"058\", \"Saturn Award Best Supporting Actress\", 2007)");

		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"001\", \"002\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"001\", \"003\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"003\", \"002\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"004\", \"005\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"001\", \"004\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"002\", \"004\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"003\", \"004\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"001\", \"005\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"002\", \"005\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"003\", \"005\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"006\", \"007\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"012\", \"013\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"014\", \"015\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"014\", \"016\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"015\", \"016\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"014\", \"017\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"017\", \"018\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"017\", \"019\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"018\", \"019\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"023\", \"024\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"025\", \"026\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"025\", \"027\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"027\", \"028\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"034\", \"035\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"036\", \"037\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"036\", \"038\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"037\", \"038\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"039\", \"040\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"044\", \"053\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"044\", \"054\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"044\", \"055\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"044\", \"056\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"044\", \"058\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"053\", \"054\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"053\", \"055\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"053\", \"056\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"053\", \"058\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"054\", \"055\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"054\", \"056\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"054\", \"058\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"055\", \"056\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"055\", \"058\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"056\", \"058\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"045\", \"046\")");
		db.execSQL("INSERT INTO "+TABLE_RELATED+" VALUES (\"051\", \"052\")");
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
		Cursor cur2 = db.rawQuery("SELECT * FROM "+TABLE_USERS, null);
		cur2.moveToFirst();
		while( !cur2.isAfterLast() ){
			cur2.moveToNext();
		}
		boolean login = cursor.getCount()==1;
		if(login){
			// update last access time into database
			SQLiteDatabase wdb = this.getWritableDatabase();
			wdb.execSQL("UPDATE "+TABLE_USERS+" SET "+USERS_LASTACCESS+" = "+
					Long.toString(currentTime.getTimeInMillis()/10000)+" WHERE "+USERS_NAME+
					" = \""+userName+"\";");
		}
		return login;
	}

	public boolean newUser(String userName, String password, Date birthday){
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
		db.execSQL("INSERT INTO "+TABLE_USERS+" VALUES (\""
				+userName+"\", \""+formatter.format( birthday )+
				"\", \""+password+"\", \""+Long.toString(currentTime.getTimeInMillis()/10000)+"\");");
		return true;
	}

	public Date getUserBirthday(String name){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( true,
				TABLE_USERS, 
				new String[] {USERS_BIRTHDAY},
				USERS_NAME+" = \""+name+"\"",
				null, null, null, null, null);
		cursor.moveToFirst();
		try{
			return  formatter.parse( cursor.getString(0) );
		}
		catch(Exception error){
			return null;
		}
	}


	public void setUserAge(String name, Date birthday){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE "+TABLE_USERS+" SET "+
				USERS_BIRTHDAY+" = "+ formatter.format(birthday) + 
				" WHERE " + USERS_NAME + " = \""+name+"\";");
	}

	/**
	 * Returns the name of the user who last accessed the application
	 * @return a String containing the name of the user, null if no user is in the DB
	 */
	public String getLastUserName(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT "+USERS_NAME+" FROM "+TABLE_USERS+
				" ORDER BY "+USERS_LASTACCESS+" DESC;", null);
		Cursor cur2 = db.rawQuery("SELECT "+USERS_NAME+","+USERS_LASTACCESS+ " FROM "+
				TABLE_USERS+" ORDER BY "+USERS_LASTACCESS+" DESC;", null);
		cur2.moveToFirst();
		while(!cur2.isAfterLast()){
			Log.e("YEK YEK", cur2.getString(0)+" >>> "+Integer.toString(cur2.getInt(1)));
			cur2.moveToNext();
		}
		if(cur.getCount() < 1){
			return null;
		}
		cur.moveToFirst();
		return cur.getString(0); // return only first entry!
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


	/**
	 * This fills in some information about the movie (only quick access movie data)
	 * @param movie the movie whose data to extract
	 * @return an object holding the data
	 */
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
		cur.moveToFirst();
		String title = cur.getString(0);
		String director = cur.getString(1);
		int year = cur.getInt(2);

		// get the interest 
		Interest interest = Interest.NOINTEREST;
		cur = db.rawQuery("SELECT "+INTEREST_INTEREST+" FROM "+TABLE_INTEREST+
				" WHERE "+ INTEREST_INTEREST + "= ?", new String[] {movie.getID()});
		if(cur.getCount() > 0){
			cur.moveToFirst();
			interest = Interest.values()[ cur.getInt(0) ];
			if(interest == null){
				// cas d'erreur bizarre
				interest = Interest.NOINTEREST;
			}
		}
		// otherwise, build value
		cur.moveToFirst();
		MovieQuickData result =  new MovieQuickData(
				title,
				director,
				year,
				interest
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
		// there is a trick here: we can use update ONLY if an interest was set before...
		// therefore, we have to check if the movie has an interest set
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT 1 FROM "+TABLE_INTEREST+" WHERE "+INTEREST_MOVIE+" = ?", new String[]{movie.getID()});
		boolean alreadyExists = cur.getCount() >0;
		db.close();
		SQLiteDatabase dbw = this.getWritableDatabase();
		if(alreadyExists){
			dbw.execSQL("UPDATE "+TABLE_INTEREST+" SET "+INTEREST_INTEREST+
					" = ? WHERE " +INTEREST_MOVIE+ " = ? AND "+INTEREST_USER+" = ? ;",
					new String[] {Integer.toString(i), movie.getID(), Application.getUser().getName()});
		}
		else{
			dbw.execSQL("INSERT INTO "+TABLE_INTEREST+" VALUES (?,?,?)",
					new String[] {movie.getID(), Application.getUser().getName(), Integer.toString(i)} );
		}
		dbw.close();
	}


	private static final int MAX_MOVIES_MATCH = 10;


	// search

	public ArrayList<Movie> Search_Movie(String query){
		return Search_Movie(query, MAX_MOVIES_MATCH);
	}
	
	/**
	 * Search the database with the given string as a query.
	 * This searches first for the name, then the year, then the director
	 * @param query: the query, that is (approximately) the title or the director, or (exactly) the year of the movie
	 * @param nMovies: number of movies to look for, per category
	 * @return a list, possibly empty, of movies
	 * TODO: add features
	 */
	public ArrayList<Movie> Search_Movie(String query, int nMovies){
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
			Cursor cur = db.rawQuery("SELECT DISTINCT "+MOVIES_ID+
					" FROM "+ TABLE_MOVIES+
					" WHERE "+MOVIES_YEAR+" = ?", 
					new String[] { query });
			cur.moveToFirst(); int iter =0;
			while(!cur.isAfterLast() && iter<nMovies) {
				Movie mov = Application.getMovie(cur.getString(0));
				mov.getQuickData(this);
				result.add(mov);
				cur.moveToNext();
				iter++;
			}
		}
		// search for name
		Cursor cur = db.rawQuery("SELECT DISTINCT "+MOVIES_ID+
				" FROM " + TABLE_MOVIES +
				" WHERE "+MOVIES_NAME+" LIKE '%"+query+"%'",
				null
				);
		// add movies found to value
		cur.moveToFirst(); int iter =0;
		while(!cur.isAfterLast() && iter<nMovies) {
			Movie mov = Application.getMovie(cur.getString(0));
			mov.getQuickData(this);
			result.add(mov);
			cur.moveToNext();
			iter++;
		}
		// search for director
		cur = db.rawQuery("SELECT DISTINCT "+MOVIES_ID+
				" FROM " + TABLE_MOVIES +" "+
				" WHERE "+MOVIES_DIRECTOR+ " LIKE '%"+query+"%'",
				null
				);
		// add movies found to value
		cur.moveToFirst(); iter =0;
		while(!cur.isAfterLast() && iter<nMovies) {
			Movie mov = Application.getMovie(cur.getString(0));
			mov.getQuickData(this);
			result.add(mov);
			cur.moveToNext();
			iter++;
		}
		db.close();
		// remove the duplicates
		Set<Movie> allitems = new LinkedHashSet<Movie>(result);
		result.clear();
		result.addAll( allitems );
		// return list
		return result;
	}

	public Movie getSuggestionMovie(){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> possibilities = new ArrayList<String>();
		Cursor cur = db.rawQuery("SELECT "+TABLE_MOVIES+"."+MOVIES_ID+" FROM "+
				TABLE_GENRE+","+TABLE_INTEREST+","+TABLE_MOVIES+" WHERE "+
				TABLE_MOVIES+"."+MOVIES_ID+" = "+TABLE_GENRE+"."+GENRE_MOVIE+" AND "+
				TABLE_INTEREST+"."+INTEREST_MOVIE+" = "+TABLE_GENRE+"."+GENRE_MOVIE+
				" AND "+TABLE_GENRE+"."+GENRE_GENRE+" = ? "+" AND "+
				TABLE_INTEREST+"."+INTEREST_INTEREST+" = ?; "/*+" AND "+
				TABLE_MOVIES+"."+ MOVIES_RATING+" > 0.6;"*/, // works, without this line ;)
				new String [] { Integer.toString(UserSuggestionGuesser.getUserGenre().ordinal()), 
								Integer.toString(Interest.NOINTEREST.ordinal())} );
		if(cur.getCount()>0){
			cur.moveToFirst();
			int iter=0;
			while(!cur.isAfterLast() && iter<MAX_MOVIES_MATCH) {
				possibilities.add(cur.getString(0));
				cur.moveToNext();
				iter++;
			}
		}
		if(possibilities.size() > 0){
			Random r = new Random();
			int pos = r.nextInt(possibilities.size());
			return Application.getMovie(possibilities.get(pos));
		}
		else{
			Log.w("SUGGEST MOVIE", "Empty possibilities list");
			return this.getRandomMovie();
		}
	}




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
