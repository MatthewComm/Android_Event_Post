package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.AdminActivity;
import com.example.affiliate.AffiliateActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class LoginRegisterActivity extends AppCompatActivity implements RegisterDialog.RegisterDialogListener{
    //declaring component variables
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressBar progressBar;

    //declaring variables for new account created
    String RegisterUsername, RegisterName, RegisterSurname, RegisterPassword, UserID;
    String UsernameLogin, PasswordLogin;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        //Initialising variables
            edtUsername = (EditText) findViewById(R.id.edtUsernameLogin);
            edtPassword = (EditText) findViewById(R.id.edtPasswordLogin);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnRegister = (Button) findViewById(R.id.btnRegister);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //Setting onClick for Register button
            btnRegister.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                openDialog();
                }
            });


        //Setting an onCLick listener to log the user in
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UsernameLogin = edtUsername.getText().toString().trim();
                    PasswordLogin = edtPassword.getText().toString().trim();

                    fAuth = FirebaseAuth.getInstance();
                    fStore = FirebaseFirestore.getInstance();

                    progressBar.setVisibility(View.VISIBLE);

                    LoginUser(fAuth, fStore, UsernameLogin, PasswordLogin);
                }
            });
    }

    //Creating method to open the input dialog
    public void openDialog(){
        RegisterDialog registerDialog = new RegisterDialog();
        registerDialog.show(getSupportFragmentManager(), "Register Dialog");
    }


    //Method to set users input to variables when creating a new account
    @Override
    public void applyTexts(String Username, String Name, String Surname, String Password) {
        RegisterUsername = Username;
        RegisterName = Name;
        RegisterSurname = Surname;
        RegisterPassword = Password;

        //ValidateUserInfo(Username, Name, Surname, Password);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
         //  startActivity(new Intent(getApplicationContext(), AffiliateActivity.class));
        }

        //Register user with firebase
        fAuth.createUserWithEmailAndPassword(RegisterUsername, RegisterPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginRegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                    UserID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(UserID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("fUsername", RegisterUsername);
                    user.put("fName", RegisterName);
                    user.put("fSurname", RegisterSurname);
                    user.put("fPassword", RegisterPassword);
                    user.put("fRole", "Affiliate");
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG","onSucess: user profile created for " + UserID);
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), AffiliateActivity.class));
                }else{
                    Toast.makeText(LoginRegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Method for logging the user in
    public void LoginUser(FirebaseAuth fAuthLogin, FirebaseFirestore fStoreLogin, String UsernameLogin, String PasswordLogin){

        fAuthLogin.signInWithEmailAndPassword(UsernameLogin, PasswordLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String UserIDLogin = fAuthLogin.getCurrentUser().getUid();
                    DocumentReference documentReference = fStoreLogin.collection("users").document(UserIDLogin);
                    documentReference.addSnapshotListener(LoginRegisterActivity.this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(value.getString("fRole").equals("Affiliate")){
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(LoginRegisterActivity.this, AffiliateActivity.class));
                            }else{
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(LoginRegisterActivity.this, AdminActivity.class));
                            }
                        }
                    });
                }
            }
        });

    }


    //Method for validating a registered users information
    /*public void ValidateUserInfo(String Username, String Name, String Surname, String Password){

        //Checking if all the fields have been filled
        if(Username.isEmpty() == true){
            Toast.makeText(LoginRegisterActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Name.isEmpty() == true){
            Toast.makeText(LoginRegisterActivity.this, "Please enter a Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Surname.isEmpty() == true){
            Toast.makeText(LoginRegisterActivity.this, "Please enter a surname", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Password.isEmpty() == true){
            Toast.makeText(LoginRegisterActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checking that password is more >= 6 characters
        if(Password.length() < 6){
            Toast.makeText(LoginRegisterActivity.this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
    }  */
}