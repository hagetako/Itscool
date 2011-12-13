package info.hagedev.itscool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mySQLiteHelper extends SQLiteOpenHelper{
    private final static String DB_NAME = "timetable";
    private final static int DB_VER = 110;
    private Context mContext;

	public mySQLiteHelper(Context c){
		super(c, DB_NAME, null, DB_VER);
		mContext = c;
	}
	@Override
	public void onCreate(SQLiteDatabase db){
		try{
			//db.execSQL("CREATE TABLE Routes(RouteID integer PRIMARY KEY, Departure text NOT NULL, Arrival text NOT NULL, TimetableURL text NOT NULL);");
			//db.execSQL("INSERT INTO Routes(RouteID, Departure, Arrival, TimetableURL) VALUES(1,'関西大学','JR高槻駅北','http://www.city.takatsuki.osaka.jp/new2001/bus/209_5.htm');");
			execSql(db, "sql/create");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		try{
			execSql(db, "sql/drop");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		onCreate(db);
	}

	private void execSql(SQLiteDatabase db, String assetsDir) throws IOException{
		AssetManager assetMgr = mContext.getResources().getAssets();

		try{
			String files[] = assetMgr.list(assetsDir);
			for(int i=0; i < files.length; ++i){
				String f = readFile(assetMgr.open(assetsDir + "/" + files[i]));

				db.beginTransaction();
				for(String sql : f.split("!")){
					System.out.println(sql);
					Log.d("",sql);
					db.execSQL(sql);
				}
				db.setTransactionSuccessful();
			}
		}
		catch(IOException e){
			Log.e("ERROR", e.toString());
		}
		finally{
			db.endTransaction();
		}
	}
	private String readFile(InputStream is) throws IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String str;
			while((str = br.readLine()) != null) sb.append(str + "\n");
			return sb.toString();
		}
		finally{
			if(br != null) br.close();
		}
	}

}