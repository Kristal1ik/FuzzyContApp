package com.example.fuzzycontapp.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.fuzzycontapp.databinding.ActivityAddRuleBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.nio.CharBuffer;
import java.util.Objects;

public class AddRule extends AppCompatActivity {
    ActivityAddRuleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRuleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CharBuffer charb = CharBuffer.allocate(10000);
        byte[] img0 = new byte[0];
        Uri uri = Objects.requireNonNull(data).getData();
//        binding.profileImg.setImageBitmap(IMG);
    }
}