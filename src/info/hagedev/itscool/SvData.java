package info.hagedev.itscool;


import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;


//svData class records for list items.
public class SvData{
	private String arrival = new String();
	private String depature = new String();
//	private String url = new String();
	private MyHTMLParser parser;
	private ArrayList<myCalendar> timetable = new ArrayList<myCalendar>();

	SvData(String _arrival, String _depature, String _url){
		arrival = _arrival;
		depature = _depature;
		parser = new MyHTMLParser(_url);
		setTimetable();
	}

	void setArrivals(String _arrival){
		arrival = _arrival;
	}
	void setDepature(String _depature){
		depature = _depature;
	}
	void setTimetable(){
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int day = now.get(Calendar.DAY_OF_WEEK);


		for(int i=hour; i<24; i++) timetable.addAll(parser.getSchedule(i, this.detSchedule(day)));
		Log.d("count", String.valueOf(timetable.size()-1));
		now.add(Calendar.DAY_OF_MONTH, 1);
		day = now.get(Calendar.DAY_OF_WEEK);
		for(int i=5; i<hour; i++)  timetable.addAll(parser.getSchedule(i, this.detSchedule(day)));
		Log.d("count", String.valueOf(timetable.size()-1));
		//for(int i=0; i<timetable.size()-1; i++) Log.d("setTimetable()", arrival+"->"+depature+" : "+timetable.get(i).get(Calendar.HOUR_OF_DAY)+"|"+timetable.get(i).get(Calendar.MINUTE));
	}

	MyHTMLParser.SCHEDULE detSchedule(int day){
		MyHTMLParser.SCHEDULE schedule;
		if(day == Calendar.SUNDAY) schedule = MyHTMLParser.SCHEDULE.HOLIDAY;
		else if(day == Calendar.SATURDAY) schedule = MyHTMLParser.SCHEDULE.SATURDAY;
		else schedule = MyHTMLParser.SCHEDULE.WEEKDAY;
		return schedule;
	}

	void setNext(ArrayList<myCalendar> calendar){
		try{
			for(int i=0; i<timetable.size(); i++){
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	ArrayList<myCalendar> getTimetable(){
		return timetable;
	}
	String getArrivals(){
		return arrival;
	}
	String getDepature(){
		return depature;
	}

}
