package com.example.deepika.travelguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepika.travelguide.beans.FourSquareVenues;
import com.example.deepika.travelguide.service.AsyncResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

    int flag = 0;
    int count;

    MarkerOptions options;
    TextView tv;
    ImageButton buttonnext, buttonprev;
    GoogleMap googleMap;
    final String TAG = "PathGoogleMapActivity";

    HashMap <String, HashSet <FourSquareVenues>> selectedPlcesMap = new HashMap <>();
    ArrayList <LatLng> markersList = new ArrayList <>();
    ArrayList <Integer> waypointsList = new ArrayList <>();
    HashMap <LatLng, String> waypointLocations = new HashMap <>();
    LatLng[] plotWaypointsOrder = null;
    String startLocName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_google_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/irmatextroundstdmedium.otf");
        tv = (TextView) findViewById(R.id.mytext2);
        tv.setTypeface(tf);
        buttonnext = (ImageButton) findViewById(R.id.button2);
        buttonprev = (ImageButton) findViewById(R.id.button1);
        options = new MarkerOptions();

        Intent receivedIntent = getIntent();
        selectedPlcesMap = (HashMap <String, HashSet <FourSquareVenues>>) receivedIntent.getSerializableExtra("map");
        Log.d("Hashmap Receieved: ", "Map values:" + String.valueOf(selectedPlcesMap));
        Log.d("Hashmap Receieved: ", "map size: " + selectedPlcesMap.keySet().size());
        if (!selectedPlcesMap.isEmpty()) {
            if (selectedPlcesMap.containsKey("startLocation")) {
                HashSet <FourSquareVenues> startLocSet = selectedPlcesMap.get("startLocation");
                for (FourSquareVenues venue : startLocSet) {
                    LatLng loc = new LatLng(venue.getLocation().getLat(), venue.getLocation().getLng());
                    markersList.add(loc);
                    Log.d("listsize after start", "is: " + markersList.size());
                    waypointLocations.put(loc, venue.getName());
                }
            }

            for (String key : selectedPlcesMap.keySet()) {
                if (!key.equals("startLocation")) {
                    HashSet <FourSquareVenues> placesSet = selectedPlcesMap.get(key);
                    for (FourSquareVenues venue : placesSet) {
                        Log.d("Inside add start loc", "is: ");
                        LatLng loc = new LatLng(venue.getLocation().getLat(), venue.getLocation().getLng());
                        markersList.add(loc);
                        waypointLocations.put(loc, venue.getName());
                    }
                }

            }
            plotWaypointsOrder = new LatLng[markersList.size() - 1];
            Log.d("Marker List:  ", "value: " + String.valueOf(markersList));
            Log.d("Size of Marker list: ", "Size: " + markersList.size());
            Log.d("Waypoint List", "Value" + String.valueOf(waypointLocations));

        } else {
            Log.d("ikde", "bagh");
        }
    }

    private String getMapsApiDirectionsUrl() {

        StringBuilder sb = new StringBuilder();
        StringBuilder waypoints = new StringBuilder();
        String startPoint = String.valueOf(markersList.get(0).latitude) + "," + String.valueOf(markersList.get(0).longitude);
        sb.append("http://maps.googleapis.com/maps/api/directions/json?origin=" + startPoint + "&" + "destination=" + startPoint);
        waypoints.append("&waypoints=optimize:true");

        for (int i = 1; i < markersList.size(); i++) {
            Log.d("Value of i: ", "i is: " + i);
            waypoints.append("|" + markersList.get(i).latitude + "," + markersList.get(i).longitude);

        }
        waypoints.append("&sensor=false");
        String url = sb.toString() + waypoints.toString();
        Log.d("Final URL is: ", "Here " + url);
        return url;

    }

    private void addMarkers() {

        String name = null;
        if (googleMap != null) {

            for (int i = 1; i < markersList.size(); i++) {
                Log.d("Inside placing markers", "Inside markers");
                plotWaypointsOrder[waypointsList.get(i - 1)] = markersList.get(i);
                //googleMap.addMarker(new MarkerOptions().position(markersList.get(i)));
                Log.d("ikde :  ", "order: " + String.valueOf(plotWaypointsOrder));

            }
            for (int i = 0; i < markersList.size() - 1; i++) {
                /*int marker=i+1;
                String imageName = "pin1";
                 int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());

                //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.shopping_active);
                //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin1);*/
                if (waypointLocations.containsKey(plotWaypointsOrder[i])) {
                    name = waypointLocations.get(plotWaypointsOrder[i]);
                }
                Log.d("Waypoint Plotting", "here " + plotWaypointsOrder[i].toString());
                googleMap.addMarker(new MarkerOptions().position(plotWaypointsOrder[i]).title(name));
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1_bitmap)));


            }

            /*Log.d("Waypoints in order: ","markerList: "+String.valueOf(markersList));
            Log.d("Waypoints in order:  ","order: "+String.valueOf(plotWaypointsOrder));
            Log.d("Waypoint in order ","hashmap"+String.valueOf(waypointLocations));*/
            /*googleMap.addMarker(new MarkerOptions().position(CLARE));
            //       .title
            //                 ("First Point"));
            googleMap.addMarker(new MarkerOptions().position(CONNAWARRA));
            //.title("Second Point"));
            googleMap.addMarker(new MarkerOptions().position(MCLAREN_VALE));
            // .title("Third Point"));*/
        }

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        // Add a marker in Sydney and move the camera

        //googleMap.addMarker(options);
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        //starting location
        if (waypointLocations.containsKey(markersList.get(0))) {
            startLocName = waypointLocations.get(markersList.get(0));
        }
        googleMap.addMarker(new MarkerOptions().position(markersList.get(0)).title(startLocName)).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersList.get(0), 13));

        count = markersList.size();

        tv.setText(startLocName);

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //buttonnext.setVisibility(View.VISIBLE);
                Log.d("Flag is: ", "Flag " + flag);
                // Log.d("Plotwayorderpoints",String.valueOf(plotWaypointsOrder));
                if (flag < count - 1) {

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(plotWaypointsOrder[flag], 17));
                    if (waypointLocations.containsKey(plotWaypointsOrder[flag])) {
                        String name = waypointLocations.get(plotWaypointsOrder[flag]);
                        tv.setText(flag+2+"."+name);
                    }


                    flag++;
                } else if (flag == count - 1) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersList.get(0), 17));
                    tv.setText("1."+startLocName);
                    flag = 0;
                }
                    /*else if(flag==count){
         //               buttonnext.setEnabled(false);
//                      buttonnext.setVisibility(View.INVISIBLE);
                        flag=0;
                    }
*/
            }

        });


        buttonprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //buttonprev.setVisibility(View.INVISIBLE);
                Log.d("Prev Flag count is: ", "Flag " + flag + " count " + count);
                if (flag > 0 && flag <= count) {

                    if (flag == 0) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersList.get(0), 17));
                        //tv.setText("Starting Point");
                        tv.setText(startLocName);
                        flag = count - 1;

                    } else {
                        flag--;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(plotWaypointsOrder[flag], 17));
                        //tv.setText("Location " + (flag));
                        if (waypointLocations.containsKey(plotWaypointsOrder[flag])) {
                            String name = waypointLocations.get(plotWaypointsOrder[flag]);
                            tv.setText(flag+2+"."+name);
                        }


                    }
                } else if (flag == 0) {
                    flag = count - 1;
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersList.get(0), 17));
                    //         tv.setText("Location "+(flag));
                    //if(waypointLocations.containsKey(plotWaypointsOrder[flag])){
                    //  String name=waypointLocations.get(plotWaypointsOrder[flag]);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersList.get(0), 17));
                    //tv.setText("Starting Point");
                    tv.setText("1."+startLocName);
                    //tv.setText(name);


                } else if (flag < 0) {
////                    buttonprev.setEnabled(false);
////                    buttonprev.setVisibility(View.INVISIBLE);
                    flag = count;
                }


            }

        });
    }

    private class ReadTask extends AsyncTask <String, Void, String> {
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

    private class ParserTask extends AsyncTask <String, Integer, List <List <HashMap <String, String>>>> {

        @Override
        protected List <List <HashMap <String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            JSONArray waypoints, jroutes;
            List <List <HashMap <String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                jroutes = jObject.getJSONArray("routes");
                Log.d("Routes here", String.valueOf(jroutes));
                waypoints = ((JSONObject) jroutes.get(0)).getJSONArray("waypoint_order");

                Log.d("Waypoints here", String.valueOf(waypoints));
                if (waypoints != null) {
                    int len = waypoints.length();
                    for (int i = 0; i < len; i++) {
                        waypointsList.add(Integer.parseInt(waypoints.get(i).toString()));
                    }
                }
                Log.d("Waypoint List is: ", "new list" + waypointsList.toString());
                PathJsonParser parser = new PathJsonParser();
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List <List <HashMap <String, String>>> routes) {
            ArrayList <LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            addMarkers();

            if (routes!=null && !routes.isEmpty()) {
                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList <LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List <HashMap <String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap <String, String> point = path.get(j);

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
            else
            {
                Toast.makeText(getApplicationContext(),"Select places",Toast.LENGTH_SHORT);
            }


        }


    }
}