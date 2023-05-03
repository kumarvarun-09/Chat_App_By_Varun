package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UserDetails extends AppCompatActivity {

    Toolbar toolbar;
    Users user;
    ImageView profilePic;
    TextView nameTxtView, emailTxtView, statusTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        toolbar = findViewById(R.id.toolbar);
        user = new Users();

        profilePic = findViewById(R.id.profilePic);
        nameTxtView = findViewById(R.id.userName);
        emailTxtView = findViewById(R.id.email);
        statusTxtView = findViewById(R.id.status);

        user.userName = getIntent().getStringExtra("User");
        user.profilePic = getIntent().getStringExtra("ProfilePicImg");
        user.email = getIntent().getStringExtra("Email");
        user.status = getIntent().getStringExtra("Status");


        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will make the back button visible
            getSupportActionBar().setTitle("");
        }

        Picasso.get().load(user.profilePic).into(profilePic);
        nameTxtView.setText(user.userName);
        emailTxtView.setText(user.email);
        statusTxtView.setText(user.status);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Profile Photo View Activity
                Intent intent = new Intent(getApplicationContext(), ViewProfilePhoto.class);
                intent.putExtra("User", user.getUserName());
                intent.putExtra("ProfilePicImg", user.getProfilePic());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if(itemId == android.R.id.home)  //this is for back button
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}