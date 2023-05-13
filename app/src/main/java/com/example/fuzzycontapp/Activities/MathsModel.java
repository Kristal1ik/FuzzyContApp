package com.example.fuzzycontapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityMathsModelBinding;

public class MathsModel extends Activity {
    MathsModelClass mathsModelClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.blue));
        mathsModelClass = new MathsModelClass(this);
        setContentView(mathsModelClass);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        mathsModelClass.pause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mathsModelClass.resume();
//    }
}
