package com.example.carstenvos.ziekeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Initialise variables
    private DrawerLayout drawer;
    private SharedPreferences sharedPref;
    private String firstName = "";
    private String lastName = "";
    private String email = "";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     **  On Create method. Creates views and drawer menu. Opens browse fragment if opened for the first time
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create views and let's activity handle selected items from drawer menu
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String currentUser = mAuth.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        Context context = getApplicationContext();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        //Set text for nav header
        View headerView = navigationView.getHeaderView(0);
        final ImageView profilePicture = headerView.findViewById(R.id.userImg);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name);
        TextView navEmail = (TextView) headerView.findViewById(R.id.userEmail);

        System.out.println(storageReference.child(currentUser + "/profilePicture.jpg").toString());

        storageReference.child(currentUser + "/profilePicture.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                //int newWidth = 64;
                //int newHeight = 64;

                // TODO: 2019-12-28 Add resizing of user profile pictures
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bmp);
                roundedBitmapDrawable.setCircular(true);
                //Bitmap resized = Bitmap.createScaledBitmap(bmp,newWidth, newHeight, true);
                //profilePicture.setImageBitmap(bmp);
                profilePicture.setImageDrawable(roundedBitmapDrawable);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("Problem fetching image!");
                System.out.println(exception.getMessage());
            }
        });

        firstName = sharedPref.getString("firstName","John");
        lastName = sharedPref.getString("lastName","Doe");
        String concat = firstName + " " + lastName;
        email = sharedPref.getString("email","foo@example.com");
        navUsername.setText(concat);
        navEmail.setText(email);

        //Create new actionbar with hamburger icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //If app is opened for the first time, open the browse fragment.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BrowseFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_browse);
        }
    }

    /**
     * Takes selected
     * @param menuItem
     * and handles action for selected item.
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_browse:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BrowseFragment()).commit();
                break;
            case R.id.nav_recipe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipeFragment()).commit();
                break;
            case R.id.nav_week:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WeekFragment()).commit();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_help:
                Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     **  Close drawer on back pressed when opened.
     **/

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
