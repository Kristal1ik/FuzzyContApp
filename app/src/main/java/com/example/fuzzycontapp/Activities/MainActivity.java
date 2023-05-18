package com.example.fuzzycontapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.fuzzycontapp.Adapters.MainAdapter;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MainAdapter mainAdapter;
    MyThread myThread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.login));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.signup));

        myThread = new MyThread();
        new Thread(myThread).start();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        mainAdapter = new MainAdapter(fragmentManager, getLifecycle());
        binding.viewPager.setAdapter(mainAdapter);


    }
    public static class MyThread implements Runnable{
        public static Socket sock;
        public static PrintWriter output;
        public static BufferedReader input;
        public static CharBuffer charb;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            try{
                sock = new Socket("46.242.119.144", 22345);
                sock.setSendBufferSize(100000);
                sock.setReceiveBufferSize(100000);
                input = new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
                output = new PrintWriter(sock.getOutputStream());
                charb = CharBuffer.allocate(100000);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}