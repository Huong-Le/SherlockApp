package com.example.shelockapp.model;

import java.io.Serializable;

/**
 * Created by shini_000 on 7/15/2016.
 */
public class Movement implements Serializable{

    private int number;
    private int id;
    private String name;
    private String note;
    private String location;
    private String date;

    public Movement() {

    }


    public Movement(int id, String name, String note, String location, String date) {

        this.name = name;
        this.note = note;
        this.location = location;
        this.date = date;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
