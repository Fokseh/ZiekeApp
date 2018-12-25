package com.example.carstenvos.ziekeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Initialise variables
    public String[] toastMsg = {"Ouch!","D'oh!","Ooo!"};
    private DBHandler myDBHandler;
    private ArrayList<Users> allUsers;
    private TextView databaseTextView;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String name = "";

    /**
     **  On Create method. Gets all users from database.
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseTextView = findViewById(R.id.databaseTextView);

        //Get all users from database as list
        myDBHandler = new DBHandler(this,null,null,1);
        allUsers = myDBHandler.getUsersAsList();
    }

    /**
     **  Perform on button tap. Show random string from array, get name and greet user
     **/

    public void onButtonTap(View v){
        //Initialise message
        String greeting = "Hello, ";

        //Show random string from array on button tap
        int i2 = (int) Math.floor(Math.random()*3);
        Toast myToast = Toast.makeText(getApplicationContext(),toastMsg[i2],Toast.LENGTH_LONG);
        myToast.show();

        //Go through local database and find user by email. Store user's name.
        for (int i = 0 ; i < allUsers.size() ; i++){
            if (allUsers.get(i).getEmail().equals(user.getEmail()))
            {
                name = allUsers.get(i).getFirstName() + " " + allUsers.get(i).getLastName();
                Log.e("Firstname", "User email: " + user.getEmail());
                break;
            }
        }

        //If no match is found in local database show error in textview
        //else show greeting message
        if (name.isEmpty()){
            databaseTextView.setText("User not known in local database");
        }
        else {
            greeting += " " + name;
            databaseTextView.setText(greeting);
        }
    }
}
