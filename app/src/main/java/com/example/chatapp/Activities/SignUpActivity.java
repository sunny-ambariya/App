package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.Activities.Models.Users;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Create Account");
        progressDialog.setMessage("We are creating your account");


        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.createUserWithEmailAndPassword(
                                binding.editEmailAddress.getText().toString(), binding.editPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    Users user = new Users(binding.editUserName.getText().toString() ,binding.editEmailAddress.getText().toString(),binding.editPassword.getText().toString() );

                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(user);
                                    Toast.makeText(getApplicationContext(), "User Create Successfully", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        binding.AlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);

            }
        });
    }
}