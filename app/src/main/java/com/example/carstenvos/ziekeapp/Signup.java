package com.example.carstenvos.ziekeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.support.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    //Declare local variables
    private static final String TAG = "SignupActivity";
    EditText firstNameText;
    EditText lastNameText;
    EditText emailText;
    EditText passwordText;
    EditText validatePasswordText;
    Button signupButton;
    TextView loginText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     **   On Create method. On click handlers defined here.
     **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Linking UI elements
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginText = (TextView) findViewById(R.id.link_login);
        firstNameText = (EditText) findViewById(R.id.input_first_name);
        lastNameText = (EditText) findViewById(R.id.input_last_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        validatePasswordText = (EditText) findViewById(R.id.input_reEnterPassword);

        mAuth = FirebaseAuth.getInstance();

        //Setting up onClickListeners
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start signup logic
                signup();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }


    //TODO onStart handling
    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    */

    /**
     **   Main signup activity. Creates progress dialog, validates input, gets text,
     *    attempts Firebase signup and returns result to Login activity or throws error
     **/

    public void signup() {
        //Disable signup button
        signupButton.setEnabled(false);

        //Create and show progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(Signup.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        //Validate user input
        if (!validate()) {
            onSignupFailed();
            return;
        }

        //Gets user input text
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        //Attempt Firebase user signup
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    new android.os.Handler().postDelayed(
                        new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess();
                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
                } else {
                    // If sign in fails, display a message to the user.
                    new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    passwordText.setError("Password must be more than 6 characters.");
                                    passwordText.requestFocus();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    emailText.setError("Email is not valid.");
                                    emailText.requestFocus();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    emailText.setError("Email already exists.");
                                    emailText.requestFocus();
                                } catch(Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                                // On complete call either onSignupSuccess or onSignupFailed
                                // depending on success
                                onSignupFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);
                }
            }
        });
    }

    /**
     **  Sends positive result back to Login activity
     **/

    public void onSignupSuccess() {

        //Gets user input text
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        final String email = emailText.getText().toString();
        String[] toastMsg = {"Ouch!","D'oh!","Ooo!","FireBase"};

        //Store names and email in users Firestore db
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);

        db.collection("users").document(mAuth.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        //Store user specified toastMsg array for Mainactivity in Firestore db
        Map<String, Object> settings = new HashMap<>();
        settings.put("tapButtonArray", toastMsg);

        db.document("users/"+mAuth.getUid()+"/settings/toastMsg")
                .set(settings)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    /**
     ** Shows error message
     */

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    /**
     **  Validates text input of all fields
     **/

    public boolean validate() {
        //Initialise variables
        boolean valid = true;

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String validatePassword = validatePasswordText.getText().toString();

        View focusView = null;

        //Validate password validation field
        if (validatePassword.isEmpty() || !password.equals(validatePassword)) {
            validatePasswordText.setError("Passwords do not match.");
            focusView = validatePasswordText;
            valid = false;
        } else {
            validatePasswordText.setError(null);
        }

        //Validate password field
        if (password.isEmpty() || password.length() < 6 ) {
            passwordText.setError("Password must be at least 6 characters.");
            focusView = passwordText;
            valid = false;
        } else {
            passwordText.setError(null);
        }

        //Validate email field
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address.");
            focusView = emailText;
            valid = false;
        } else {
            emailText.setError(null);
        }

        //Validate last name field
        if (lastName.isEmpty() || lastName.length() < 3) {
            lastNameText.setError("Name must be at least 3 characters.");
            focusView = lastNameText;
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        //Validate first name field
        if (firstName.isEmpty() || firstName.length() < 3) {
            firstNameText.setError("Name must be at least 3 characters.");
            focusView = firstNameText;
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        //If any error found, set focus to field that contains error
        if (!valid){
            focusView.requestFocus();
        }
        return valid;
    }
}
