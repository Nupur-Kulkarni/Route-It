package com.example.deepika.travelguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.example.deepika.travelguide.R;
import com.example.deepika.travelguide.beans.FourSquareVenues;
import com.example.deepika.travelguide.beans.VenueCategory;
import com.example.deepika.travelguide.beans.VenueLocation;
import com.example.deepika.travelguide.service.AsyncResponse;
import com.example.deepika.travelguide.service.WebServiceAsynTask;
import com.example.deepika.travelguide.util.PropertyReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VenueDetails_Activity extends Activity implements AsyncResponse{

    private double latitude=39.2904, longitude=-76.6122;
    private String venueId="49eeaf08f964a52078681fe3";
    final String CLIENT_ID = "VHQHDON5JD310HTPPEEMR0WY4FZQIK32QYP22R1NWMQTKU45";
    final String CLIENT_SECRET = "BK5L0JPJ1M144E3DWX24L5DOZNXNPVOPLYGFCLL4QXQRIOSM";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details_);
        String[] params = new String[12];

        int i=0;
        String url=PropertyReader.getProperty(getApplicationContext(), "application.properties","FOURSQUARE_VENUE_DETAILS");
        params[i++]= url+"/"+venueId;
        params[i++]="GET";
        params[i++]="client_id";
        params[i++]=CLIENT_ID;
        params[i++]="client_secret";
        params[i++]=CLIENT_SECRET;
        params[i++]="v";
        params[i++]="20140715";

        WebServiceAsynTask task = new WebServiceAsynTask(params,this);
        task.execute(params);
    }

    @Override
    public void processFinish(String output) {

        Log.d("venue details output",output);
        try {
            parseFourSquareVenueDetailsAPIJson(output);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void parseFourSquareVenueDetailsAPIJson(String output) throws JSONException {
        JSONObject jsonObject = new JSONObject(output);
        if(output!=null) {
            JSONObject pageDetails = jsonObject.getJSONObject("response")
                    .getJSONObject("venue")
                    .getJSONObject("page")
                    .getJSONObject("pageInfo");
            String desc=pageDetails.getString("description");
            Log.d("venue description ",desc);

        }
    }
}
