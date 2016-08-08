package com.example.jsterchele.game0;

import android.graphics.Point;
import android.graphics.RectF;

/**
 * Created by jsterchele on 6/23/2016.
 */
public class Bullet {
   /**Instance Variables **/

    // X & Y coordinates
    private float x;
    private float y;

    //creating a RectF blank object
    private RectF rect;

    //Which way is it shooting
    public final int UP = 0;
    public final int RIGHT = 1;
    public final int DOWN = 2;
    public final int LEFT = 3;

    //Going nowhere
    int heading = -1;

    //setting a speed
    float speed;

    //setting width and height of a bullet
    private int width;
    private int height;

    //if we need to draw and update each bullet
    private boolean isActive;

    /**End Instance Variables**/

    /**
     * Constructs a bullet.
     * @param screenY the Height of the screen. Used for scaling the size of the bullets.
     */
    public Bullet(int screenY){
        //setting a speed
        speed = 750;
        height = screenY / 100 ;
        width = height;
        isActive = false;
        //create the rect for the bullet
        rect = new RectF();
    }//end Bullet(int)

    /**
     * Constructs a bullet.
     * @param screenY the Height of the screen. Used for scaling the size of the bullets.
     * @param w, the Width of the bullet
     */
    public Bullet(int w, int screenY){
        //setting a speed
        speed = 350;
        width = w;
        height = screenY / 20 ;
        isActive = false;
        //create the rect for the bullet
        rect = new RectF();
    }//end Bullet()

    /**
     * Constructs a bullet.
     * @param screenY the Height of the screen. Used for scaling the size of the bullets.
     * @param w, the Width of the bullet
     */
    public Bullet(int w, int screenY, int bulletSpeed){
        //setting a speed
        speed = bulletSpeed;
        width = w;
        height = screenY / 20 ;
        isActive = false;
        //create the rect for the bullet
        rect = new RectF();
    }//end Bullet()

    /**
     * Gets the RectF object
     * @return rect the RectF object
     */
    public RectF getRect(){
        return rect;
    }//end getRect()

    /**
     * Sets the Rect for the bullet
     */
    public void setRect(float left, float top, float right, float bottom)
    {
        rect.left = left;
        rect.top = top;
        rect.right = right;
        rect.bottom = bottom;
    }

    /**
     * Gets the status of the bullet
     * @return isActive boolean for Activity
     */
    public boolean getStatus()
    {
        return isActive;
    }//end getStatus()

    /**
     * Set the bullet status as inactive
     */
    public void setInactive(){
        isActive = false;
    }//end setInActive()

    /**
     * Returns the top Y pixel of the bullet
     * @return Y coordinate
     */
    public float getImpactPointYTop(){
            return rect.top;
    }//end getImpactPointYTop()

    /**
     * Returns the top Y pixel of the bullet
     * @return Y coordinate
     */
    public float getImpactPointYBottom(){
        return rect.bottom;
    }//end getImpactPointYBottom()

    /**
     * Returns the heading X pixel of the bullet
     * @return X coordinate
     */
    public float getImpactPointXLeft(){
            return rect.left;
    }//end getImpactPointXLeft()

    /**
     * Returns the heading X pixel of the bullet
     * @return X coordinate
     */
    public float getImpactPointXRight(){
        return rect.right;
    }//end getImpactPointXLeft()


    /**
     * Shoot Method
     * @param startX the starting X coordinate
     * @param startY the starting Y coordinate
     * @param direction the direction the bullet is heading
     */
    public boolean shoot(float startX, float startY, int direction){
        //confirm the bullet is not already active
        if(!isActive)
        {
            //getting mid point of zooka
            float xMid = (startX + startX + 250) / 2;
            float yMid = (startY + startY + 250) / 2;
            //initialize the starting x and y locations.
            heading = direction;
            isActive = true;
            rect.set(xMid - width, yMid - height, xMid + width, yMid + height);
            //successful bullet launch.
            return true;
        }
        //bullet is already active -- bullet launch failed
        return false;
    }//end shoot(float, float, int)

    /**
     * Update Method for the bullet
     * @param fps Frames Per Second
     */
    public void update(long fps){
        //move up or down
        float l = rect.left;
        float t = rect.top;
        if(heading == UP)
            t = t - speed / fps;
        if (heading == RIGHT)
            l = l - speed / fps;
        if (heading == DOWN)
            t = t + (speed / fps);
        if (heading == LEFT)
            l = l + speed / fps;
        if (heading == -1)
            //do nothing

        //Update the rect
        rect.set(l, t, l + width, t + height);
    }//end update(long)

}//end [Class] Bullet
