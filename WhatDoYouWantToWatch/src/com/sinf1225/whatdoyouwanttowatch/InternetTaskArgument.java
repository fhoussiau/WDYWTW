package com.sinf1225.whatdoyouwanttowatch;


/**
 * Class holding arguments for the internet task
 */
public class InternetTaskArgument {

	public SearchActivity context;
	public String query;
	
	public InternetTaskArgument(SearchActivity context, String query) {
		this.context = context;
		this.query = query;
	}

}
