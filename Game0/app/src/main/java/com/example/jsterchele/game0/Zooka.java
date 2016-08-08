package com.example.jsterchele.game0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by jsterchele on 6/23/2016.
 */
public class Zooka {

    /** Instance Variables **/
    private Context context;
    RectF rect;
    Random gen = new Random();
    Bitmap zookaBit;

    private Bitmap rightZooka;
    private Bitmap leftZooka;
    private Bitmap bottomZooka;

    //height and width of zookas
    private float height = 250;
    private float width = 250;

    //coordinates
    private float x;
    private float y;

    //constants for location
    private int TOP = 0;
    private int RIGHT = 1;
    private int BOTTOM = 2;
    private int LEFT = 3;

    //hidden
    private boolean isVisible;
    /** End Instance Variables **/

    /**
     * Constructor
     * Creates a Zooka
     * @param context
     * @param location the location of the Zooka (TOP/RIGHT/BOTTOM/LEFT)
     * @param screenX  the amount of X pixels on device
     * @param screenY  the amount of Y pixels on device
     */
    public Zooka(Context context, int location, int screenX, int screenY) {

        //Initialize a bitmap
        zookaBit = null;

        //set visibility to TRUE
        isVisible = true;

        //determining where to put the Zooka and what bitmap to use.
        if (location == TOP) {
            x = gen.nextInt(screenX - (int) width) + (int) (.5 * width);
            y = 0;
        }
        else if (location == RIGHT) {
            x = screenX -  width;
            y = gen.nextInt(screenY - (int) height) + (int) (.5 * height);
        }
        else if (location == BOTTOM) {
            y = screenY - height;
            x = gen.nextInt(screenX - (int) width) + (int) (.5 * width);
            //set as BOTTOM bitmap
        } else {
            x = 0;
            y = gen.nextInt(screenY - (int) height) + (int) (.5 * height);
            //set bitmap as LEFT bitmap
        }
        //Initialize a blank RectF
        rect = new RectF(x, y, x + height, y + width);
    }// end of Zooka(Context, int, int, int)


    public Zooka()
    {
        //Initialize a blank RectF
        rect = new RectF();
        //set visibility to TRUE
        isVisible = true;
        y = 25;
        x = 250;
    }

    //Getter and Setter methods

    /**
     * Gets the Bitmap image
     * @return zookaBit the Bitmap image
     */
    public Bitmap getZookaBit(){
        return zookaBit;
    }//emd getZookaBit

    /**
     * Sets visibility as false
     */
    public void setInvisible(){
        isVisible = false;
    }//end setInvisible()

    /**
     * Gets the visibility
     * @return isVisible
     */
    public boolean getVisibility(){
        return isVisible;
    }//end getVisibility()

    /**
     * Gets the RectF object
     * @return rect
     */
    public RectF getRect(){
        return rect;
    }//end getRect()

    /**
     * Gets the Center X coordinate
     * @return x
     */
    public float getX(){
        return x;
    }//end getX()

    /**
     * Gets the Center Y coordinate
     * @return y
     */
    public float getY(){
        return y;
    }//end getY()

    /**
     * Gets the Width
     * @return width
     */
    public float getWidth(){
        return width;
    }//end getWidth()

    /**
     * Gets the Height
     * @return height
     */
    public float getHeight(){
        return height;
    }//end getHeight()

    /**
     * Aiming Method for Zookas.
     * Our version of Artificial Intelligence
     * @param jTop Top Y coordinate
     * @param jRight Right X coordinate
     * @param jBottom Bottom Y coordinate
     * @param jLeft Left X coordinate
     */
    public boolean aimAtPlayer(float jTop, float jRight, float jBottom, float jLeft){
        int randomNumber = -1;

        //Detects if the player is near (Horizontally and Vertically)
        if((jRight > x && jRight < x + width) || ( jLeft > x && jLeft < x + width)
                || (jTop > y && jTop < y + width) || (jBottom > y && jBottom < y + width) )
        {
            //1:100 chance
            randomNumber = gen.nextInt(100);
            if(randomNumber == 69)
            {  //shoots
                return true;
            }//end if
        } //end if

        //Firing randomly (not near the player)
        //1:1000 chance
        randomNumber = gen.nextInt(1000);
        if(randomNumber == 69)
        {   //shoots
            return true;
        }
        //doesn't shoot
        return false;
    }//end aimAtPlayer(float, float, float, float)



}//end [Class] Zooka
