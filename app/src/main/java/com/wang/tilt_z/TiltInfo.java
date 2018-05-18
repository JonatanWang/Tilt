package com.wang.tilt_z;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import static android.graphics.Color.*;

/**
 * Created by Zhengyu Wang on 2016-12-04.
 * Email: zhengyuw@kth.se
 * View class to show the tilting grade
 */

public class TiltInfo extends View {

    private Paint paint;
    private Paint backgroundPaint;
    private String grade;

    public TiltInfo(Context context) {
        super(context);
        paint = new Paint();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.YELLOW);
        paint.setTextSize(250);
        paint.setColor(BLUE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(50);
        Typeface font = Typeface.create(Typeface.SERIF, Typeface.BOLD);
        paint.setTypeface(font);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(backgroundPaint);
        canvas.drawText(grade + "°", canvas.getWidth() / 5, canvas.getHeight() / 2, paint);
        invalidate();
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setRedColor() {
        this.paint.setColor(Color.RED);
    }

    public void setBlueColor() {
        this.paint.setColor(Color.BLUE);
    }

    // for test purpose
    public void toggleColor() {
        if (this.paint.getColor() == BLUE) {
            setRedColor();
        } else if (this.paint.getColor() == RED) {
            setBlueColor();
        } else {
        }
    }
}
