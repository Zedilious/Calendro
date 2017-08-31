package com.example.adam.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.myapplication.Games.MatchTheFace;
import com.example.adam.myapplication.Games.MatchingCards;
import com.example.adam.myapplication.R;

import java.util.Random;

/**
 * Java Class for handling what game is displayed for the week.
 * Author: Adam Children
 */

public class GameOfTheWeek extends AppCompatActivity {
    Random gen;                                                    //New random generator.
    int game1,game2;                                               //Drawable integer values.
    int games[];                                                   //Array list of drawables.
    int rand;                                                      //Random number selector for which game will be displayed
    TextView tv;                                                   //TextView located on the XML file
    String names[] = {"Memory Game", "Name This Person"};          //Names of the games available to the user.
    @Override
    protected void onCreate(Bundle savedInstanceState){
        game1= R.drawable.memorygame;                               //Memory Game drawable image to be displayed.
        game2 = R.drawable.whoisthis;                               //Who Is This Game drawable image to be displayed.
        games = new int[]{game1,game2};                             //Integer array storing both drawable values.
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS); //Add transition to the opening of the activity.
        setContentView(R.layout.game);                                  //Set layout to XML file.
        ImageView iv = (ImageView)findViewById(R.id.gmPhoto);       //Gather the ImageView located on the XML layout file.
        gen = new Random();                                         //Generate what game will be displayed.
        rand = gen.nextInt(games.length) + 0;
        int img = games[rand];                                      //Randomly select what image is to be displayed

        iv.setImageResource(img);                                   //Set img to the randomly selected image.
        iv.setScaleType(ImageView.ScaleType.FIT_START);
       // int randomNum = rand.nextInt((max - min) + 1) + min;
        tv = (TextView)findViewById(R.id.gmName);                   //Set TextView to the appropriate game image.
        tv.setText(names[rand]);                                    //Name of game matching the image.

    }

    public void onClick(View v) {
        //Skip the game but activate on next launch of the application
        if (v.getId() == R.id.gmSkip){
            finish(); //    Finish intent
        }
        //Else start the game!
        else{
            //Look for what game is to be dispalyed.
            Intent i;
            if(rand == 0){
                i = new Intent(GameOfTheWeek.this, MatchingCards.class);
                startActivity(i);
                finish();
            }
            else{
                i = new Intent(GameOfTheWeek.this, MatchTheFace.class);
                startActivity(i);
                finish();
            }
        }

    }



}
