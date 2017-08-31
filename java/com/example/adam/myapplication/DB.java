package com.example.adam.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Database being used for the following activities:
 * - Image Selector
 * - WhoAreYou
 *
 * Database stores users into the database alongside the users image.
 *
 * Author: Adam
 *
 */

public class DB extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    //DB data being stored in.
    public static final int DATABASE_VERSION = 2;                           //Database version 2 for debugging reasons and to avoid using old database
    public static final String DATABASE_NAME = "uCases.db";                 //Database name
    public static final String TABLE_NAME = "userImg";                      //Table name
    public static final String COLUMN_NAME = "name";                        //Name fo the User
    public static final String COLUMN_URI = "uri";                          //Column for URI image file names.

    SQLiteDatabase db;      //SQL Database to store all of this data into.

    //SQL query being used to create table in database
    public static final String TABLE_CREATE = "create table "+ TABLE_NAME + "(" +
            "name text primary key not null, uri text);";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);                      //Recreate the SQL table stored locally in the users phone
    }

    /*updating the database once new account has been created */
    public void insertCase(SQLCases sc){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, sc.getName());
        //If image has been selected store this data, else leave the column blank.
        if(sc.getImg() != null && sc.getImg() != ""){
            values.put(COLUMN_URI, sc.getImg());         //If image exists, store the image into the database.
        }
        else{
            values.put(COLUMN_URI, "");
        }
        //Insert case data into database
        db.insert(TABLE_NAME,null,values);              //Insert query into the database.
    }

    //Add an image to the database.
    public void addImage(String img, String user ){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_URI, img);
        db.update(TABLE_NAME,values,"name ='" + user+ "'",null);        //Update table with the selected image.


    }

    //Return img file
    public String getImg(String user){
        db = this.getReadableDatabase();
        String q = "select * from " + TABLE_NAME;
        Cursor CURSOR = db.rawQuery(q, null);
        String a,b;
        b = null;
        if(CURSOR.moveToFirst()){       //If the collumn actually exsists do this.

            do {
                a = CURSOR.getString(0);        //Locate name of user in the data base.


                if (a.equals(user)) {
                    b = CURSOR.getString(1);    //Collect the image linked to the user's name
                    break;

                }
            }
            while(CURSOR.moveToNext());
        }
        return b;                       //Return whether an image has been found or not

    }

    //Locate the user within the database.
    public String searchUser(String user){
        db = this.getReadableDatabase();                //Gather current database.
        String q = "select * from " + TABLE_NAME;       //Select all from the table.
        Cursor CURSOR = db.rawQuery(q, null);           //Execute this query and use cursor as a locating methodology.
        String a,b;                                     //Strings for value storage
        b = null;                                       //Default b value;
        if(CURSOR.moveToFirst()){       //If the collumn actually exsists do this.

            do {
                a = CURSOR.getString(0);        //Locate name of user in the data base.


                if (a.equals(user)) {
                    b = a;                      //Collect User's name has been found
                    break;                      //Stop statement and return value collected

                }
            }
            while(CURSOR.moveToNext());         //Keep looking until user has been found in the database.
        } else{
        }
        return b;                       //Return whether an image has been found or not
    }
    //Update table with new entries or new data input
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Query upon updating the database
        String q = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(q);          //Execute staement
        this.onCreate(db);      //Make new database
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);      //Recover old version of table data.
    }





}
