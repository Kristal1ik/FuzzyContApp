package com.example.fuzzycontapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuzzycontapp.Indiv.Rule_model;
import com.example.fuzzycontapp.PageRuleInterface;
import com.example.fuzzycontapp.R;

import java.util.ArrayList;

public class GlobalRulesAdapter extends RecyclerView.Adapter<GlobalRulesAdapter.Global_RulesHolder> {
    private final PageRuleInterface pageRuleInterface;
    Context context;
    ArrayList<Rule_model> rule_models;
    public GlobalRulesAdapter(Context context, ArrayList<Rule_model> rule_models, PageRuleInterface pageRuleInterface){
        this.context = context;
        this.rule_models = rule_models;
        System.out.println(rule_models.size() + "fghjk");
        this.pageRuleInterface = pageRuleInterface;
    }
    @NonNull
    @Override
    public GlobalRulesAdapter.Global_RulesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Раздувание макета и придание вида каждого элемента
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.global_rule_row, parent, false);

        return new GlobalRulesAdapter.Global_RulesHolder(view, pageRuleInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull GlobalRulesAdapter.Global_RulesHolder holder, int position) {
        // Присваивание значений для каждой из строк по мере их появления на экране
        holder.textView_name.setText(rule_models.get(position).getName());
        System.out.println(position + " b");
        holder.imageView_img.setImageBitmap(rule_models.get(position).getImgs().get(0));
    }

    @Override
    public int getItemCount() {
        // Сколько всего элементов
        return rule_models.size();
    }
    public static class Global_RulesHolder extends RecyclerView.ViewHolder{
        // Извлекание вида из макета (onCreate)
        ImageView imageView_img;
        TextView textView_name;
        public Global_RulesHolder(@NonNull View itemView, PageRuleInterface pageRuleInterface) {
            super(itemView);
            imageView_img = itemView.findViewById(R.id.img);
            textView_name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pageRuleInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            pageRuleInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
