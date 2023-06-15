package com.example.fuzzycontapp.Adapters;

import android.graphics.Color;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuzzycontapp.Indiv.TheoryRow;
import com.example.fuzzycontapp.R;
import com.example.fuzzycontapp.TheoryInterface;

import java.util.ArrayList;

public class TheoryAdapter extends RecyclerView.Adapter<TheoryAdapter.TheoryViewHolder>{

    ArrayList<TheoryRow> dataholder;
    TheoryInterface theoryInterface;
    public TheoryAdapter(ArrayList<TheoryRow> dataholder, TheoryInterface theoryInterface) {
        this.dataholder = dataholder;
        this.theoryInterface = theoryInterface;
    }

    @NonNull
    @Override
    public TheoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theory_row, parent, false);
        return new TheoryViewHolder(view, theoryInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TheoryViewHolder holder, int position) {
        holder.img.setImageResource(dataholder.get(position).getImg());
        holder.name.setText(dataholder.get(position).getName());
        holder.name.setTextColor(Color.parseColor("#9B9BB6"));
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    static class TheoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        public TheoryViewHolder(@NonNull View itemView, TheoryInterface theoryInterface) {
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.t1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (theoryInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            theoryInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
