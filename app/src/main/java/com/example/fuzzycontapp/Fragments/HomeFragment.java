package com.example.fuzzycontapp.Fragments;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.Activities.AccelerometerModel;
import com.example.fuzzycontapp.Activities.HomePageActivity;
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
import com.example.fuzzycontapp.databinding.ActivityHomePageBinding;
import com.example.fuzzycontapp.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

public class HomeFragment extends Fragment implements PageRuleInterface, PageCategoryInterface {
    FragmentHomeBinding binding;
    public ArrayList<Bitmap> user_img;
    private ArrayList<CategoryRow> categoryRows;
    private ArrayList<ArrayList<Bitmap>> bmp = new ArrayList<ArrayList<Bitmap>>();
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> base_rules = new ArrayList<>();
    private ArrayList<Integer> id = new ArrayList<>();
    public static ArrayList<Rule_model> rule_models = new ArrayList<>();
    private Rules_Adapter rules_adapter;
    int[] categoryImg = {R.drawable.c_acc, R.drawable.c_optim, R.drawable.c_pend, R.drawable.c_pend_rules};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        user_img = new ArrayList<Bitmap>();
        categoryRows = new ArrayList<>();

        try {
            getImg();
            System.out.println(user_img.size() + "hjk");
            Global.IMG = user_img.get(0);
            binding.ava.setImageBitmap(user_img.get(0));
            binding.hi.setText("Hi, " + Global.USERNAME);
            binding.slogan.setText(Global.EMAIL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!(Global.GLOBAL_RULES == get_rules_count())){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        bmp = new ArrayList<ArrayList<Bitmap>>();
                        usernames = new ArrayList<>();
                        base_rules = new ArrayList<>();
                        id = new ArrayList<>();
                        rule_models = new ArrayList<>();
                        Message msg = handler.obtainMessage();
                        collect_img();
                        setRule_models();
                        handler.sendMessage(msg);
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(rule_models.size() + "rule_models");
        setUpCategories();
        CategoriesAdapter adapter = new CategoriesAdapter(this.getContext(), categoryRows, this);
        binding.rviewCateg.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rviewCateg.setLayoutManager(linearLayoutManager);


        rules_adapter = new Rules_Adapter(this.getContext(), rule_models, this);
        binding.rviewRules.setAdapter(rules_adapter);
        binding.rviewRules.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));


        binding.read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.frame_layout, new Theory());
                fm.commit();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rules_adapter.notifyDataSetChanged();
            binding.progress.setVisibility(View.INVISIBLE);
        }
    };


    @Override
    public void onItemClick(int position) {
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
                Intent intent = new Intent(this.getContext(), AccelerometerModel.class);
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


    private void setRule_models() {
        System.out.println(bmp.size());
        for (int i = 0; i < usernames.size(); i++) {
            System.out.println(i + "aaaa");
            rule_models.add(new Rule_model(usernames.get(i), bmp.get(i), base_rules.get(i), id.get(i)));
        }
    }

    private ArrayList<ArrayList<Bitmap>> collect_img() {
        CharBuffer charb = CharBuffer.allocate(10000);
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            Log.i(TAG, "Кот нажал на кнопку");

            try {
                request.put("Command", "get_base");
                request.put("StartIdx", 0);
                request.put("EndIdx", 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "Кот нажал на кнопку1");

            output.println(request);
            output.flush();
            try {
                while (!input.ready()) ;
                String s = input.readLine();
                charb.put(s);
                int n = new JSONObject(new String(charb.array())).getInt("RulesCount");
                System.out.println(n);

                for (int i = 0; i < n; i++) {
                    bmp.add(new ArrayList<Bitmap>());
                    while (!input.ready()) ;
                    String s3 = input.readLine();
                    System.out.println(s3);
                    usernames.add(new JSONObject(s3).getString("Username"));
                    base_rules.add(new JSONObject(s3).getString("Base"));
                    id.add(new JSONObject(s3).getInt("RuleID"));
                    for (int j = 0; j < 3; j++) {
                        while (!input.ready()) ;
                        String s2 = input.readLine();
                        System.out.println(s2);
                        bmp.get(i).add(conv_bitmap(get_img(s2)));
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return bmp;
    }

    static public Bitmap conv_bitmap(String str_img) {

        byte[] img_byte = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            img_byte = Base64.getDecoder().decode(str_img);
        }
        return BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);

    }

    static public String get_img(String img) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAA");
        JSONObject jsonObject;
        StringBuilder enc_img = new StringBuilder();
        try {
            System.out.println(" q " + img + "q");
            jsonObject = new JSONObject(img);
            int n = jsonObject.getInt("Parts");
            for (int i = 0; i < n; i++) {
                while (!input.ready());
                String readed = String.valueOf(input.readLine());
//                output.println("{\"Status\": \"OK\"}");
//                output.flush();
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
    public static Integer get_rules_count() throws IOException {
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        int n = 0;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            try {
                request.put("Command", "get_rules_count");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            output.println(request);
            output.flush();

            while (!input.ready()) ;
            try {
                n = new JSONObject(input.readLine()).getInt("RulesCount");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return n;
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
            user_img.add(conv_bitmap(get_img(s2)));
        }
        return user_img;
    }
}