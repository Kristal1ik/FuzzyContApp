package com.example.fuzzycontapp;

import static com.example.fuzzycontapp.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.MainActivity.MyThread.output;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.example.fuzzycontapp.Activities.ChangePassword;
import com.example.fuzzycontapp.databinding.ActivityRecoverPasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RecoverPassword extends AppCompatActivity {
    ActivityRecoverPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecoverPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = binding.loginPass.getText().toString();

                try {
                    int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        JSONObject request = new JSONObject();
                        try {
                            request.put("Command", "reset_password");
                            request.put("Login", login);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        output.println(request);
                        output.flush();
                        while(!input.ready());
                        String s = input.readLine();
                        if ((new JSONObject(s).get("Status")).equals("OK")){
                            Intent intent = new Intent(RecoverPassword.this, ChangePassword.class);
                            startActivity(intent);
                        }


                    }} catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            };
        });
        }}