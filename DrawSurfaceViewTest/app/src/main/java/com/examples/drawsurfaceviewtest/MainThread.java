package com.examples.drawsurfaceviewtest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private SurfaceHolder holder;
    private boolean isRunning = false;
    int x, y;
    private static final Object lock = new Object();

    public MainThread (SurfaceHolder surfaceHolder) {
        holder = surfaceHolder;
        x = y = 0;
    }

    public void setRunning(boolean b) {
        isRunning = b;
    }

    public void setXY (int x, int y) {
        synchronized (lock) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void run() {
        //while (!isRunning);
        while (isRunning) {
            // Lock the canvas before drawing
            if (holder != null) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    // Perform drawing operations on the canvas
                    render(canvas);
                    // After drawing, unlock the canvas and display it
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void render (Canvas canvas) {
        int xx, yy;
        // Fill the entire canvas' bitmap with 'black'
        canvas.drawColor(Color.BLACK);
        // Instantiate a Paint object
        Paint paint = new Paint();
        // Set the paint color to 'white'
        paint.setColor(Color.WHITE);
        // Draw a white circle at position (100, 100) with a radius of 100
        synchronized (lock) {
            x = (x + 1) % canvas.getWidth();
            y = (y + 1) % canvas.getHeight();
            xx = x;
            yy = y;
        }
        canvas.drawCircle(xx, yy, 100, paint);
    }
}
