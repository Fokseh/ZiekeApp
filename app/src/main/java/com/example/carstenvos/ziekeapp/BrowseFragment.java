package com.example.carstenvos.ziekeapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
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

public class BrowseFragment extends Fragment {

    //Initialise variables
    public String[] toastMsg;
    private TextView databaseTextView;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name = "";
    private View view;

    /**
     * onCreateView is called when the fragment is opened.
     * @return the fragment as a View
     *
     * Creates onclick handler for UI elements and gets user data from Firestore database
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Inflates fragment to get UI elements and return view later
        view = inflater.inflate(R.layout.fragment_browse, container, false);

        //Get UI elements
        databaseTextView = view.findViewById(R.id.databaseTextView);
        Button btnTap = view.findViewById(R.id.button);

        //Create listener to handle onClick event of button
        btnTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonTap(v);
            }
        });

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

        return view;
    }



    /**
     **  Perform on button tap. Show random string from array and greet user
     **/

    public void onButtonTap(View v){
        //Initialise message
        String greeting = "Hello, ";

        //Show random string from array on button tap
        int i2 = (int) Math.floor(Math.random()*toastMsg.length);
        Toast myToast = Toast.makeText(getContext(),toastMsg[i2],Toast.LENGTH_LONG);
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
