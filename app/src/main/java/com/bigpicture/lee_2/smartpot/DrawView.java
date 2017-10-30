package com.bigpicture.lee_2.smartpot;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

/**
 * Created by lee-2 on 2017-10-06.
 */

public class DrawView extends DrawViewSet{
    private float point =0;
    public DrawView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint pnt = PaintSetting();
        Paint pnt2 = Paint2Setting();
        RectF rect = RectFSetting();
        rect.set(canvas.getWidth()/10, canvas.getHeight()/6, canvas.getWidth()*9/10,canvas.getHeight()*5/6);
        canvas.drawArc(rect, 135, 270, false, pnt);
        canvas.drawArc(rect, 135, point, false, pnt2);

    }
    public void setPoint(float point){this.point = point;}
    public float getPoint(){return this.point;}
}