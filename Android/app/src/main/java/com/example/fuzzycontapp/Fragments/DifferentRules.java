package com.example.fuzzycontapp.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.fuzzycontapp.Activities.AccelerometerModelClass;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.FragmentDifferentRulesBinding;

public class DifferentRules extends Fragment implements View.OnClickListener{
    ColorStateList def;
    TextView item1;
    TextView item2;
    TextView select;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_different_rules, container, false);

        item1 = root.findViewById(R.id.item1);
        item2 = root.findViewById(R.id.item2);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        select = root.findViewById(R.id.select);
        def = item2.getTextColors();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container, new SavedRules());
        fm.commit();
        return root;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item1){
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(def);
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_container, new SavedRules());
            fm.commit();
        } else if (view.getId() == R.id.item2){
            item1.setTextColor(def);
            item2.setTextColor(Color.WHITE);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_container, new MyRules());
            fm.commit();
        }
    }
}