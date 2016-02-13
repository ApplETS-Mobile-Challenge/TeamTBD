package com.teamtbd.teamtbdapp.activities;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.teamtbd.teamtbdapp.R;

import java.util.Random;

public class AnimationActivity extends AppCompatActivity {

    int actualColor;
    LinearLayout layout;

    public AnimationActivity(int color){
        this.actualColor = color;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        layout = (LinearLayout)findViewById(R.id.animationBackground);
    }

    protected void setColor(int color){
        this.actualColor = color;
        layout = (LinearLayout)findViewById(R.id.animationBackground);
        layout.setBackgroundColor(color);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);}
    }



}
