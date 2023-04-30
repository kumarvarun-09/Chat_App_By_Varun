package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    ArrayList<Users> userArrayList;
    ArrayList<String> receiverArrayList;
    ImageView logoutImageView, searchImg;
    String senderUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        logoutImageView = findViewById(R.id.logout);
        searchImg = findViewById(R.id.searchImg);
        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        senderUid = auth.getUid();

        DatabaseReference chatReference = database.getReference().child("chats");
        receiverArrayList = new ArrayList<>();
        DatabaseReference reference = database.getReference().child("user");
        userArrayList = new ArrayList<>();

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String chatRoom = dataSnapshot.getKey();
                    if(chatRoom.startsWith(senderUid))
                    {
                        String receiverUid = chatRoom.substring(senderUid.length(), chatRoom.length());
                        if(!senderUid.equals(receiverUid))
                        {
                            receiverArrayList.add(receiverUid);
                        }
                    }
                }


                adapter.notifyDataSetChanged();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userArrayList.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Users user = dataSnapshot.getValue(Users.class);
                            if(receiverArrayList.contains(user.getUserId()))
                            {
                                userArrayList.add(user);
                            }
                        }

                        adapter.notifyDataSetChanged();

                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder logout = new AlertDialog.Builder(MainActivity.this);
                logout.setTitle("Logout?");
                logout.setIcon(R.drawable.baseline_logout_24);
                logout.setMessage("Are you sure you want to Logout?");

                // positive, negative and neutral here is related to positions only
                logout.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      // logout
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });

                logout.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                logout.show(); // very IMPORTANT
            }
        });

        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
                //finish();
            }
        });


        adapter = new UserAdapter(MainActivity.this, userArrayList);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainUserRecyclerView.setAdapter(adapter);

    }

}
