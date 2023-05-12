package com.example.fuzzycontapp.Activities;

import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import com.example.fuzzycontapp.databinding.ActivityOptimizationBinding;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Optimization extends AppCompatActivity {
    ActivityOptimizationBinding binding;
    public static String title, x_start, x_finish, iterations, step;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOptimizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.optim.setOnClickListener(new View.OnClickListener() {
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