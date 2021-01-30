package com.example.expensesspark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView appLogoImgView = findViewById(R.id.appImgView);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        appLogoImgView.startAnimation(animation);

        //final Intent loginActivity = new Intent(this, LoginActivity.class);
        final Intent loginActivity = new Intent(this, AddTransaction.class);


        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(loginActivity);
                    finish();
                }
            }
        };
        timer.start();
}
}