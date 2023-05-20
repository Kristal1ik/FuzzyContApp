package com.example.fuzzycontapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.fuzzycontapp.Indiv.ChartArrays;
import com.example.fuzzycontapp.databinding.ActivityHomePageBinding;
import com.example.fuzzycontapp.databinding.ActivityShowChartsBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ShowCharts extends AppCompatActivity {
    ActivityShowChartsBinding binding;
    LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowChartsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ArrayList<Entry> entriesSecond = new ArrayList<>();
        for (int i=0; i< ChartArrays.XXX.size(); i++){
            entriesSecond.add(new Entry(ChartArrays.TIME.get(i), ChartArrays.XXX.get(i)));
        }
        LineDataSet datasetSecond = new LineDataSet(entriesSecond, "График второй");
        datasetSecond.setColor(Color.GREEN);
        datasetSecond.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<ILineDataSet> dataSets = new ArrayList();
        dataSets.add(datasetSecond);

        // Создадим переменную  данных для графика
        LineData data = new LineData(dataSets);
        // Передадим данные для графика в сам график
        binding.chartXxx.setData(data);

        // График будет анимироваться 0.5 секунды
        binding.chartXxx.animateY(500);

    }
}