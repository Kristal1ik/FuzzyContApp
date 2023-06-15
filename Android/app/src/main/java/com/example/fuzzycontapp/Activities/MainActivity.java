package com.example.fuzzycontapp.Activities;

import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.checkSock;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.sock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.fuzzycontapp.Adapters.MainAdapter;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MainAdapter mainAdapter;
    MyThread myThread;
    static ThreadConnection threadConnection;

    @RequiresApi(api = Build.VERSION_CODES.S)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        System.out.println(resourceId);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.login));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.signup));

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.BLUETOOTH_CONNECT }, 1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.BLUETOOTH_SCAN }, 1);
        }
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
        public static Socket sock, checkSock;
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

//                checkSock = new Socket();
//                checkSock.bind(new InetSocketAddress("46.242.119.144", 22344));
//                checkSock.setSendBufferSize(100000);
//                checkSock.setReceiveBufferSize(100000);

                input = new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
                output = new PrintWriter(sock.getOutputStream());
                charb = CharBuffer.allocate(100000);
                threadConnection = new ThreadConnection();
                new Thread(threadConnection).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static class ThreadConnection implements Runnable{
        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        @Override
        public void run() {
//            while (true){
//                try {
//                    checkSock = new Socket("46.242.119.144", 22345);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}