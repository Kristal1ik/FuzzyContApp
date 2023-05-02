package com.example.fuzzycontapp;

import static com.example.fuzzycontapp.Pendulum1.rule_models;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.example.fuzzycontapp.databinding.ActivityPageRuleBinding;

import java.util.ArrayList;

public class PageRule extends AppCompatActivity {
    ActivityPageRuleBinding binding;
    ArrayList<Bitmap> all_imgs;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPageRuleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        position = getIntent().getIntExtra("POS", 0);
        binding.name.setText(getIntent().getStringExtra("NAME"));
        all_imgs = rule_models.get(position).getImgs();
        binding.x.setImageBitmap(all_imgs.get(0));
        binding.v.setImageBitmap(all_imgs.get(1));
        binding.w.setImageBitmap(all_imgs.get(2));
    }

}