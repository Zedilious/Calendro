package com.example.adam.myapplication.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adam.myapplication.DB;
import com.example.adam.myapplication.R;

/**
 * Image selector allows the user to choose a image to represent them on there calendar.
 * The image is shown on the main page before proceeding, this image can be changed at anytime.
 */


public class ImageSelector extends AppCompatActivity {

    private static final int final_img = 1;
    ImageView iv;       //Image view on the xml design file.
    SQLiteDatabase DB;  //Reference to the database to store the image.
    Button imgSelect;   //Button select image: Grants the user to select an image from their gallery.
    Button confirm;     //Button Confirm image: Confirm the image they have selected.
    String imgUri;      //URI string reference to the image selected.
    String name;        //Name (known by)

    private DB db = new DB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgselector);

        imgSelect = (Button)this.findViewById(R.id.imgSel);         //Button to open internal phone gallery.
        confirm = (Button)this.findViewById(R.id.imgConfirm);       //Confirmation button.
        Bundle b = getIntent().getExtras();                         //Retrieve previous data passed from last activity.
        name = b.getString("name");                                 //Name of the user.
        TextView tv = (TextView)this.findViewById(R.id.imgName);
        tv.setText("Choose an image for you " + name);              //Basic output message displaying users name and task to complete.
        iv = (ImageView)this.findViewById(R.id.imageView);          //Location of where the image will be displayed.

    }

    //Button action listeners.
    public void onClick(View v){
        switch (v.getId()){
            //Proceeding onto the next activity with image or without.
            case R.id.imgConfirm:
                Intent i = new Intent(ImageSelector.this, CalendarActivity.class);      //Prepare Calendar Activity.
                // start activity 2.
                i.putExtra("name", name);
                //If image was selected pass data through to next activity.
                if (imgUri != null) {
                    i.putExtra("imageUri", imgUri);      //Store image file in bundle data.
                    db.addImage(imgUri.toString(),name);
                }
                startActivity(i);       //WhoAreYou activity, sending data through.
                finish();               //Close activity completely.
                break;

            //WhoAreYou galery selection activity.
            case R.id.imgSel:

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //Prepare Users internal image gallery
                startActivityForResult(gallery,final_img);                                                      //WhoAreYou Image gallery activity.
                break;
        }


    }
    /*
    This method listens to when the user has selected an image.
    This is then outputted onto the page and reformatted to the designated size.
     */
    @Override
    protected void onActivityResult(int request, int result, Intent i){
        super.onActivityResult(request,result,i);
        //Image is not null or has been selected.
        if(request == final_img && result == RESULT_OK && i != null){
            //Gathering the external image from the image gallery.
            Uri u = i.getData();                //Retrieve the image selected
            imgUri = u.toString();              //Convert and store Image URI into a string to transfer in bundle package.
            Toast.makeText(getBaseContext(), imgUri, Toast.LENGTH_LONG).show();     //Display image file name in toast message
            //Update the image view on the activity page.
            iv.setImageURI(u);                                  //Set Image view to this image.
            iv.setScaleType(ImageView.ScaleType.FIT_START);     //Scale to original height and width.

        }

    }
}
