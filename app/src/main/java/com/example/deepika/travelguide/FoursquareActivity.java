package com.example.deepika.travelguide;

import android.Manifest;
import android.content.Intent;
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

import com.example.deepika.travelguide.activity.VenueDetails_Activity;
import com.example.deepika.travelguide.beans.FourSquareVenues;
import com.example.deepika.travelguide.beans.VenueCategory;
import com.example.deepika.travelguide.beans.VenueLocation;
import com.example.deepika.travelguide.service.AsyncResponse;
import com.example.deepika.travelguide.service.WebServiceAsynTask;
import com.example.deepika.travelguide.util.PropertyReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FoursquareActivity extends AppCompatActivity implements AsyncResponse{

    LocationManager locationManager;
    private double latitude=39.2904, longitude=-76.6122;
    private String categoryid="4d4b7104d754a06370d81259";//Arts & Entertainment
    private static final String SIZE="300x300";
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
        params[0]= PropertyReader.getProperty(getApplicationContext(), "application.properties","FOURSQUARE_PLACES_API_URL");
        params[1]="GET";
        params[2]="ll";
        params[3]=String.valueOf(latitude)+","+String.valueOf(longitude);
        params[4]="client_id";
        params[5]=CLIENT_ID;
        params[6]="client_secret";
        params[7]=CLIENT_SECRET;
        params[8]="v";
        params[9]="20140715";
        params[10]="categoryId";
        params[11]=categoryid;

        WebServiceAsynTask task = new WebServiceAsynTask(params,this,FoursquareActivity.this);
        task.execute(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

        @Override
        public void processFinish(String output) {

            Log.d("recommended api output",output);
            try {
                parseFourSquareRecommendedAPIJson(output);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    private void parseFourSquareRecommendedAPIJson(String output) throws JSONException {
        JSONObject jsonObject = new JSONObject(output);
        if(output!=null) {
            JSONArray resultsArray = jsonObject.getJSONObject("response")
                    .getJSONObject("group")
                    .getJSONArray("results");
            ArrayList<FourSquareVenues> venues = new ArrayList<>();
            if(resultsArray!=null) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    Log.d("venues ", "venues" + resultsArray.get(i));
                    FourSquareVenues venue = new FourSquareVenues();
                    JSONObject singleresult = resultsArray.getJSONObject(i);

                    JSONObject venueDetailsJson = singleresult.getJSONObject("venue");

                    venue.setId((String) venueDetailsJson.get("id"));
                    venue.setName((String) venueDetailsJson.get("name"));

                    JSONObject locationDetailsJson = venueDetailsJson.getJSONObject("location");
                    VenueLocation venueLocation = new VenueLocation();
                    venueLocation.setLat((Double) locationDetailsJson.get("lat"));
                    venueLocation.setLng((Double) locationDetailsJson.get("lng"));
                    venueLocation.setDistance((Integer) locationDetailsJson.get("distance"));
                    venueLocation.setCc((String) locationDetailsJson.get("cc"));
                    venueLocation.setCity((String) locationDetailsJson.get("city"));
                    venueLocation.setState((String) locationDetailsJson.get("state"));
                    venueLocation.setCountry((String) locationDetailsJson.get("country"));
                    venue.setLocation(venueLocation);

                    JSONArray categoriesDetailsJsonArray = venueDetailsJson.getJSONArray("categories");
                    JSONObject categoriesDetailsObject = categoriesDetailsJsonArray.getJSONObject(0);
                    VenueCategory venueCategory = new VenueCategory();
                    venueCategory.setCategoryId(categoriesDetailsObject.getString("id"));
                    venueCategory.setName(categoriesDetailsObject.getString("name"));
                    venue.setVenueCategory(venueCategory);

                    JSONObject photoDetailJson = singleresult.getJSONObject("photo");
                    venue.setPhotoURL(photoDetailJson.get("prefix") + SIZE + photoDetailJson.get("suffix"));

                    venues.add(venue);




                }
            }
            Log.d("venues ","Size "+venues.size() +" Records : "+venues);
            Intent i=new Intent(this, VenueDetails_Activity.class);
            //i.putExtra("venueObj",venues.get(0));
            startActivity(i);
        }

  }

}
