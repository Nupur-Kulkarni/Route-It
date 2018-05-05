package com.example.deepika.travelguide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.deepika.travelguide.service.AsyncResponse;
import com.example.deepika.travelguide.service.WebServiceAsynTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoursquareActivity extends AppCompatActivity implements AsyncResponse{

    LocationManager locationManager;
    private double latitude, longitude;
    ListView lv;
    ArrayList<FoursquareModel> venuesList;
    final String CLIENT_ID = "VHQHDON5JD310HTPPEEMR0WY4FZQIK32QYP22R1NWMQTKU45";
    final String CLIENT_SECRET = "BK5L0JPJ1M144E3DWX24L5DOZNXNPVOPLYGFCLL4QXQRIOSM";

    private AsyncResponse delegate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foursquare);


        String[] params = new String[12];
            params[0]="https://api.foursquare.com/v2/search/recommendations";
        params[1]="GET";
//        params[2]="ll=39.2904,-76.6122&client_id=VHQHDON5JD310HTPPEEMR0WY4FZQIK32QYP22R1NWMQTKU45&client_secret=BK5L0JPJ1M144E3DWX24L5DOZNXNPVOPLYGFCLL4QXQRIOSM&v=20140715&categoryId=4d4b7104d754a06370d81259";
        params[2]="ll";
        params[3]=String.valueOf(39.2904)+","+String.valueOf(-76.6122);
        params[4]="client_id";
        params[5]=CLIENT_ID;
        params[6]="client_secret";
        params[7]=CLIENT_SECRET;
        params[8]="v";
        params[9]="20140715";
        params[10]="categoryId";
        params[11]="4d4b7104d754a06370d81259";  //Arts & Entertainment

        WebServiceAsynTask task = new WebServiceAsynTask(params,this,FoursquareActivity.this);
        task.execute(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

        @Override
        public void processFinish(String output) {

            Log.d("output >> ",output);
            try {
                JSONObject jsonObject = new JSONObject(output);

                if (jsonObject.has("response")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

}
