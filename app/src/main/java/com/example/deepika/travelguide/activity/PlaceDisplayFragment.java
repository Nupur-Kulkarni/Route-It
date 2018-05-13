package com.example.deepika.travelguide.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.deepika.travelguide.R;
import com.example.deepika.travelguide.beans.FourSquareVenues;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@SuppressLint("ValidFragment")
public class PlaceDisplayFragment extends Fragment {
    ListAdapter adapter;
    String category = null;
    ListData listData = null;
    ArrayList<FourSquareVenues> place = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<String>();
    HashSet<FourSquareVenues> set;

    ListView list;
    String[] name = new String[3];
    String[] addr= new String[3];
    String[] img= new String[3];
    /**
     @param category : category selected by user
     @param selectCatagory: object of class that called this constructor
     @param set: set that contains selected places for the category
     */

    public PlaceDisplayFragment(String category, ListData selectCatagory, HashSet<FourSquareVenues> set) {
        this.category = category;
        this.listData = selectCatagory;
        this.set = set;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {

            place = (ArrayList<FourSquareVenues>) getArguments().getSerializable("Places");

        }

        ArrayList<FourSquareVenues> mylist = new ArrayList<FourSquareVenues>();     //mylist is a list with limited FourSquarevenues objects
        for (int i =0;i<3;i++)
        {
            mylist.add(place.get(i));
        }
        View view = inflater.inflate(R.layout.fragment_placedisplay, container, false);

        //listData has object of select category class which is used to call getData in selectcategory
        adapter = new customAdaptor((Activity) getContext(), mylist,listData,set);

        list = (ListView) view.findViewById(R.id.listV);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        listData.getData(place.get(position),true);
                }
        });
        return view;
    }
}

class customAdaptor extends  ArrayAdapter<FourSquareVenues> {   //custom adapter binds ArrayList<FourSquareVenues> to list view

    private Activity context;
    private String category;
    private ArrayList<FourSquareVenues> place;
    private HashSet<FourSquareVenues> set;
    private ListData listData = null;
    static boolean tick;

    public customAdaptor(Activity context, ArrayList<FourSquareVenues> place, ListData category, HashSet<FourSquareVenues> set) {
        super(context, R.layout.list_row, place);
        this.place = place;
        this.listData = category;
        this.context=context;
        this.set=set;
    }

    static class ViewHolder{            //ViewHolder class holds references to row views in list view & this reference
                                        // is assigned to row view via setTag() method
        protected CheckBox checkBox;
        protected ImageView imageView;
        protected TextView name;
        protected TextView addr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View Custom=null;
            if (convertView == null) {  //this is null only initially
                LayoutInflater infla = LayoutInflater.from(getContext());
                Custom = infla.inflate(R.layout.list_row, parent, false); //inflate row view
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.checkBox = (CheckBox) Custom.findViewById(R.id.check);
                viewHolder.name = (TextView) Custom.findViewById(R.id.name);
                viewHolder.addr = (TextView) Custom.findViewById(R.id.addr);
                viewHolder.imageView = (ImageView) Custom.findViewById(R.id.list_image);

                Typeface tf = Typeface.createFromAsset(context.getAssets(),
                        "font/irmatextroundstdmedium.otf");
                viewHolder.addr.setTypeface(tf);
                viewHolder.name.setTypeface(tf);

                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        FourSquareVenues place = (FourSquareVenues) viewHolder.checkBox.getTag();
                        listData.getData(place,compoundButton.isChecked());

                        //place.setSelected(tickedAlready);


                        Log.d("place checkbox", String.valueOf(place));
                    }
                });

                Custom.setTag(viewHolder);  //reference to elements in row view is assigned to custom view
                viewHolder.checkBox.setTag(place.get(position));


            } else {    //we don't need to findViewById for elements in row view as ViewHoldel has reference to it
                Custom = convertView;
                ((ViewHolder) Custom.getTag()).checkBox.setTag(place.get(position)); //place is assigned to setTag so that we can get this place
                //whenever a checkbox is ticked
            }

            ViewHolder holder = (ViewHolder) Custom.getTag();
            holder.name.setText(place.get(position).getName());
            holder.addr.setText(place.get(position).get_Address());
            Picasso.with(getContext()).load(place.get(position).getPhotoURL()).into(holder.imageView);
            if (set != null) {
                Iterator iterator = set.iterator();

                while (iterator.hasNext()) {
                    FourSquareVenues venue = (FourSquareVenues) iterator.next();
                    if (venue.equals(place.get(position))) {
                        holder.checkBox.setChecked(true);
                    }
                }
            }
            Custom.setMinimumHeight(500);

            return Custom;
    }
}
