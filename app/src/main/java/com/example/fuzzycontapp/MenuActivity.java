package com.example.fuzzycontapp;

import static com.example.fuzzycontapp.Login.USERNAME;
import static com.example.fuzzycontapp.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.MainActivity.MyThread.output;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    TextView name;
    ImageView user_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        name = headerLayout.findViewById(R.id.user);
        user_img = headerLayout.findViewById(R.id.user_img);
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, UserPage.class);
                startActivity(intent);
            }
        });
        name.setText(Global.USERNAME);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name,
                R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Pendulum1()).commit();
            navigationView.setCheckedItem(R.id.pend1);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pend1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Pendulum1()).commit();
                break;

            case R.id.pend2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Pendulum2()).commit();
                break;

            case R.id.pend3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Pendulum3()).commit();
                break;

            case R.id.pend4:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Pendulum4()).commit();
                break;
            case R.id.pend5:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Pendulum5()).commit();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
    }

