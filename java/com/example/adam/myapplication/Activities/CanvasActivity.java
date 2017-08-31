package com.example.adam.myapplication.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.adam.myapplication.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * The purpose of this activity is to handle all actions made on the dates selected on the calendar-view.
 * The bitmaps are stored as a png and reopened once the user has reselected the date.
 * If a MQTT connection is established the image will be sent across to the external software.
 * This Class implements the android libray : PHAO MQTT - To send images across in a 64-Base String format.
 *
 * Author: Adam Children
 * (MQTT library used: PAHO Android MQTT - (IBM 2017))
 */

public class CanvasActivity extends AppCompatActivity {

    com.example.adam.myapplication.CanvasView mvs;                         //The paint canvas being used.
    Bitmap bm;                                                             //Bitmap storing the user drawings being passed through.
    int day, month, year;                                                  //Date selected
    boolean con;                                                           //Checking for successful connection
    MqttAndroidClient client;                                              //MQTT client being used
    ByteArrayOutputStream bos = new ByteArrayOutputStream();               //Bitmap Output Stream for compressing Bitmap image into 64-base String.
    String topic = "ac573∕Feeds∕Calendro";                                  //MQTT Topic to subscribe/publish to
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(getWindow().FEATURE_CONTENT_TRANSITIONS); //Apply transition upon opening application.
        setContentView(R.layout.appcal);                                     //Set layout to XML file.
        DisplayMetrics met = new DisplayMetrics();                           //Metrics of the layout, not to cover the entire screen.
        getWindowManager().getDefaultDisplay().getMetrics(met);              //Gather the metrics to be adapted.
        int x = met.widthPixels;                                             //X and Y parameters for the popup window display.
        int y = met.heightPixels;
        Bundle b = getIntent().getExtras();                                  //Retrieve data from previous Activity
        day = b.getInt("day");
        month = b.getInt("month");
        year = b.getInt("year");
        name = (":"+b.getString("name"));
        //Times the width and height by the amount adjacent to the edges.
        getWindow().setLayout((int) (x * .8), (int) (y * .8));                  //Window pop up width and height
        mvs = (com.example.adam.myapplication.CanvasView) findViewById(R.id.paintCanvas);    //Gather the paint canvas information for the acitvity
        ContextWrapper cw = new ContextWrapper(getApplicationContext());        //Get this context
        File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);                 //Get local app directory
        File file = new File(dir, (day + "_" + month + "_" + year) + ".png");    //Set the bitmap to this specific date
        if (file.exists()) {
            mvs.drawBitmap(file);                                               //If bitmap has already been created, recover this file.
        }

        mvs.setDrawingCacheEnabled(true);                                       //Allow the canvas data to be retrieved.


    }

    /*Once the canvas/layout has been closed, store the bitmap for later use.
      Also attempt to send the image a a 64 bit string to the mqtt server.
     */
    @Override
    public void finish() {
        if (catchBitmap() != null) {
            if (con) {
                connected(catchBitmap());   //Send bitmap to the mqtt server.

            }
        }
        super.finish(); //End activity completely
    }

    /*
        Connect to HiveMQTT broker through the use of PAHO MQTT.
        Documentation followed at : HiveMQ.com
     */
    private boolean connected(Bitmap img) {
        if (img == null) {
            String clientId = MqttClient.generateClientId();                                            //Randomly generated ClientID to prevent connection interference.
            client = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);     //Hive broker used to establish MQTT connection.
            MqttConnectOptions options = new MqttConnectOptions();                                      //New connection rules for MQTT connection.
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);                                //Latest version of MQTT
            try {
                IMqttToken token = client.connect(options);                                             //MQTT-Token to gather whether a connection is established or not.
                token.setActionCallback(new IMqttActionListener() {
                    //If there is a connection perform this action.
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        String dayS= Integer.toString(day);
                        String monthS= Integer.toString(month);
                        if(day <= 10){                                                   //Make char sequence 8 in length for retrieval of messages on the front-end able to handle.
                           dayS = ("0" + day);
                        }if(month <= 10) {
                            monthS = ("0" + month);
                        }
                        String date = (dayS + "_" + monthS + "_" + year);                //Store current date and use this date as the first message to be sent across.
                        try {
                            MqttMessage message = new MqttMessage();                          //Convert the image Byte array to the MQTT formatted message.
                            message.setPayload(name.getBytes());                              //Message to be sent = date of the image.
                            message.setQos(0);                                                //Quality of service set to 0 to send image only once, to prevent duplicates.
                            message.setRetained(false);                                       //Do not save message sent.
                            client.publish(topic, message);                                   //Post name of user to MQTT broker
                            MqttMessage message2 = new MqttMessage();                          //Convert the image Byte array to the MQTT formatted message.
                            message2.setPayload(date.getBytes());
                            client.publish(topic, message2);                                   //Post Date of image
                            con = true;                                                                         //Inform activity that the connection is infact live.
                            Toast.makeText(getBaseContext(), "Connection Successful", Toast.LENGTH_SHORT).show();
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                    //If there is not an connection
                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Connection was not successful, toast message is display stating this.
                        Toast.makeText(getBaseContext(), "No connection has been found", Toast.LENGTH_SHORT).show();
                        con = false;
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
                return false;

            }
            return true;
        } else {              //If bitmap is provided.
            if (con && client.isConnected()) {

                img.compress(Bitmap.CompressFormat.PNG, 100, bos);
                String encode = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);           //Convert bitmap into base64 image.
                try {
                    MqttMessage message = new MqttMessage(encode.getBytes());                       //Convert the image Byte array to the MQTT formatted message.
                    client.publish(topic, message);                                                 //Publish Image through payload to this server.
                    //client.publish(topic, message, 0, null);
                    Toast.makeText(getBaseContext(), day + "_" + month + "_" + year + " : Image sent successfully", Toast.LENGTH_SHORT).show();      //Display this toast once the image has successfully been sent to the MQTT cloud broker.
                    //client.disconnect();
                    return true;
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        }

        return false;
    }

    /*Save bitmaps specific to the date.
        The bitmaps are stored as a png file for memory purposes.
     */
    public Bitmap catchBitmap() {
        try {
            mvs.buildDrawingCache();                                                //Gather all data.
            Bitmap bitmap = Bitmap.createBitmap(mvs.getDrawingCache());             //Gather the drawing made on the canvas from the view and store the data onto a bitmap.
            ContextWrapper cw = new ContextWrapper(getApplicationContext());        //Get this context
            File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);                 //Get local app directory
            File file = new File(dir, (day + "_" + month + "_" + year) + ".png");    //Set the bitmap to this specific date
            if (file.exists()) {
                // file.deleteOnExit();  //Update file with the latest bitmap edits made by the user.
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));        //Compress the bitmap into a png file.
            bm = bitmap;
        } catch (Exception e) {

            e.printStackTrace();                                                     //Print reason for crash, assume bitmap was corrupt.

        }
        return bm;
    }

    @Override
    public void onStart() {
        super.onStart();                                                            //Calls super class to ensure all runs before the code bellow.
        connected(null);                                                            //Establish a connection to the MQTT server being used.
        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());        //Get this context.
            File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);                 //Get local app directory.
            File file = new File(dir, (day + "_" + month + "_" + year) + ".png");   //Set the bitmap to this specific date.
            if (file.exists()) {
                bm = Bitmap.createBitmap(540, 512, Bitmap.Config.ARGB_8888);        //Size of bitmap.
                FileOutputStream fos = new FileOutputStream(file);                  //File output stream to store image in correct location.
                bm.compress(Bitmap.CompressFormat.PNG, 100, fos);                   //Compress the bitmap into a png file.
                mvs.setBitmap(bm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    ---------------------------------------------------------------------------
        Button action listeners for the canvas display

        Resetbut = Reset the radio buttons to it's original state

     */
    private void resetBut(int j) throws ArrayIndexOutOfBoundsException {
        RadioButton[] buttons = new RadioButton[4]; //Holds 4 radio buttons
        buttons[0] = (RadioButton) findViewById(R.id.RedBut);    //red
        buttons[1] = (RadioButton) findViewById(R.id.BlueBut);   //blue
        buttons[2] = (RadioButton) findViewById(R.id.BlackBut);  //black
        buttons[3] = (RadioButton) findViewById(R.id.GreenBut);  //green
        for (int i = 0; i < buttons.length; i++) {
            if (i != j) {
                buttons[i].setChecked(false);

            }

        }

    }

    /*Change paint colours of the strokes being used */
    public void onClick(View v) {
        if (v instanceof RadioButton) {


            // If radio button is red, set paint to red
            if (v.getId() == R.id.RedBut) {
                mvs.paintColor(Color.RED);
                resetBut(0);
            }
            // If radio button is blue, set paint to blue
            else if (v.getId() == R.id.BlueBut) {
                mvs.paintColor(Color.BLUE);
                resetBut(1);
            }
            // If Radio Button is Black, set paint to black
            else if (v.getId() == R.id.BlackBut) {
                mvs.paintColor(Color.BLACK);
                resetBut(2);

            }
            // Else, set paint to green
            else {
                mvs.paintColor(Color.GREEN);
                resetBut(3);
            }
        } else {
            //Allow the user to selectively erase the sections
            if (v.getId() == R.id.ERASE) {
                mvs.eraser();
            }
            //Clear the entire canvas
            else {
                mvs.clearCanvas();

            }
        }


    }

}

