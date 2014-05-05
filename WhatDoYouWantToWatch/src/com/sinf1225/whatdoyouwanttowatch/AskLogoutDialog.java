package com.sinf1225.whatdoyouwanttowatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * This dialog asks if the user wants to log out of the application
 *
 */
public class AskLogoutDialog extends DialogFragment {

	public Context context;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		context = getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Do you want to log out?");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing
            }
        });
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Application.fullLogout(context);
            }
        });
		return builder.create();
	}

}
