package com.example.fuzzycontapp;



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
import java.nio.CharBuffer;


public class Login extends Fragment {
    FragmentLoginBinding binding;

    CharBuffer charb = CharBuffer.allocate(10000);
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
                charb.clear();
                String login = binding.usernameLogin.getText().toString();
                String password = binding.passwordLogin.getText().toString();
                binding.usernameLogin.setText(""); binding.passwordLogin.setText("");

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
                    System.out.println(request);
                    output.print(request);
                    output.flush();
                    try {
                        while(!input.ready());
                            System.out.println("assd");
                            System.out.println(input.read(charb));
                            System.out.println(new String (charb.array()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (new JSONObject(new String (charb.array())).get("Status").equals("OK"))
                    {
                        Intent intent = new Intent(v.getContext(), MenuActivity.class);
                        startActivity(intent);
                    }
                    else{
                        System.out.println("wrong password");

                    }                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}