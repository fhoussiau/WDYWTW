package com.sinf1225.whatdoyouwanttowatch;

// enum containing different values for genre
public enum Genre {
	THRILLER,
	COMEDY,
	TRAGEDY,
	CHILDREN,
	SCIENCEFICTION,
	HORROR,
	ADULT;
	
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
			default: throw new IllegalArgumentException();
		}
	}
}

//TODO: add (a lot) more