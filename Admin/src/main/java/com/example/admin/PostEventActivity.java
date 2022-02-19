package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class PostEventActivity extends AppCompatActivity {
    //Declaring component variables
        private TextView txtWelcomeAdmin;
        private TextView txtAdminPost;
        private Button btnCancel;
        private Button btnPostEvent;

    //Declaring Firebase variables
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;

     //Declaring variables
        String UserID;
        Timestamp PostTimeStamp;
        String Post, PreviousPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        //Initialising variable components and Firebase components
            txtWelcomeAdmin = (TextView) findViewById(R.id.txtWelcomeAdmin);
            txtAdminPost = (TextView) findViewById(R.id.MlEdtPost);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnPostEvent = (Button) findViewById(R.id.btnPost);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            UserID = fAuth.getCurrentUser().getUid();

        //Setting the welcome text view to welcome the admin
            DocumentReference docRef = fStore.collection("users").document(UserID);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    txtWelcomeAdmin.setText("Hi, " + value.getString("fName"));
                }
            });

            DocumentReference docRefAdminPost = fStore.collection("posts").document(UserID);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    txtAdminPost.setText(value.getString("fPost"));
                }
            });


        //onClick event to Post event details for firebase
            btnPostEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Getting the users post
                        Post = txtAdminPost.getText().toString();

                    //Getting the time stamp of when the post was posted
                        PostTimeStamp = new Timestamp(System.currentTimeMillis());
                        String TimeStamp = PostTimeStamp.toString();

                    //Calling method to post the admins post
                        PostEvent(fAuth, fStore, UserID, TimeStamp, Post);
                }
            });

        //Setting an onClick for Canceling the process
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
    }

    //Method to save post posted by admin to firebase firestore
        public void PostEvent(FirebaseAuth fAuthPost, FirebaseFirestore fStorePost, String UserID, String TimeStamp, String Post){
            DocumentReference documentReference = fStorePost.collection("posts").document(UserID);
            Map<String, Object> post = new HashMap<>();
            post.put("fDatePosted", TimeStamp);
            post.put("fPost", Post);
            post.put("fUserID", UserID);

            documentReference.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("************", "Post has been uploaded.");
                    Toast.makeText(PostEventActivity.this, "Post successfully uploaded", Toast.LENGTH_SHORT).show();
                }
            });
                finish();
        }
}