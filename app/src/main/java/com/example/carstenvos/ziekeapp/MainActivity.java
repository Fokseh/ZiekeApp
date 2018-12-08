package com.example.carstenvos.ziekeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    public String[] toastMsg = {"Ouch!","D'oh!","Ooo!"};
    DBHandler myDBHandler;
    ArrayList<Users> allUsers;
    private TextView databaseTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDBHandler = new DBHandler(this,null,null,1);

        allUsers = myDBHandler.getUsersAsList();

        databaseTextView = findViewById(R.id.databaseTextView);
    }

    public void onButtonTap(View v){
        int i2 = (int) Math.floor(Math.random()*3);
        Toast myToast = Toast.makeText(getApplicationContext(),toastMsg[i2],Toast.LENGTH_LONG);
        myToast.show();

        String databaseContents = "";

        for (int i = 0 ; i < allUsers.size() ; i++) {
            databaseContents += allUsers.get(i).toString();
            databaseContents += '\n';
        }

            databaseTextView.setText(databaseContents);

    }
}
