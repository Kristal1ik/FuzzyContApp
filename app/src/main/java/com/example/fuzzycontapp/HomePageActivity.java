
package com.example.fuzzycontapp;

import static com.example.fuzzycontapp.Fragments.Pendulum1.conv_bitmap;
import static com.example.fuzzycontapp.Fragments.Pendulum1.get_img;
import static com.example.fuzzycontapp.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.MainActivity.MyThread.output;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;


import com.example.fuzzycontapp.Adapters.CategoriesAdapter;
import com.example.fuzzycontapp.Indiv.CategoryRow;
import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityHomePageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class HomePageActivity extends AppCompatActivity {
    public ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
    ActivityHomePageBinding binding;
    ArrayList<CategoryRow> categoryRows = new ArrayList<>();
    int[] categoryImg = {R.drawable.c_acc, R.drawable.c_optim, R.drawable.c_pend, R.drawable.c_pend_rules};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        try {
            getImg();
            System.out.println(bmp.size());
            Global.IMG = bmp.get(0);
            binding.ava.setImageBitmap(bmp.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, UserPage.class);
                startActivity(intent);
            }
        });
        setUpCategories();
        CategoriesAdapter adapter = new CategoriesAdapter(this, categoryRows);
        binding.rviewCateg.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rviewCateg.setLayoutManager(linearLayoutManager);

    }

    public void setUpCategories() {
        String[] names = getResources().getStringArray(R.array.names);
        for (int i = 0; i < names.length; i++){
            categoryRows.add(new CategoryRow(names[i], categoryImg[i]));
        }
    }

    private ArrayList<Bitmap> getImg() throws IOException {
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            try {
                request.put("Command", "get_profile_image");
                request.put("StartIdx", 0);
                request.put("EndIdx", 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            output.println(request);
            output.flush();

            while(!input.ready());
            String s2 = input.readLine();
            System.out.println(s2);
            bmp.add(conv_bitmap(get_img(s2)));}
            return bmp;}
    }

