package com.example.fuzzycontapp.Activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fuzzycontapp.Fragments.AccelerometerFragment;
import com.example.fuzzycontapp.Indiv.Const;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivitySensorBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AccelerometerModel extends AppCompatActivity {
    ActivitySensorBinding binding;
    SensorManager sensorManager;
    Sensor sensorLinAccel;

    StringBuilder sb = new StringBuilder();

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySensorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorLinAccel = sensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        setFragment(new AccelerometerFragment());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.blue));

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AccelerometerModel.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog, (LinearLayout)findViewById(R.id.container));
                SeekBar seekBar_sens = bottomSheetView.findViewById(R.id.seekbar_sensitivity);
                SeekBar seekBar_mass = bottomSheetView.findViewById(R.id.seekbar_mass);
                SeekBar seekBar_bottom = bottomSheetView.findViewById(R.id.seekbar_bottom);
                SeekBar seekBar_top = bottomSheetView.findViewById(R.id.seekbar_top);

                TextView sens_pr = bottomSheetView.findViewById(R.id.sensitivity_pr);
                TextView mass_pr = bottomSheetView.findViewById(R.id.mass_pr);
                TextView bottom_pr = bottomSheetView.findViewById(R.id.bottom);
                TextView top_pr = bottomSheetView.findViewById(R.id.top);

                seekBar_mass.setProgress(Const.position_mass);
                mass_pr.setText(Const.m_current + "kg");

                seekBar_sens.setProgress(Const.position_sens);
                sens_pr.setText(Const.position_sens * 20 + "%");

                seekBar_bottom.setProgress(Const.position_bottom);
                bottom_pr.setText(Const.x2_current + "m");

                seekBar_top.setProgress(Const.position_top);
                top_pr.setText(Const.x1_current + "m");
                seekBar_sens.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Const.sensitivity_current = (Const.sensitivity * (progress * 20)) / 100;
                        sens_pr.setText(progress * 20 + " %");
                        Const.position_sens = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                seekBar_mass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Const.m_current = (Const.m * (progress * 0.1)) / 0.5;
                        Const.position_mass = progress;
                        mass_pr.setText(Const.m_current + " kg");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                seekBar_bottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Const.x2_current = (Const.x2 * (progress * 5)) / 50;
                        bottom_pr.setText(Const.x2_current + "m");
                        Const.position_bottom = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                seekBar_top.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Const.x1_current = (Const.x1 * (progress * 5)) / 50;
                        top_pr.setText(Const.x1_current + "m");
                        Const.position_top = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();



            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensorLinAccel,
                SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showInfo();
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    static float[] valuesLinAccel = new float[3];


    public static double showInfo() {
        return valuesLinAccel[1];
    }


    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    for (int i = 0; i < 3; i++) {
                        valuesLinAccel[i] = event.values[i];
                    }
                    break;
            }
        }
    };
    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_accelerometer, fragment);
        fragmentTransaction.commit();
    }
}