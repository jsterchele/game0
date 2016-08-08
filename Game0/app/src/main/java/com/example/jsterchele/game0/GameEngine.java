package com.example.jsterchele.game0;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

/***************ACTIVITY***************/
public class GameEngine extends Activity {
    // gameView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    GameView gameView;

/***********onCreate(Bundle)*********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize gameView and set it as the view
        gameView = new GameView(this);
        setContentView(gameView);

    }// end onCreate

    /**
     * GameView Class
     */
    class GameView extends SurfaceView implements Runnable {

        /**Instance Variables**/

        //Context
        private Context context;

        //creating a new thread
        Thread gameThread = null;

        //SurfaceHolder
        SurfaceHolder myHolder;

        //a boolean that will keep track of the game to determine if the game is running or not
        volatile boolean playing;

        //a Canvas and a Paint object
        Canvas canvas;
        Paint paint;
        Paint white;

        //GameFrameRate
        long fps;

        //help calculate the fps
        private long timeThisFrame;

        //Object of type Bitmap -> character
        Bitmap bitmapJ = BitmapFactory.decodeResource(this.getResources(), R.drawable.bob);
        //Zookas will be represented by bitmaps
        Bitmap a = BitmapFactory.decodeResource(this.getResources(), R.drawable.a);
        Bitmap b = BitmapFactory.decodeResource(this.getResources(), R.drawable.b);
        Bitmap c = BitmapFactory.decodeResource(this.getResources(), R.drawable.c);
        Bitmap d = BitmapFactory.decodeResource(this.getResources(), R.drawable.d);
        //scale the bitmap images
        Bitmap e = Bitmap.createScaledBitmap(a, 250, 250, false);
        Bitmap f = Bitmap.createScaledBitmap(b, 250, 250, false);
        Bitmap g = Bitmap.createScaledBitmap(c, 250, 250, false);
        Bitmap h = Bitmap.createScaledBitmap(d, 250, 250, false);

        // The  bullets
        private Bullet[] bullets = new Bullet[25];

        //The Zookas
        private int numZookas = 4;
        private Zooka[] zookas = new Zooka[numZookas];
        private int nextBullet = 0;
        private int maxBoobBullets = 10;

        //Setting a walk speed -> pixels per second
        float walkSpeedPerSecondY = 0;
        float walkSpeedPerSecondX = 0;
        float walkSpeedPerSecond = 600;

        //Starting position -> in the center of the screen
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        int height = display.heightPixels;

        //sets J in the middle of the screen
        float jXPosition = (width / 2) - (bitmapJ.getWidth() / 2);
        float jYPosition = (height / 2) - (bitmapJ.getHeight() / 2);


        Random rando = new Random();
        /**End Instance Variables**/

        /**
         * GameView Constructor
         */
        public GameView(Context context) {
            //SurfaceView class to set up our object
            super(context);

            //Initialize myHolder and Paint object
            myHolder = getHolder();
            paint = new Paint();
            white = new Paint();
            //prepares the level
            prepareLevel();
        }//end GameView

        /**
         *  Here we will initialize all game objects.
         *  This is help set up the game
         */
        private void prepareLevel(){
            //Initialize the Zookas array
            zookas[0] = new Zooka(context, 0, width, height);
            zookas[1] = new Zooka(context, 1, width, height);
            zookas[2] = new Zooka(context, 2, width, height);
            zookas[3] = new Zooka(context, 3, width, height);
            Log.v("TIT", String.valueOf(zookas[2].getY()));
            //Initialize Bullets
            // Initialize the invadersBullets array
            for(int i = 0; i < bullets.length; i++){
                bullets[i] = new Bullet(height);
            }
        }//end prepareLevel

        /**
         * Run -> game loop
         */
        public void run() {
            nextBullet = 0;
            while (playing) {
                //capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();

                //update the frame and then draw it
                update();
                draw();

                //Calculate the fps
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }
            }//end while
        }//end run()

        /**
         * Updates the J-position
         */
        public void update() {
            //if J is moving -> the player is touching the screen
            if(walkSpeedPerSecondX != 0 )
                jXPosition = jXPosition + (walkSpeedPerSecondX / fps);
            if(walkSpeedPerSecondY != 0 )
                jYPosition = jYPosition + (walkSpeedPerSecondY / fps);

            //if a bullet is active, update it
            for(int i = 0; i < bullets.length; i++){
                if(bullets[i].getStatus()) {
                    bullets[i].update(fps);
                    //If the bullet is off of the screen, set it as inactive so it can be shot again.
                    if(bullets[i].getImpactPointXLeft() <= 0 || bullets[i].getImpactPointXRight() >= width ||
                            bullets[i].getImpactPointYTop() <= 0 || bullets[i].getImpactPointYBottom() >= height)
                        bullets[i].setInactive();
                }
            }

            //Shooting
            int i = 100;
            int r = rando.nextInt(i);
            if(r < 4)
            {
                //bullets[nextBullet].shoot(zookas[r].getX() + zookas[r].getHeight() / 2, zookas[r].getY(), rando.nextInt(4)) ;
                bullets[nextBullet].shoot(zookas[r].getX(), zookas[r].getY(), rando.nextInt(4));

                if(nextBullet >= bullets.length - 1)
                {

                    nextBullet = 0;
                }
                else
                {
                    nextBullet++;
                }
                Log.v("NEXTBULLET", String.valueOf(nextBullet + " " + bullets[nextBullet].getStatus() + bullets[nextBullet].getRect().toString()));
            }
        }//end update()

        /**
         * Draw the newly updated screen
         */
        public void draw() {
            //Check if surface is valid
            if (myHolder.getSurface().isValid()) {
                //Lock the canvas ready to draw
                //Make the drawing surface OUR canvas object
                canvas = myHolder.lockCanvas();

                //Draw the background color
                canvas.drawColor(Color.argb(255, 26, 128, 182));

                //Paint brush color
                paint.setColor(Color.argb(255, 249, 129, 0));
                white.setColor(Color.argb(255, 255, 255, 255));

                //Set text size larger
                paint.setTextSize(50);

                //Display the current FPS onto the screen
                canvas.drawText("FPS:" + fps, 20, 40, paint);

                //Draw J at jXPosition, jYPosition
                canvas.drawBitmap(bitmapJ, jXPosition, jYPosition, paint);

                //Draw Zookas
                canvas.drawBitmap(f, zookas[0].getX(), zookas[0].getY(), paint);
                canvas.drawBitmap(f, zookas[1].getX(), zookas[1].getY(), paint);
                canvas.drawBitmap(f, zookas[2].getX(), zookas[2].getY(), paint);
                canvas.drawBitmap(f, zookas[3].getX(), zookas[3].getY(), paint);

                //Draw bullets, if they are active
                // Update all the invader's bullets if active
                for(int i = 0; i < bullets.length; i++){
                    if(bullets[i].getStatus()) {
                        canvas.drawRect(bullets[i].getRect(), white);
                    }
                }

                //Finally draw everything to the screen and unlock the drawing surface
                myHolder.unlockCanvasAndPost(canvas);
            }//end if
        }//end draw()

        /**
         * Checks if the game is paused
         * Pauses the thread.
         */
        public void pause() {
            playing = false;
            try {
                //when paused, this will shut down the game thread.
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining a thread");
            }
        }//end pause()

        /**
         * Resumes or Starts the game
         */
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }//end resume()

        /**
         * SurfaceView class implements onTouchListener
         * We can override this method and detect screen touches
         */
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            //touch sensors
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            //creating coordinates for J
            float jBtm = jYPosition + bitmapJ.getHeight();
            float jTop = jYPosition;
            float jLeft = jXPosition;
            float jRight = jXPosition + bitmapJ.getWidth();
                /*****************************************
                 * Creating 9 Quadrants for J movement purposes
                               1 |   2    | 3
                             ____|________|____
                                 |        |
                               8 |  [J]   | 4
                             ____|________|____
                                 |        |
                               7 |   6    | 5
                 * Allows the user to touch the screen in relation to J
                 * and have him moving in that direction
                 ******************************************/
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    //Q1
                    if((y < jTop && y < jBtm) && (x < jLeft && x < jLeft)) {
                        walkSpeedPerSecondX = -walkSpeedPerSecond;
                        walkSpeedPerSecondY = -walkSpeedPerSecond;
                        Log.v("Q69", "1");
                    }
                    //Q2
                    if((y < jTop && y < jBtm) && (x > jLeft && x < jRight)) {
                        walkSpeedPerSecondX = 0;
                        walkSpeedPerSecondY = -walkSpeedPerSecond;
                        Log.v("Q69", "2");
                    }
                    //Q3
                    if((y < jTop && y < jBtm) && (x > jLeft && x > jRight)) {
                        walkSpeedPerSecondX = walkSpeedPerSecond;
                        walkSpeedPerSecondY = -walkSpeedPerSecond;
                        Log.v("Q69", "3");
                    }
                    //Q4
                    if((y > jTop && y < jBtm) && (x > jLeft && x > jRight)) {
                        walkSpeedPerSecondX = walkSpeedPerSecond;
                        walkSpeedPerSecondY = 0;
                        Log.v("Q69", "4");
                    }
                    //Q5
                    if((y > jTop && y > jBtm) && (x > jRight && x > jLeft)) {
                        walkSpeedPerSecondX = walkSpeedPerSecond;
                        walkSpeedPerSecondY = walkSpeedPerSecond;
                        Log.v("Q69", "5");
                    }
                    //Q6
                    if((y > jTop && y > jBtm) && (x > jLeft && x < jRight)) {
                        walkSpeedPerSecondX = 0;
                        walkSpeedPerSecondY = walkSpeedPerSecond;
                        Log.v("Q69", "6");
                    }
                    //Q7
                    if((y > jTop && y > jBtm) && (x < jLeft && x < jRight)) {
                        walkSpeedPerSecondX = -walkSpeedPerSecond;
                        walkSpeedPerSecondY = walkSpeedPerSecond;
                        Log.v("Q69", "7");
                    }
                    //Q8
                    if((y > jTop  && y < jBtm) && (x < jLeft && x < jRight)) {
                        walkSpeedPerSecondX = -walkSpeedPerSecond;
                        walkSpeedPerSecondY = 0;
                        Log.v("Q69", "8");
                    }
                    //if you click on J
                    if( (jXPosition + (bitmapJ.getWidth() * .75) > x && x > jXPosition + (bitmapJ.getWidth() * .25) ) && (jYPosition + (bitmapJ.getHeight() * .75) > y && y > jYPosition + (bitmapJ.getHeight() * .25)) ){
                       Toast.makeText(getApplicationContext(),"Stoping touching J's No-No square you pervert.", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case MotionEvent.ACTION_MOVE:
                    //check which direction the touch is moving
                    //Q1
                    if((y < jTop && y < jBtm) && (x < jLeft && x < jLeft)) {
                        walkSpeedPerSecondX = -walkSpeedPerSecond;
                        walkSpeedPerSecondY = -walkSpeedPerSecond;
                        Log.v("Q69", "1");
                    }
                    //Q2
                    if((y < jTop && y < jBtm) && (x > jLeft && x < jRight)) {
                        walkSpeedPerSecondX = 0;
                        walkSpeedPerSecondY = -walkSpeedPerSecond;
                        Log.v("Q69", "2");
                    }
                    //Q3
                    if((y < jTop && y < jBtm) && (x > jLeft && x > jRight)) {
                        walkSpeedPerSecondX = walkSpeedPerSecond;
                        walkSpeedPerSecondY = -walkSpeedPerSecond;
                        Log.v("Q69", "3");
                    }
                    //Q4
                    if((y > jTop && y < jBtm) && (x > jLeft && x > jRight)) {
                        walkSpeedPerSecondX = walkSpeedPerSecond;
                        walkSpeedPerSecondY = 0;
                        Log.v("Q69", "4");
                    }
                    //Q5
                    if((y > jTop && y > jBtm) && (x > jRight && x > jLeft)) {
                        walkSpeedPerSecondX = walkSpeedPerSecond;
                        walkSpeedPerSecondY = walkSpeedPerSecond;
                        Log.v("Q69", "5");
                    }
                    //Q6
                    if((y > jTop && y > jBtm) && (x > jLeft && x < jRight)) {
                        walkSpeedPerSecondX = 0;
                        walkSpeedPerSecondY = walkSpeedPerSecond;
                        Log.v("Q69", "6");
                    }
                    //Q7
                    if((y > jTop && y > jBtm) && (x < jLeft && x < jRight)) {
                        walkSpeedPerSecondX = -walkSpeedPerSecond;
                        walkSpeedPerSecondY = walkSpeedPerSecond;
                        Log.v("Q69", "7");
                    }
                    //Q8
                    if((y > jTop  && y < jBtm) && (x < jLeft && x < jRight)) {
                        walkSpeedPerSecondX = -walkSpeedPerSecond;
                        walkSpeedPerSecondY = 0;
                        Log.v("Q69", "8");
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    //Set walkspeedspersecond to 0
                    walkSpeedPerSecondX = 0;
                    walkSpeedPerSecondY = 0;
                    break;
            }//end switch
            return true;
        }//end onTouchEvent()
    }//end of GameView class

    /**
     * onResume()
     * This method executes when the player starts the game
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }//end onResume()

    /**
     * onPause()
     * This method executes when the player quits the game
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }//end onPause()

}//end [Class] GameEngine