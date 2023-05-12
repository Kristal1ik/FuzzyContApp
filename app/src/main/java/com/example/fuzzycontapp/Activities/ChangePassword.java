package com.example.fuzzycontapp.Activities;

import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.example.fuzzycontapp.databinding.ActivityChangePasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChangePassword extends AppCompatActivity {
    ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String conf = binding.code.getText().toString();

                if (binding.pass1.getText().toString().equals(binding.pass2.getText().toString())) {
                    String pass = binding.pass1.getText().toString();
                    try {
                        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            JSONObject request = new JSONObject();
                            try {
                                request.put("Command", "confirm_email");
                                request.put("ConfirmationCode", conf);
                                request.put("Password", pass);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            output.println(request);
                            output.flush();
                            while (!input.ready()) ;
                            String s = input.readLine();
                            if ((new JSONObject(s).get("Status")).equals("OK")) {
                                Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }}
