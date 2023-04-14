package com.example.fuzzycontapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.databinding.FragmentSignupBinding;


public class SignUp extends Fragment {
    FragmentSignupBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.usernameSignup.getText().toString();
                String password = binding.passwordSignup.getText().toString();
                String confirm = binding.againPasswordSignup.getText().toString();
                binding.usernameSignup.setText(""); binding.passwordSignup.setText(""); binding.againPasswordSignup.setText("");
            }
        });
    }
}