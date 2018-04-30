package com.example.deepika.travelguide;

public class CategoryData {

    private String title;
    private int imageUrl;
    private boolean isSelected=false;

    CategoryData(String title, int imageUrl){

        this.title = title;
        this.imageUrl = imageUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTitle() {
        return title;
    }

    public int getImageUrl() {
        return imageUrl;
    }
}