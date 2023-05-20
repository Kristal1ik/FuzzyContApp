package com.example.fuzzycontapp.Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.FragmentChooseBluetoothBinding;
import com.example.fuzzycontapp.databinding.FragmentPhysControllerBinding;

import java.io.IOException;
import java.util.UUID;

public class PhysController extends Fragment {

    FragmentPhysControllerBinding binding;
    BluetoothAdapter myBluetooth = null;
    boolean isBtConnected = false;
    public BluetoothSocket btSocket = null;
    // This UUID is unique and fix id for this device
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhysControllerBinding.inflate(inflater, container, false);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTHandler handler = new BTHandler(binding.send.getText().toString());
                handler.start();

            }
        });
        return binding.getRoot();
    }

    class BTHandler extends Thread {
        String data;

        BTHandler(String s) {
            data = s;
        }

        void connect() {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();

                    // This will connect the device with address as passed
                    BluetoothDevice hc = myBluetooth.getRemoteDevice(Global.ADDRESS);
                    btSocket = hc.createInsecureRfcommSocketToServiceRecord(myUUID);
//                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                    btSocket.connect();
                    isBtConnected = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void send(){
            try {
                btSocket.getOutputStream().write(data.getBytes(), 0, data.getBytes().length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        @Override
        public void run(){
            connect();
            send();
        }
    }
}