package com.example.smedicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MedicalListActivity extends AppCompatActivity {

    // FirebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // Firebase Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference =
            db.collection("Medical");


    // Firebase Storage
    private StorageReference storageReference;

    // List of Journals
    private List<Medical> journalList;

    // RecyclerView
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    // Widgets
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_list);

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Widgets
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Posts Arraylist
        journalList = new ArrayList<>();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MedicalListActivity.this, AddMedicalActivity.class);
                startActivity(i);
            }
        });
    }

    // 2- Adding a menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_add) {

            if (user != null && firebaseAuth != null) {
                Intent i = new Intent(MedicalListActivity.this,
                        AddMedicalActivity.class);
                startActivity(i);
            }
        } else if (itemId == R.id.action_signOut) {
            if (user != null && firebaseAuth != null) {
                firebaseAuth.signOut();
                Intent i = new Intent(MedicalListActivity.this,
                        MainActivity.class);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // QueryDocumentSnapshot: is an object that represents
                // a single document retrieved from a Firestore query
                // QueryDocumentSnapshot --> Document

                for (QueryDocumentSnapshot journals: queryDocumentSnapshots)
                {
                    // Convert the document into a custom Object (Medical)
                    Medical medical = journals.toObject(Medical.class);

                    journalList.add(medical);

                }

                // RecyclerView
                myAdapter = new MyAdapter(MedicalListActivity.this,
                        journalList);

                recyclerView.setAdapter(myAdapter);

                myAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MedicalListActivity.this,
                        "Opps! Something went wrong!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}