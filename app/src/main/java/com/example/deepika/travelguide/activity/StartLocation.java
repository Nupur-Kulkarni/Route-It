package com.example.deepika.travelguide.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.deepika.travelguide.R;
import com.example.deepika.travelguide.autocomplete.AutoCompleteBean;
import com.example.deepika.travelguide.autocomplete.PlaceAPI;
import com.example.deepika.travelguide.service.AsyncResponse;
import com.example.deepika.travelguide.service.WebServiceAsynTask;
import com.example.deepika.travelguide.util.PropertyReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class StartLocation extends AppCompatActivity implements AsyncResponse{

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";
    private ArrayList<AutoCompleteBean> resultList;
    private String API_KEY;
    private ArrayList<Double> latlon;
    String city_userselected="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        city_userselected=getIntent().getStringExtra("City_latlon");;
        Log.d("city_userselected",""+city_userselected);


    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoCompleteTextView autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_list_item,getLatLon(city_userselected)));

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    Details(resultList.get(position).getDescription(), resultList.get(position).getReference());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String description = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Details(String description, String reference ) throws UnsupportedEncodingException {
        API_KEY=PropertyReader.getProperty(getApplicationContext(), "application.properties","GOOGLE_API_KEY");
        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
        sb.append("?reference=" + URLEncoder.encode(reference, "utf8"));
        sb.append("&key=" + API_KEY);

        String[] params=new String[2];
        params[0]=sb.toString();
        params[1]="GET";
        WebServiceAsynTask webServiceAsynTask=new WebServiceAsynTask(params,this);
        webServiceAsynTask.execute();
        //ArrayList<Double> resultList = null;
       /* HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {


            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }*/


        //return resultList;
    }


    private String getLatLon(String city_userselected) {
        Log.d("City selected by user ",""+city_userselected);
        if(city_userselected.equals(PropertyReader.getProperty(getApplicationContext(), "application.properties","Baltimore"))){
            return PropertyReader.getProperty(getApplicationContext(), "application.properties","BaltimoreLatLon");
        }else if(city_userselected.equals(PropertyReader.getProperty(getApplicationContext(), "application.properties","NewYork"))){
            return PropertyReader.getProperty(getApplicationContext(), "application.properties","NewYorkLatLon");
        }else if(city_userselected.equals(PropertyReader.getProperty(getApplicationContext(), "application.properties","WashingtonDC"))){
            return PropertyReader.getProperty(getApplicationContext(), "application.properties","WashingtonDCLatLon");
        }else if(city_userselected.equals(PropertyReader.getProperty(getApplicationContext(), "application.properties","LasVegas"))){
            return PropertyReader.getProperty(getApplicationContext(), "application.properties","LasVegasLatLon");
        }
        return "";
    }


    @Override
    public void processFinish(String jsonResults) {
        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject jsonObjResult = jsonObj.getJSONObject("result");
            JSONObject jsonObjGemmetry = jsonObjResult.getJSONObject("geometry");
            JSONObject jsonObjLocation = jsonObjGemmetry.getJSONObject("location");

            System.out.println("jsonObj.toString() :::: " + jsonObj.toString());
            System.out.println("jsonObjLocation.toString() :::: " + jsonObjLocation.toString());

            latlon = new ArrayList<Double>(2);
            latlon.add(jsonObjLocation.getDouble("lat"));
            latlon.add(jsonObjLocation.getDouble("lng"));
            Intent i=new Intent(getApplicationContext(), SelectCatagory.class);
            startActivity(i);
        } catch (JSONException e) {
            Log.e("start location", "Cannot process JSON results", e);
        }
    }
    class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

        //ArrayList<String> resultList;

        Context mContext;
        int mResource;
        String cityLatLon;

        PlaceAPI mPlaceAPI ;

        public PlacesAutoCompleteAdapter(Context context, int resource, String city) {
            super(context, resource);

            mContext = context;
            mResource = resource;
            this.cityLatLon=city;
            mPlaceAPI=new PlaceAPI(cityLatLon);
        }

        @Override
        public int getCount() {
            // Last item will be the footer
            return resultList.size();
        }

        @Override
        public String getItem(int position) {
            if(resultList==null || resultList.size()==0)return "";
            return resultList.get(position).getDescription();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = mPlaceAPI.autocomplete(constraint.toString());
                        //resultList = mPlaceAPI.autocomplete(constraint.toString());

                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return filter;
        }
    }

}
