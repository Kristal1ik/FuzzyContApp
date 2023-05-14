package com.example.fuzzycontapp.Activities;


import static com.example.fuzzycontapp.Indiv.Const.dt;
import static com.example.fuzzycontapp.Indiv.Const.g;
import static com.example.fuzzycontapp.Indiv.Const.k;
import static com.example.fuzzycontapp.Indiv.Const.l;
import static com.example.fuzzycontapp.Indiv.Const.m;
import static com.example.fuzzycontapp.Indiv.Const.maxis;
import static com.example.fuzzycontapp.Indiv.Const.r;
import static com.example.fuzzycontapp.Indiv.Const.v0;
import static com.example.fuzzycontapp.Indiv.Const.x0;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.example.fuzzycontapp.FuzzyLogic.Controller;
import com.example.fuzzycontapp.FuzzyLogic.Rules;
import com.example.fuzzycontapp.FuzzyLogic.Trapezoid;
import com.example.fuzzycontapp.Indiv.Const;
import com.example.fuzzycontapp.R;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class MathsModelClass extends View {
    Paint blue, grey_line, border_line;
    double width, height;
    int x_pos, y_pos;
    double delta;

    ArrayList<Double> xx = new ArrayList<>();
    double x = x0;
    double v = v0;

    public MathsModelClass(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.lav1));
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        System.out.println(width + " " + height);
        x_pos = (int)width / 2; y_pos = 0;
        delta = (height / Const.height) * 4100;
        System.out.println(delta);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        blue = new Paint();
        blue.setColor(getResources().getColor(R.color.blue));
        blue.setStyle(Paint.Style.FILL);

        grey_line = new Paint();
        grey_line.setColor(getResources().getColor(R.color.purple_700));
        grey_line.setStyle(Paint.Style.STROKE);
        grey_line.setStrokeWidth(Const.strWidth);


        border_line = new Paint();
        border_line.setColor(getResources().getColor(R.color.purple_700));
        border_line.setStyle(Paint.Style.STROKE);
        border_line.setAlpha(50);
        border_line.setStrokeWidth(Const.borderWidth);


        for (int i=0; i< height; i+=height/5){
            Rect border = new Rect();
            border.set(0, i, (int)height, i+2);
            canvas.drawRect(border, border_line);
        }

        xx.add(x);
        List<Trapezoid> lst = new ArrayList<Trapezoid>();
        double[] trap = new Controller(x, v).return_();
        Trapezoid trunc1 = new Trapezoid(new double[] {Rules.w1[0], Rules.w1[1], Rules.w1[2], Rules.w1[3], trap[0]});
        Trapezoid trunc2 = new Trapezoid(new double[] {Rules.w2[0], Rules.w2[1], Rules.w2[2], Rules.w2[3], trap[1]});
        Trapezoid trunc3 = new Trapezoid(new double[] {Rules.w3[0], Rules.w3[1], Rules.w3[2], Rules.w3[3], trap[2]});
        Trapezoid trunc4 = new Trapezoid(new double[] {Rules.w4[0], Rules.w4[1], Rules.w4[2], Rules.w4[3], trap[3]});
        Trapezoid trunc5 = new Trapezoid(new double[] {Rules.w5[0], Rules.w5[1], Rules.w5[2], Rules.w5[3], trap[4]});
        lst.add(trunc1);
        lst.add(trunc2);
        lst.add(trunc3);
        lst.add(trunc4);
        lst.add(trunc5);
        double w = area(lst);
        double[] xv = f(x, v, w);
        x = xv[0]; v = xv[1];


        Rect rect = new Rect();
        Rect silk = new Rect();
        rect.set(x_pos, 0, x_pos, (int)(x*delta));
        silk.set(0, 0, (int)width, Const.silWidth);
        canvas.drawRect(rect, grey_line);
        canvas.drawCircle(x_pos, (int)(x*delta), Const.radius, blue);
        invalidate();
    }

    private static double[] f(double x, double v, double w) {
        double a = (m * r * r * (g - w)) / (0.5 * (m * Const.R * Const.R + maxis * r * r) + (m + maxis) * r * r);
        if ((x == Const.R && v < 0) || (x == l && v > 0)) {
            v = -v * (1 - k);
        }
        double xnew = x + v * dt + 0.5 * Math.pow(dt, 2) * a;
        double vnew = v + a * dt;
        if (xnew > l) {
            xnew = l;
        }
        if (xnew < Const.R) {
            xnew = Const.R;
        }
        return new double[]{xnew, vnew};
    }
    private static double area(List<Trapezoid> lst_w){
        double dx = 0.1;
        double integr1 = 0.0;
        double integr2 = 0.0;
        for (double i=0; i < 30; i+=dx){
            double def_h = 0.0;
            for (Trapezoid elem: lst_w){
                double h = elem.muh(i);
                def_h = Math.max(def_h, h);
                integr1 += def_h * dx * i;
                integr2 += def_h * dx;}}
        if (integr1 == 0) return 0;
        try{
            return integr1 / integr2;}
        catch (ArithmeticException ex){
            return 0;}
    }
}