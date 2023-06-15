package com.example.fuzzycontapp.Fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.Activities.PageRule;
import com.example.fuzzycontapp.Adapters.Rules_Adapter;
import com.example.fuzzycontapp.Adapters.SavedRulesAdapter;
import com.example.fuzzycontapp.Indiv.CategoryRow;
import com.example.fuzzycontapp.Indiv.Rule_model;
import com.example.fuzzycontapp.PageRuleInterface;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.FragmentSavedRulesBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;


public class MyRules extends Fragment implements PageRuleInterface {
    FragmentSavedRulesBinding binding;
    private ArrayList<ArrayList<Bitmap>> bmp = new ArrayList<ArrayList<Bitmap>>();
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> base_rules = new ArrayList<>();
    private ArrayList<Integer> id = new ArrayList<>();
    public static ArrayList<Rule_model> my_rule_models = new ArrayList<>();
    private SavedRulesAdapter myRulesAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedRulesBinding.inflate(inflater, container, false);
        bmp = new ArrayList<ArrayList<Bitmap>>();
        usernames = new ArrayList<>();
        base_rules = new ArrayList<>();
        id = new ArrayList<>();
        my_rule_models = new ArrayList<>();
        saved_collect_img();
        setRule_models();

        myRulesAdapter = new SavedRulesAdapter(this.getContext(), my_rule_models, this);
        binding.rViewSavedRules.setAdapter(myRulesAdapter);
        binding.rViewSavedRules.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        return binding.getRoot();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this.getContext(), PageRule.class);
        intent.putExtra("NAME", my_rule_models.get(position).getName());
        intent.putExtra("RULES", my_rule_models.get(position).getRules());
        intent.putExtra("POS", position);
//        send_heart(rule_models.get(position).getId());
        startActivity(intent);
    }
    private void setRule_models() {
        System.out.println(bmp.size());
        for (int i = 0; i < usernames.size(); i++) {
            System.out.println(i + "aaaa");
            my_rule_models.add(new Rule_model(usernames.get(i), bmp.get(i), base_rules.get(i), id.get(i)));
        }
    }

    private ArrayList<ArrayList<Bitmap>> saved_collect_img() {
        CharBuffer charb = CharBuffer.allocate(10000);
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            Log.i(TAG, "Кот нажал на кнопку");

            try {
                request.put("Command", "get_my_rules");
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

                JSONObject input_json = new JSONObject(readed);
                enc_img.append(input_json.get("SplittedData"));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return enc_img.toString();
    }



}