package com.example.fuzzycontapp.Fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.airbnb.lottie.L;
import com.example.fuzzycontapp.Activities.MathsModelClass;
import com.example.fuzzycontapp.R;

public class MathsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maths, container, false);
        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.maths_fragment);
        frameLayout.addView(new MathsModelClass(getActivity()));
        return rootView;
    }
}