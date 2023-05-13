package com.example.fuzzycontapp.Activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.example.fuzzycontapp.Indiv.Const;
import com.example.fuzzycontapp.R;

//
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.util.DisplayMetrics;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//import com.example.fuzzycontapp.R;
//
//public class MathsModelClass extends SurfaceView implements Runnable {
//    Thread thread = null;
//    boolean canDraw = false;
//
//    Paint blue, grey_line;
//    Path d_square = new Path();
//
//    Canvas canvas;
//    SurfaceHolder surfaceHolder;
//
//    int circle_x, circle_y;
//    int d_circle_x, d_circle_y;
//
//    int width, height;
//
//    public MathsModelClass(Context context) {
//        super(context);
//
//        surfaceHolder = getHolder();
//        circle_x = 130;
//        circle_y = 130;
//
//        d_circle_x = toPxs(65);
//        d_circle_y = toPxs(65);
//
//        width = Resources.getSystem().getDisplayMetrics().widthPixels;
//        height = Resources.getSystem().getDisplayMetrics().heightPixels;
//    }
//
//    @Override
//    public void run() {
//        brushes();
//        while (canDraw){
//            if(!surfaceHolder.getSurface().isValid()){
//                continue;
//            }
//            canvas = surfaceHolder.lockCanvas();
//            motion_circle(10);
//            motion_d_circle(10);
//            canvas.drawColor(getResources().getColor(R.color.lav1));
//            draw_density_square(width / 2, height / 2, 325, 325);
//            canvas.drawCircle(circle_x, circle_y, 50, blue);
//            canvas.drawCircle(d_circle_x, d_circle_y, toPxs(25), blue);
//            surfaceHolder.unlockCanvasAndPost(canvas );
//        }    }
//
//    public void pause(){
//        canDraw = false;
//        while (true){
//        try {
//            thread.join();
//            break;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        }
//        thread = null;
//    }
//    public void resume(){
//        canDraw = true;
//        thread = new Thread(this);
//        thread.start();
//
//    }
//    public void brushes(){
//        blue = new Paint();
//        blue.setColor(getResources().getColor(R.color.blue));
//        blue.setStyle(Paint.Style.FILL);
//
//        grey_line = new Paint();
//        grey_line.setColor(getResources().getColor(R.color.purple_700));
//        grey_line.setStyle(Paint.Style.STROKE);
//        grey_line.setStrokeWidth(30);
//    }
//
//    public void motion_circle(int speed){
//        if ((circle_y == 130) && (circle_x < 650)){
//            circle_x = circle_x + speed;
//        }
//        if ((circle_y < 650) && (circle_x == 650)){
//            circle_y = circle_y + speed;
//        }
//        if ((circle_y == 650) && (circle_x > 130)){
//            circle_x = circle_x - speed;
//        }
//        if ((circle_y > 130) && (circle_x == 130)){
//            circle_y = circle_y - speed;
//        }
//    }
//
//    private int toPxs(int dps){
//        return (int) (dps * getResources().getDisplayMetrics().density);
//    }
//
//    private void draw_density_square(int x1, int y1, int x2, int y2){
//        int xdp1, ydp1, xdp2, ydp2;
//
//        xdp1 = toPxs(x1);
//        ydp1 = toPxs(y1);
//        xdp2 = toPxs(x2);
//        ydp2 = toPxs(y2);
//
//        d_square.moveTo(xdp1, ydp1);
//        d_square.lineTo(xdp2, ydp1);
//        d_square.moveTo(xdp2, ydp1);
//        d_square.lineTo(xdp2, ydp2);
//        d_square.moveTo(xdp2, ydp2);
//        d_square.lineTo(xdp1, ydp2);
//        d_square.moveTo(xdp1, ydp2);
//        d_square.lineTo(xdp1, ydp1);
//
//        this.canvas.drawPath(d_square, grey_line);
//
//    }
//    public void motion_d_circle(int speed){
//        if ((d_circle_y == toPxs(65)) && (d_circle_x < toPxs(325))){
//            d_circle_x = d_circle_x + toPxs(speed);
//        }
//        if ((d_circle_y < toPxs(325)) && (d_circle_x == toPxs(325))){
//            d_circle_y = d_circle_y + toPxs(speed);
//        }
//        if ((d_circle_y == toPxs(325)) && (d_circle_x > toPxs(65))){
//            d_circle_x = d_circle_x - toPxs(speed);
//        }
//        if ((d_circle_y > toPxs(65)) && (d_circle_x == toPxs(65))){
//            d_circle_y = d_circle_y - toPxs(speed);
//        }
//    }
//}
public class MathsModelClass extends View {
    Paint blue, grey_line, blue_line;
    int width, height;
    int x_pos, y_pos;
    int y_dir;

    public MathsModelClass(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.lav1));
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        x_pos = width / 2; y_pos = height / 2;
        y_dir = 1;
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

        blue_line = new Paint();
        blue_line.setColor(getResources().getColor(R.color.blue));
        blue_line.setStyle(Paint.Style.STROKE);
        blue_line.setStrokeWidth(Const.silWidth);

        Rect rect = new Rect();
        Rect silk = new Rect();
        rect.set(x_pos, 0, x_pos, y_pos);
        silk.set(0, 0, width, Const.silWidth);
        canvas.drawRect(rect, grey_line);
        canvas.drawRect(silk,blue_line);
        canvas.drawCircle(x_pos, y_pos, Const.r, blue);
        y_pos = y_dir + y_pos;

        invalidate();
    }
}