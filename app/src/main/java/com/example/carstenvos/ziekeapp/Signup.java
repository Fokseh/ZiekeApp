package com.example.carstenvos.ziekeapp;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class Signup extends AppCompatActivity {

    //public class SignupActivity extends AppCompatActivity {
        private static final String TAG = "SignupActivity called";

        EditText firstNameText; // = (EditText) findViewById(R.id.input_first_name);
        EditText lastNameText; // = (EditText) findViewById(R.id.input_last_name);
        EditText emailText; // = (EditText) findViewById(R.id.input_email);
        EditText passwordText; // = (EditText) findViewById(R.id.input_password);
        EditText validatePasswordText; // = (EditText) findViewById(R.id.input_reEnterPassword);
        Button signupButton; // = (Button) findViewById(R.id.btn_signup);
        TextView loginText; // = (TextView) findViewById(R.id.link_login);
        DBHandler dbHandler;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);
            //setContentView(R.layout.activity_signup);

            signupButton = (Button) findViewById(R.id.btn_signup);
            loginText = (TextView) findViewById(R.id.link_login);

            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

            firstNameText = (EditText) findViewById(R.id.input_first_name);
            lastNameText = (EditText) findViewById(R.id.input_last_name);
            emailText = (EditText) findViewById(R.id.input_email);
            passwordText = (EditText) findViewById(R.id.input_password);
            validatePasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
            dbHandler = new DBHandler(this,null,null,1);
        }

        public void signup() {
            if (!validate()) {
                onSignupFailed();
                return;
            }

            signupButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(Signup.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            String firstName = firstNameText.getText().toString();
            String lastName = lastNameText.getText().toString();
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            //String validatePassword = validatePasswordText.getText().toString();

            // TODO: Implement your own signup logic here.

            Users newUser = new Users(email,password,firstName,lastName);

            if (dbHandler.addUser(newUser))
            {
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
            }
            else
            {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onSignupSuccess or onSignupFailed
                                // depending on success
                                emailText.setError("Email already exists");
                                emailText.requestFocus();
                                onSignupFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);
            }


        }


        public void onSignupSuccess() {
            signupButton.setEnabled(true);
            setResult(RESULT_OK, null);
            finish();
        }

        public void onSignupFailed() {
            Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

            signupButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            String firstName = firstNameText.getText().toString();
            String lastName = lastNameText.getText().toString();
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            String validatePassword = validatePasswordText.getText().toString();

            View focusView = null;

            if (validatePassword.isEmpty() || !password.equals(validatePassword)) {
                validatePasswordText.setError("passwords do not match");
                focusView = validatePasswordText;
                valid = false;
            } else {
                validatePasswordText.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                passwordText.setError("between 4 and 10 alphanumeric characters");
                focusView = passwordText;
                valid = false;
            } else {
                passwordText.setError(null);
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailText.setError("enter a valid email address");
                focusView = emailText;
                valid = false;
            } else {
                emailText.setError(null);
            }

            if (lastName.isEmpty() || lastName.length() < 3) {
                lastNameText.setError("at least 3 characters");
                focusView = lastNameText;
                valid = false;
            } else {
                lastNameText.setError(null);
            }

            if (firstName.isEmpty() || firstName.length() < 3) {
                firstNameText.setError("at least 3 characters");
                focusView = firstNameText;
                valid = false;
            } else {
                firstNameText.setError(null);
            }

            if (!valid){
                focusView.requestFocus();
            }
            return valid;
        }
    }
//}
