//package com.example.chatapp;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class Search extends AppCompatActivity {
//    FirebaseAuth auth;
//    FirebaseDatabase database;
//    RecyclerView mainUserRecyclerView;
//    SearchAdapter adapter;
//    ArrayList<Users> userArrayList;
//    ImageView backTop;
//    EditText searchBar;
//    String senderUid;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        backTop = findViewById(R.id.backTop);
//        searchBar = findViewById(R.id.searchName);
//
//        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference().child("user");
//
//        senderUid = auth.getUid();
//
//        userArrayList = new ArrayList<>();
//
//        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//
//                userArrayList.clear();
//                String searchText = textView.getText().toString().trim();
//                if(TextUtils.isEmpty(searchText))
//                {
//                    searchBar.setError("Enter Name or Email");
//                    return true;
//                }
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
//                        {
//                            Users user = dataSnapshot.getValue(Users.class);
//                            if(!senderUid.equals(user.getUserId()) && (user.userName.startsWith(searchText) || user.email.startsWith(searchText))) // this will not show same user
//                            {
//                                userArrayList.add(user);
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                        if(userArrayList.isEmpty())
//                        {
//                            Toast.makeText(Search.this, "No results found", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                return true;
//            }
//        });
//
//        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
//        adapter = new SearchAdapter(Search.this, userArrayList);
//        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mainUserRecyclerView.setAdapter(adapter);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        Intent intent = new Intent(Search.this, MainActivity.class);
////        startActivity(intent);
//        finish();
//    }
//
//    public void topBackPressed(View view)
//    {
//        onBackPressed();
//    }
//}


// new code

package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Search extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView mainUserRecyclerView;
    SearchAdapter adapter;
    ArrayList<Users> userArrayList;
    ImageView backTop;
    EditText searchBar;
    String senderUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backTop = findViewById(R.id.backTop);
        searchBar = findViewById(R.id.searchName);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("AppUser");

        senderUid = auth.getUid();

        userArrayList = new ArrayList<>();

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                String searchText = textView.getText().toString().trim().toLowerCase();
                if (TextUtils.isEmpty(searchText)) {
                    searchBar.setError("Enter Name or Email");
                    return true;
                }
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // Fetching Nodes with name IDs
                            Users user = dataSnapshot.child("UserData").getValue(Users.class);
                            String userName = user.getUserName().toLowerCase();
                            String email = user.getEmail().toLowerCase();
                            if (!senderUid.equals(user.getUserId()) && (userName.startsWith(searchText) || email.startsWith(searchText))) // this will not show same user
                            {
                                userArrayList.add(user);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (userArrayList.isEmpty()) {
                            Toast.makeText(Search.this, "No results found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
            }
        });

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        adapter = new SearchAdapter(Search.this, userArrayList);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainUserRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(Search.this, MainActivity.class);
//        startActivity(intent);
        finish();
    }

    public void topBackPressed(View view) {
        onBackPressed();
    }
}