package com.example.hm14_shrestha;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hm14_shrestha.R;
public class Assets extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset);
    }

    static Bitmap background;
    static Bitmap gameover;
    static Bitmap foodbar;
    static Bitmap ant1;
    static Bitmap ant2;
    static Bitmap ant3;
    static Bitmap bigAnt1;
    static Bitmap bigAnt2;
    static Bitmap bigAnt3;
    static Bitmap life;
    public static MediaPlayer mediaPlayer;
    static float gameTimer;
    static int livesLeft;
    static int score;
    static int highscore;
    static SoundPool soundPool;
    static int getReadySound;
    static int gamePlaySound;
    static int squishSound;
    static int squishSound1;
    static int squishSound2;
    static int superSquishSound;
    static int gameoverSound;
    static int thumpSound;
    static int bottomSound;
    static Context context;
    static Ant[] ant;
    static BigAnt bigAnt;
    static GameState state;

    enum GameState {
        GettingReady,
        Starting,
        Running,
        GameEnding,
        GameOver,
    };


}

