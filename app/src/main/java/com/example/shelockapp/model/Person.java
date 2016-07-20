package com.example.shelockapp.model;

import java.io.Serializable;

/**
 * Created by shini_000 on 7/15/2016.
 */
public class Person implements Serializable{

    private int id;
    private String name;
    private String cover;
    private int age;
    private int height;
    private String gender;
    private String hairColor;
    private String address;
    private String comment;
    private Movement movement;

    public Person(){

    }

    public Person(String cover, String name, int age, int height, String gender, String hairColor,String address, String comment) {
        this.address = address;
        this.age = age;
        this.cover = cover;
        this.comment = comment;
        this.gender = gender;
        this.hairColor = hairColor;
        this.height = height;
        this.name = name;
    }






    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
