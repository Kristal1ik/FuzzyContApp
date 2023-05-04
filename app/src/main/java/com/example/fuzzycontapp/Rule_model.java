package com.example.fuzzycontapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Rule_model {
    String name;
    Bitmap image;
    ArrayList<Bitmap> imgs;
    String rules;


    public Rule_model(String name, ArrayList<Bitmap> imgs, String rules) {
        this.name = name;
        this.imgs = imgs;
        this.rules = rules;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Bitmap> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<Bitmap> imgs) {
        this.imgs = imgs;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
