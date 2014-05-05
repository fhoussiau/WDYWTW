package com.sinf1225.whatdoyouwanttowatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * This dialog allows the user to change the interest for a movie
 *
 */
public class SetInterestDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String[] interests = new String[ Interest.values().length];
		for(int i=0; i<Interest.values().length; i++){
			interests[i] = Interest.values()[i].toString();
		}
		builder.setItems(interests, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DisplayMovieActivity act = (DisplayMovieActivity) getActivity();
					act.setMovieInterest( which );
				}
			}).setTitle("Pick your Interest");
		return builder.create();
	}
}
