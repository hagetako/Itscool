package info.hagedev.itscool;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Result extends Activity{
	private SvData data = null;
	private ArrayList<myCalendar> timetable = new ArrayList<myCalendar>();
	String from = "", to = "", url = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.result);

    	Bundle extras = getIntent().getExtras();
    	if(extras != null){
    		from = extras.getString("departure");
    		to = extras.getString("arrival");
    		url = extras.getString("url");
    	}
    	if(from.length() > 0 && to.length() > 0 && url.length() > 0) data = new SvData(from, to, url);

        if(data != null) setScreen(data);

    }
    private void setScreen(SvData sd){
    	final float scale = getResources().getDisplayMetrics().density;
    	TableLayout tl = (TableLayout)findViewById(R.id.Timetable);
    	TextView label;
    	label = (TextView)findViewById(R.id.From);
    	label.setText(this.from);
    	label = (TextView)findViewById(R.id.To);
    	label.setText(this.to);

    	if(tl == null) Log.e("", "null");
     	timetable = sd.getTimetable();

     	int i=0;
     	while(i < timetable.size() - 1){
			TableRow tr;
			TextView tv;
			GradientDrawable green = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{0xFF8ED32B, 0xFFBBE192, 0xFFBBE192, 0xFF8ED32B});
			GradientDrawable gray = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{0xFFDDDDDD, 0xFFEEEEEE, 0xFFE0E0E0, 0xFFCCCCCC});

 			tl.setOrientation(LinearLayout.VERTICAL);

			try{
     			int hour, j=0;
     			tr = new TableRow(this);
     			tv = new TextView(this);

     			tr.removeAllViews();

    			tr.setOrientation(LinearLayout.HORIZONTAL);
				tv.setBackgroundDrawable(green);
				tv.setTextColor(Color.BLACK);
				hour = timetable.get(i).get(Calendar.HOUR_OF_DAY);
				tv.setText(Integer.toString(hour));
				tv.setHeight((int)(scale*40));
				tv.setWidth((int)(scale*40));
				tv.setGravity(Gravity.CENTER);
				tr.addView(tv);

	        	while( i+j < timetable.size() - 1 && timetable.get(i).get(Calendar.HOUR_OF_DAY) == timetable.get(i+j).get(Calendar.HOUR_OF_DAY)){
	        		if(j>0 && j%7 == 0){
	        			Log.d(String.valueOf(j),"new line.");

	        			tl.addView(tr);
	        			tl.removeAllViews();
	        			tr = new TableRow(this);

	        			tr.setOrientation(LinearLayout.HORIZONTAL);
	    				tv.setBackgroundDrawable(green);
	    				tv.setTextColor(Color.BLACK);
	    				tv.setText(" ");
	    				tv.setHeight((int)(scale*40));
	    				tv.setWidth((int)(scale*40));
	    				tv.setGravity(Gravity.CENTER);
	    				tr.addView(tv);

	        		}
					tv = new TextView(this);
					tv.setTag(timetable.get(i+j));
					tv.setOnClickListener(panelEvent);
					tv.setBackgroundColor(Color.GRAY);
					gray.setStroke(1, Color.GRAY);
					tv.setBackgroundDrawable(gray);
					tv.setTextColor(Color.BLACK);
					tv.setText(Integer.toString(timetable.get(i+j).get(Calendar.MINUTE)));
					tv.setHeight((int)(scale*40));
					tv.setWidth((int)(scale*40));
					tv.setGravity(Gravity.CENTER);
					tr.addView(tv);
	        		j++;
	     		}
				tl.addView(tr);
				i += j;
     		}
     		catch(Exception e){
     			e.printStackTrace();
//     			Log.e("", e.toString());
     			finish();
     		}
     	}
    }

    private OnClickListener panelEvent = new OnClickListener(){
    	View prev = null;
    	public void onClick(View v){
    		selectTableItem(v);
    		if(prev != null) deselectTableItem(prev);
        	prev = v;
    	}
    };

    private void selectTableItem(View v){
		myCalendar cal = null;
		TextView tv = null;
		DigitalClock clock = (DigitalClock)findViewById(R.id.Clock);
		String s, n[];

		GradientDrawable green = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{0xFF8ED32B, 0xFFBBE192, 0xFFBBE192, 0xFF8ED32B});

    	v.setBackgroundDrawable(green);
    	cal = (myCalendar)v.getTag();

    	tv = (TextView)findViewById(R.id.HourLabelText);
   		tv.setText(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
    	tv = (TextView)findViewById(R.id.MinuteLabelText);
    	tv.setText(Integer.toString(cal.get(Calendar.MINUTE)));

    	s = clock.getText().toString();
    	n = s.split(":");
    	int sminute = Integer.parseInt(n[0]) * 60 + Integer.parseInt(n[1]);
    	int nminute = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
    	int dif = nminute - sminute;
    	if(dif < 0) dif += 720;

    	tv = (TextView)findViewById(R.id.countdown);
    	tv.setText(String.valueOf(dif));
    }

    private void deselectTableItem(View v){
    	GradientDrawable gray = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{0xFFDDDDDD, 0xFFEEEEEE, 0xFFE0E0E0, 0xFFCCCCCC});
    	v.setBackgroundDrawable(gray);
    }
}
