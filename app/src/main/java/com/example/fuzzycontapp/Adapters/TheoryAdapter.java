package com.example.fuzzycontapp.Adapters;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuzzycontapp.Indiv.TheoryRow;
import com.example.fuzzycontapp.R;

import java.util.ArrayList;

public class TheoryAdapter extends RecyclerView.Adapter<TheoryAdapter.TheoryViewHolder>{

    ArrayList<TheoryRow> dataholder;
    public TheoryAdapter(ArrayList<TheoryRow> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public TheoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theory_row, parent, false);
        return new TheoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheoryViewHolder holder, int position) {
        holder.img.setImageResource(dataholder.get(position).getImg());
        holder.name.setText(dataholder.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class TheoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        public TheoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.t1);
        }
    }
}
