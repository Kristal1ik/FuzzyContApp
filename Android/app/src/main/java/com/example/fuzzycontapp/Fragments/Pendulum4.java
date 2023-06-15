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

import com.example.fuzzycontapp.Adapters.Rules_Adapter;
import com.example.fuzzycontapp.Activities.PageRule;
import com.example.fuzzycontapp.PageRuleInterface;
import com.example.fuzzycontapp.Indiv.Rule_model;
import com.example.fuzzycontapp.databinding.FragmentPendulum4Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;

public class Pendulum4 extends Fragment implements PageRuleInterface {
    FragmentPendulum4Binding binding;

    public static ArrayList<ArrayList<Bitmap>> bmp = new ArrayList<ArrayList<Bitmap>>();
    public static ArrayList<String> usernames = new ArrayList<>();
    public static ArrayList<String> base_rules = new ArrayList<>();
    public static ArrayList<Integer> id = new ArrayList<>();
    public static ArrayList<Rule_model> rule_models = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendulum4Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bmp = new ArrayList<ArrayList<Bitmap>>();
        usernames = new ArrayList<>();
        base_rules = new ArrayList<>();
        id = new ArrayList<>();
        rule_models = new ArrayList<>();
        ThreadSetBase threadSetBase = new ThreadSetBase();
        threadSetBase.start();
        try {
            threadSetBase.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Rules_Adapter rules_adapter = new Rules_Adapter(this.getContext(), rule_models, this);
        binding.recyclerViewRules.setAdapter(rules_adapter);
        binding.recyclerViewRules.setLayoutManager(new LinearLayoutManager(this.getContext()));}

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this.getContext(), PageRule.class);

        intent.putExtra("NAME", rule_models.get(position).getName());
        intent.putExtra("RULES", rule_models.get(position).getRules());
        intent.putExtra("POS", position);

        startActivity(intent);
    }


    static class ThreadSetBase extends Thread {
        @Override
        public void run() {
            collect_img();
            setRule_models();
        }
    }
    static private void setRule_models(){
        System.out.println(bmp.size());
        for (int i = 0; i<usernames.size(); i++){
            System.out.println(i);
            rule_models.add(new Rule_model(usernames.get(i), bmp.get(i), base_rules.get(i), id.get(i)));
        }
    }
    static private ArrayList<ArrayList<Bitmap>> collect_img(){
        CharBuffer charb = CharBuffer.allocate(10000);
        int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject request = new JSONObject();
            try {
                request.put("Command", "get_saved_rules");
                request.put("StartIdx", 0);
                request.put("EndIdx", 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            output.println(request);
            output.flush();
            try {
                while(!input.ready());
                String s = input.readLine();
                charb.put(s);
                System.out.println(s);
                int n = new JSONObject(new String(charb.array())).getInt("RulesCount");
                System.out.println(n);

                for (int i=0; i < n; i++){
                    bmp.add(new ArrayList<Bitmap>());
                    while(!input.ready());
                    String s3 = input.readLine();
                    System.out.println(s3);
                    usernames.add(new JSONObject(s3).getString("Username"));
                    base_rules.add(new JSONObject(s3).getString("Base"));
                    id.add(new JSONObject(s3).getInt("RuleID"));
                    for (int j=0; j < 3; j++){
                        while(!input.ready());
                        String s2 = input.readLine();
                        System.out.println(s2);
                        bmp.get(i).add(conv_bitmap(get_img(s2)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }}
        return bmp;
    }
    static private Bitmap conv_bitmap(String str_img){

        byte[] img_byte = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            img_byte = Base64.getDecoder().decode(str_img);
        }
        return BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);

    }
    static private String get_img(String img){
        System.out.println("AAAAAAAAAAAAAAAAAAAAA");
        JSONObject jsonObject;
        StringBuilder enc_img = new StringBuilder();
        try {
            System.out.println(" q " + img + "q");
            jsonObject = new JSONObject(img);
            int n = jsonObject.getInt("Parts");
            for (int i=0; i < n; i++){
                while (!input.ready());
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
}
//package com.example.fuzzycontapp;
//
//import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.charb;
//import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
//import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Build;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.fragment.app.Fragment;
//
//import android.os.StrictMode;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.fuzzycontapp.databinding.FragmentPendulum4Binding;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.CharBuffer;
//import java.util.Base64;
//
//
//public class Pendulum4 extends Fragment {
//    FragmentPendulum4Binding binding;
//    OutputStream outputStream;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        binding = FragmentPendulum4Binding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        CharBuffer charb = CharBuffer.allocate(10000);
//        binding.button2.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View v) {
//                BitmapDrawable drawable = (BitmapDrawable) binding.imageView.getDrawable();
//                Bitmap img = drawable.getBitmap();
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                img.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                byte[] bitmapdata = bos.toByteArray();
//                String new_img = Base64.getEncoder().encodeToString(bitmapdata);
//
//
//                if (new_img.length() > 4096){
//                    int range = new_img.length() / 4096;
//                    if ((new_img.length() % 4096) != 0){
//                        range = new_img.length() / 4096 + 1;
//                    }
//                    int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
//                    if (SDK_INT > 8)
//                    {
//                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                                .permitAll().build();
//                        StrictMode.setThreadPolicy(policy);
//                        JSONObject request = new JSONObject();
//                        try {
//                            request.put("Command", "splitted_data");
//                            request.put("Parts", range);
//                            request.put("SplittedCommand", "send_image");
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        output.println(request);
//                        output.flush();
//                        try {
//                            while(!input.ready());
//                            System.out.println(input.read(charb));
//                            System.out.println("==" + new String (charb.array()));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    send_packs(new_img, range);
//                }
//            }
//            });
//
//}
//    private void send_packs(String img, int range){
//        int n = 0;
//        for (int i=0; n < range; i+=4096){
//            int step = 4096;
//            if (i + 4096 > img.length()){
//                step = img.length() - i;
//                System.out.println(step);
//            }
//            int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
//            if (SDK_INT > 8)
//            {
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                        .permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//                JSONObject request = new JSONObject();
//                try {
//                    request.put("Command", "splitted_data");
//                    request.put("SplittedData", img.substring(i, i+step));
//                    request.put("SplittedCommand", "continue");
//                    request.put("Part", n);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                output.println(request);
//                output.flush();
//                try {
//                    while(!input.ready());
//                    System.out.println(input.read(charb));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            n += 1;
//
//        }
//
//    }
//}