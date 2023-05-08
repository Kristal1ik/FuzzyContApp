package com.example.fuzzycontapp.Fragments;

import static com.example.fuzzycontapp.MainActivity.MyThread.charb;
import static com.example.fuzzycontapp.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.MainActivity.MyThread.output;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.databinding.FragmentPendulum1Binding;
import com.example.fuzzycontapp.databinding.FragmentPendulum3Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Base64;


public class Pendulum3 extends Fragment {
    FragmentPendulum3Binding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendulum3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getImg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                CharBuffer charb = CharBuffer.allocate(10000);

                int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        JSONObject request = new JSONObject();
                        try {
                            request.put("Command", "get_image");
                            request.put("Image", "");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        output.println(request);
                        output.flush();
                        try {
                            while(!input.ready());
                            input.read(charb);
                            String img = get_img(charb);
                            byte[] img_byte = Base64.getDecoder().decode(img);

                            Bitmap bmp = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                            binding.img.setImageBitmap(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });

    }
    private String get_img(CharBuffer img){
        JSONObject jsonObject;
        String img_str = new String(img.array());
        StringBuilder enc_img = new StringBuilder();
        try {
            jsonObject = new JSONObject(img_str);
            int n = jsonObject.getInt("Parts");
            for (int i=0; i < n; i++){
                while (!input.ready());
                String readed = String.valueOf(input.readLine());
                output.println("{\"Status\": \"OK\"}");
                output.flush();
                System.out.println(readed);
                JSONObject input_json = new JSONObject(readed);
                enc_img.append(input_json.get("SplittedData"));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    return enc_img.toString();
    }
}