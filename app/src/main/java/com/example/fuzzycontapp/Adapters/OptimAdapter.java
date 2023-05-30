package com.example.fuzzycontapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fuzzycontapp.Fragments.BasinHopping;
import com.example.fuzzycontapp.Fragments.Login;
import com.example.fuzzycontapp.Fragments.SignUp;

public class OptimAdapter extends FragmentStateAdapter {

    public OptimAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new SignUp();
        }
        return new BasinHopping();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
