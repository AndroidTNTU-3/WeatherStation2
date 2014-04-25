package com.unrealedz.wstation.fragments;

import com.unrealedz.wstation.R;
import com.unrealedz.wstation.bd.DataWeekHelper;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentDay extends Fragment {
	
	TextView city;
	TextView region;
	TextView temperatureMin;
	TextView temperatureMax;
	ImageView imageView;
	Context context;
	
	long id;
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_day, container, false);
		
		city = (TextView) v.findViewById(R.id.tvCity);
		region = (TextView) v.findViewById(R.id.tvRegion);
		temperatureMin = (TextView) v.findViewById(R.id.tvDayTmin);
		temperatureMax = (TextView) v.findViewById(R.id.tvDayTmax);
		imageView = (ImageView) v.findViewById(R.id.imageDay);
		context = container.getContext();
	    return v;
	  }
	
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);{
	    	DataWeekHelper dataWeekHelper = new DataWeekHelper(context);
	    	//dataWeekHelper.getCursorDay(long id);
	    }
	}
	
	public void setId(long id){
		this.id = id;
	}
}
