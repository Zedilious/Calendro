package com.example.adam.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.adam.myapplication.R;
/*
Start screen of the application.
Provides the user an option to choose whether they would like to test out the games or the main application itself.
Author: Adam Children
 */
public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demodisplay);               //Set Layout to this XML file
        ImageView iv = (ImageView)findViewById(R.id.logoDemo);  //ImageView to where the logo will be displayed
        int img = R.drawable.loadingfont;                   //Drawable file location.
        iv.setImageResource(img);                           //Logo of the application (Calendro)
    }

    /*
    Button action listeners.
    If mainapp- open main application (calendar).
    if gameT- open games list.
    This activity is never closed in the background.
     */
    public void onClick(View v){
        if(v.getId() == R.id.mainApp){
            Intent i = new Intent(StartScreenActivity.this, WhoAreYou.class);
            startActivity(i);
        }
        else if(v.getId() == R.id.gameT){
            Intent i = new Intent(StartScreenActivity.this, GamesList.class);
            startActivity(i);
        }

    }



}
