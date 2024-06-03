package com.example.smedicalapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Date;

public class AddMedicalActivity extends AppCompatActivity {

    // Widgets
    private Button saveButton;
    private ImageView addPhotoBtn;
    private ProgressBar progressBar;
    private EditText titleEditText;
    private EditText thoughtsEditText;
    private ImageView imageView;


    // Firebase (FireStore)
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference =
            db.collection("Medical");


    // Firebase (Storage)
    private StorageReference storageReference;


    // Firebase Auth : UserId and UserName
    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // Using Activity Result Launcher
    ActivityResultLauncher<String> mTakePhoto;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical);

        progressBar = findViewById(R.id.post_progressBar);
        titleEditText = findViewById(R.id.post_title_et);
        thoughtsEditText = findViewById(R.id.post_description_et);
        imageView = findViewById(R.id.post_imageView);
        saveButton = findViewById(R.id.post_save_journal_button);
        addPhotoBtn = findViewById(R.id.postCameraButton);


        progressBar.setVisibility(View.INVISIBLE);

        // Firebase Storage Reference
        storageReference = FirebaseStorage.getInstance()
                .getReference();

        // Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Getting the Current User
        if (user != null){
            currentUserId = user.getUid();
            currentUserName = user.getDisplayName();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMedical();
            }
        });


        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        // Showing the image
                        imageView.setImageURI(result);

                        // Get the image URI
                        imageUri = result;
                    }
                }
        );




        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting Image From The Gallery
                mTakePhoto.launch("image/*");
            }
        });




    }

    private void SaveMedical() {

        String title = titleEditText.getText().toString().trim();
        String thoughts = thoughtsEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts)
                && imageUri !=null){


            // the saving path of the images in Firebase Storage:
            // ........./journal_images/my_image_202310071430.png
            final StorageReference filePath = storageReference.
                    child("medical_images")
                    .child("my_image_"+ Timestamp.now().getSeconds());

            // Uploading the image
            filePath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    // Creating a Medical Object
                                    Medical medical = new Medical();
                                    medical.setTitle(title);
                                    medical.setInfo(thoughts);
                                    medical.setImageUrl(imageUrl);

                                    medical.setTimeAdded(new Timestamp(new Date()));
                                    medical.setUserName(currentUserName);
                                    medical.setUserId(currentUserId);


                                    collectionReference.add(medical)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    progressBar.setVisibility(View.INVISIBLE);

                                                    Intent i = new Intent(AddMedicalActivity.this, MedicalListActivity.class);

                                                    startActivity(i);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddMedicalActivity.this,
                                                            "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddMedicalActivity.this,
                                    "Failed !!!!", Toast.LENGTH_SHORT).show();
                        }
                    });


        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
    }


}