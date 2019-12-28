package com.example.carstenvos.ziekeapp;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
