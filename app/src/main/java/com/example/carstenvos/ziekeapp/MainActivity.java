package com.example.carstenvos.ziekeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    public String[] toastMsg = {"Ouch!","D'oh!","Ooo!"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonTap(View v){
        int i = (int) Math.floor(Math.random()*3);
        Toast myToast = Toast.makeText(getApplicationContext(),toastMsg[i],Toast.LENGTH_LONG);
        myToast.show();
    }
}
