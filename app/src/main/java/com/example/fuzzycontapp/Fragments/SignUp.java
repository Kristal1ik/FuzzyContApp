package com.example.fuzzycontapp.Fragments;

import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import android.content.DialogInterface;
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

import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.FragmentSignupBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.CharBuffer;


public class SignUp extends Fragment {
    FragmentSignupBinding binding;
    CharBuffer charb = CharBuffer.allocate(10000);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = 0;
                charb.clear();
                String username = binding.usernameSignup.getText().toString();
                String email = binding.emailSignup.getText().toString();
                String password = binding.passwordSignup.getText().toString();
                String confirm = binding.againPasswordSignup.getText().toString();
                binding.usernameSignup.setBackgroundResource(R.drawable.edit);
                binding.passwordSignup.setBackgroundResource(R.drawable.edit);
                binding.againPasswordSignup.setBackgroundResource(R.drawable.edit);
                binding.emailSignup.setBackgroundResource(R.drawable.edit);
                if (!isEmailValid(email)){
                    binding.emailSignup.setBackgroundResource(R.drawable.pg_red);
                    n += 1;
                    binding.emailSignup.setText("");
                }

                if (!isLoginValid(username)){
                    binding.usernameSignup.setBackgroundResource(R.drawable.pg_red);
                    n += 1;
                    binding.usernameSignup.setText("");
                }
                if ((!isPasswordValid(password)) && (!isPasswordValid(confirm))){
                    binding.passwordSignup.setBackgroundResource(R.drawable.pg_red);
                    binding.againPasswordSignup.setBackgroundResource(R.drawable.pg_red);
                    n += 1;
                    binding.passwordSignup.setText("");
                    binding.againPasswordSignup.setText("");
                }
                if (!password.equals(confirm)){
                    binding.passwordSignup.setBackgroundResource(R.drawable.pg_red);
                    binding.againPasswordSignup.setBackgroundResource(R.drawable.pg_red);
                    n += 1;
                    binding.passwordSignup.setText("");
                    binding.againPasswordSignup.setText("");
                }
                if (n == 0){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.setTitle(R.string.code);
                    final EditText confirm_email = new EditText(v.getContext());
                    confirm_email.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alertDialog.setView(confirm_email);

                    alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            charb.clear();
                            String confirm_code = confirm_email.getText().toString();
                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8)
                            {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                JSONObject request = new JSONObject();
                                try {
                                    request.put("Command", "confirm_email");
                                    request.put("ConfirmationCode", confirm_code);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                output.println(request);
                                output.flush();
                                try {
                                    while(!input.ready());
                                    JSONObject s = (new JSONObject(input.readLine()));
                                    if (s.get("Status").equals("OK")){
                                        binding.usernameSignup.setBackgroundResource(R.drawable.pg_green);
                                        binding.passwordSignup.setBackgroundResource(R.drawable.pg_green);
                                        binding.againPasswordSignup.setBackgroundResource(R.drawable.pg_green);
                                        binding.emailSignup.setBackgroundResource(R.drawable.pg_green);
                                    }
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                    alertDialog.show();
                    int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        JSONObject request = new JSONObject();
                        try {
                            request.put("Command", "register");
                            request.put("Login", username);
                            request.put("Password", password);
                            request.put("Email", email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(request);
                        output.println(request);
                        output.flush();
                        try {
                            while(!input.ready());
                            JSONObject s = (new JSONObject(input.readLine()));} catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                    }

                }
            }
        );
    }
    public boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean isLoginValid(String login){
        return login.length() >= 6;
    }
    public boolean isPasswordValid(String password){
        return password.length() >= 4;
    }
}