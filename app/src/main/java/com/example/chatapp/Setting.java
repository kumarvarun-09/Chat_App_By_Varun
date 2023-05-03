package com.example.chatapp;

import static com.example.chatapp.ChatWindow.senderImg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.ref.Reference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends AppCompatActivity {

    CircleImageView profilePic;
    EditText nameEditText, statusEditText;
    Uri imageUri;
    Button saveBtn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference userDataReference;
    String userId, profilePicStr;
    Users user;
    ProgressBar progressBar;
    ImageView backTop, profilePhotoChange;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will make the back button visible
            getSupportActionBar().setTitle("Settings");
        }

        profilePic = findViewById(R.id.profilePic);
        profilePhotoChange = findViewById(R.id.profilePhotoChange);
        nameEditText = findViewById(R.id.name);
        statusEditText = findViewById(R.id.status);
        progressBar = findViewById(R.id.progressBar);
        saveBtn = findViewById(R.id.saveBtn);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        userId = auth.getUid();

        userDataReference = database.getReference()
                .child("AppUser")
                .child(userId)
                .child("UserData");

        storageReference = firebaseStorage.getReference()
                .child("Upload")
                .child(userId);

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

        profilePhotoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder logout = new AlertDialog.Builder(Setting.this);
                logout.setTitle("Profile Photo");
                logout.setIcon(R.drawable.baseline_photo_camera_24);
//                logout.setMessage("Change or Delete Profile Photo");

                // positive, negative and neutral here is related to positions only
                logout.setPositiveButton("Change Photo", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Change Photo
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 10);
                    }
                });

                if(user.profilePic != "https://firebasestorage.googleapis.com/v0/b/chat-app-70dc8.appspot.com/o/userDefaultImage.png?alt=media&token=2113f5de-c75d-4c3d-bf3d-a2da4fde3eae")
                {
                    logout.setNegativeButton("Delete Photo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            user.profilePic = "https://firebasestorage.googleapis.com/v0/b/chat-app-70dc8.appspot.com/o/userDefaultImage.png?alt=media&token=2113f5de-c75d-4c3d-bf3d-a2da4fde3eae";
                            Picasso.get().load(user.profilePic).into(profilePic);
                            imageUri = null;
                            saveData();
                        }
                    });
                }


                logout.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                logout.show(); // very IMPORTANT

            }
        });
        userDataReference.addValueEventListener(new ValueEventListener() {
            // This will work when we'll click save button
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(Users.class);

                Picasso.get().load(user.profilePic).into(profilePic);
                nameEditText.setText(user.userName);
                statusEditText.setText(user.status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNameStatus();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                imageUri = data.getData();
                profilePic.setImageURI(imageUri);
                saveData();
            }
        }
    }

    void saveData() {
        progressBar.setVisibility(View.VISIBLE);
        if (imageUri != null) {
            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                user.profilePic = uri.toString();
                                userDataReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(Setting.this, "Profile Photo Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            userDataReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Setting.this, "Profile Photo Removed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void saveNameStatus() {
        // here we have to check if name is range [1, 20] characters and status is in range [1, 35] characters
        String userName = nameEditText.getText().toString().trim();
        String status = statusEditText.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            nameEditText.setError("Name can't be empty");
            return;
        } else if (TextUtils.isEmpty(status)) {
            statusEditText.setError("Status can't be empty");
            return;
        }

        user.userName = userName;
        user.status = status;
        userDataReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Setting.this, "Details Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if(itemId == android.R.id.home)  //this is for back button
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void topBackPressed(View view) {
        finish();
    }
}