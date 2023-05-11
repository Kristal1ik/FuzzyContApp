package com.example.fuzzycontapp.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fuzzycontapp.Activities.UserPage;
import com.example.fuzzycontapp.databinding.FragmentPendulum5Binding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Pendulum5 extends Fragment{
    FragmentPendulum5Binding binding;
    FloatingActionButton button;
    ImageView user_img;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendulum5Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(view.getContext(), UserPage.class);
        startActivity(intent);
    }

}