package com.sinf1225.whatdoyouwanttowatch;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter {
	// Une liste de personnes
	private ArrayList<Movie> MovieList;
	    	
	//Le contexte dans lequel est present notre adapter
	private Context mContext;
	    	
	//Un mecanisme pour gerer l'affichage graphique depuis un layout XML
	private LayoutInflater mInflater;


	public MovieAdapter(Context context, ArrayList<Movie> ListMovie) {
		mContext = context;
		MovieList = ListMovie;
		mInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return MovieList.size();
	}

	public Object getItem(int position) {
		return MovieList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layoutItem;
		//(1) : Reutilisation des layouts
		if (convertView == null) {
			//Initialisation de notre item a partir du  layout XML "quickmovie_layout.xml"
			layoutItem = (LinearLayout) mInflater.inflate(R.layout.quickmovie_layout, parent, false);
		} else {
			layoutItem = (LinearLayout) convertView;
		}
 
		//(2) : recuperation des TextView de notre layout      
		TextView Movie_Title = (TextView)layoutItem.findViewById(R.id.Movie_Title);
		TextView Movie_Director = (TextView)layoutItem.findViewById(R.id.Movie_Director);
		TextView Movie_Year = (TextView)layoutItem.findViewById(R.id.Movie_Year);
		
		//(3) : Renseignement des valeurs 
		MovieQuickData quickdat = MovieList.get(position).getQuickData(mContext);
		Movie_Title.setText(quickdat.title);
		Movie_Director.setText(quickdat.director);
		Movie_Year.setText(Integer.toString(quickdat.year));

		//On retourne l'item cree.
		return layoutItem;
}


}
