package com.unrealedz.wstation;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import com.unrealedz.wstation.LocationLoader.LocationLoaderCallBack;
import com.unrealedz.wstation.bd.DataCityDbInfoHelper;
import com.unrealedz.wstation.bd.DataCityHelper;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.CitiesDB;
import com.unrealedz.wstation.entity.CityDB;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.parsers.CityDbParser;
import com.unrealedz.wstation.parsers.WeatherParser;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkLoader extends AsyncTask<String, Void, String>{
	
	Forecast forecast;
	CityDB cityDB;
	
	Object ob;
	
	Context context;
	String nameLocation;
			
	public static interface LoaderCallBack{
		public void setLocationInfo();
		public void setCurrentDay();
		public void setWeekList();
		public void onLoadCityDB();
	}
	
	LoaderCallBack loaderCallBack;
	
	NetworkLoader(Context context){
		this.context = context;	  
	}
	
	NetworkLoader(){
		
	}

	@Override
	protected String doInBackground(String... params) {
		
		HttpClient client = new DefaultHttpClient();
		
		HttpGet httpRequest = null;
		InputStream stream = null;

        httpRequest = new HttpGet(params[1]);
        
        String keyLoader = params[0];
        
        try {
			HttpResponse response = (HttpResponse) client.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			
			stream = entity.getContent();
			try {
				if (keyLoader.equals(MainActivity.GET_CITY_DB)) {
					
				CitiesDB citiesDB= new CityDbParser().parse(stream);	
				
				DataCityDbInfoHelper dbInfoHelper = new DataCityDbInfoHelper(context);
		    	  DataCityHelper dataCity = new DataCityHelper(context);
		    	  Cursor cursor = dbInfoHelper.getCursor(DbHelper.CITY_DB_INFO_TABLE);
		    	  
		    	  if (cursor.getCount() != 0){
		    		  cursor.moveToFirst();
		    		  String lastUpdated = cursor.getString(cursor.getColumnIndex(DbHelper.LAST_UPDATED));
		    	  
		    		  if (!lastUpdated.equals((citiesDB).getLastUpdated())) {
		    			  dbInfoHelper.cleanOldRecords();
		    			  dbInfoHelper.insertCitiesDB(citiesDB);
		    			  dataCity.cleanOldRecords();
		    			  dataCity.insertCitiesDB(citiesDB);
		    		  }
		    	  } else{
		    		  dbInfoHelper.cleanOldRecords();
					  dbInfoHelper.insertCitiesDB(citiesDB);
					  dataCity.cleanOldRecords();
					  dataCity.insertCitiesDB(citiesDB);
					  Log.i("DEBUG INFO", "update");
		    	  }
				  Log.i("DEBUG INFO", "Not update");
								
				} else if(keyLoader.equals(MainActivity.GET_FORECAST)){
					
					Forecast forecast = new WeatherParser().parse(stream);
					
					DataHelper dh = new DataHelper(context);
					DataDayHelper ddh = new DataDayHelper(context);
					DataWeekHelper dwh = new DataWeekHelper(context);
			      
					dh.cleanOldRecords();
					ddh.cleanOldRecords();
					dwh.cleanOldRecords();
			      
					dh.insertCityItem(forecast);
					ddh.insertDayItem(forecast);
					dwh.insertDayItem(forecast);
				}
							
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        
		return keyLoader;
	}
	
	@Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      
      if (result.equals(MainActivity.GET_CITY_DB)){
    	  
    	  if (loaderCallBack != null){
    			 loaderCallBack.onLoadCityDB();
    	    	}    	          

      } else if(result.equals(MainActivity.GET_FORECAST)){
    	  
    	  if (loaderCallBack != null){
        	  loaderCallBack.setLocationInfo();
        	  loaderCallBack.setWeekList();
          	} 		  
      }
	}	
	
	public void setLoaderCallBack(LoaderCallBack loaderCallBack) {
		this.loaderCallBack = loaderCallBack;
	}	     

}
