package com.example.fuzzycontapp.Activities;


import static com.example.fuzzycontapp.Indiv.Global.*;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.input;
import static com.example.fuzzycontapp.Activities.MainActivity.MyThread.output;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;

import com.example.fuzzycontapp.databinding.ActivityUserPageBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

public class UserPage extends AppCompatActivity {
    ActivityUserPageBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                JSONObject request = new JSONObject();
                try {
                    request.put("Command", "get_profile_info");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                output.println(request);
                output.flush();
            while(!input.ready());
            String s = input.readLine();
            String myRules = Integer.toString(new JSONObject(s).getInt("myRules"));
            String savedRules = Integer.toString(new JSONObject(s).getInt("savedRules"));
            binding.rules.setText(myRules);
            binding.sRules.setText(savedRules);
            }

        binding.username.setText(USERNAME);
        binding.email.setText(EMAIL);
        binding.profileImg.setImageBitmap(IMG);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this, HomePageActivity.class);
                startActivity(intent);
            }
        });
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserPage.this).crop().compress(1024).maxResultSize(1080, 1080).start();

            }
        });
    } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserPage.this, HomePageActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CharBuffer charb = CharBuffer.allocate(10000);
        byte[] img0 = new byte[0];
        Uri uri = Objects.requireNonNull(data).getData();
        try {
            img0 = Files.readAllBytes(Paths.get(uri.getPath()));
            String img = Base64.getEncoder().encodeToString(img0);
            if (img.length() > 4096) {
                int range = img.length() / 4096;
                if ((img.length() % 4096) != 0) {
                    range = img.length() / 4096 + 1;
                }
                int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    JSONObject request = new JSONObject();
                    try {
                        request.put("Command", "splitted_data");
                        System.out.println(range);
                        request.put("Parts", range);
                        request.put("SplittedCommand", "upload_profile_image");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    output.println(request);
                    output.flush();
                    try {
                        while (!input.ready()) ;
                        System.out.println(input.read(charb));
                        System.out.println("==" + new String(charb.array()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                send_packs(img, range);
            }
            try {
                IMG = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.profileImg.setImageBitmap(IMG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            Intent intent = new Intent(UserPage.this, HomePageActivity.class);
            startActivity(intent);
        }
    }


    private void send_packs(String img, int range) {
        System.out.println(img.length());
        CharBuffer charb = CharBuffer.allocate(10000);
        int n = 0;
        for (int i = 0; n < range; i += 4096) {
            int step = 4096;
            if (i + 4096 > img.length()) {
                step = img.length() - i;
            }
            int SDK_INT = android.os.Build.VERSION.SDK_INT; // check
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                JSONObject request = new JSONObject();
                try {
                    request.put("Command", "splitted_data");
                    request.put("SplittedData", img.substring(i, i + step));
                    request.put("SplittedCommand", "continue");
                    request.put("Part", n);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                output.println(request);
                output.flush();
                try {
                    while (!input.ready()) ;
                    System.out.println(input.read(charb));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            n += 1;
        }
    }
//    static class ChangeAt extends Thread {
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public void run() {
//            Bitmap prof1 = BitmapFactory.decodeFile(String.valueOf(R.drawable.at));
//            ByteArrayOutputStream prof2 = new ByteArrayOutputStream();
//            prof1.compress(Bitmap.CompressFormat.JPEG, 100, prof2);
//            Base64.getEncoder().encodeToString(prof1);
//        }
//    }
}