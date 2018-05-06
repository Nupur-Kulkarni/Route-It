package com.example.deepika.travelguide.beans;

/**
 * Created by deepi on 5/5/2018.
 */

public class FourSquareVenues {
    private String id;
    private String name;
    private VenueLocation location;
    private VenueCategory venueCategory;
    private String photoURL;
    private String description;
    private String hours_of_operation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VenueLocation getLocation() {
        return location;
    }

    public void setLocation(VenueLocation location) {
        this.location = location;
    }

    public VenueCategory getVenueCategory() {
        return venueCategory;
    }

    public void setVenueCategory(VenueCategory venueCategory) {
        this.venueCategory = venueCategory;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHours_of_operation() {
        return hours_of_operation;
    }

    public void setHours_of_operation(String hours_of_operation) {
        this.hours_of_operation = hours_of_operation;
    }

    @Override
    public String toString() {
        return "FourSquareVenues{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", venueCategory=" + venueCategory +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }
}


