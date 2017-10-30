package com.bigpicture.lee_2.smartpot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lee-2 on 2017-10-29.
 */

public class LineDrawView extends View {
    public LineDrawView(Context context, AttributeSet attr){
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint pnt = new Paint();
        pnt.setShader(new LinearGradient(0,0,120,0, Color.parseColor("#eb847b"),Color.parseColor("#f2c29e"), Shader.TileMode.MIRROR));
        pnt.setStyle(Paint.Style.STROKE);
        pnt.setStrokeWidth(10f);
        canvas.drawLine(canvas.getWidth()/4,0,canvas.getWidth()*3/4,0,pnt);
    }
}
