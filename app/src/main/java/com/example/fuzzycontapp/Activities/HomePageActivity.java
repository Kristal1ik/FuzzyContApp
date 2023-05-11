
package com.example.fuzzycontapp.Activities;

import static com.example.fuzzycontapp.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.MainActivity.MyThread.output;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;


import com.example.fuzzycontapp.Fragments.HomeFragment;
import com.example.fuzzycontapp.Fragments.Optimization;
import com.example.fuzzycontapp.Fragments.Pendulum1;
import com.example.fuzzycontapp.Fragments.Pendulum5;
import com.example.fuzzycontapp.Fragments.Theory;
import com.example.fuzzycontapp.MainActivity;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityHomePageBinding;

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
        replace(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:{
                    replace(new HomeFragment());
                    break;
                }
                case R.id.my_rules:{
                    replace(new Pendulum1());
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
                Intent intent = new Intent(HomePageActivity.this, AddRule.class);
                startActivity(intent);
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




