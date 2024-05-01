package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatDetailBinding;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}