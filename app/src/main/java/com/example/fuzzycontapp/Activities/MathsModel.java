package com.example.fuzzycontapp.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.fuzzycontapp.Fragments.MathsFragment;

import com.example.fuzzycontapp.Indiv.ChartArrays;
import com.example.fuzzycontapp.Indiv.Const;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.databinding.ActivityMathsModelBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class MathsModel extends AppCompatActivity {
    ActivityMathsModelBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMathsModelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.blue));
        setFragment(new MathsFragment());
        setContentView(view);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MathsModel.this, ShowCharts.class);
                System.out.println(ChartArrays.XXX);
                System.out.println(ChartArrays.VVV);
                System.out.println(ChartArrays.WWW);

                startActivity(intent);
            }
        });
        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MathsModel.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_maths, (LinearLayout)findViewById(R.id.container));
                SeekBar seekBar_bottom = bottomSheetView.findViewById(R.id.seekbar_bottom);
                SeekBar seekBar_top = bottomSheetView.findViewById(R.id.seekbar_top);

                TextView bottom_pr = bottomSheetView.findViewById(R.id.bottom);
                TextView top_pr = bottomSheetView.findViewById(R.id.top);


                seekBar_bottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Const.x2_current_maths  = (Const.x2_maths  * (progress * 5)) / 50;
                        bottom_pr.setText(Const.x2_current_maths  + "m");
                        Const.position_bottom_maths = progress;
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
                        Const.x1_current_maths = (Const.x1_maths * (progress * 5)) / 50;
                        top_pr.setText(Const.x1_current_maths + "m");
                        Const.position_top_maths = progress;
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
    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_maths, fragment);
        fragmentTransaction.commit();
    }


}
