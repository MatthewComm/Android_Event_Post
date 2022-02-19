package com.example.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AdminActivity extends AppCompatActivity {
    //Declaring component variables
        private TextView txtSignedIn;
        private Button btnPostEvent;
        private Button btnLogout;
        private Button btnDeleteEvent;
        private Button btnChangeUserRole;

    //Declaring Firebase variables
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;
        String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Initialising component variables
            txtSignedIn = (TextView) findViewById(R.id.txtSignedInAs);
            btnChangeUserRole = (Button) findViewById(R.id.btnChangeUsersRole);
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnPostEvent = (Button) findViewById(R.id.btnPostEvent);

        //Initialising Firebase variables
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            UserID = fAuth.getCurrentUser().getUid();

        //Displaying the Admin that is signed in
            DocumentReference documentReference = fStore.collection("users").document(UserID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    txtSignedIn.setText("Signed in as: " + value.getString("fName"));
                }
            });

        //onClick to redirect admin to change user role
            btnChangeUserRole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(AdminActivity.this, ChangeUserRoleActivity.class));
                }
            });

        //onClick to redirect admin to post event activity
            btnPostEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(AdminActivity.this, PostEventActivity.class));
                }
            });

        //onClick to logout out the user
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fAuth.signOut();
                    finish();
                }
            });
    }
}