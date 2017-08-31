package com.example.adam.myapplication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.adam.myapplication.R;

/**
 * This is where the main activity happens.
 * This references to the activity_main layout xml file.
 * A calendar is displayed and the user can use the provided canvas to input information.
 * Author: Adam Children
 */
public class CalendarActivity extends AppCompatActivity {

    private String name;            //Name of the current user of the application.
    ImageView iv;                   //Image of the user in the top right corner of the screen.
    String img;                     //Img file name of users icon.
    Uri u;                          //Image URI of image selected by the User.
    boolean gamePlayed = false;     //Gathers data of whether a game has been played or not.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //SplashActivity Activity-main.java file

        /*Creating the calendar and making the time accurate */
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MMMM/d/E", Locale.ENGLISH);
        String time = date.format(cal.getTime());
        String[] calValues = time.split("/", 0);            //Too apply the correct data to the calendar
        iv = (ImageView) findViewById(R.id.userIcon);        //Image Icon where the Users photo will be located.
        Bundle b = getIntent().getExtras();                 //Get intent information from previous activity.
        name = b.getString("name");                         //Name from intent.
        if (b.size() > 1) {                                 //If a URI was selected.
            u = Uri.parse(b.getString("imageUri"));
        }
        TextView p = (TextView) findViewById(R.id.uName);   //Get the textview from the xml file
        p.setText("Welcome " + name.toUpperCase());         // Welcome message on the xml output.
        img = b.getString("image");                         // Get image URI name located in the Drawable folder.

        //Check to see if image has been selected
        if (u == null) {                                             // If image doesn't exist then continue without image

        } else {
            //Update the image view on the activity page.

            iv.setImageURI(u);                                  //Set the image to the selected URI.
            iv.setScaleType(ImageView.ScaleType.FIT_START);     //Ensure the image fits the padding provided.

        }


        CalendarView cw = (CalendarView) findViewById(R.id.calendarView);   //

        //Listens to what date is selected by the user
        cw.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView View, int year, int month, int day) {
                //Every 10th of the month will be a game day!
                if (day % 10 == 0 && !gamePlayed) {
                    Toast.makeText(getBaseContext(), "It's time for a game!!!", Toast.LENGTH_SHORT).show();    //Display that today is the game
                    Intent i = new Intent(CalendarActivity.this, GameOfTheWeek.class);
                    startActivity(i);
                    gamePlayed = true;                                                                  //Let the game be postponed till next time the application is used.
                } else {
                    Toast.makeText(getBaseContext(), "Date: " + day + ": " + (month + 1) + ": " + year, Toast.LENGTH_LONG).show();    //Display what day has been selected
                    Intent i = new Intent(CalendarActivity.this, CanvasActivity.class);
                    i.putExtra("name",name);
                    i.putExtra("day", day);                             //Pass the day
                    i.putExtra("month", month + 1);                     //Pass the month
                    i.putExtra("year", year);                           //Pass the year
                    startActivity(i);                                   //WhoAreYou new intent
                }
            }

        });

    }

}
