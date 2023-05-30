package com.example.fuzzycontapp.Fragments;

import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.FragmentBasinHoppingBinding;
import com.example.fuzzycontapp.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class BasinHopping extends Fragment {
    FragmentBasinHoppingBinding binding;
    public static String title, x_start, x_finish, iterations, step;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentBasinHoppingBinding.inflate(inflater, container, false);

        binding.optim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = binding.title.getText().toString();
                x_start = binding.topBorder.getText().toString();
                x_finish = binding.bottomBorder.getText().toString();
                iterations = binding.indiv.getText().toString();
                step = binding.gener.getText().toString();
                SendData sendData = new SendData();
                sendData.start();
                binding.title.setText("");
                binding.topBorder.setText("");
                binding.bottomBorder.setText("");
                binding.indiv.setText("");
                binding.gener.setText("");
            }
        });


        return binding.getRoot();
    }
    private void send_data(){
        String s = "";
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            try {
                request.put("Command", "optimize");
                request.put("Title", title);
                request.put("x_start", x_start);
                request.put("x_finish", x_finish);
                request.put("iterations", iterations);
                request.put("step", step);
                request.put("type", "public");
                request.put("Type", "genetic");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            output.println(request);
            output.flush();
            try {
                while(!input.ready());
                s = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class SendData extends Thread {
        @Override
        public void run() {
            send_data();
        }
    }
}