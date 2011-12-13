package info.hagedev.itscool;

import android.util.Log;


import java.util.ArrayList;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.*;
import org.jsoup.nodes.*;

public class MyHTMLParser{
	private String uri = null;
	private byte[] rawdata;
	private String source;
	private Document doc;

	public enum SCHEDULE{
		WEEKDAY, SATURDAY, HOLIDAY;
	}

	MyHTMLParser(String _uri){
		uri = _uri;
		rawdata = httpRequest(uri);
		source = getSourceFromResponse(rawdata, "Shift-JIS");
		if(source != null) doc = Jsoup.parse(source);
	}

	public final ArrayList<myCalendar> getSchedule(int hour, SCHEDULE schedule){
		ArrayList<myCalendar> fragmentTable = new ArrayList<myCalendar>();
		Element rootTable = doc.select("table[width]").first();	//this select table[width] gives the first element of a timetable.
		Element unit = rootTable.select("tr[valign]").first();
		Element temp = unit;

		int tableHour = 0;

		tableHour = Integer.parseInt(unit.select("td").first().text());
		while(hour != tableHour){
			try{
				unit = unit.nextElementSibling();
				//System.out.println("tablehour:"+unit.select("td").first().text());
				tableHour = Integer.parseInt(unit.select("td").first().text());
			}
			catch(Exception e){
				System.err.println(e.getMessage());
				break;
			}
		}

		//Log.d("tableHour", "tableHour(="+ tableHour +") equals the parameter of getSchedule(hour (="+ hour +")");

		try{
			temp = unit;	//perhaps it will be something useful if I took previous unit...
			unit = unit.children().first();
			//now unit points hour field. so let it shift to diagram field.
			switch(schedule){
			case WEEKDAY:
				unit = unit.nextElementSibling();
				break;
			case SATURDAY:
				unit = unit.nextElementSibling().nextElementSibling();	//saturday's diagram is located in td tag of the third.
				break;
			case HOLIDAY:
				unit = unit.nextElementSibling().nextElementSibling().nextElementSibling();
				break;
			default:
				break;
			}

			//System.out.println(unit.text());

			//now let it extract!
			unit = unit.children().select("td").first();

			//System.out.println(unit.text());

			while(unit != null){
				myCalendar cal = new myCalendar();
				Element minuteField = unit;
				//System.out.println(minuteField.text());
				String kigo;
				int minute;

				kigo = minuteField.children().select("tr").first().text();
				minute = Integer.parseInt(minuteField.children().select("tr").first().nextElementSibling().text());
				cal.setCalendar(hour, minute, false, false);
				fragmentTable.add(cal);

				//Log.d("timeTable", tableHour+"|"+minute+"("+kigo+")");
				unit = minuteField.nextElementSibling();
			}

		}
		catch(Exception e){
			System.err.println(e.getStackTrace().toString());
		}


		//Log.d("timeTable", "now you can get timetable for each hours");

		return fragmentTable;
	}

	public final String getSource(){
		return source;
	}
	public final byte[] getRawdata(){
		return rawdata;
	}

	private byte[] httpRequest(String _uri){
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpGet get = new HttpGet(_uri);

		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 8000);

		try{
			HttpResponse response = client.execute(get);
			int status = response.getStatusLine().getStatusCode();

			if(status != HttpStatus.SC_OK){
				Log.e("Connection.download", "failed to download html.");
			}
			else if(status < 400){
				Log.d("Connection.downalod", "downloaded a html.");
				return EntityUtils.toByteArray(response.getEntity());
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("[Exception]", e.toString()+" from httpRequest()");
			return null;
		}
		return null;
	}

	private final String getSourceFromResponse(byte[] rawdata, String charsetName){
		String source;
		if(rawdata != null){
			try{
				source = new String(rawdata, charsetName);
				source.replaceAll("\t", "");
//				System.out.println(source);
				return source;
			}
			catch(Exception e){
				System.err.println(e.getStackTrace().toString());
				return null;
			}
		}
		return null;
	}



}
