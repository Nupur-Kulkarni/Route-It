package com.example.deepika.travelguide.service;

import com.example.deepika.travelguide.beans.FourSquareVenues;

import java.util.ArrayList;

/**
 * Created by Kanchi on 5/8/2018.
 */

public interface ServiceResponse {
    void getResponse(ArrayList<FourSquareVenues> venues);
}
