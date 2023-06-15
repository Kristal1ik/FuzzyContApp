package com.example.fuzzycontapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fuzzycontapp.Fragments.Login;
import com.example.fuzzycontapp.Fragments.MyRules;
import com.example.fuzzycontapp.Fragments.SavedRules;
import com.example.fuzzycontapp.Fragments.SignUp;

public class DifferentRulesAdapter extends FragmentStateAdapter {

    public DifferentRulesAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new MyRules();
        }
        return new SavedRules();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
