package com.example.fuzzycontapp.Activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.fuzzycontapp.Indiv.ChartArrays;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityHomePageBinding;
import com.example.fuzzycontapp.databinding.ActivityShowChartsBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowCharts extends AppCompatActivity {
    ActivityShowChartsBinding binding;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowChartsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if(ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ShowCharts.this, new String[] { WRITE_EXTERNAL_STORAGE}, 1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ShowCharts.this, new String[] { WRITE_EXTERNAL_STORAGE }, 1);
        }

        ArrayList<Entry> entriesFirst = new ArrayList<>();
        ArrayList<Entry> entriesSecond = new ArrayList<>();
        ArrayList<Entry> entriesThird = new ArrayList<>();
        System.out.println(ChartArrays.WWW);
        for (int i=0; i< ChartArrays.XXX.size(); i++){
            entriesFirst.add(new Entry(ChartArrays.TIME.get(i), ChartArrays.XXX.get(i)));
            entriesSecond.add(new Entry(ChartArrays.TIME.get(i), ChartArrays.VVV.get(i)));
            entriesThird.add(new Entry(ChartArrays.TIME.get(i), ChartArrays.WWW.get(i)));
        }
        LineDataSet x_chart = new LineDataSet(entriesFirst, "x(t)");
        LineDataSet v_chart = new LineDataSet(entriesSecond, "v(t)");
        LineDataSet w_chart = new LineDataSet(entriesThird, "w(t)");

        x_chart.setColor(R.color.lav1);
        x_chart.setDrawCircles(false);
        x_chart.setDrawValues(false);
        ArrayList<ILineDataSet> dataSets1 = new ArrayList();
        dataSets1.add(x_chart);

        v_chart.setColor(R.color.lav1);
        v_chart.setDrawCircles(false);
        v_chart.setDrawValues(false);
        ArrayList<ILineDataSet> dataSets2 = new ArrayList();
        dataSets2.add(v_chart);

        w_chart.setColor(R.color.lav1);
        w_chart.setDrawCircles(false);
        w_chart.setDrawValues(false);
        ArrayList<ILineDataSet> dataSets3 = new ArrayList();
        dataSets3.add(w_chart);

        // Создадим переменную данных для графика
        LineData data1 = new LineData(dataSets1);
        LineData data2 = new LineData(dataSets2);
        LineData data3 = new LineData(dataSets3);

        // Передадим данные для графика в сам график
        binding.chartXxx.setData(data1);
        binding.chartXxx.getXAxis().setEnabled(false);
        binding.chartXxx.getAxisRight().setEnabled(false); //hide y-axis at right
        binding.chartXxx.getXAxis().setDrawGridLines(false);
        binding.chartXxx.getAxisLeft().setDrawGridLines(false);
        binding.chartXxx.getAxisRight().setDrawGridLines(false);

        binding.chartVvv.setData(data2);
        binding.chartVvv.getXAxis().setEnabled(false);
        binding.chartVvv.getAxisRight().setEnabled(false); //hide y-axis at right
        binding.chartVvv.getXAxis().setDrawGridLines(false);
        binding.chartVvv.getAxisLeft().setDrawGridLines(false);
        binding.chartVvv.getAxisRight().setDrawGridLines(false);

        binding.chartWww.setData(data3);
        binding.chartWww.getXAxis().setEnabled(false);
        binding.chartWww.getAxisRight().setEnabled(false); //hide y-axis at right
        binding.chartWww.getXAxis().setDrawGridLines(false);
        binding.chartWww.getAxisLeft().setDrawGridLines(false);
        binding.chartWww.getAxisRight().setDrawGridLines(false);
//        binding.chartXxx.saveToGallery()

        binding.chartXxx.animateY(1000);
        binding.chartVvv.animateY(2000);
        binding.chartWww.animateY(3000);

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.chartXxx.saveToGallery(Calendar.getInstance().getTime().toString() + "xChart", 85);
                binding.chartXxx.setSaveEnabled(true);
                binding.chartVvv.saveToGallery(Calendar.getInstance().getTime().toString() + "vChart", 85);
                binding.chartVvv.setSaveEnabled(true);
                binding.chartWww.saveToGallery(Calendar.getInstance().getTime().toString() + "wChart", 85);
                binding.chartWww.setSaveEnabled(true);

                Toast toast = Toast.makeText(getApplicationContext(), "charts are saved", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            boolean ok = true;
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                String permission = permissions[i];
                if(grantResults[i] != PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: missing permission " + permission);
                    ok = false;
                }
            }
            if(ok){
                System.out.println("everything is ok!");
            }

        }

    }

}