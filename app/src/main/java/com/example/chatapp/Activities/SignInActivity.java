package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.Activities.Models.Users;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.editEmailAddress.getText().toString(), binding.editPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });

        binding.ClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });

        binding.buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        if (auth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }

    private static final int RC_SIGN_IN = 2;
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:"+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {

                Log.w("TAG", "Google sign in failed");
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential Credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(Credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilePic(user.getPhotoUrl().toString());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignInActivity.this, "Sign in with Google", Toast.LENGTH_SHORT).show();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar.make(binding.getRoot(), "Authentication Failed", Snackbar.LENGTH_SHORT).show();

//                            updateUI(null);
                        }
                    }
                });


    }
}