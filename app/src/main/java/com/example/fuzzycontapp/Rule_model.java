package com.example.fuzzycontapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Rule_model {
    String name;
    Bitmap image;
    ArrayList<Bitmap> imgs;
    String rules;
    Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public Rule_model(String name, ArrayList<Bitmap> imgs, String rules, Integer id) {
        this.name = name;
        this.imgs = imgs;
        this.rules = rules;
        this.id = id;
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
