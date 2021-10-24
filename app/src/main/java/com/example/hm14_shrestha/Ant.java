package com.example.hm14_shrestha;

import android.graphics.Canvas;

import java.util.Random;
import com.example.hm14_shrestha.R;

public class Ant {

    // States of a Ant
    enum AntState {
        Dead,
        ComingBackToLife,
        Alive, 			    // in the game
        DrawDead,			// draw dead body on screen
    };

    AntState state;			// current state of ant
    int x,y;                // location on screen (in screen coordinates)
    int i=1,j=2;
    double speed;			// speed of ant (in pixels per second)
    // All times are in seconds
    float timeToBirth;		// # seconds till birth
    float startBirthTimer;	// starting timestamp when decide to be born
    float deathTime;		// time of death
    float animateTimer;		// used to move and animate the bug

    // Ant starts not alive
    public Ant () {
        state = AntState.Dead;
    }

    // Ant birth processing
    public void birth (Canvas canvas) {
        // Bring a ant to life?
        if (state == AntState.Dead) {
            state = AntState.ComingBackToLife;              // Set it to coming alive
            timeToBirth = (float)Math.random () * 5;        // Set a random number of seconds before it comes to life
            startBirthTimer = System.nanoTime() / 1000000000f;  // Note the current time
        }
        else if (state == AntState.ComingBackToLife) {          // Check if bug is alive yet
            float curTime = System.nanoTime() / 1000000000f;
            if (curTime - startBirthTimer >= timeToBirth) {     // Has birth timer expired?
                state = AntState.Alive;                         // If so, then bring bug to life
                x = (int)(Math.random() * canvas.getWidth());   // Set ant starting location at top of screen
                if (x < Assets.ant1.getWidth()/2)               // Keep entire ant on screen
                    x = Assets.ant1.getWidth()/2;
                else if (x > canvas.getWidth() - Assets.ant1.getWidth()/2)
                    x = canvas.getWidth() - Assets.ant1.getWidth()/2;
                y = 0;
                // Set speed of this bug

                int rand=(int)((Math.random()*3)+4);
                speed = canvas.getHeight() /rand; // no faster than 1/4 a screen per second

                // subtract a random amount off of this so some bugs are a little slower
                // ADD CODE HERE
                // Record timestamp of this bug being born
                animateTimer = curTime;
            }
        }
    }

    // Ant movement processing
    public void move (Canvas canvas) {
        if (state == AntState.Alive) {                          // Make sure this Ant is alive
            float curTime = System.nanoTime() / 1000000000f;    // Get elapsed time since last call here
            float elapsedTime = curTime - animateTimer;
            animateTimer = curTime;                             // Compute the amount of pixels to move (vertically down the screen)
            y += (speed * elapsedTime);
            // Draw ant on screen
            if(i<j) {
                canvas.drawBitmap(Assets.ant1, x, y, null);
                i++;
            }
            else{
                canvas.drawBitmap(Assets.ant2, x,  y, null);
                j++;
            }

            // Has it reached the bottom of the screen?
            if (y >= canvas.getHeight()) {
                // Kill the bug
                state = AntState.Dead;
                // Subtract 1 life
                Assets.soundPool.play(Assets.bottomSound, 1, 1, 1, 0, 1);
                Assets.livesLeft--;
            }
        }
    }

    // Process touch to see if kills bug - return true if bug killed
    public boolean touched (Canvas canvas, int touchx, int touchy) {
        boolean touched = false;
        // Make sure this bug is alive
        if (state == AntState.Alive) {
            // Compute distance between touch and center of bug
            float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
            // Is this close enough for a kill?
            if (dis <= Assets.ant1.getWidth()*0.75f) {
                state = AntState.DrawDead;	// need to draw dead body on screen for a while
                touched = true;
                // Record time of death
                deathTime = System.nanoTime() / 1000000000f;
            }
            else if (dis <= Assets.ant2.getWidth()*0.75f) {
                state = AntState.DrawDead;	// need to draw dead body on screen for a while
                touched = true;
                // Record time of death
                deathTime = System.nanoTime() / 1000000000f;
            }
        }
        return (touched);
    }

    Random rand=new Random(2);
    // Draw dead bug body on screen, if needed
    public void drawDead (Canvas canvas) {
        if (state == AntState.DrawDead) {

            int k=rand.nextInt(2);

            canvas.drawBitmap(Assets.ant3, x,  y, null);

            // Get time since death
            float curTime = System.nanoTime() / 1000000000f;
            float timeSinceDeath = curTime - deathTime;
            // Drawn dead body long enough (4 seconds) ?
            if (timeSinceDeath >4)
                state = AntState.Dead;
        }
    }

}
