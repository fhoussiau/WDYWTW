package com.sinf1225.whatdoyouwanttowatch;

import android.content.SearchRecentSuggestionsProvider;

/**
 * This class allows for suggestion to be made during searches
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
