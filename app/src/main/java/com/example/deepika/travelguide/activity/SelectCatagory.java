package com.example.deepika.travelguide.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.deepika.travelguide.R;
import com.example.deepika.travelguide.beans.FourSquareVenues;
import com.example.deepika.travelguide.beans.FoursquareAPIClass;
import com.example.deepika.travelguide.service.ServiceResponse;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SelectCatagory extends AppCompatActivity  implements View.OnClickListener, ServiceResponse, ListData {
    ImageButton shopping, parks, food, fav;
    ImageButton attraction;
    Tooltip tooltip_,tooltip_2,tooltip_3,tooltip_1;
    String category = null;
    HashMap<String, HashSet<FourSquareVenues>> map = new HashMap<>();

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
    public void getData(FourSquareVenues place) {

        Log.d("Place", String.valueOf(place));
        HashSet<FourSquareVenues> set = new HashSet<>();
        Log.d("category",category);
        if(map.containsKey(category)){
            set=map.get(category);
            if(set.contains(place)){
                Log.d("set","object in set");
            }
            else {

                set.add(place);
                map.put(category,set);
                Log.d("set contains 12", String.valueOf(set));
            }
        }
        else{
            set.add(place);
            map.put(category,set);
            Log.d("set contains", String.valueOf(set));
        }
        Log.d("hashmap", String.valueOf(map));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_catagory);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "font/irmatextroundstdmedium.otf");
        TextView tv = (TextView) findViewById(R.id.txt);
        tv.setTypeface(tf);

        //listView = (ListView) findViewById(R.id.listV);
        attraction = (ImageButton) findViewById(R.id.attraction);
        shopping = (ImageButton) findViewById(R.id.shopping);

        parks = (ImageButton) findViewById(R.id.parks);
        food = (ImageButton) findViewById(R.id.food);

        attraction.setOnClickListener(this);
        shopping.setOnClickListener(this);
        parks.setOnClickListener(this);
        food.setOnClickListener(this);

    }

    void onButtonClick(){
        Log.d("click","method called on click");
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(),VenueDetails_Activity.class);
        FoursquareAPIClass foursquareActivity=null;
        switch (view.getId()) {
            case R.id.attraction:

                if(tooltip_1 != null) {
                    tooltip_1.dismiss();
                }
                if(tooltip_2 != null) {
                    tooltip_2.dismiss();
                }
                if(tooltip_3 != null) {
                    tooltip_3.dismiss();
                }
                tooltip_ = new Tooltip.Builder(attraction).setText("Attraction").show();


                attraction.setImageResource(R.drawable.attractions_active);
                shopping.setImageResource(R.drawable.shopping_button);
                parks.setImageResource(R.drawable.parks_button);
                food.setImageResource(R.drawable.food_button);

                foursquareActivity=new FoursquareAPIClass("4d4b7104d754a06370d81259",this);
                category="attraction";
                foursquareActivity.callService();

                break;
            case R.id.shopping:
                attraction.setImageResource(R.drawable.attraction);
                shopping.setImageResource(R.drawable.shopping_active);
                parks.setImageResource(R.drawable.parks_button);
                food.setImageResource(R.drawable.food_button);
                if(tooltip_ != null) {
                    tooltip_.dismiss();
                }
                if(tooltip_2 != null) {
                    tooltip_2.dismiss();
                }
                if(tooltip_3 != null) {
                    tooltip_3.dismiss();
                }


                 tooltip_1 = new Tooltip.Builder(shopping).setText("Shopping").show();

                foursquareActivity=new FoursquareAPIClass("4d4b7104d754a06370d81259",this);
                category="shopping";
                foursquareActivity.callService();
                tooltip_1.dismiss();

                break;
            case R.id.parks:
                attraction.setImageResource(R.drawable.attractions_button);
                shopping.setImageResource(R.drawable.shopping_button);
                parks.setImageResource(R.drawable.parks_active);
                food.setImageResource(R.drawable.food_button);
                if(tooltip_ != null) {
                    tooltip_.dismiss();
                }
                if(tooltip_1 != null) {
                    tooltip_1.dismiss();
                }
                if(tooltip_3 != null) {
                    tooltip_3.dismiss();
                }
                 tooltip_2 = new Tooltip.Builder(parks).setText("Parks").show();

                foursquareActivity=new FoursquareAPIClass("4d4b7104d754a06370d81259",this);
                category="parks";
                foursquareActivity.callService();
                tooltip_2.dismiss();

                break;
            case R.id.food:
                attraction.setImageResource(R.drawable.attractions_button);
                shopping.setImageResource(R.drawable.shopping_button);
                parks.setImageResource(R.drawable.parks_button);
                food.setImageResource(R.drawable.food_active);
                if(tooltip_ != null) {
                    tooltip_.dismiss();
                }
                if(tooltip_1 != null) {
                    tooltip_1.dismiss();
                }
                if(tooltip_2 != null) {
                    tooltip_2.dismiss();
                }
                 tooltip_3 = new Tooltip.Builder(food).setText("Food").show();

                foursquareActivity=new FoursquareAPIClass("4d4b7105d754a06374d81259",this);
                category="food";
                foursquareActivity.callService();
                tooltip_3.dismiss();

                break;

        }
    }

}

