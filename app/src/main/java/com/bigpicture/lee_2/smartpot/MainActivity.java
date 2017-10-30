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

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    public static final int RESULT_CODE_MENU = 100;
    private TextView plantTitleText, tipText, properMoist, moistView,cdsView,temperatureView;
    private ImageView titleImage;
    private TimerTask timerTask;
    private Timer timer;
    static boolean running = false;
    private float moist, cds, temperature =0f;
    String moistStr, cdsStr, temperatureStr;
    ImageButton titlePreView;
    GetValueHandler getValueHandler;
    DrawView drawView;
    DrawView2 drawView2;
    DrawView3 drawView3;
    GetParsingData gpd ;
    String valueUrl = "http://110.46.204.146:8080/arduinowebserver/sendingsmartphonedata";
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
        getValueHandler = new GetValueHandler();
        gpd = new GetParsingData(valueUrl);
        moistView = (TextView)findViewById(R.id.moist_value);
        cdsView = (TextView)findViewById(R.id.cds_value);
        temperatureView = (TextView)findViewById(R.id.temperature_value);

    }
    public void setValue(float moist,float cds, float temperature){this.moist =moist; this.cds = cds; this.temperature = temperature;}
    @Override
    protected void onResume() {
        super.onResume();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                try{
                    if(running) {
                        gpd.start();
                        try{
                            gpd.join();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        XmlParser xp = new XmlParser(gpd.GetResult());
                        setValue(Float.parseFloat(xp.DoXmlParser("water")),Float.parseFloat(xp.DoXmlParser("light")),Float.parseFloat(xp.DoXmlParser("temperature")));
                        drawView.setPoint(moist*2.7f);
                        drawView2.setPoint2(cds*2.7f);
                        drawView3.setPoint3(temperature*2.7f);
                         getValueHandler.sendEmptyMessage(1);
                        Log.d("습도",Float.parseFloat(xp.DoXmlParser("water"))+"");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    public class GetValueHandler extends Handler{
//        private final WeakReference<MainActivity> mActivity;
//        public GetValueHandler(MainActivity activity) { mActivity = new WeakReference<MainActivity>(activity); }
        public void handleMessage(Message msg){
                try {
                    moistStr = String.format("%.1f",moist);
                    cdsStr = String.format("%.1f",cds);
                    temperatureStr = String.format("%.1f",temperature);

                    moistView.setText(moistStr+"%");
                    cdsView.setText(cdsStr+"%");
                    temperatureView.setText(temperatureStr+"%");
                    moistView.invalidate();
                    cdsView.invalidate();
                    temperatureView.invalidate();
                    drawView.invalidate();
                    drawView2.invalidate();
                    drawView3.invalidate();
                }catch (Exception e){
                    e.printStackTrace();
                }

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
                running = true;
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                thread.start();
            }
        }
    }
}
