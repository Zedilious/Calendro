package com.example.adam.myapplication;


import android.graphics.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * This view class allows the user to draw on the canvas screen.
 * The color of the paint can be adjusted by the radio button supplied on appcal.
 * This will then save the users canvas state once the user has closed the view and edit it again once reopened.
 *
 * Author: Adam Children
 */
public class CanvasView extends View {

    private View mView; //New View for the users date selected
    public Bitmap bm;   //Store the canvas data into this bitmap and when finished save as a image file to internal directory.
    protected Paint p = new Paint();    // Paint colours being used.
    protected Path path = new Path();   //Path of the stroke on the canvas.
    //Store all paths into an array.
    private HashMap<Path,Paint> paths = new HashMap<Path,Paint>();
    public Canvas c;            //Canvas used to import original Bitmap image.
    Bitmap bmp;

    public CanvasView(Context context, AttributeSet As) {
        super(context, As, 0);
        p.setAntiAlias(true);               //Curved lines.
        p.setColor(Color.BLACK);            //Default colour being used "Black"
        p.setStrokeJoin(Paint.Join.ROUND); //Make the movement of the finger be recognised as round.
        p.setStyle(Paint.Style.STROKE); //What you want to draw
        p.setStrokeWidth(0);            //Strength of the stroke used.
        setDrawingCacheEnabled(true);   //Enable new drawings to be accessed.
        c = new Canvas();
        bmp = null;                     //Previous Bitmap design.
    }

    /*
    Needs to inflate the original XML layout i've designed.
    Otherwise this canvas will take up the entirety of the layout and override other components.
     */
    @Override
    protected void onFinishInflate() {
        mView = (View) findViewById(R.id.appcal);
    }

    /*
    Inbuilt Function of drawing to the canvas in real time
    The method works as an ActionListener as such to when the Users touches the canvas at any point.
     */
    @Override
    public void onDraw(Canvas canvas) {
        //Draw the current path
        if (bmp != null) {
            canvas.drawBitmap(bmp, 0, 0, p);    //Ensure old bitmap is always being displayed.
        }
        canvas.drawPath(path, p);                //Draw new stroke path with the colour selected.
        invalidate();                           //Show this happening in action instantaneously.
    }



    //Returns the paint colour of the stroke.
    public Paint paint() {
        return p;
    }

    //Sets the stroke paint colour selected by the user.
    public void paintColor(int c) {
        // paths.add(new Path());
        p.setColor(c);
        p.setStrokeWidth(0);;
        paths.put(path,p);
        invalidate();
    }

    /*Make the entire canvas white to clear the image */
    public void eraser() {
        path.rewind();
        invalidate();
    }


    /* Clear entire canvas */
    public void clearCanvas() {
        path.reset();       //Reset entire canvas
        bmp = null;         //Remove old bitmap to clear canvas entirely.
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //X and Y axis of the users finger location.
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: //Move to finger position.
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:   //Draw the line to this position.
                path.lineTo(x, y);
                return true;
            case MotionEvent.ACTION_UP:     //Finger off screen
                break;
            default:                        //If nothing is happening
                return false;
        }

        //Schedule repaint
        invalidate();
        return true;
    }

    /*
    Prepare old bitmap for use.
    This method is accessed from external class 'Main Interface'.
     */
    public boolean setBitmap(Bitmap bitmap) throws FileNotFoundException {
        bm = bitmap;
        if (bm != null) {
            Canvas canvas = new Canvas(bm.copy(Bitmap.Config.ARGB_8888, true));
            return true;
        } else {
            return false;
        }
    }

    /*
        If bitmap has already been created under this date, load up the previous image.
     */
    public void drawBitmap(File file) {
        bmp = BitmapFactory.decodeFile(file.toString());   //Gather Bitmap data.
        c.drawBitmap(bmp, 0, 0, p);                            //Draw the bitmap to the canvas.
    }
}

