package com.example.adam.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adam.myapplication.DB;
import com.example.adam.myapplication.R;
import com.example.adam.myapplication.SQLCases;

/*
    Find out the Users name and birthday for later actions throughout the application
 */

public class WhoAreYou extends AppCompatActivity {

    String namePassing;
    private DB db = new DB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whoareyou);


        Button but = (Button)findViewById(R.id.Sub);


        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText called = (EditText) findViewById(R.id.NameA);       //What to be called
                namePassing = called.getText().toString();
                //If a name has not been inputted, display this toast message
                if (namePassing == null || namePassing.trim().length() == 0){
                    Toast t = Toast.makeText(WhoAreYou.this,"Who are you? Enter your name :)", Toast.LENGTH_SHORT);t.show();
                }
                    String located = db.searchUser(namePassing.toLowerCase());
                    if (located != null) {
                        String img = db.getImg(located);
                        if (img != null) {
                            Bundle b = new Bundle();
                            Intent i = new Intent(WhoAreYou.this, CalendarActivity.class);
                            i.putExtra("name", namePassing);
                            i.putExtra("imageUri", img);      //Store image file in bundle data.
                            startActivity(i);
                            finish();
                        }else{
                            Bundle b = new Bundle();
                            Intent i = new Intent(WhoAreYou.this, ImageSelector.class);
                            i.putExtra("name", namePassing);
                            startActivity(i);
                            finish();
                        }


                    }
                else{
                        SQLCases sc = new SQLCases();
                        sc.setName(namePassing.toLowerCase());
                        sc.setImg("");
                        db.insertCase(sc);

                    Bundle b = new Bundle();
                    Intent i = new Intent(WhoAreYou.this, ImageSelector.class);
                    i.putExtra("name", namePassing);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

}
