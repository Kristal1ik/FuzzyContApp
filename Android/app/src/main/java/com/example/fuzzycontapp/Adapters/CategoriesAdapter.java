package com.example.fuzzycontapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fuzzycontapp.Indiv.CategoryRow;
import com.example.fuzzycontapp.PageCategoryInterface;
import com.example.fuzzycontapp.PageRuleInterface;
import com.example.fuzzycontapp.R;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.Categories_ViewHolder> {
    Context context;
    ArrayList<CategoryRow> categoryRows;
    private final PageCategoryInterface pageCategoryInterface;

    public CategoriesAdapter(Context context, ArrayList<CategoryRow> categoryRows, PageCategoryInterface pageCategoryInterface) {
        this.context = context;
        this.categoryRows = categoryRows;
        this.pageCategoryInterface = pageCategoryInterface;
    }

    @NonNull
    @Override
    public CategoriesAdapter.Categories_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_row, parent, false);
        return new Categories_ViewHolder(view, pageCategoryInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.Categories_ViewHolder holder, int position) {
        holder.name.setText(categoryRows.get(position).getName());
        holder.img.setImageResource(categoryRows.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return categoryRows.size();
    }
    public static class Categories_ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView img;
        public Categories_ViewHolder(@NonNull View itemView, PageCategoryInterface pageCategoryInterface) {
            super(itemView);
            img = itemView.findViewById(R.id.c_img);
            name = itemView.findViewById(R.id.c_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pageCategoryInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            pageCategoryInterface.onCategoryClick(pos);
                        }
                    }
                }
            });
        }
    }
}

