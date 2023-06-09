
package com.example.fuzzycontapp.Activities;

import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.fuzzycontapp.Fragments.DifferentRules;
import com.example.fuzzycontapp.Fragments.HomeFragment;
import com.example.fuzzycontapp.Fragments.MathsFragment;
import com.example.fuzzycontapp.Fragments.Pendulum1;
import com.example.fuzzycontapp.Fragments.Pendulum5;
import com.example.fuzzycontapp.Fragments.Theory;
import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityHomePageBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HomePageActivity extends AppCompatActivity {
    ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.purple_700));


        if (Global.TO_READ){
            Global.TO_READ = false;
            replace(new Theory());
        }
        else{
        replace(new HomeFragment());}

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:{
                    replace(new HomeFragment());
                    break;
                }
                case R.id.my_rules:{
                    replace(new DifferentRules());
                    break;
                }
                case R.id.theory:{
                    replace(new Theory());
                    break;
                }
                case R.id.profile:{
                    replace(new Pendulum5());
                    break;
                }
            }
            return true;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePageActivity.this, AddRule.class);
//                startActivity(intent);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomePageActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_support, (LinearLayout)findViewById(R.id.container));

                EditText user_email = bottomSheetView.findViewById(R.id.user_email);
                user_email.setText(Global.EMAIL);

                EditText question = bottomSheetView.findViewById(R.id.question);
                Button btn_send = bottomSheetView.findViewById(R.id.send);
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user_email_text, question_text;
                        user_email_text = user_email.getText().toString();
                        question_text = question.getText().toString();

                        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            JSONObject request = new JSONObject();
                            try {
                                request.put("Command", "send_question");
                                request.put("Email", user_email_text);
                                request.put("Question", question_text);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            output.println(request);
                            output.flush();
                            Toast.makeText(HomePageActivity.this, R.string.send_question_ok, Toast.LENGTH_LONG).show();
                    }
                    bottomSheetDialog.dismiss();
                    }

                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        logout();
    }

    public void replace(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }
    public void logout() {
        String s = "";
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            try {
                request.put("Command", "logout");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            output.println(request);
            output.flush();
            try {
                while (!input.ready()) ;
                s = input.readLine();
                finish();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




