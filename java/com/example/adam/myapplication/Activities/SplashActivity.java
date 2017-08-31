package com.example.adam.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/**
 * Splash screen display once Calendro has been opened.
 * Author: Adam Children
 */

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
        finish();
    }

}