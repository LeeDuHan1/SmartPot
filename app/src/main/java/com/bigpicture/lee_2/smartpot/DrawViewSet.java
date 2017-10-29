package com.bigpicture.lee_2.smartpot;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lee-2 on 2017-10-28.
 */

public class DrawViewSet extends View{
    private Paint pnt, pnt2;
    private RectF rectF;
    private float width, height;

    public DrawViewSet(Context context, AttributeSet attr){
        super(context, attr);
        pnt = new Paint();
        pnt.setStrokeWidth(15f);
        pnt.setColor(Color.rgb(230,238,240));
        pnt.setStyle(Paint.Style.STROKE);


        rectF = new RectF();
//        rectF.set(30, 30, 300,300);

        pnt2 = new Paint();
        pnt2.setStrokeWidth(12f);
        pnt2.setShader(new LinearGradient(0,0,270,0, Color.parseColor("#ff5c55"),Color.parseColor("#edb989"), Shader.TileMode.MIRROR));
        pnt2.setStyle(Paint.Style.STROKE);


    }

    public void SetWidth(float width){this.width = width;}
    public void SetHeight(float height){this.height = height;}
    public Paint PaintSetting(){
        return pnt;
    }
    public Paint Paint2Setting(){
        return pnt2;
    }
    public RectF RectFSetting(){
        return rectF;
    }
}
