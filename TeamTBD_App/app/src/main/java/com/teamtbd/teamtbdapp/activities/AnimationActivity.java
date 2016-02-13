package com.teamtbd.teamtbdapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamtbd.teamtbdapp.R;

import java.util.Random;

public class AnimationActivity extends AppCompatActivity {

    LinearLayout layout;
    TextView label;
    String[] colors = new String[]{ "#ff0000", "#008080", "#0000ff", "#eeeeee", "#40e0d0", "#ff7373", "#00ff00", "#800080", "#00ffff", "#ccff00"};
    int colorIt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        final boolean won = getIntent().getExtras().getBoolean("won");
        layout = (LinearLayout)findViewById(R.id.animationBackground);

        label = (TextView)findViewById(R.id.label);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(colorIt < colors.length) {
                    layout.setBackgroundColor(Color.parseColor(colors[colorIt]));
                    label.setText("" + (colors.length - colorIt));
                    colorIt++;
                }else{
                    if(won) {
                        layout.setBackgroundColor(Color.parseColor("#00ff00"));
                        label.setText("Congrats!\nYou won!");
                    }
                    else {
                        layout.setBackgroundColor(Color.parseColor("#ff0000"));
                        label.setText("Ah,\nTry again!");
                    }
                }
                handler.postDelayed(this, 500 + 25 * colorIt);
            }
        };
        handler.postDelayed(runnable, 400);

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
