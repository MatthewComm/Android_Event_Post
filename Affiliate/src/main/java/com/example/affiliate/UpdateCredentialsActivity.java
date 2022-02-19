package com.example.affiliate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UpdateCredentialsActivity extends AppCompatActivity {
    //Declaring variable components
        private EditText edtNameUpdate;
        private EditText edtUpdateSurname;
        private EditText edtUpdateUsername;
        private EditText edtUpdatePassword;
        private Button btnSaveChanges;

    //Declaring Firebase variables
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_credentials);

        //Initialising component variables
            edtNameUpdate = (EditText) findViewById(R.id.edtUpdateName);
            edtUpdateSurname = (EditText) findViewById(R.id.edtUpdateSurname);
            edtUpdateUsername = (EditText) findViewById(R.id.edtUpdateUsername);
            edtUpdatePassword = (EditText) findViewById(R.id.edtUpdatePassword);
            btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);

        //Initialising Firebase components
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            String UserID = fAuth.getCurrentUser().getUid();

        //Populating the edits with the users current information
            DocumentReference documentReference = fStore.collection("users").document(UserID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    edtNameUpdate.setText(value.getString("fName"));
                    edtUpdateSurname.setText(value.getString("fSurname"));
                    edtUpdateUsername.setText(value.getString("fUsername"));
                    edtUpdatePassword.setText(value.getString("fPassword"));
            }
        });

        //Creating an onClick event for saving the users details
            btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Setting the users input to variables
                        String name, surname, password, username;
                        name = edtNameUpdate.getText().toString();
                        surname = edtUpdateSurname.getText().toString();
                        username = edtUpdateUsername.getText().toString();
                        password = edtUpdatePassword.getText().toString();

                        UpdateUserChanges(fStore, fAuth, UserID, name, surname, password, username);

                        startActivity(new Intent(UpdateCredentialsActivity.this, AffiliateActivity.class));
                        finish();
                }
            });
    }

    //Method to update the user information in Firebase
        public void UpdateUserChanges(FirebaseFirestore fStoreNew, FirebaseAuth fAuthNew, String UserIDUpdate, String NewName, String NewSurname, String NewPassword, String NewUsername){
            fStoreNew.collection("users").document(UserIDUpdate).update(
                                                                                    "fName", NewName,
                                                                                    "fSurname", NewSurname,
                                                                                    "fUsername", NewUsername,
                                                                                    "fPassword", NewPassword);

            FirebaseUser user = fAuthNew.getCurrentUser();
            user.updateEmail(NewUsername).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("****************", "User email changed");
                    }
                }
            });

            user.updatePassword(NewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("**************", "User password updated");
                    }
                }
            });

            Toast.makeText(UpdateCredentialsActivity.this, "User details have been updated successfully!", Toast.LENGTH_SHORT).show();
        }
}