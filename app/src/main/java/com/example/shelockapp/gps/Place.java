package com.example.shelockapp.gps;

/**
 * Created by shini_000 on 7/18/2016.
 */
import com.google.api.client.util.Key;

import java.io.Serializable;

public class Place implements Serializable {


    @Key
    public String id;

    @Key
    public String name;

    @Key
    public String reference;

    @Key
    public String icon;

    @Key
    public String vicinity;

    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }


}
