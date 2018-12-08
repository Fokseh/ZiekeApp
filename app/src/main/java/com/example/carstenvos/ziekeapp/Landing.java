package com.example.carstenvos.ziekeapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;


public class Landing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);
    }

    public void onEnterClick (View v){
        Intent landingNavigation = new Intent(Landing.this, Login.class);
        Landing.this.startActivity(landingNavigation);
    }
}
