package com.example.fuzzycontapp;

import static com.example.fuzzycontapp.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.MainActivity.MyThread.output;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

 import com.example.fuzzycontapp.databinding.FragmentPendulum2Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Pendulum2 extends Fragment{
    FragmentPendulum2Binding binding;
    public static String title, x_start, x_finish, iterations, step;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendulum2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = binding.name.getText().toString();
                x_start = binding.xStart.getText().toString();
                x_finish = binding.xFinish.getText().toString();
                iterations = binding.iterations.getText().toString();
                step = binding.step.getText().toString();
                binding.name.setText("");
                binding.xStart.setText("");
                binding.xFinish.setText("");
                binding.iterations.setText("");
                binding.step.setText("");
                SendData sendData = new SendData();
                sendData.start();
            }
        });

        }

    private static void send_data(){
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
    static class SendData extends Thread {
        @Override
        public void run() {
            send_data();
        }
    }
}
