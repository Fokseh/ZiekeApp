package com.example.carstenvos.ziekeapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Initialise variables
    public String[] toastMsg;
    private TextView databaseTextView;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name = "";

    /**
     **  On Create method. Gets user's toast messages and user details.
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseTextView = findViewById(R.id.databaseTextView);

        //Get user's toast messages from Firestore database
        final DocumentReference toastMsgRef = db.document("users/"+user.getUid()+"/settings/tapButtonArray");
        toastMsgRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        List<String> group = (List<String>) document.get("toastMsg");
                        toastMsg = group.toArray(new String[0]);
                    } else {
                        Log.e("Toast", "No such document");
                    }
                } else {
                    Log.e("Toast", "get failed with ", task.getException());
                }
            }
        });

        //Get user's first name and last name from Firestore database
        final DocumentReference userDetailsRef = db.document("users/"+user.getUid());
        userDetailsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDoc = task.getResult();

                    if (userDoc.exists()) {
                        Map<String, Object> userDetails = userDoc.getData();
                        name = userDetails.get("firstName").toString() + " " + userDetails.get("lastName").toString();
                    } else {
                        Log.e("User", "No such document");
                    }
                } else {
                    Log.e("User", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     **  Perform on button tap. Show random string from array and greet user
     **/

    public void onButtonTap(View v){
        //Initialise message
        String greeting = "Hello, ";

        //Show random string from array on button tap
        int i2 = (int) Math.floor(Math.random()*toastMsg.length);
        Toast myToast = Toast.makeText(getApplicationContext(),toastMsg[i2],Toast.LENGTH_LONG);
        myToast.show();

        //If no name has come back from Firestore show error in textview
        //else show greeting message
        if (name.isEmpty()){
            databaseTextView.setText("User not retrieved from database");
        }
        else {
            greeting += " " + name;
            databaseTextView.setText(greeting);
        }
    }
}
