package com.sinf1225.whatdoyouwanttowatch;

// enum containing different values for genre
public enum Genre {
	THRILLER,  // 0
	COMEDY,    // 1
	TRAGEDY,   // 2
	CHILDREN,  // 3
	SCIENCEFICTION, // 4
	HORROR, // 5
	ADULT,  // 6
	ACTION, // 7
	ADVENTURE, // 8
	FANTASY,  // 9
	MYSTERY,  // 10
	ROMANCE,  // 11
	DRAMA,    // 12
	MUSICAL,  // 13
	WESTERN,  // 14
	ANIMATED, // 15
	SUPERHERO,// 16 
	HISTORICAL, // 17
	CRIME,  // 18
	BOLLYWOOD, // 19
	BIOGRAPHY, // 20
	DANCE; // 21
	
	@Override
	public String toString() {
		switch(this) {
			case THRILLER: return "thriller";
			case COMEDY: return "comedy";
			case TRAGEDY: return "tragedy";
			case CHILDREN: return "children";
			case SCIENCEFICTION: return "science-fiction";
			case HORROR: return "horror";
			case ADULT: return "adult ;)";
			case ACTION: return "action";
			case ADVENTURE: return "adventure";
			case FANTASY: return "fantasy";
			case MYSTERY: return "mystery";
			case ROMANCE: return "romance";
			case DRAMA: return "drama";
			case MUSICAL: return "musical";
			case WESTERN: return "western";
			case ANIMATED: return "animated";
			case SUPERHERO: return "super-hero";
			case HISTORICAL: return "historical";
			case CRIME: return "crime";
			case BOLLYWOOD: return "Bollywood";
			case BIOGRAPHY: return "biography";
			case DANCE: return "dance";
			default: throw new IllegalArgumentException();
		}
	}
}