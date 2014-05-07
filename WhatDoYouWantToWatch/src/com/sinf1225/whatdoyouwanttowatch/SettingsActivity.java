package com.sinf1225.whatdoyouwanttowatch;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Settings activity: part of the UI focused on Settings
 */
public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.preferences);
	}

}
