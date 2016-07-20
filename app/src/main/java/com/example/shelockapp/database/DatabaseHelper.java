package com.example.shelockapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.shelockapp.constant.Constant;
import com.example.shelockapp.model.Movement;
import com.example.shelockapp.model.Person;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{
	


	public SQLiteDatabase db;
	
	public DatabaseHelper(Context context) {
		super(context, Constant.DATABASE_NAME, null, Constant.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Constant.TABLE_CREATE_INFO);
		db.execSQL(Constant.TABLE_CREATE_MOVEMENT);
		db.execSQL(Constant.UPDATE_TRIGGER);
		this.db = db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query_info = "DROP TABLE IF EXISTS " + Constant.TABLE_INFO_NAME;
		String query_movement = "DROP TABLE IF EXISTS " + Constant.TABLE_MOVEMENT_NAME;
		db.execSQL(query_info);
		db.execSQL(query_movement);
		onCreate(db);
	}
	
	public long insertInfo(Person person){
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
//		values.put(ID, person.getId());
		Log.e("huongle", "Vao roi");
		values.put(Constant.COVER, person.getCover());
		values.put(Constant.NAME, person.getName());
		values.put(Constant.AGE, person.getAge());
		values.put(Constant.HEIGHT, person.getHeight());
		values.put(Constant.GENDER, person.getGender());
		values.put(Constant.HAIR_COLOR, person.getHairColor());
		values.put(Constant.ADDRESS, person.getAddress());
		values.put(Constant.COMMENT, person.getComment());
		long i = db.insert(Constant.TABLE_INFO_NAME, null, values);
		db.close();
		return i;
	}

	public long insertMovement(Movement movement, Person person){
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constant.ID, person.getId());
		values.put(Constant.NAME, person.getName());
		values.put(Constant.LOCATION, movement.getLocation());
		values.put(Constant.NOTE, movement.getNote());
		values.put(Constant.DATE, movement.getDate());
		long i = db.insert(Constant.TABLE_MOVEMENT_NAME, null, values);
		db.close();
		return i;
	}
	public void loadInfoData(ArrayList<Person> lstPerson){
			db = this.getReadableDatabase();
			String query = "SELECT * FROM " + Constant.TABLE_INFO_NAME;
			Cursor cursor = db.rawQuery(query, null);
				lstPerson.clear();
			if (cursor.moveToFirst()) {
		          do {
		              Person person = new Person();
					  person.setId(cursor.getInt(0));
		              person.setCover(cursor.getString(1));
		              person.setName(cursor.getString(2));
		              person.setAge(cursor.getInt(3));
		              person.setHeight(cursor.getInt(4));
		              person.setGender(cursor.getString(5));
		              person.setHairColor(cursor.getString(6));
		              person.setAddress(cursor.getString(7));
					  person.setComment(cursor.getString(8));

		              lstPerson.add(person);
		          } while (cursor.moveToNext());
		      }
			db.close();
	}
	public void loadMovementData(ArrayList<Movement> lstMovement, int id){
		db = getReadableDatabase();
		String query = "SELECT * FROM " + Constant.TABLE_MOVEMENT_NAME + " where " + Constant.ID + " =?";
		Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
			lstMovement.clear();
		if (cursor.moveToFirst()) {
			do {
				Movement movement = new Movement();
				movement.setNumber(cursor.getInt(0));
				movement.setName(cursor.getString(2));
				movement.setLocation(cursor.getString(3));
				movement.setNote(cursor.getString(4));
				movement.setDate(cursor.getString(5));
				lstMovement.add(movement);
			} while (cursor.moveToNext());
		}
		db.close();
	}

	public void deleteInfo(int id){
		db = this.getWritableDatabase();
		db.delete(Constant.TABLE_INFO_NAME, Constant.ID + "=?", new String[]{String.valueOf(id)});
		db.close();
	}

	public void deleteMovement(int number){
		db = this.getWritableDatabase();
		db.delete(Constant.TABLE_MOVEMENT_NAME, Constant.NUMBER + "=?", new String[]{String.valueOf(number)});
		db.close();
	}

	public void updateInfo(Person person, int id){
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constant.COVER, person.getCover());
		values.put(Constant.NAME, person.getName());
		values.put(Constant.AGE, person.getAge());
		values.put(Constant.HEIGHT, person.getHeight());
		values.put(Constant.GENDER, person.getGender());
		values.put(Constant.ADDRESS, person.getAddress());
		values.put(Constant.HAIR_COLOR, person.getHairColor());
		values.put(Constant.COMMENT, person.getComment());

		db.update(Constant.TABLE_INFO_NAME, values, Constant.ID + "=?", new String[]{String.valueOf(id)});
		db.close();
	}

	public int countMovement(int id) {
		String query = "SELECT  * FROM " + Constant.TABLE_MOVEMENT_NAME + " WHERE " + Constant.ID + " =?";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
}
