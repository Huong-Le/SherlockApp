package com.example.shelockapp.constant;

/**
 * Created by shini_000 on 7/15/2016.
 */
public class Constant {
    public static final int REQUEST_PICK_FROM_GALLERY = 0;
    public static final int REQUEST_TAKE_A_PHOTO = 1;
    public static final int REQUEST_INFO = 0;
    public static final int REQUEST_ADD = 1;
    public static final String LISTDATA = "listdata";
    public static final String PERSON = "person";
    public static final String MOVEMENT = "movement";
    public static final String INFOMATION_LABEL = "INFORMATION";
    public static final String MOVEMENT_LABEL = "MOVEMENT LIST";
    public static final int ADD_MOVEMENT = 1;
    public static final int RESULT_OK = -1;
    public static final int SPACE = 5;
    public static final String API_KEY = "AIzaSyBtW-Sv9HsNkPzCucI-gt93wV7Topx6wXc";
    public static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    public static final String DATABASE_NAME = "PersonData.db";
    public static final String TABLE_INFO_NAME = "InfoData";
    public static final String TABLE_MOVEMENT_NAME = "MovementData";
    public static final int VERSION = 1;
    public static final String ID = "id";
    public static final String COVER = "cover";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String HEIGHT = "height";
    public static final String GENDER = "gender";
    public static final String ADDRESS = "address";
    public static final String HAIR_COLOR = "hairColor";
    public static final String COMMENT = "comment";
    public static final String NOTE = "note";
    public static final String NUMBER = "number";
    public static final String LOCATION = "location";
    public static final String DATE = "date";
    public static final String TABLE_CREATE_INFO = "create table " + TABLE_INFO_NAME +"( " + ID + " integer primary key, " + COVER + " text, " + NAME + " text not null, " + AGE + " integer not null, " + HEIGHT + " integer not null, " + GENDER + " text not null, " + HAIR_COLOR + " text, " + ADDRESS + " text, " + COMMENT + " text);";
    public static final String TABLE_CREATE_MOVEMENT = "create table " + TABLE_MOVEMENT_NAME +"("+ NUMBER + " integer primary key autoincrement, " + ID + " integer not null, " + NAME + " text not null, " + LOCATION +" text not null, " + NOTE + " text not null, " + DATE + " text not null);";
    public static final String UPDATE_TRIGGER = "create trigger insert_trigger after update on InfoData begin update MovementData set name = new.name where id = old.id; end;";

}
