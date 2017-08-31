package com.example.adam.myapplication.Games;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.adam.myapplication.R;


import java.util.ArrayList;
import java.util.Random;

/**
 *  Matching card game.
 *  The users attempt to locate and link the ImageButtons to one another.
 *  The buttons are randomly assigned images to which the user has to locate.
 *  If the user is successful the score will increment applying the deductions of attempts it had taken the user to do so.
 *  If unsuccessful the buttons selected will reset.
 *
 *  Author: Adam Children
 *  (Game originally created in Java by: Jack Rodgers and Luke Hipkiss)
 *
 */
public class MatchingCards extends AppCompatActivity {
    static int files[];                                 //Array of all images available
    private int intScore = 0;
    TextView lblSetScore;                               //TextView for displaying the current score.
    private int finished = 0;                           //Integer to track when
    int img, img2, img3, img4, img5;                    //Image integer values
    Button playAgain;                                   //Button that allows the player to restart the game.
    ImageButton buttons[];                              //Array of all ImageButtons.
    ArrayList<Integer> applied = new ArrayList<Integer>();    //Array-List of the Integer values of the ImageButtons
    ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8; //All of the image buttons used on the XML layout file.
    int selected;                                       //Currently selected buttons
    int deduction;                                      //Deduction of the attempts taken by the user.
    boolean check = false;                              //Boolean operator to check whether a button has already been selected.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.memorygame);                    //Set layout to this XML layout file.

        //Storing the integer values from the correct drawable images.
        img = R.drawable.husband;
        img2 = R.drawable.daughter;
        img3 = R.drawable.son;
        img4 = R.drawable.neigbour;
        img5 = R.drawable.egg;
        files = new int[]{img, img2, img3, img4};

        //Initializing the ImageButton values.
        ib1 = (ImageButton) findViewById(R.id.but1);
        ib2 = (ImageButton) findViewById(R.id.but2);
        ib3 = (ImageButton) findViewById(R.id.but3);
        ib4 = (ImageButton) findViewById(R.id.but4);
        ib5 = (ImageButton) findViewById(R.id.but5);
        ib6 = (ImageButton) findViewById(R.id.but6);
        ib7 = (ImageButton) findViewById(R.id.but7);
        ib8 = (ImageButton) findViewById(R.id.but8);

        //Initializing the ImageButton Array with the new values.
        buttons = new ImageButton[]{ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8};


        //Set all buttons to the default image display.
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setScaleType(ImageButton.ScaleType.FIT_START);
            buttons[i].setImageResource(R.drawable.egg);    //Default image display

        }
           //Randomly generate the numbers, to select images from the file
            Random gen = new Random();
            int rand1 = gen.nextInt(1 + 1 - 0) + 0;
            int rand2 = gen.nextInt(3 + 1 - 2) + 2;

                applied.add(rand1);
                applied.add(rand2);
                if (rand2 == 2 && rand1 == 1) {
                    applied.add(3);
                    applied.add(0);
                    applied.add(rand2);
                    applied.add(rand1);
                    applied.add(3);
                    applied.add(0);
                } else if (rand2 == 3 && rand1 == 0) {
                    applied.add(2);
                    applied.add(1);
                    applied.add(rand1);
                    applied.add(rand2);
                    applied.add(1);
                    applied.add(2);
                } else if (rand2 == 3 && rand1 == 1) {
                    applied.add(2);
                    applied.add(0);
                    applied.add(rand2);
                    applied.add(rand1);
                    applied.add(2);
                    applied.add(0);
                } else {
                    applied.add(3);
                    applied.add(3);
                    applied.add(rand1);
                    applied.add(rand2);
                    applied.add(1);
                    applied.add(1);

                }



        lblSetScore = (TextView) findViewById(R.id.score);

        //Play again button hidden until game is completed.
        playAgain = (Button) findViewById(R.id.again);
        playAgain.setVisibility(View.INVISIBLE);
        playAgain.setActivated(false);
    }


    public void onClick(View v) {
        if (!check) {
            if (v.getId() == R.id.but1) {
                apply(buttons[0], 0);
            } else if (v.getId() == R.id.but2) {
                apply(buttons[1], 1);
            } else if (v.getId() == R.id.but3) {
                apply(buttons[2], 2);
            } else if (v.getId() == R.id.but4) {
                apply(buttons[3], 3);
            } else if (v.getId() == R.id.but5) {
                apply(buttons[4], 4);
            } else if (v.getId() == R.id.but6) {
                apply(buttons[5], 5);
            } else if (v.getId() == R.id.but7) {
                apply(buttons[6], 6);
            } else if (v.getId() == R.id.but8) {
                apply(buttons[7], 7);
            } else {
                Intent i = new Intent(MatchingCards.this, MatchingCards.class);
                startActivity(i);
                finish();
            }
        }
        /*If a button has already been selected, locate the next button and apply the following changes.
            If buttons == same ->   The buttons are no longer accessible and score is added alongside reductions.
            If buttons != same ->   The buttons are reset and deduction is stored.
         */
        else {
            if (v.getId() == R.id.but1) {

                if (files[applied.get(0)] != files[applied.get(selected)]) {
                    reset(buttons[0], buttons[selected]);
                } else {
                    result(buttons[0], 0);
                }
            } else if (v.getId() == R.id.but2) {
                if (files[applied.get(1)] != files[applied.get(selected)]) {
                    reset(buttons[1], buttons[selected]);
                } else {
                    result(buttons[1], 1);
                }
            } else if (v.getId() == R.id.but3) {

                if (files[applied.get(2)] != files[applied.get(selected)]) {
                    reset(buttons[2], buttons[selected]);
                } else {
                    result(buttons[2], 2);
                }
            } else if (v.getId() == R.id.but4) {

                if (files[applied.get(3)] != files[applied.get(selected)]) {
                    reset(buttons[3], buttons[selected]);
                } else {
                    result(buttons[3], 3);
                }
            } else if (v.getId() == R.id.but5) {

                if (files[applied.get(4)] != files[applied.get(selected)]) {
                    reset(buttons[4], buttons[selected]);
                } else {
                    result(buttons[4], 4);
                }
            } else if (v.getId() == R.id.but6) {

                if (files[applied.get(5)] != files[applied.get(selected)]) {
                    reset(buttons[5], buttons[selected]);
                } else {
                    result(buttons[5], 5);
                }
            } else if (v.getId() == R.id.but7) {

                if (files[applied.get(6)] != files[applied.get(selected)]) {
                    reset(buttons[6], buttons[selected]);
                } else {
                    result(buttons[6], 6);
                }
            } else if (v.getId() == R.id.but8) {

                if (files[applied.get(7)] != files[applied.get(selected)]) {
                    reset(buttons[7], buttons[selected]);
                } else {
                    result(buttons[7], 7);
                }
            }
        }
    }

    /*
        Reset the image display of the ImgButtons to the default image and enable them.
     */
    public void reset(ImageButton ib1, ImageButton ib2) {
        ib1.setImageResource(img5);    //Default image display
        ib2.setImageResource(img5);    //Default image display
        ib1.setEnabled(true);          //Enable both buttons
        ib2.setEnabled(true);
        deduction += 2;
        selected = 0;                  //Reset onClick IF statement
        check = false;
    }


    /*
        Show image linked to the button.
     */
    protected void apply(ImageButton i, int file) {
        i.setImageResource(files[applied.get(file)]);       //Set image to this image.
        selected = file;                                    //Used for comparison in the onClick method.
        i.setEnabled(false);                                //Access to the button is no longer applicable.
        check = true;                                       //Boolean operator for the onClick method, second section of the code will open.
    }

    /*
    The method checks whether the game is complete and updates the score.
    If the game is complete the user has access to restarting the game.
     */
    public void result(ImageButton ib, int i) {
        finished++;
        lblSetScore.setText("Score: " + ((finished * 10) - deduction));
        ib.setImageResource(files[applied.get(i)]);
        ib.setEnabled(false);
        check = false;
        selected = 0;
        if (finished == files.length) {                                //If the game is finished completely.
            for (ImageButton but : buttons) {
                but.setEnabled(false);
            }
            intScore = 50 - deduction;
            playAgain.setVisibility(View.VISIBLE);
            playAgain.setActivated(true);
            lblSetScore.setText("Score: " + intScore);
        }
    }


}