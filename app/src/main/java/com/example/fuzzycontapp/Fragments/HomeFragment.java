package com.example.fuzzycontapp.Fragments;


import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.Accelerometer;
import com.example.fuzzycontapp.Activities.ChangePassword;
import com.example.fuzzycontapp.Activities.MathsModel;
import com.example.fuzzycontapp.Activities.Optimization;
import com.example.fuzzycontapp.Activities.PhysicsModel;
import com.example.fuzzycontapp.Adapters.CategoriesAdapter;
import com.example.fuzzycontapp.Adapters.Rules_Adapter;
import com.example.fuzzycontapp.Indiv.CategoryRow;
import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.Activities.PageRule;
import com.example.fuzzycontapp.PageCategoryInterface;
import com.example.fuzzycontapp.PageRuleInterface;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.Indiv.Rule_model;
import com.example.fuzzycontapp.databinding.ActivityOptimizationBinding;
import com.example.fuzzycontapp.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;

public class HomeFragment extends Fragment implements PageRuleInterface, PageCategoryInterface {
    FragmentHomeBinding binding;
    public static ArrayList<Bitmap> bmp;
    ArrayList<CategoryRow> categoryRows;
    public static ArrayList<Rule_model> rule_models;
    public static ArrayList<ArrayList<Bitmap>> bmp2;
    public static ArrayList<String> usernames;
    public static ArrayList<String> base_rules;
    public static ArrayList<Integer> id;
    int[] categoryImg = {R.drawable.c_acc, R.drawable.c_optim, R.drawable.c_pend, R.drawable.c_pend_rules};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bmp = new ArrayList<Bitmap>();
        categoryRows = new ArrayList<>();
        bmp2 = new ArrayList<ArrayList<Bitmap>>();
        usernames = new ArrayList<>();
        base_rules = new ArrayList<>();
        id = new ArrayList<>();
        rule_models = new ArrayList<>();
        try {
            getImg();
            System.out.println(bmp.size() + "hjk");
            Global.IMG = bmp.get(0);
            binding.ava.setImageBitmap(bmp.get(0));
            binding.hi.setText("Hi, " + Global.USERNAME);
            binding.slogan.setText(Global.EMAIL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpCategories();
        CategoriesAdapter adapter = new CategoriesAdapter(this.getContext(), categoryRows, this);
        binding.rviewCateg.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rviewCateg.setLayoutManager(linearLayoutManager);

        ThreadSetBase threadSetBase = new ThreadSetBase();
        threadSetBase.start();
        try {
            threadSetBase.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Rules_Adapter rules_adapter = new Rules_Adapter(this.getContext(), rule_models, this);
        binding.rviewRules.setAdapter(rules_adapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rviewRules.setLayoutManager(linearLayoutManager2);

        binding.read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("qwe");
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        System.out.println(position);
        Intent intent = new Intent(this.getContext(), PageRule.class);
        intent.putExtra("NAME", rule_models.get(position).getName());
        intent.putExtra("RULES", rule_models.get(position).getRules());
        intent.putExtra("POS", position);
        send_heart(rule_models.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(int position) {
        switch (position) {
            case 0: {
                Intent intent = new Intent(this.getContext(), Accelerometer.class);
                startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(this.getContext(), Optimization.class);
                startActivity(intent);
                break;
            }
            case 2: {
                Intent intent = new Intent(this.getContext(), PhysicsModel.class);
                startActivity(intent);
                break;
            }
            case 3: {
                Intent intent = new Intent(this.getContext(), MathsModel.class);
                startActivity(intent);
                break;
            }
        }
    }


    static class ThreadSetBase extends Thread {
        @Override
        public void run() {
            collect_img();
            setRule_models();
        }
    }

    static private void setRule_models() {
        System.out.println(bmp2.size());
        for (int i = 0; i < usernames.size(); i++) {
            System.out.println(i);
            rule_models.add(new Rule_model(usernames.get(i), bmp2.get(i), base_rules.get(i), id.get(i)));
        }
    }

    static private ArrayList<ArrayList<Bitmap>> collect_img() {
        CharBuffer charb = CharBuffer.allocate(10000);
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            try {
                request.put("Command", "get_base");
                request.put("StartIdx", 0);
                request.put("EndIdx", 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            output.println(request);
            output.flush();
            try {
                while (!input.ready()) ;
                String s = input.readLine();
                charb.put(s);
                int n = new JSONObject(new String(charb.array())).getInt("RulesCount");
                for (int i = 0; i < n; i++) {
                    bmp2.add(new ArrayList<>());
                    while (!input.ready()) ;
                    JSONObject s3 = (new JSONObject(input.readLine()));
                    usernames.add(s3.getString("Username"));
                    base_rules.add(s3.getString("Base"));
                    id.add(s3.getInt("RuleID"));
                    for (int j = 0; j < 3; j++) {
                        while (!input.ready()) ;
                        String s2 = input.readLine();
                        bmp2.get(i).add(conv_bitmap(get_img(s2)));
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return bmp2;
    }

    static public Bitmap conv_bitmap(String str_img) {

        byte[] img_byte = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            img_byte = Base64.getDecoder().decode(str_img);
        }
        return BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);

    }

    static public String get_img(String img) {
        JSONObject jsonObject;
        StringBuilder enc_img = new StringBuilder();
        try {
            jsonObject = new JSONObject(img);
            int n = jsonObject.getInt("Parts");
            for (int i = 0; i < n; i++) {
                while (!input.ready()) ;
                String readed = String.valueOf(input.readLine());
                output.println("{\"Status\": \"OK\"}");
                output.flush();
                JSONObject input_json = new JSONObject(readed);
                enc_img.append(input_json.get("SplittedData"));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return enc_img.toString();
    }

    public static void send_heart(int ID) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            try {
                request.put("Command", "save_rule");
                request.put("RuleID", ID);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            output.println(request);
            output.flush();
        }
    }


    public void setUpCategories() {
        String[] names = getResources().getStringArray(R.array.names);
        for (int i = 0; i < names.length; i++) {
            categoryRows.add(new CategoryRow(names[i], categoryImg[i]));
        }
    }

    private ArrayList<Bitmap> getImg() throws IOException {
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8) {
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

            while (!input.ready()) ;
            String s2 = input.readLine();
            System.out.println(s2);
            bmp.add(conv_bitmap(get_img(s2)));
        }
        return bmp;
    }
}