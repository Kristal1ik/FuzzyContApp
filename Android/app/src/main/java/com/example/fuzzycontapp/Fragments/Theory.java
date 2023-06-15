package com.example.fuzzycontapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuzzycontapp.Activities.Fuzzy1;
import com.example.fuzzycontapp.Adapters.TheoryAdapter;
import com.example.fuzzycontapp.Indiv.Global;
import com.example.fuzzycontapp.Indiv.TheoryRow;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.TheoryInterface;
import com.example.fuzzycontapp.databinding.FragmentHomeBinding;
import com.example.fuzzycontapp.databinding.FragmentTheoryBinding;

import java.util.ArrayList;

public class Theory extends Fragment implements TheoryInterface {
    FragmentTheoryBinding binding;
    ArrayList<TheoryRow> dataholder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTheoryBinding.inflate(inflater, container, false);
        dataholder = new ArrayList<>();
        dataholder.add(new TheoryRow("Теория нечетких множеств. Алгоритм Мамдани.", R.drawable.pic));
        dataholder.add(new TheoryRow("База правил.", R.drawable.pic));
        dataholder.add(new TheoryRow("Фаззификация. Функция принадлежности.", R.drawable.pic));


        binding.rview.setAdapter(new TheoryAdapter(dataholder, this));

        binding.rview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return binding.getRoot();
    }


    @Override
    public void onItemClick(int position) {
        Global.THEORY_NUMBER = position;
        Intent intent = new Intent(this.getContext(), Fuzzy1.class);
        startActivity(intent);
    }
}