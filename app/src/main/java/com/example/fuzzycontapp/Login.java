package com.example.fuzzycontapp;



import static com.example.fuzzycontapp.MainActivity.MyThread.charb;
import static com.example.fuzzycontapp.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.MainActivity.MyThread.output;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;



public class Login extends Fragment {
    FragmentLoginBinding binding;
    public static String USERNAME = "";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = binding.usernameLogin.getText().toString();
                String password = binding.passwordLogin.getText().toString();
                String s = "";

                int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    JSONObject request = new JSONObject();
                    try {
                        request.put("Command", "login");
                        request.put("Login", login);
                        request.put("Password", password);

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
                try {
                    if (new JSONObject(s).get("Status").equals("OK"))
                    {
                        System.out.println(login);
                        Global.USERNAME = login;
                        Global.EMAIL = (String) new JSONObject(s).get("Email");
                        System.out.println(Global.EMAIL);
                        System.out.println("fs"+USERNAME);
                        Intent intent = new Intent(v.getContext(), MenuActivity.class);
                        startActivity(intent);
                    }
                    else{
                        binding.usernameLogin.setText(""); binding.passwordLogin.setText("");
                        System.out.println("wrong password");

                    }                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}