package com.example.deepika.travelguide;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;




public class PathGoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng ADELAIDE = new LatLng(-34.9284449,138.6005793);
    private static final LatLng CLARE = new LatLng(-33.8414278,138.5768093);
    private static final LatLng CONNAWARRA= new LatLng(-37.2898509,140.812181 );
    private static final LatLng MCLAREN_VALE = new LatLng(-35.2052922, 138.4825192);
    MarkerOptions options;
    TextView tv;
    Button buttonnext,buttonprev;
    GoogleMap googleMap;
    final String TAG = "PathGoogleMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_google_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        Typeface tf = Typeface.createFromAsset(getAssets(),
        //              "font/irmatextroundstdmedium.otf");
        tv = (TextView) findViewById(R.id.mytext2);
        //      tv.setTypeface(tf);
        buttonnext=(Button)findViewById(R.id.button2);
        buttonprev=(Button)findViewById(R.id.button1);
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
        // String params = origin + "&" +dest+"&"+waypoints + "&" + sensor;
        String output = "json";
        // String url = "https://maps.googleapis.com/maps/api/directions/"
        //       + output + "?" + params;
        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=-34.9284449,138.6005793&destination=-34.9284449,138.6005793&waypoints=optimize:true|-33.8414278,138.5768093|-37.2898509,140.812181|-35.2052922,138.4825192&sensor=false";
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

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MCLAREN_VALE,
                        13));

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
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
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
