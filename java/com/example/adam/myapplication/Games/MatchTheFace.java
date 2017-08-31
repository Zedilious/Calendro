package com.example.adam.myapplication.Games;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.myapplication.R;

import java.util.ArrayList;
import java.util.Random;

public class MatchTheFace extends AppCompatActivity {

    int[] icons;                                                    //All of the image drawables stored into a Integer array.
    String[] names;                                                    //All of the image drawables names stored into a String array.
    public ArrayList<Integer> imgsLeft = new ArrayList<Integer>();        //All of the left over image drawables stored into a Integer ArrayList.
    public ArrayList<String> stringNames = new ArrayList<String>();        //All of the left over image drawables names stored into a String array.
    int img, img2, img3, img4, img5;                                //Img Drawables stored into these Integers.
    String input, image;                                            //Input from the User in the EditText box/ Current image name.
    EditText et;                                                    //EditText box located on the XML layout file.
    ImageView iv;                                                    //ImageView of the current image, this iamge is what the user attempts to guess.
    int deduction, correct;                                            //Deduction points of incorrect answers/ Correct answer points.
    int answers = 0;                                                //Number of images guessed.
    Button pA, pass, confirm;                                        //Buttons used.
    TextView congrats, instruction, score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchtheface);                        //Set layout to this XML file.

        //Retrieve the integer values of the drawables being used.
        img = R.drawable.stefankahrs;
        img2 = R.drawable.sallyfincher;
        img3 = R.drawable.simonthompson;
        img4 = R.drawable.olafchitil;
        img5 = R.drawable.scottowens;

        icons = new int[]{img, img2, img3, img4, img5};            //Images stored into an array.

        score = (TextView) findViewById(R.id.scoreI);                //Score TextView located in the XML layout file.
        names = new String[]{"stefankahrs", "sallyfincher", "simonthompson", "olafchitil", "scottowens"};        //Names of the files being used.

        Random gen = new Random();                                    //Random number generator used to select the first image being displayed
        int rand = gen.nextInt(icons.length) + 0;


        et = (EditText) findViewById(R.id.inputI);                    //The input text provided by the user.
        instruction = (TextView) findViewById(R.id.wit);                //Instructions on the XML layout.

        congrats = (TextView) findViewById(R.id.congrats);            //Congratulations message when game is completed.
        congrats.setVisibility(View.INVISIBLE);

        //create arraylist with all left over images.
        for (int i = 0; i < icons.length; i++) {
            if (icons[i] != icons[rand]) {
                imgsLeft.add(icons[i]);
                stringNames.add(names[i]);
            } else {

            }
        }

        image = names[rand];                                        //Gather the current image name for comparrison in the onClick method.
        iv = (ImageView) findViewById(R.id.personI);
        iv.setScaleType(ImageView.ScaleType.FIT_START);
        iv.setImageResource(icons[rand]);

        pass = (Button) findViewById(R.id.pass);
        confirm = (Button) findViewById(R.id.confirmI);

        pA = (Button) findViewById(R.id.pA);
        pA.setVisibility(View.INVISIBLE);
        pA.setActivated(false);
    }

    /*
    Method that all Buttons are linked to for action handling.
     */
    public void onClick(View v) {
        //Confirmed answer from the User.
        if (v.getId() == R.id.confirmI) {
            //Gather the input text from the user in the EditText box located on the XML file.
            input = et.getText().toString().replaceAll("\\s", "").toLowerCase();
            if (input.equals(image)) {
                answers++;                                                //Increment number of images passed.
                if (answers != 5) {
                    correct += 10;                                        //Increment score by 10 points.
                    score.setText("Score: " + (correct - deduction));    //Update score display.
                    Random gen = new Random();                            //Randomly generated integer to select next image to be displayed.
                    int rand = gen.nextInt(imgsLeft.size() - 0) + 0;
                    iv.setImageResource(imgsLeft.get(rand));            //Set next image display to the randomly select Drawable integer.
                    image = stringNames.get(rand);                        //Update to the latest image name.
                    imgsLeft.remove(rand);                                //Remove new image from array list.
                    stringNames.remove(rand);                            //Remove image name from String array.
                    et.setText("");                                        //Reset the EditText box.
                } else {
                    restart();                                            //Set the XML file to restart screen.
                    score.setText("Score: " + (correct - deduction));
                }
            } else {
                deduction++;                                            //Increment deduction by one.
                et.setText("");                                            //Reset EditText box to empty.
            }

        }
        //If the User skips the current image.
        else if (v.getId() == R.id.pass) {
            answers++;                                                    //Increment number of images passe
            if (answers != 5) {
                deduction += 5;                                            //Increment Score by 10 points
                score.setText("Score: " + (correct - deduction));        //Update Score Display
                Random gen = new Random();                                //Randomly generated integer to select next image to be displayed.
                int rand = gen.nextInt(imgsLeft.size() - 0) + 0;
                iv.setImageResource(imgsLeft.get(rand));                //Set next image display to the randomly select Drawable integer.
                image = stringNames.get(rand);                            //Update to the latest image name.
                imgsLeft.remove(rand);                                    //Remove new image from array list.
                stringNames.remove(rand);                                //Remove image name from String array.
                et.setText("");                                            //Reset the EditText box.
            } else {
                restart();                                                //Set the XML file to restart screen.
                score.setText("Score: " + (correct - deduction));
            }
        } else {
            Intent i = new Intent(MatchTheFace.this, MatchTheFace.class);    //Get next game ready.
            startActivity(i);                                                //WhoAreYou new game.
            finish();                                                        //End current activity.

        }
    }

    /*
    This method provides the user the option to restart the game.
    Certain objects on the XML layout are set invisible  in order for the final display to become clear.
    Restart button appears alongside a congratulations message.
     */
    public void restart() {
        pA.setVisibility(View.VISIBLE);
        pA.setActivated(true);
        confirm.setVisibility(View.INVISIBLE);
        confirm.setActivated(false);
        pass.setVisibility(View.INVISIBLE);
        pass.setActivated(false);
        congrats.setVisibility(View.VISIBLE);
        et.setEnabled(false);
        et.setVisibility(View.INVISIBLE);
        instruction.setVisibility(View.INVISIBLE);
    }


}



