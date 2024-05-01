package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chatapp.Activities.Adapter.FragmentsAdapter;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());



        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("message");

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.setting) {
            Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.logout) {
            Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}