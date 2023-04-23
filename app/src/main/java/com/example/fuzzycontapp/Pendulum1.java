package com.example.fuzzycontapp;

import static com.example.fuzzycontapp.MainActivity.MyThread.charb;
import static com.example.fuzzycontapp.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.MainActivity.MyThread.output;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.databinding.FragmentPendulum1Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Base64;


public class Pendulum1 extends Fragment {
    FragmentPendulum1Binding binding;
    OutputStream outputStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendulum1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        charb.clear();
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) binding.imageView.getDrawable();
                Bitmap img = drawable.getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();
                String new_img = Base64.getEncoder().encodeToString(bitmapdata);
                if (new_img.length() > 4096){
                    int range = new_img.length() / 4096;
                    if ((new_img.length() % 4096) != 0){
                        range = new_img.length() / 4096 + 1;
                    }
                    int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        JSONObject request = new JSONObject();
                        try {
                            request.put("Command", "splitted_data");
                            request.put("Parts", range);
                            request.put("SplittedCommand", "send_image");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        output.print(request);
                        output.flush();
                        try {
                            while(!input.ready());
                            System.out.println(input.read(charb));
                            System.out.println("==" + new String (charb.array()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    send_packs(new_img, range);
                }
            }
            });

}
    private void send_packs(String img, int range){
        int n = 0;
        for (int i=0; n < range; i+=4096){
            int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                JSONObject request = new JSONObject();
                try {
                    request.put("Command", "splitted_data");
                    request.put("SplittedData", img.substring(i, i+4096));
                    request.put("SplittedCommand", "continue");
                    request.put("Part", n);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                output.print(request);
                output.flush();
                try {
                    while(!input.ready());
                    System.out.println(input.read(charb));
                    System.out.println("==" + new String (charb.array()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            n += 1;

        }

    }
}