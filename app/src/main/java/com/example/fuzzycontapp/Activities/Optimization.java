package com.example.fuzzycontapp.Activities;

import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.sock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuzzycontapp.Adapters.MainAdapter;
import com.example.fuzzycontapp.Adapters.OptimAdapter;
import com.example.fuzzycontapp.Fragments.BasinHopping;
import com.example.fuzzycontapp.Fragments.GeneticAlgorithm;
import com.example.fuzzycontapp.Fragments.MyRules;
import com.example.fuzzycontapp.Fragments.SavedRules;
import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityOptimizationBinding;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

public class Optimization extends AppCompatActivity implements View.OnClickListener{
    ActivityOptimizationBinding binding;
    ColorStateList def;
    TextView select;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOptimizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Global.INFO = 0;
//        item1 = findViewById(R.id.item1);
//        item2 = findViewById(R.id.item2);
        binding.include.item1.setOnClickListener(this);
        binding.include.item2.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = binding.include.item2.getTextColors();
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container, new GeneticAlgorithm());
        fm.commit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Die?");

        View view2 = getLayoutInflater().inflate(R.layout.dialog_info, null);
        builder.setView(view2);
        dialog = builder.create();

        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView info = view2.findViewById(R.id.textView4);
                if (Global.INFO == 0){
                    info.setText(R.string.info_gen);
                }
                else{
                    info.setText(R.string.info_basin);
                }
                dialog.show();
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item1){
            select.animate().x(0).setDuration(100);
            binding.include.item1.setTextColor(Color.WHITE);
            binding.include.item2.setTextColor(def);
            Global.INFO = 0;
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_container, new GeneticAlgorithm());
            fm.commit();
        } else if (v.getId() == R.id.item2){
            binding.include.item1.setTextColor(def);
            Global.INFO = 1;
            binding.include.item2.setTextColor(Color.WHITE);
            int size = binding.include.item2.getWidth();
            select.animate().x(size).setDuration(100);
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_container, new BasinHopping());
            fm.commit();
        }
    }
}