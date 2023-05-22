package com.example.fuzzycontapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.fuzzycontapp.Fragments.ChooseBluetooth;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityMathsModelBinding;
import com.example.fuzzycontapp.databinding.ActivityPhysicsModelBinding;

public class PhysicsModel extends AppCompatActivity {
    ActivityPhysicsModelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhysicsModelBinding.inflate(getLayoutInflater());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.blue));
        View view = binding.getRoot();
        setContentView(view);
        setFragment(new ChooseBluetooth());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_phys, fragment);
        fragmentTransaction.commit();
    }

}