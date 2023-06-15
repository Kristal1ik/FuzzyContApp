package com.example.fuzzycontapp.Activities;



import static com.example.fuzzycontapp.Fragments.HomeFragment.rule_models;
//import static com.example.fuzzycontapp.Fragments.Pendulum1.rule_models;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityPageRuleBinding;

import java.util.ArrayList;

public class PageRule extends AppCompatActivity {
    ActivityPageRuleBinding binding;
    ArrayList<Bitmap> all_imgs;
    int position;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPageRuleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        position = getIntent().getIntExtra("POS", 0);
        binding.name.setText(getResources().getString(R.string.by) + " " + getIntent().getStringExtra("NAME"));
        all_imgs = rule_models.get(position).getImgs();
        binding.x.setImageBitmap(all_imgs.get(0));
        binding.v.setImageBitmap(all_imgs.get(1));
        binding.w.setImageBitmap(all_imgs.get(2));

        binding.save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForColorStateLists")
            @Override
            public void onClick(View v) {
                binding.save.setText(getResources().getString(R.string.saved_data));
                binding.save.setBackgroundTintList(getResources().getColorStateList(R.color.purple_700));
            }
        });
    }

}