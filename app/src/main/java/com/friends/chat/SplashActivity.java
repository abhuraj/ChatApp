package com.friends.chat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.friends.chat.behaviours.TypeWriter;

public class SplashActivity extends AppCompatActivity {

    TypeWriter tvTextView;
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Ruthie.ttf");

        tvTextView = findViewById(R.id.text_view);
        ivImage = findViewById(R.id.image);

        /*tvTextView.setTypeface(typeface);
        tvTextView.setText("");
        tvTextView.setCharacterDelay(150);
        tvTextView.animateText(this.getString(R.string.app_name));*/

        Animation animRotateAclk = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate_clockwise);
        ivImage.startAnimation(animRotateAclk);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callMainPage();
            }
        },3000);
    }

    private void callMainPage() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}