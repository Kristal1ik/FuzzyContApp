package com.example.fuzzycontapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityFuzzy1Binding;

public class Fuzzy1 extends AppCompatActivity {
    ActivityFuzzy1Binding binding;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFuzzy1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        webView = binding.fuzzy1;
        switch (Global.THEORY_NUMBER){
            case 0:{
                webView.loadUrl("file:///android_asset/fuzzy1.html");
                break;
            }
            case 1:{
                webView.loadUrl("file:///android_asset/fuzzy2.html");
                break;
            }
            case 2:{
                webView.loadUrl("file:///android_asset/fuzzy3.html");
                break;
            }
        }

    }
}