package com.example.loginregister;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterDialog extends AppCompatDialogFragment {

    //Declaring component variables for when a user registers a new account
    private EditText edtRegisterUsername;
    private EditText edtRegisterName;
    private EditText edtRegisterSurname;
    private EditText edtRegisterPassword;
    private RegisterDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        //Building the Register dialog by setting the title and components
        builder.setView(view)
                .setTitle("Register a new account")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = edtRegisterUsername.getText().toString();
                        String name = edtRegisterName.getText().toString();
                        String surname = edtRegisterSurname.getText().toString();
                        String password = edtRegisterPassword.getText().toString();

                        listener.applyTexts(username, name, surname, password);

                    }
                });

        edtRegisterUsername = view.findViewById(R.id.edtRegisterNewUsername);
        edtRegisterName = view.findViewById(R.id.edtRegisterNewName);
        edtRegisterSurname = view.findViewById(R.id.edtRegisterSurname);
        edtRegisterPassword = view.findViewById(R.id.edtRegisterPassword);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RegisterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RegisterDialogListener");
        }
    }

    public interface RegisterDialogListener{
        void applyTexts(String Username, String Name, String Surname, String Password);

    }
}

