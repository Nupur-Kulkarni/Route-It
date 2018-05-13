package com.example.deepika.travelguide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.deepika.travelguide.beans.FourSquareVenues;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class PathGoogleMapActivity extends FragmentActivity implements OnMapReadyCallback,Serializable {

    int flag=0;
    int count;
    private static final LatLng ADELAIDE = new LatLng(-34.9284449,138.6005793);
    private static final LatLng CLARE = new LatLng(-33.8414278,138.5768093);
    private static final LatLng CONNAWARRA= new LatLng(-37.2898509,140.812181 );
    private static final LatLng MCLAREN_VALE = new LatLng(-35.2052922, 138.4825192);
    MarkerOptions options;
    TextView tv;
    Button buttonnext,buttonprev;
    GoogleMap googleMap;
    final String TAG = "PathGoogleMapActivity";

    HashMap<String, HashSet<FourSquareVenues>> selectedPlcesMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_google_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                      "font/irmatextroundstdmedium.otf");
        tv = (TextView) findViewById(R.id.mytext2);
              tv.setTypeface(tf);
        buttonnext=(Button)findViewById(R.id.button2);
        buttonprev=(Button)findViewById(R.id.button1);

        Intent receivedIntent = getIntent();
        //Bundle bundle = receivedIntent.getExtras();
        selectedPlcesMap =(HashMap<String, HashSet<FourSquareVenues>>)receivedIntent.getSerializableExtra("map");


        options = new MarkerOptions();
        options.position(ADELAIDE);
        options.position(CLARE);
        options.position(CONNAWARRA);
        options.position(MCLAREN_VALE);

    }
    private String getMapsApiDirectionsUrl() {
        String waypoints ="waypoints=optimize:true|"
                + CLARE.latitude + "," + CLARE.longitude
                + "|" + "|" + CONNAWARRA.latitude + ","
                + CONNAWARRA.longitude + "|" + MCLAREN_VALE.latitude + ","
                + MCLAREN_VALE.longitude;
        String origin = "origin="+ADELAIDE.latitude+","+ADELAIDE.longitude;
        String dest = "destination="+ADELAIDE.latitude+","+ADELAIDE.longitude;
        String sensor = "sensor=false";
         String params = origin + "&" +dest+"&"+waypoints + "&" + sensor;
        String output = "json";
         String url = "https://maps.googleapis.com/maps/api/directions/"
               + output + "?" + params;
        //String url = "http://maps.googleapis.com/maps/api/directions/json?origin=-34.9284449,138.6005793&destination=-34.9284449,138.6005793&waypoints=optimize:true|-33.8414278,138.5768093|-37.2898509,140.812181|-35.2052922,138.4825192&sensor=false";
        return url;
    }

    private void addMarkers() {
        if (googleMap != null) {

            googleMap.addMarker(new MarkerOptions().position(CLARE));
            //       .title
            //                 ("First Point"));
            googleMap.addMarker(new MarkerOptions().position(CONNAWARRA));
            //.title("Second Point"));
            googleMap.addMarker(new MarkerOptions().position(MCLAREN_VALE));
            // .title("Third Point"));
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        // Add a marker in Sydney and move the camera

        googleMap.addMarker(options);
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
        googleMap.addMarker(new MarkerOptions().position(ADELAIDE));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ADELAIDE,
                13));
        addMarkers();

        final ArrayList<LatLng> Locname=new ArrayList <LatLng>();
        Locname.add(ADELAIDE);
        Locname.add(CLARE);
        Locname.add(CONNAWARRA);
        Locname.add(MCLAREN_VALE);
        count=Locname.size();

        tv.setText("Starting Point");
      //   buttonnext.setEnabled(true);
            buttonnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("Flag is: ","Flag " +flag);
                    if(flag<count-1){
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Locname.get(flag+1), 13));
                        tv.setText("Location "+(flag+1));
                        flag++;
                    }
                    else if(flag==count-1){
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Locname.get(0), 13));
                        tv.setText("Location 0");
                        flag++;
                    }
                    else if(flag>=count){
         //               buttonnext.setEnabled(false);
           //             buttonnext.setVisibility(View.INVISIBLE);
                        flag=0;
                    }

                }

            });

    //    buttonprev.setEnabled(true);
        buttonprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //buttonprev.setVisibility(View.INVISIBLE);
                Log.d("Flag count is: ","Flag " +flag);
                if(flag>0 && flag<=count){
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Locname.get(flag-1), 13));
                    tv.setText("Location "+(flag-1));
                    flag--;
                }

                else if(flag==0){
      //              buttonprev.setEnabled(false);
       //             buttonprev.setVisibility(View.INVISIBLE);
                    flag=count;
                }

            }

        });
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            JSONArray waypoints,jroutes;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                jroutes = jObject.getJSONArray("routes");
                Log.d("Routes here",String.valueOf(jroutes));
                waypoints = ((JSONObject) jroutes.get(0)).getJSONArray("waypoint_order");

                Log.d("Waypoints here",String.valueOf(waypoints));

                PathJsonParser parser = new PathJsonParser();
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLACK);
            }

            googleMap.addPolyline(polyLineOptions);
        }
    }



}
