package com.example.adam.myapplication;

/**
 *
 * SQLCases being used for the database.
 *
 * Author: Adam
 */

public class SQLCases {
    String name; //Name Of the user
    String img;  //Image linking to the name of the user

    public void setName(String name){
        this.name=name;
    }   //Set Users name

    public void setImg (String img) {this.img = img;}      //Set Image

    public String getImg () {return this.img;}             //Get Image of user

    public String getName(){
        return this.name;
    }          //Get Name of user


}
