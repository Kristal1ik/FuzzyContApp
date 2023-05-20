package com.example.fuzzycontapp.Activities;

import static com.example.fuzzycontapp.Activities.AccelerometerModel.showInfo;
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
import android.util.DisplayMetrics;
import android.view.View;

import com.example.fuzzycontapp.FuzzyLogic.Controller;
import com.example.fuzzycontapp.FuzzyLogic.Rules;
import com.example.fuzzycontapp.FuzzyLogic.Trapezoid;
import com.example.fuzzycontapp.Indiv.Const;
import com.example.fuzzycontapp.R;

import java.util.ArrayList;
import java.util.List;

public class AccelerometerModelClass extends View {
    Paint blue, grey_line, border_line;
    double width, height;
    int x_pos, y_pos;
    double delta;
    int delta_borders;

    double x = x0;
    double v = v0;
    double w = 0;

    public AccelerometerModelClass(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.lav1));
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        System.out.println(height);
        x_pos = (int) width / 2;
        y_pos = 0;
        delta = (height / Const.height) * 4100;
        delta_borders = (int)height / 5;
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


        Rect border = new Rect();
        border.set(0, (int)(Const.x1_current* 10 *delta_borders), (int) height, (int)(Const.x1_current*delta_borders * 10) + 2);
        canvas.drawRect(border, border_line);

        Rect border2 = new Rect();
        border2.set(0, (int)(Const.x2_current* 10 *delta_borders), (int) height, (int)(Const.x2_current*delta_borders * 10) + 2);
        canvas.drawRect(border2, border_line);

        w = showInfo();
        if (w > 0) {
            w = w * Const.sensitivity_current;
        } else w = 0;
        System.out.println(x * delta);
        double[] xv = f(x, v, w);
        x = xv[0];
        v = xv[1];


        Rect rect = new Rect();
        Rect silk = new Rect();
        rect.set(x_pos, 0, x_pos, (int) (x * delta));
        silk.set(0, 0, (int) width, Const.silWidth);
        canvas.drawRect(rect, grey_line);
        canvas.drawCircle(x_pos, (int) (x * delta), Const.radius, blue);
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


}
