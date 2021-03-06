package com.unrealedz.wstation;


import com.unrealedz.wstation.LocationLoader.LocationLoaderCallBack;
import com.unrealedz.wstation.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.fragments.FragmentCurrent;
import com.unrealedz.wstation.fragments.FragmentInfo;
import com.unrealedz.wstation.fragments.FragmentList;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity implements LoaderCallBack, LoaderCallbacks<Cursor>, LocationLoaderCallBack{
	
	FragmentCurrent fragCurrent;
	FragmentList fragList;
	FragmentInfo fragInfo;
	FragmentTransaction fTrans;
	NetworkLoader nLoader;
	NetworkLoader cLoader;
	LocationLoader locationLoader;
	
	
	String url = "http://xml.weather.co.ua/1.2/forecast/23?dayf=5&lang=uk";
	public static final String CITY_URL = "http://xml.weather.co.ua/1.2/city/?country=804";
	public static final String GET_CITY_DB = "getCityDB";
	public static final String GET_FORECAST = "getForecast";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        

        fragCurrent = new FragmentCurrent();
        fragList =  new FragmentList();
        fragInfo = new FragmentInfo();
        
        fragCurrent.setRetainInstance(true);
        fragList.setRetainInstance(true); 
        fragInfo.setRetainInstance(true); 
        
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragCurrent, fragCurrent);
        fTrans.add(R.id.fragList, fragList);
        fTrans.add(R.id.fragInfoUpdate, fragInfo);
        fTrans.commit();
        
             
        cityLoad();
      //locationLoader = new LocationLoader(context);
	               
    }
    
    private void cityLoad(){
    	cLoader = new NetworkLoader(this);
        cLoader.setLoaderCallBack(this); 
    	if (isOnline(getApplicationContext())) cLoader.execute(GET_CITY_DB, CITY_URL);
    }
    
    private void refresh(){
        nLoader = new NetworkLoader(this);
        nLoader.setLoaderCallBack(this); 
    	if (isOnline(getApplicationContext())) nLoader.execute(GET_FORECAST, url);
    }
    
    
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
        case R.id.action_refresh:
        	refresh();       	
            return true;       
        default:

        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void setLocationInfo() {
		
		DataHelper dh = new DataHelper(this);
	    DataDayHelper dd = new DataDayHelper(this);
	    Cursor cursorCity = dh.getCursor(DbHelper.CITY_TABLE);
	    Cursor cursorCurrent = dd.getCursor(DbHelper.CURRENT_DAY_TABLE);
	    
		fragCurrent.setData(cursorCity, cursorCurrent);
		fragInfo.setData(cursorCurrent);
		
	}

	@Override
	public void setCurrentDay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWeekList() {
        getLoaderManager().initLoader(0, null, this);
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {	
		return new MyCursorLoader(getApplicationContext());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		
		fragList.setCursor(cursor);
		//test(); 
	}
	
	public void test(){
		DataWeekHelper dwh = new DataWeekHelper(getApplicationContext());
		Cursor c = dwh.getTemperatureDay(DbHelper.WEEK_TABLE);
		while(c.moveToNext()){
			String date = c.getString(c.getColumnIndex(DbHelper.DATE));
			int tmin = c.getInt(c.getColumnIndex(DbHelper.TEMPERATURE_MIN));
			int tmax = c.getInt(c.getColumnIndex(DbHelper.TEMPERATURE_MAX));
			String pname = c.getString(c.getColumnIndex(DbHelper.PICTURE_NAME));
			Log.i("DEBUG TEMP", date + " " + tmin + " " +  tmax + pname);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadCityDB() {
		Log.i("DEBUG INFO", "In onload:");
		locationLoader = new LocationLoader(getApplicationContext());
		locationLoader.setLoaderCallBack(this);
		locationLoader.getLocation();
	}

	@Override
	public void setLocation(String nameLocation) {
		Log.i("DEBUG CUR", "COD:" + nameLocation); 
		url = "http://xml.weather.co.ua/1.2/forecast/" + nameLocation + "?dayf=5&lang=uk";
		refresh();
	}
	

    
}
