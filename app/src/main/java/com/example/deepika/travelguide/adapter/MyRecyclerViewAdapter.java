package com.example.deepika.travelguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deepika.travelguide.CategoryData;
import com.example.deepika.travelguide.R;

import java.util.ArrayList;
import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private CategoryData[] unSelectedCategory_icon;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private CategoryData[] selectedCategory_icon;
    private String[] selectedCategories_array;
    private List<ViewHolder> viewHolders=null;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, CategoryData[] unSelectedCategory, CategoryData[] selectedCategory) {
        this.mInflater = LayoutInflater.from(context);
        this.unSelectedCategory_icon = unSelectedCategory;
        this.selectedCategory_icon=selectedCategory;
        selectedCategories_array=new String[unSelectedCategory.length];
        viewHolders=new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.myTextView.setText(unSelectedCategory_icon[position].getTitle());
        holder.myView.setImageResource(unSelectedCategory_icon[position].getImageUrl());
        holder.position=position;
        viewHolders.add(holder);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return unSelectedCategory_icon.length;
    }

    public List<String> getSelectedCategories(){
        List<String> output=new ArrayList<>();
        for(String s :selectedCategories_array){
            if(!"".equals(s) && s!=null){
                output.add(s);
            }
        }
        return output;

    }
    public List<String> resetCategories(){
        for(ViewHolder holder:viewHolders){
            holder.myView.setImageResource(unSelectedCategory_icon[holder.position].getImageUrl());
        }
        selectedCategories_array=new String[unSelectedCategory_icon.length];
        return null;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myView;
        TextView myTextView;
        boolean isSelected=true;
        int position;


        ViewHolder(View itemView) {

            super(itemView);
            myView = itemView.findViewById(R.id.category_icon);
            myTextView = itemView.findViewById(R.id.category_title);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            myView = itemView.findViewById(R.id.category_icon);
            if(isSelected) {
                isSelected = false;
                myView.setImageResource(selectedCategory_icon[position].getImageUrl());
                selectedCategories_array[position]=getItem(position);
            }
            else{
                isSelected=true;
                myView.setImageResource(unSelectedCategory_icon[position].getImageUrl());
                selectedCategories_array[position]="";
            }
            //if (mClickListener != null) mClickListener.onItemClick(view, position);
        }
    }

    // convenience method for getting data at click position
    private String getItem(int id) {
        return unSelectedCategory_icon[id].getTitle();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}