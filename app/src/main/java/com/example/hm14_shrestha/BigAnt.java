package com.example.hm14_shrestha;

import android.graphics.Canvas;
import com.example.hm14_shrestha.R;

public class BigAnt {

    // States of a Ant
    enum AntState {
        Dead,
        ComingBackToLife,
        Alive, 			    // in the game
        DrawDead,			// draw dead body on screen
    };

    AntState state;			// current state of Ant
    int x,y; // location on screen (in screen coordinates)
    int i=1,j=2;
    int k=1,l=2;
    int touchcount1=0,touchcount2=0;
    int newtouch1=0,newtouch2=0;
    double speed;			// speed of Ant (in pixels per second)
    // All times are in seconds
    float timeToBirth;		// # seconds till birth
    float startBirthTimer;	// starting timestamp when decide to be born
    float deathTime;		// time of death
    float animateTimer;		// used to move and animate the Ant

    // Ant starts not alive
    public BigAnt () {
        state = AntState.Dead;
    }

    // Ant birth processing
    public void birth (Canvas canvas) {
        if (state == AntState.Dead) {
            state = AntState.ComingBackToLife;
            startBirthTimer = System.nanoTime() / 1000000000f;
        }
        // Check if Ant is alive yet
        else if (state == AntState.ComingBackToLife) {
            float curTime = System.nanoTime() / 1000000000f;
            // Has birth timer expired?
            if (curTime - deathTime >20) {
                // If so, then bring Ant to life
                state = AntState.Alive;
                // Set Ant starting location at top of screen
                x = (int)(Math.random() * canvas.getWidth());
                // Keep entire Ant on screen
                if (x < Assets.bigAnt1.getWidth()/2)
                    x = Assets.bigAnt1.getWidth()/2;
                else if (x > canvas.getWidth() - Assets.bigAnt1.getWidth()/2)
                    x = canvas.getWidth() - Assets.bigAnt1.getWidth()/2;
                y = 0;
                // Set speed of this Ant
                speed = canvas.getHeight() / 6; // no faster than 1/4 a screen per second
                animateTimer = curTime;
            }
        }
    }

    public void move (Canvas canvas) {
        // Make sure this Ant is alive
        if (state == AntState.Alive) {
            // Get elapsed time since last call here
            float curTime = System.nanoTime() / 1000000000f;
            float elapsedTime = curTime - animateTimer;
            animateTimer = curTime;
            // Compute the amount of pixels to move (vertically down the screen)
            y += (speed * elapsedTime);
            // Draw Ant on screen

            if(i<j) {
                canvas.drawBitmap(Assets.bigAnt1, x, y, null);
                i++;
            }
            else{
                canvas.drawBitmap(Assets.bigAnt2, x, y, null);
                j++;
            }

            // Has it reached the bottom of the screen?
            if (y >= canvas.getHeight()) {
                // Kill the Ant
                state = AntState.Dead;
                // Subtract 1 life
                Assets.soundPool.play(Assets.bottomSound, 1, 1, 1, 0, 1);
                Assets.livesLeft--;
            }
        }
    }

    // Process touch to see if kills Ant - return true if Ant killed
    public boolean supertouched (Canvas canvas, int touchx, int touchy) {
        boolean touched = false;

        // Make sure this Ant is alive
        if (state == AntState.Alive) {
            // Compute distance between touch and center of Ant
            float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
            // Is this close enough for a kill?
            if (dis <= Assets.bigAnt1.getWidth()*0.75f) {
                touchcount1++;
            }
            else if (dis <= Assets.bigAnt2.getWidth()*0.75f) {
                touchcount2++;
            }
            if (touchcount1-newtouch1 == 4 && dis <= Assets.bigAnt1.getWidth()*0.75f) {
                newtouch1=touchcount1;
                state = AntState.DrawDead;    // need to draw dead body on screen for a while
                touched = true;
                // Record time of death
                deathTime = System.nanoTime() / 1000000000f;
            }
            else if (touchcount2-newtouch2 == 4 && dis <= Assets.bigAnt1.getWidth()*0.75f) {
                newtouch2=touchcount2;
                state = AntState.DrawDead;    // need to draw dead body on screen for a while
                touched = true;
                // Record time of death
                deathTime = System.nanoTime() / 1000000000f;
            }
        }
        return (touched);
    }
    // Draw dead Ant body on screen, if needed
    public void drawDead (Canvas canvas) {
        if (state == AntState.DrawDead) {
            canvas.drawBitmap(Assets.bigAnt3, x, y, null);
            // Get time since death
            float curTime = System.nanoTime() / 1000000000f;
            float timeSinceDeath = curTime - deathTime;
            if (timeSinceDeath > 4)
                state = AntState.Dead;
        }
    }

}

