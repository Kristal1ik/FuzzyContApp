package com.example.fuzzycontapp.Fragments;


import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.fuzzycontapp.Accelerometer;
import com.example.fuzzycontapp.Activities.ChangePassword;
import com.example.fuzzycontapp.Activities.HomePageActivity;
import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Login extends Fragment {
    FragmentLoginBinding binding;
    public static String USERNAME = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        binding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("dfghjk");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle(R.string.enter_login);
                EditText login = new EditText(v.getContext());
                login.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialog.setView(login);

                alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                                    if (SDK_INT > 8) {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                        JSONObject request = new JSONObject();
                                        try {
                                            request.put("Command", "reset_password");
                                            request.put("Login", login.getText());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        output.println(request);
                                        output.flush();
                                        while (!input.ready()) ;
                                        String s = input.readLine();
                                        if ((new JSONObject(s).get("Status")).equals("OK")) {
                                            Intent intent = new Intent(v.getContext(), ChangePassword.class);
                                            startActivity(intent);
                                        }
                                        else{
                                        login.setText(R.string.not_found);}
                                    }
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                alertDialog.show();
                ;
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = binding.usernameLogin.getText().toString();
                String password = binding.passwordLogin.getText().toString();
                String s = "";

                int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                if (SDK_INT > 8) {
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
                        while (!input.ready()) ;
                        s = input.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (new JSONObject(s).get("Status").equals("OK")) {
                        System.out.println(login);
                        Global.USERNAME = login;
                        Global.EMAIL = (String) new JSONObject(s).get("Email");
                        Intent intent = new Intent(v.getContext(), HomePageActivity.class);
                        startActivity(intent);
                    } else {
                        binding.usernameLogin.setText("");
                        binding.passwordLogin.setText("");
                        System.out.println("wrong password");

                    }
                } catch (JSONException e) {
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