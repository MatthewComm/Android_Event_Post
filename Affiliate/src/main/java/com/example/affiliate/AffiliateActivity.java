package com.example.affiliate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class AffiliateActivity extends AppCompatActivity {

    //Declaring variable components
        private TextView txtProfileName;
        private TextView txtProfileSurname;
        private TextView txtProfileUsername;
        private ImageView imgUserProfile;
        private Button btnLogout;
        private Button btnUpdate;

    //Declaring variables
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;
        String UserID;
        RecyclerView recyclerView;
        ArrayList<PostModel> postList;
        PostAdapter myAdapter;

        String ID, AdminName, PostDate, Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate);

        //Initialising variable components
            txtProfileName = (TextView) findViewById(R.id.txtUserName);
            txtProfileSurname = (TextView) findViewById(R.id.txtUserSurname);
            txtProfileUsername = (TextView) findViewById(R.id.txtUserUsername);
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnUpdate = (Button) findViewById(R.id.btnUpdateProfile);

        //Initialising firebase variables and userId
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            UserID = fAuth.getCurrentUser().getUid();

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            postList = new ArrayList<PostModel>();
            myAdapter = new PostAdapter(AffiliateActivity.this, postList);
            recyclerView.setAdapter(myAdapter);

        //Calling method to display the posts by the admins to the affiliates
            DisplayAdminPosts();


        //Retrieving user information from the collection stored in firebase
            DocumentReference documentReference = fStore.collection("users").document(UserID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    txtProfileName.setText(value.getString("fName"));
                    txtProfileSurname.setText(value.getString("fSurname"));
                    txtProfileUsername.setText(value.getString("fUsername"));
                }
            });

        //Button To redirect user to update their profile
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(AffiliateActivity.this, UpdateCredentialsActivity.class));
                }
            });

        //onClick to logout the user
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fAuth.signOut();
                }
            });
    }

    //Method to display admin posts to affiliates
        public void DisplayAdminPosts(){
            CollectionReference UsersRef = fStore.collection("posts");
            UsersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            ID = document.getString("fUserID");
                            DocumentReference docRef = fStore.collection("users").document(ID);
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    AdminName = documentSnapshot.getString("fName");
                                    PostDate =  document.getString("fDatePosted");
                                    Post = document.getString("fPost");
                                    postList.add(new PostModel(AdminName, PostDate, Post));
                                    Log.d("===========", AdminName);
                                    myAdapter.notifyDataSetChanged();
                                }
                            });

                            //myAdapter.notifyDataSetChanged();
                        }
                        //myAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
}
/* docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        AdminName = documentSnapshot.getString("fName");
                                        Log.d("===========", AdminName);
                                        postList.add(new PostModel(AdminName, PostDate, Post));
                                        //myAdapter.notifyDataSetChanged();
                                    }
                                    myAdapter.notifyDataSetChanged();
                                }
                            }); */