package info.hagedev.itscool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Itscool extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = new Intent(this, info.hagedev.itscool.SelectStation.class);
        intent.setAction(Intent.ACTION_VIEW);
    	startActivity(intent);
    }
}