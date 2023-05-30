package com.example.fuzzycontapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.FragmentDifferentRulesBinding;

public class DifferentRules extends Fragment {
    FragmentDifferentRulesBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDifferentRulesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}