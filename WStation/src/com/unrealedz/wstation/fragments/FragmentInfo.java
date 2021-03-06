

package com.unrealedz.wstation.fragments;

import com.unrealedz.wstation.R;
import com.unrealedz.wstation.R.id;
import com.unrealedz.wstation.R.layout;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DbHelper;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentInfo extends Fragment {
	
	TextView tvLastUpdate;
	Cursor cursor;
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_info, container, false);
		tvLastUpdate = (TextView) view.findViewById(R.id.tvLastUpdate);
		
		return view;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	DataDayHelper ddh = new DataDayHelper(this.getActivity());
	cursor = ddh.getCursor(DbHelper.CURRENT_DAY_TABLE);
	
	if(cursor.getCount() != 0) setData(cursor);
	
	}
	
	public void setData(Cursor cursor) {
		
		cursor.moveToFirst();
		tvLastUpdate.setText(cursor.getString(cursor.getColumnIndex(DbHelper.LAST_UPDATED)));
		Log.i("DEBUG LAST", cursor.getString(cursor.getColumnIndex(DbHelper.LAST_UPDATED)));

	}
	
	public void onDestroy() {
	    super.onDestroy();
		cursor.close();;
	  }

}
