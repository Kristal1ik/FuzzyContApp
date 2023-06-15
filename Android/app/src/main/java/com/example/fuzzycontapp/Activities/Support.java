package com.example.fuzzycontapp.Activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;

import com.example.fuzzycontapp.databinding.ActivitySupportBinding;


public class Support extends AppCompatActivity {
    ActivitySupportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }

}