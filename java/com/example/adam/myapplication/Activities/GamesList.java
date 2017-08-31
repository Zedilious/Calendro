package com.example.adam.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.adam.myapplication.Games.MatchTheFace;
import com.example.adam.myapplication.Games.MatchingCards;
import com.example.adam.myapplication.R;

/**
 * Created by Adam on 16/03/2017.
 */

public class GamesList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.gamedemo);                               //Set layout to XML file.

        ImageView iv1 = (ImageView)findViewById(R.id.game1);
        ImageView iv2 = (ImageView)findViewById(R.id.game2);

        iv1.setImageResource(R.drawable.whoisthis);
        iv1.setScaleType(ImageView.ScaleType.FIT_START);
        iv2.setImageResource(R.drawable.memorygame);
        iv2.setScaleType(ImageView.ScaleType.FIT_START);
    }

    public void onClick(View v) {
        //Skip the game but activate on next launch of the application
        if (v.getId() == R.id.ntp){
            Intent i = new Intent(GamesList.this, MatchTheFace.class);
            startActivity(i);
        }
        //Else start the game!
        else{

            Intent i = new Intent(GamesList.this, MatchingCards.class);
            startActivity(i);
            /*
            Implement game here with the Jar file jack has created.
             */
        }

    }

}

