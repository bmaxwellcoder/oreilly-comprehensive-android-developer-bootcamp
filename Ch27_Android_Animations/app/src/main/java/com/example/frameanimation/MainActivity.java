package com.example.frameanimation;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private AnimationDrawable batAnimation;
    private ImageView batImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        batImage = findViewById(R.id.bat_ImageView);

        // for Frame animation
//        batImage.setBackgroundResource(R.drawable.bat_anim);
//        batAnimation = (AnimationDrawable) batImage.getBackground();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // for Frame animation
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        batAnimation.start();
//
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // stop the animation
//                batAnimation.stop();
//            }
//        },
//                5000);
//        return super.onTouchEvent(event);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            Animation startAnimation = AnimationUtils
                    .loadAnimation(getApplicationContext(),
                            R.anim.fade_in_animation);
            batImage.startAnimation(startAnimation);
        }, 50);
        return super.onTouchEvent(event);
    }
}