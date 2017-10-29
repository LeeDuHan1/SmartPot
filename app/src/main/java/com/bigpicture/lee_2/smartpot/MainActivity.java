package com.bigpicture.lee_2.smartpot;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    public static final int RESULT_CODE_MENU = 100;
    private TextView plantTitleText, tipText, properMoist;
    private ImageView titleImage;
    ImageButton titlePreView;
    GetValueHandler getValueHandler;
    DrawView drawView;
    DrawView2 drawView2;
    DrawView3 drawView3;
    GetPotValue gwv = new GetPotValue();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView)findViewById(R.id.circle1);
        drawView2 = (DrawView2)findViewById(R.id.circle2);
        drawView3 = (DrawView3)findViewById(R.id.circle3);
        plantTitleText = (TextView)findViewById(R.id.plantTitle);
        tipText = (TextView)findViewById(R.id.tip_text);
        titleImage = (ImageView) findViewById(R.id.titleImage);
        titlePreView = (ImageButton) findViewById(R.id.titlePreView);
        properMoist = (TextView)findViewById(R.id.properMoist);


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public class GetValueHandler extends Handler{
        public void handleMessage(Message msg){
            drawView2.setPoint2(130);
            Toast.makeText(getApplicationContext(),"핸들러",Toast.LENGTH_LONG).show();
        }
    }

    public void onImageClicked(View view){
        Intent intent = new Intent(getApplicationContext(),AddPicture.class);
        startActivityForResult(intent,RESULT_CODE_MENU);
    }
    public void onCreateClicked(View view){
        Intent intent = new Intent(getApplicationContext(),AddPicture.class);
        startActivityForResult(intent,RESULT_CODE_MENU);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_CODE_MENU){
            if (resultCode == RESULT_OK) {
                String photo = data.getExtras().getString("photo");
                String name = data.getExtras().getString("name");
                if(name != null){
                    plantTitleText.clearComposingText();
                    plantTitleText.setText(name);
                    GetDetail gd = new GetDetail(name);
                    tipText.setText(gd.GetDetailText("watercycleAutumnCode"));
                    properMoist.setText(gd.GetDetailText("hdCode"));
                }
                if (photo != null) {
                    ChangeStrBitmap csb = new ChangeStrBitmap();
                    titlePreView.setVisibility(View.GONE);
                    titleImage.setImageBitmap(csb.StringToBitmap(photo));
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Message msg = getValueHandler.obtainMessage();
                            getValueHandler.sendMessage(msg);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }
    }
}
