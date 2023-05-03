package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ViewProfilePhoto extends AppCompatActivity {

    Toolbar toolbar;
    Users user;
    ImageView profilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_photo);

        toolbar = findViewById(R.id.toolbar);
        user = new Users();

        user.userName = getIntent().getStringExtra("User");
        user.profilePic = getIntent().getStringExtra("ProfilePicImg");


        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will make the back button visible
            getSupportActionBar().setTitle(user.userName);
        }
        profilePic = findViewById(R.id.profilePic);
        Picasso.get().load(user.profilePic).into(profilePic);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if(itemId == android.R.id.home)  //this is for back button
            super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}