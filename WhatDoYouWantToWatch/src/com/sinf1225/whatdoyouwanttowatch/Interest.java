package com.sinf1225.whatdoyouwanttowatch;

// enum containing the different values for interest
// the order of the interests is such that i1 < i2 ==> i2 is better than i1
public enum Interest {
	HATED,
	NOTLIKED,
	INDIFFERENT,
	NOINTEREST, // this is the default: the user never specified anything for this movie
	NOTWATCHED, // the user has acknowledged never seeing this movie, but it not very interested by it
	WILLNOTWATCHIT, // the user has never seen this movie, and doesn't want to (i. e. Twilight)
	WANTTOWATCHIT, // the user wants to watch this movie, and hasn't yet
	LIKED,
	LOVED,
	FAVORITE;
	
	@Override
	public String toString(){
		switch(this){
			case NOINTEREST: return "No Interest";
			case NOTWATCHED: return "Not Watched";
			case WILLNOTWATCHIT: return "Don't want to watch it!";
			case WANTTOWATCHIT: return "Want to watch it!";
			case HATED: return "Hated it";
			case NOTLIKED: return "Didn't like it";
			case INDIFFERENT: return "Left me indifferent";
			case LIKED: return "Liked it";
			case LOVED: return "Loved it";
			case FAVORITE: return "Favorite!";
			default:
				return "(...)";
		}
	}
	
	public int toColor(){
		switch(this){
			case NOINTEREST: return 0xFFBBBBBB; // grey
			case NOTWATCHED: return 0xFF556699; // blueish grey
			case WILLNOTWATCHIT: return 0xFF660000; // dark red
			case WANTTOWATCHIT: return 0xFFFFFF00; // yellow
			case HATED: return 0xFFFF0000; // red
			case NOTLIKED: return 0xFFFF9900; // reddish orange
			case INDIFFERENT: return 0xFF999999; // darker grey
			case LIKED: return 0xFF00FF00; // green
			case LOVED: return 0xFF00DDDD; // blueish green (yuk)
			case FAVORITE: return 0xFFFF69B4; // "hot pink" (according to the internet)
			default: return 0x00000000;
		}
	}
	

}
