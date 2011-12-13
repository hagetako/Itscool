package info.hagedev.itscool;



import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class SelectStation extends Activity implements OnClickListener, OnItemSelectedListener{

//	private SvData data;
    private mySQLiteHelper helper;
    private SQLiteDatabase db;
    private SQLiteCursor cursor;
    private Spinner departure, arrival;
    private Button gobtn;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select);

        helper = new mySQLiteHelper(this);
        db = helper.getWritableDatabase();
        if (!db.isOpen()) {
        	Log.d("", "db is not open");
            db = this.openOrCreateDatabase("/data/data/info.hagedev.itscool/databases/timetable", SQLiteDatabase.OPEN_READWRITE, null);
         }

        gobtn = (Button)findViewById(R.id.Go);
        departure = (Spinner)findViewById(R.id.spinner_From);
        arrival = (Spinner)findViewById(R.id.spinner_To);

        departure.setPrompt("乗車するバス停を選択してください");
        arrival.setPrompt("降車するバス停を選択してください");

        gobtn.setOnClickListener(this);

        setSpinnerAdapter(departure, "Departure", "");
        departure.setOnItemSelectedListener((OnItemSelectedListener)this);
//       arrival.setOnItemSelectedListener((OnItemSelectedListener)this);
//       setSpinnerAdapter(arrival, "Arrival", "");
        db.close();

    }

	public void onClick(View v){
    	Log.d("", "button click");
        Intent intent = new Intent(this, info.hagedev.itscool.Result.class);
        String dept, arvl, q="", url="http://www.city.takatsuki.osaka.jp/new2001/bus/209_5.html";
        dept = departure.getSelectedItem().toString(); arvl = arrival.getSelectedItem().toString();

        if (!db.isOpen()) {
        	Log.d("", "db is not open");
            db = this.openOrCreateDatabase("/data/data/info.hagedev.itscool/databases/timetable", SQLiteDatabase.OPEN_READWRITE, null);
         }

        q = "SELECT TimetableURL FROM Routes WHERE Departure = '"+dept+"' AND Arrival ='"+arvl+"';";
        cursor = (SQLiteCursor)db.rawQuery(q, null);
        if(cursor.moveToFirst()){
        		url = cursor.getString(cursor.getColumnIndex("TimetableURL"));
        }
        db.close();
        cursor.close();

        Log.d("query",q);
        Log.d("departure",dept);
        Log.d("arrival", arvl);
        Log.d("url", url);


        intent.putExtra("departure", dept);
        intent.putExtra("arrival", arvl);
        intent.putExtra("url", url);
        intent.setAction(Intent.ACTION_VIEW);

        startActivityForResult(intent, 0);
    }

	public void onItemSelected(AdapterView<?> av, View v, int position, long id){
		Spinner spinner = (Spinner)av;
		String station = spinner.getSelectedItem().toString();

		if(spinner.getId() == R.id.spinner_From) setSpinnerAdapter(arrival, "Arrival", station);
		if(spinner.getId() == R.id.spinner_To) setSpinnerAdapter(departure, "Departure", station);
	}
	public void onNothingSelected(AdapterView<?> av){
		//nothing to do
	}

	private void setSpinnerAdapter(Spinner spinner, String column, String where){
        ArrayAdapter<String> adp;
        String q = "";

        if (!db.isOpen()) {
        	Log.d("", "db is not open");
            db = this.openOrCreateDatabase("/data/data/info.hagedev.itscool/databases/timetable", SQLiteDatabase.OPEN_READWRITE, null);
         }

        if(!(column == "Departure" || column == "Arrival")) column = "Departure";

        if(where.length() > 0){
        	String wh = "Arrival";
        	String sl = column;
        	Log.d("itscool", "where phrase is used");

        	if(wh == "Departure") sl = "Arrival";
        	if(wh == "Arrival") sl = "Departure";

        	q = "SELECT DISTINCT "+sl+" FROM Routes";
        	q = q + " WHERE "+wh+" = '"+ where +"'";
        	Log.d("query", q);
        	column = sl;
        }
        else q = "SELECT DISTINCT "+column+" FROM Routes";

        q = q +";";

        cursor = (SQLiteCursor)db.rawQuery(q, null);	//2nd argument is used for prepared statements.
        adp = new ArrayAdapter<String>(this, R.layout.list_item, R.id.SpinnerListItem);

        if(cursor.moveToFirst()){
        	do{
        		String s = cursor.getString(cursor.getColumnIndex(column));
            	adp.add(s);
            	Log.d("itscool", s);
        	}while(cursor.moveToNext());
        }

        adp.setDropDownViewResource(R.layout.spinnerlist);
        spinner.setAdapter(adp);
        cursor.close();
        db.close();
	}
}
