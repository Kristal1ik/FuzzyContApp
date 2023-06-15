package com.example.fuzzycontapp.Fragments;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuzzycontapp.Activities.HomePageActivity;
import com.example.fuzzycontapp.Activities.PhysicsModel;
import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.FragmentChooseBluetoothBinding;
import com.example.fuzzycontapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class ChooseBluetooth extends Fragment {
    FragmentChooseBluetoothBinding binding;
    private BluetoothAdapter BluetoothAdap = null;
    private ListView devicelist = null;
    private Set<BluetoothDevice> Devices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChooseBluetoothBinding.inflate(inflater, container, false);
        devicelist = binding.deviceList;
        BluetoothAdap = BluetoothAdapter.getDefaultAdapter();
        pairedDevices();
        return binding.getRoot();
    }
    private void pairedDevices() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Devices = BluetoothAdap.getBondedDevices();
        ArrayList list = new ArrayList();
        if (Devices.size() > 0) {
            for (BluetoothDevice bt : Devices) {
                // Add all the available devices to the list
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else {
//            Toast.makeText(getView().getContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        // Adding the devices to the list with ArrayAdapter class
        final ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);

//        Toast.makeText(getView().getContext(), "OK", Toast.LENGTH_LONG).show();
        devicelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  info = ((TextView)view).getText().toString();
                Global.ADDRESS = info.substring(info.length() - 17);
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.frame_layout_phys, new PhysController());
                fm.commit();
            }
        });
    }
}