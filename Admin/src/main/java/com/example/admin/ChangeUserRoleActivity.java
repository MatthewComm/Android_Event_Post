package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChangeUserRoleActivity extends AppCompatActivity {
    //Declaring component variables
        private Button btnApplyChanges;
        private Spinner spnNames;

        String NewRole, UserID, SelectedUser;

    //Declaring Firebase variables
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_role);

        //Initialising component variables
            btnApplyChanges = (Button) findViewById(R.id.btnApplyChange);
            spnNames = (Spinner) findViewById(R.id.spinner);

        //Initialising Firebase variables
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();

        //Populating the spinner
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            CollectionReference UsersRef = rootRef.collection("users");
            List<String> Names = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnNames.setAdapter(adapter);
            UsersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            String Name = document.getString("fName") + " : " + document.getId();
                            Names.add(Name);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });

        //onClick for Changing the users role
            btnApplyChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectedUser = spnNames.getSelectedItem().toString().trim();
                    int ColonPos = SelectedUser.indexOf(":");
                    StringBuffer SelectedUserID = new StringBuffer(SelectedUser);

                    SelectedUserID.delete(0, ColonPos+1);
                    SelectedUserID.delete(0,1);
                    UserID = SelectedUserID.toString();
                    Log.d("****************", "'" + UserID + "'");

                    DocumentReference docRef = fStore.collection("users").document(UserID);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.getString("fRole").equals("Affiliate")){
                                    NewRole = "Admin";
                                }else{
                                    NewRole = "Affiliate";
                                }
                                ChangeSelectedUserRole(fStore, UserID, NewRole);
                                Toast.makeText(ChangeUserRoleActivity.this, "Users Role Successfully Changed!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChangeUserRoleActivity.this, AdminActivity.class));
                                finish();
                            }
                        }
                    });
                }
            });
    }

    //Method to Change the users role
        public void ChangeSelectedUserRole(FirebaseFirestore fStoreRoleChange, String UserID, String newRole){
            fStoreRoleChange.collection("users").document(UserID).update("fRole", newRole);
        }
}