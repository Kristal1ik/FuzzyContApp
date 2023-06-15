package com.example.fuzzycontapp.Fragments;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.fuzzycontapp.Activities.AccelerometerModelClass;
import com.example.fuzzycontapp.R;

public class AccelerometerFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accelerometer, container, false);
        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.accelerometer_fragment);
        frameLayout.addView(new AccelerometerModelClass((getActivity())));
        return rootView;
    }

}