package com.example.deepika.travelguide.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.deepika.travelguide.beans.CategoryData;
import com.example.deepika.travelguide.R;
import com.example.deepika.travelguide.adapter.MyRecyclerViewAdapter;

import java.util.List;


public class CategoriesFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    private OnFragmentInteractionListener mListener;
    private View view;
    private Button applyFilter,resetFilter;
    private List<String> selectedCategories;



    public CategoriesFragment() {
        // Required empty public constructor
    }
    private MyRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        view=inflater.inflate(R.layout.fragment_categories, container, false);
        applyFilter=view.findViewById(R.id.applyFilter);
        resetFilter=view.findViewById(R.id.resetFilter);
        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCategories=adapter.getSelectedCategories();
                Log.d("selected categories",""+selectedCategories);
            }
        });
        resetFilter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selectedCategories=adapter.resetCategories();
            }
        });

        CategoryData itemData[] = new CategoryData[5];

        itemData[0]=(new CategoryData("Attraction",R.drawable.attractions_button));
        itemData[1]=(new CategoryData("Shopping",R.drawable.shopping_button));
        itemData[2]=(new CategoryData("Parks",R.drawable.parks_button));
        itemData[3]=(new CategoryData("Food",R.drawable.food_button));
        itemData[4]=(new CategoryData("Favorites",R.drawable.favorites_button));

        CategoryData selectedCategory[] = new CategoryData[5];

        selectedCategory[0]=(new CategoryData("Attraction",R.drawable.attractions_active));
        selectedCategory[1]=(new CategoryData("Shopping",R.drawable.shopping_active));
        selectedCategory[2]=(new CategoryData("Parks",R.drawable.parks_active));
        selectedCategory[3]=(new CategoryData("Food",R.drawable.food_active));
        selectedCategory[4]=(new CategoryData("Favorites",R.drawable.favorites_active));



        RecyclerView recyclerView = view.findViewById(R.id.categoriesView);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new MyRecyclerViewAdapter(getActivity(), itemData,selectedCategory);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onItemClick(View view, int position) {
    }
}
