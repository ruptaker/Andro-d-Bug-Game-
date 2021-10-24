package com.examples.drawviewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    public class MyView extends View {
        int x, y;
        Bitmap bmp;
        //    	 Paint bmp_paint;
        public MyView(Context context) {
            super(context);
            x = 0;
            y = 0;
            bmp = BitmapFactory.decodeResource (getResources(), R.drawable.roach1_250);
//              bmp_paint = new Paint();
//              bmp_paint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        protected void onDraw (Canvas canvas) {
            x = (x + 1) % canvas.getWidth();
            y = (y + 1) % canvas.getHeight();
            int radius;
            radius = 100;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            canvas.drawBitmap (bmp, canvas.getWidth()/2-bmp.getWidth()/2, canvas.getHeight()/2-bmp.getHeight()/2, null);

            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawCircle (x/2, y/2, radius, paint);
            invalidate();
        }
    }
}
