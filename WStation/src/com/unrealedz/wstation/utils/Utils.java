package com.unrealedz.wstation.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

public class Utils {
	
		
	public static String getStringDate(String date){
		
		Date mdate = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			mdate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TimeZone MyTimezone = TimeZone.getDefault();
		
		GregorianCalendar calendar = new GregorianCalendar(MyTimezone);
		calendar.setTime(mdate);
		
		int mday = calendar.get(Calendar.DAY_OF_MONTH);
		String dayOfWeek = DateUtils.getDayOfWeekString(calendar.get(Calendar.DAY_OF_WEEK), DateUtils.LENGTH_LONG);
		String month = DateUtils.getMonthString(calendar.MONTH, DateUtils.LENGTH_LONG);
		
		String dateWeek = dayOfWeek + " " + mday + " " + month;
		return dateWeek;
	}
		

}
