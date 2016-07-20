package com.example.shelockapp.gps;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shini_000 on 7/18/2016.
 */
public class PlacesList implements Serializable {


    @Key
    public String status;

    @Key
    public List<Place> results;

}