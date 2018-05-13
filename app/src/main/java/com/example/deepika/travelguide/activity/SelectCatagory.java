package com.example.deepika.travelguide.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.deepika.travelguide.PathGoogleMapActivity;
import com.example.deepika.travelguide.R;
import com.example.deepika.travelguide.beans.FourSquareVenues;
import com.example.deepika.travelguide.beans.FoursquareAPIClass;
import com.example.deepika.travelguide.service.ServiceResponse;
import com.tooltip.Tooltip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SelectCatagory extends AppCompatActivity  implements View.OnClickListener, ServiceResponse, ListData {
    ImageButton shopping, parks, food, fav;
    ImageButton attraction;
    Tooltip tooltip_, tooltip_2, tooltip_3, tooltip_1;
    String category = null;
    Button mymapbutton;
    HashMap<String, HashSet<FourSquareVenues>> map = new HashMap<>();
    Handler handler = new Handler();
    FoursquareAPIClass foursquareActivity=null, foursquareActivity1=null;
    public class  M2Update implements Runnable {
        private String category;
        public M2Update(String category1) {

            category = category1;
        }

        @Override
        public void run() {
            switch (category) {
                case "attraction":



                    attraction.setImageResource(R.drawable.attractions_active);
                    shopping.setImageResource(R.drawable.shopping_button);
                    parks.setImageResource(R.drawable.parks_button);
                    food.setImageResource(R.drawable.food_button);
                    break;
                case "shopping":
                    attraction.setImageResource(R.drawable.attraction);
                    shopping.setImageResource(R.drawable.shopping_active);
                    parks.setImageResource(R.drawable.parks_button);
                    food.setImageResource(R.drawable.food_button);

                    break;
                case "parks":
                    attraction.setImageResource(R.drawable.attractions_button);
                    shopping.setImageResource(R.drawable.shopping_button);
                    parks.setImageResource(R.drawable.parks_active);
                    food.setImageResource(R.drawable.food_button);


                    break;
                case "food":
                    attraction.setImageResource(R.drawable.attractions_button);
                    shopping.setImageResource(R.drawable.shopping_button);
                    parks.setImageResource(R.drawable.parks_button);
                    food.setImageResource(R.drawable.food_active);

                    break;


            }
        }

    }


    @Override
    public void getResponse(ArrayList<FourSquareVenues> venues) {
        Bundle bn=new Bundle();
        bn.putSerializable("Places",venues);
        HashSet<FourSquareVenues> set = map.get(category);
        PlaceDisplayFragment fragment = new PlaceDisplayFragment(category,SelectCatagory.this,set);
        fragment.setArguments(bn);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager!=null)
        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frag,fragment);
            transaction.commit();

        }
    }

    @Override
    public void getData(FourSquareVenues place, boolean checked) {
        HashSet<FourSquareVenues> set = new HashSet<>();
        if(checked) {
            Log.d("Place", String.valueOf(place));

            Log.d("category", String.valueOf(category));
            if (map.containsKey(category)) {
                set = map.get(category);
                if (set.contains(place)) {
                    Log.d("set", "object in set");
                } else {

                    set.add(place);
                    map.put(category, set);
                    Log.d("set contains 12", String.valueOf(set));
                }
            } else {
                set.add(place);
                map.put(category, set);
                Log.d("set contains", String.valueOf(set));
            }
        }
        else{
            map.get(category).remove(place);
            Log.d("remove","place removed from set");
        }
        Log.d("hashmap", String.valueOf(map));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_catagory);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "font/irmatextroundstdmedium.otf");
        Typeface tfb =Typeface.createFromAsset(getAssets(),
                "font/irmatextroundstdbold.otf");
        TextView tv = (TextView) findViewById(R.id.txt);
        tv.setTypeface(tf);
        TextView attraction_txtView = (TextView)findViewById(R.id.attraction_txt);
        attraction_txtView.setTypeface(tfb);
        TextView shopping_txtView = (TextView)findViewById(R.id.shopping_txt);
        shopping_txtView.setTypeface(tfb);
        TextView parks_textView = (TextView)findViewById(R.id.parks_txt);
        parks_textView.setTypeface(tfb);
        TextView food_txtView = (TextView)findViewById(R.id.food_txt);
        food_txtView.setTypeface(tfb);
        mymapbutton = (Button) findViewById(R.id.myMapbutton);
        mymapbutton.setOnClickListener(this);
        //listView = (ListView) findViewById(R.id.listV);
        attraction = (ImageButton) findViewById(R.id.attraction);
        shopping = (ImageButton) findViewById(R.id.shopping);

        parks = (ImageButton) findViewById(R.id.parks);
        food = (ImageButton) findViewById(R.id.food);

        attraction.setOnClickListener(this);
        shopping.setOnClickListener(this);
        parks.setOnClickListener(this);
        food.setOnClickListener(this);
        M2Update m2UpdateUI = new M2Update("attraction");
        handler.post(m2UpdateUI);
        foursquareActivity1=new FoursquareAPIClass("4d4b7104d754a06370d81259",this);
        category="attraction";
        foursquareActivity1.callService();

    }

    void onButtonClick(){
        Log.d("click","method called on click");
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(),VenueDetails_Activity.class);

        switch (view.getId()) {
            case R.id.attraction:


                M2Update m2UpdateUI = new M2Update("attraction");
                handler.post(m2UpdateUI);
                foursquareActivity=new FoursquareAPIClass("4d4b7104d754a06370d81259",this);
                category="attraction";
                foursquareActivity.callService();




                break;
            case R.id.shopping:
                M2Update m2UpdateUI1 = new M2Update("shopping");
                handler.post(m2UpdateUI1);
                foursquareActivity=new FoursquareAPIClass("4d4b7104d754a06370d81259",this);
                category="shopping";
                foursquareActivity.callService();


                break;
            case R.id.parks:
                M2Update m2UpdateUI2 = new M2Update("parks");
                handler.post(m2UpdateUI2);

                foursquareActivity=new FoursquareAPIClass("4d4b7104d754a06370d81259",this);
                category="parka";
                foursquareActivity.callService();


                break;
            case R.id.food:

                M2Update m2UpdateUI3 = new M2Update("food");
                handler.post(m2UpdateUI3);
                category="food";
                foursquareActivity=new FoursquareAPIClass("4d4b7105d754a06374d81259",this);
                foursquareActivity.callService();


                break;
            case R.id.myMapbutton:
                Intent intent1 = new Intent(getApplicationContext(),PathGoogleMapActivity.class);
                intent1.putExtra("map", (Serializable) map);
                startActivity(intent1);
                break;


        }
    }

}

