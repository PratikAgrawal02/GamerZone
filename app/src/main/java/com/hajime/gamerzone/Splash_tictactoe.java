package com.hajime.gamerzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Splash_tictactoe extends AppCompatActivity {
    Handler h= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_tictactoe);
        ImageView loading = (ImageView) findViewById(R.id.loading);
        loading.animate().rotation(3600).setDuration(5000);
        h.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i= new Intent(Splash_tictactoe.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}