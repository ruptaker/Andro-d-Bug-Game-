package com.example.hm14_shrestha;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.example.hm14_shrestha.R;

public class MainThread extends Thread implements Runnable {
    private SurfaceHolder holder;
    private Handler handler;
    boolean isTouched;
    private boolean isRunning = false;
    Context context;
    Paint paint;
    Paint paintText;
    int touchX, touchY;
    private static final Object lock = new Object();

    public MainThread (SurfaceHolder surfaceHolder, Context context) {
        holder = surfaceHolder;
        this.context = context;
        handler = new Handler();
        isTouched = false;
    }

    public void setRunning(boolean b) {
        isRunning = b;
    }

    public void setXY (int x, int y) {
        synchronized (lock) {
            touchX = x;
            touchY = y;
            this.isTouched = true;
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                render(canvas);
                holder.unlockCanvasAndPost (canvas);
            }
        }
    }

    // Graphic Loading
    private void loadData (Canvas canvas) {
        Bitmap bmp;
        Bitmap bmp1;
        int newWidth, newHeight;
        float scaleFactor;

        paint = new Paint();
        paintText=new Paint();

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.lifebar);
        newWidth = (int)(canvas.getWidth() * 0.1f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.life = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.foodbar);
        newHeight = (int)(canvas.getHeight() * 0.1f);
        Assets.foodbar = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.gameover);
        newHeight = (int)(canvas.getHeight() * 0.1f);
        Assets.gameover = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant1);
        newWidth = (int)(canvas.getWidth() * 0.2f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.ant1 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant2);
        newWidth = (int)(canvas.getWidth() * 0.2f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.ant2 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.antdead);
        newWidth = (int)(canvas.getWidth() * 0.2f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.ant3 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant3);
        newWidth = (int)(canvas.getWidth() * 0.2f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.bigAnt1 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant4);
        newWidth = (int)(canvas.getWidth() * 0.2f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.bigAnt2 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.antdead);
        newWidth = (int)(canvas.getWidth() * 0.2f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.bigAnt3 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;


        Assets.ant = new Ant[4];
        Assets.ant[0]=new Ant();
        Assets.ant[1]=new Ant();
        Assets.ant[2]=new Ant();
        Assets.ant[3]=new Ant();
        Assets.bigAnt =new BigAnt();
    }

    private void loadBackground (Canvas canvas, int resId) {
        Bitmap bmp = BitmapFactory.decodeResource (context.getResources(), resId);
        Assets.background = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), canvas.getHeight(), false);
        bmp = null;
    }

    private void loadgameover (Canvas canvas, int resId) {
        Bitmap bmp = BitmapFactory.decodeResource (context.getResources(), resId);
        Assets.gameover = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), canvas.getHeight(), false);
        bmp = null;
    }

    private void render (Canvas canvas) {
        int i, x, y;

        switch (Assets.state) {
            case GettingReady:
                loadBackground (canvas, R.drawable.getready);
                loadData(canvas);
                canvas.drawBitmap (Assets.background, 0, 0, null);
                Assets.soundPool.play(Assets.getReadySound, 1, 1, 1, 0, 1);
                Assets.gameTimer = System.nanoTime() / 1000000000f;
                Assets.state = Assets.GameState.Starting;
                Assets.score=0;
                break;
            case Starting:
                canvas.drawBitmap (Assets.background, 0, 0, null);
                Assets.soundPool.play(Assets.gamePlaySound, 1, 1, 1, 0, 1);
                float currentTime = System.nanoTime() / 1000000000f;
                if (currentTime - Assets.gameTimer >= 3) {
                    loadBackground (canvas, R.drawable.gamescreen);//wood
                    Assets.state = Assets.GameState.Running;
                }
                break;
            case Running:
                canvas.drawBitmap (Assets.background, 0, 0, null);
                canvas.drawBitmap (Assets.foodbar, 0, canvas.getHeight()-Assets.foodbar.getHeight(), null);


                paintText.setTextAlign(Paint.Align.LEFT);
                paintText.setColor(Color.BLACK);
                paintText.setTextSize(40);
                int space=150;
                paintText.setTypeface(Typeface.DEFAULT_BOLD);
                String scorePrint=Integer.toString(Assets.score);
                canvas.drawText("SCORE :",canvas.getWidth()/17,canvas.getHeight()/17,paintText);
                canvas.drawText(scorePrint,canvas.getWidth()/17+space,canvas.getHeight()/17,paintText);


                int space1=230;
                int radius = (int)(canvas.getWidth() * 0.05f);
                int spacing = 8;
                x = canvas.getWidth() - radius - spacing;
                y = radius + spacing;
                for (i=0; i<Assets.livesLeft; i++) {
                    //Life Icon
                    canvas.drawBitmap (Assets.life, x, y, null);
                    x -= (radius*2 + spacing);
                }



                int ran=(int) (Math.random()*2);
                if (isTouched) {
                    isTouched = false;
                    boolean isAntKilled=false;
                    boolean isSuperantkill =Assets.bigAnt.supertouched(canvas, touchX, touchY);
                    for(int j=0;j<Assets.ant.length;j++) {
                        isAntKilled = Assets.ant[j].touched(canvas, touchX, touchY);
                    }

                    if (isAntKilled) {
                        switch (ran) {
                            case 0:
                                Assets.soundPool.play(Assets.squishSound, 10, 10, 1, 0, 1);
                                break;
                            case 1:
                                Assets.soundPool.play(Assets.squishSound1, 10, 10, 1, 0, 1);
                                break;
                            default:
                                Assets.soundPool.play(Assets.squishSound2, 10, 10, 1, 0, 1);
                                break;
                        }
                        Assets.score++;
                    }

                    else if(isSuperantkill){
                        Assets.soundPool.play(Assets.superSquishSound, 10, 10, 1, 0, 1);
                        Assets.score = Assets.score + 10;
                    }
                    else{
                        Assets.soundPool.play(Assets.thumpSound, 1, 1, 1, 0, 1);
                    }
                }

                for(int j=0;j<Assets.ant.length;j++) {
                    Assets.ant[j].drawDead(canvas);
                    Assets.ant[j].move(canvas);
                    Assets.ant[j].birth(canvas);
                }

                Assets.bigAnt.move(canvas);
                Assets.bigAnt.birth(canvas);
                Assets.bigAnt.drawDead(canvas);
                if (Assets.livesLeft == 0)
                    Assets.state = Assets.GameState.GameEnding;
                break;
            case GameEnding:
                final SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

                int High = prefs.getInt("key_highscore",0);
                Assets.highscore=High;
                if (Assets.highscore < Assets.score) {
                    Assets.highscore = Assets.score;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("key_highscore", Assets.score);
                    editor.commit();
                    Assets.highscore=Assets.score;
                    Assets.soundPool.play(Assets.gameoverSound, 1, 1, 1, 0, 1);
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(context, "New High Score: "+ Assets.score,
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                Assets.state = Assets.GameState.GameOver;
                if (Assets.mediaPlayer != null)  {
                    Assets.mediaPlayer.release();
                    Assets.mediaPlayer = null;

                }

                break;
            case GameOver:
                loadgameover (canvas, R.drawable.gameover);
                canvas.drawBitmap (Assets.background, 0, 0, null);
                canvas.drawBitmap (Assets.gameover, 0, 0, null);
                break;
        }
    }
}
