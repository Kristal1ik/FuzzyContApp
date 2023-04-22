package com.example.fuzzycontapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    Button button;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MainAdapter mainAdapter;
    MyThread myThread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager2 = (ViewPager2) findViewById(R.id.view_pager);
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));

        button.setOnClickListener(listener);
        myThread = new MyThread();
        new Thread(myThread).start();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
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
        viewPager2.setAdapter(mainAdapter);
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Accelerometer.class);
            startActivity(intent);
        }
    };
    static class MyThread implements Runnable{
        public static Socket sock;
        public static PrintWriter output;
        public static BufferedReader input;
        @Override
        public void run() {
            try{
                sock = new Socket("46.242.119.144", 22345);
                input = new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
                output = new PrintWriter(sock.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}