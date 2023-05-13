package com.example.fuzzycontapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.fuzzycontapp.databinding.ActivityMathsModelBinding;
import com.example.fuzzycontapp.databinding.ActivityPhysicsModelBinding;

public class PhysicsModel extends AppCompatActivity {
    ActivityPhysicsModelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhysicsModelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}