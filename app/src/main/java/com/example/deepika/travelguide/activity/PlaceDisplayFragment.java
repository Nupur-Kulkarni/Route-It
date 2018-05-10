package com.example.deepika.travelguide.activity;


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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.deepika.travelguide.R;
import com.example.deepika.travelguide.beans.FourSquareVenues;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class PlaceDisplayFragment extends Fragment {
    ListAdapter adapter;


    ArrayList<FourSquareVenues> place = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<String>();
    ListView list;
    String[] name = new String[3];
    String[] addr= new String[3];
    String[] img= new String[3];


    public PlaceDisplayFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {

            place = (ArrayList<FourSquareVenues>) getArguments().getSerializable("Places");

        }

        for (int i =0;i<3;i++)
        {
            name[i]=place.get(i).getName();
            addr[i]=place.get(i).get_Address();
            img[i]=place.get(i).getPhotoURL();
        }
        View view = inflater.inflate(R.layout.fragment_placedisplay, container, false);
        adapter = new customAdaptor((Activity) getContext(), name,addr,img);

        list = (ListView) view.findViewById(R.id.listV);

        list.setAdapter(adapter);



            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Log.d("Position_0", "working"+place.get(position));
                   
                }
            });


            return view;


    }
}

class customAdaptor extends  ArrayAdapter<String> {
    private  String[] foods;
    private  String[]  des;
    private  String[] imag;
    private Activity context;
    public customAdaptor(Activity context, String[] foods, String[]des, String[]imag) {
        super(context, R.layout.list_row, foods);
        this.context=context;
        this.foods=foods;
        this.des=des;
        this.imag=imag;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater infla = LayoutInflater.from(getContext());
        View Custom = infla.inflate(R.layout.list_row,parent,false);
        // String f = getItem(position);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
               "font/irmatextroundstdmedium.otf");
        TextView txt = (TextView)Custom.findViewById(R.id.addr);
        txt.setTypeface(tf);
        TextView t = (TextView)Custom.findViewById(R.id.name);
        t.setTypeface(tf);

        ImageView img = (ImageView)Custom.findViewById(R.id.list_image);
        t.setText(foods[position]);
        txt.setText(des[position]);
        Picasso.with(getContext()).load(imag[position]).into(img);


        Custom.setMinimumHeight(500);
        return Custom;

    }
}
