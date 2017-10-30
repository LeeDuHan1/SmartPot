package com.bigpicture.lee_2.smartpot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout menuLayout;
    Button menuBtn;
    Boolean isMenuOpen = false;
    Boolean watering =false;
    Boolean lightning = false;
    Animation moveLeftAnim, moveRightAnim;
    String moistStr, cdsStr, temperatureStr;
    ImageButton titlePreView, waterBtn, lightBtn;
    GetValueHandler getValueHandler;
    DrawView drawView;
    DrawView2 drawView2;
    DrawView3 drawView3;
    GetParsingData gpd ;
    String baseUrl = "http://110.46.204.146:8080";
    String valueUrl = baseUrl+"/arduinowebserver/sendingsmartphonedata";
    String modeUrl = baseUrl+"/arduinowebserver/settingmode";
    String setUrl = baseUrl+"/arduinowebserver/settingsmartphone";
    String autoUrl = baseUrl+"/arduinowebserver/setautodata";
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
        menuBtn = (Button) findViewById(R.id.menuBtn);
        menuLayout =(LinearLayout)findViewById(R.id.menuLayout);
        waterBtn =(ImageButton)findViewById(R.id.waterBtn);
        lightBtn=(ImageButton)findViewById(R.id.lightBtn);;
        moveLeftAnim = AnimationUtils.loadAnimation(this,R.anim.move_left);
        moveRightAnim = AnimationUtils.loadAnimation(this,R.anim.move_right);
        SlidingPageAniListener aniListener = new SlidingPageAniListener();
        moveLeftAnim.setAnimationListener(aniListener);
//        moveRightAnim.setAnimationListener(aniListener);
    }
    public void onWaterClicked(View view){
        if(watering){
            GetParsingData gpd = new GetParsingData(setUrl+"?water=Off");
            gpd.start();
            waterBtn.setImageResource(R.drawable.water_off);
            watering = false;
        }else{
            GetParsingData gpd = new GetParsingData(setUrl+"?water=On");
            gpd.start();
            waterBtn.setImageResource(R.drawable.water_on);
            watering = true;
        }
    }
    public void onLightClicked(View view){
        if(lightning){
            GetParsingData gpd = new GetParsingData(setUrl+"?light=Off");
            gpd.start();
            lightBtn.setImageResource(R.drawable.light_off);
            lightning = false;
        }else {
            GetParsingData gpd = new GetParsingData(setUrl+"?light=On");
            gpd.start();
            lightBtn.setImageResource(R.drawable.light_on);
            lightning = true;
        }
    }
    public void onMenuClicked(View view){
        DialogInterface.OnClickListener modeListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GetParsingData gpd = new GetParsingData(modeUrl+"?mode=0");
                gpd.start();
                isMenuOpen = true;
                menuBtn.setVisibility(View.GONE);
                menuLayout.setVisibility(View.VISIBLE);
                menuLayout.startAnimation(moveRightAnim);
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(this)
                .setTitle("수동모드로 전환하시겠습니까?.")
                .setPositiveButton("확인",modeListener)
                .setNegativeButton("취소",cancelListener)
                .show();
    }
    public void onCloseClicked(View view){
        DialogInterface.OnClickListener modeListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GetParsingData gpd = new GetParsingData(modeUrl+"?mode=1");
                gpd.start();
                isMenuOpen = false;
                menuLayout.startAnimation(moveLeftAnim);
                menuLayout.setVisibility(View.GONE);
                menuBtn.setVisibility(View.VISIBLE);
                menuLayout.startAnimation(moveLeftAnim);
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(this)
                .setTitle("자동모드로 전환하시겠습니까?.")
                .setPositiveButton("확인",modeListener)
                .setNegativeButton("취소",cancelListener)
                .show();

    }
    private class SlidingPageAniListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
        @Override
        public void onAnimationEnd(Animation animation) {
               menuBtn.setVisibility(View.VISIBLE);
        }
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
                        GetParsingData gpd = new GetParsingData(valueUrl);
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
                    Toast.makeText(getApplicationContext(),"Value 변경",Toast.LENGTH_LONG).show();
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
        int properWater, properTemperature = 0;
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

                    properWater = gd.GetDetailValue("hdCode");
                    properTemperature = gd.GetDetailValue("grwhTpCode");
                    GetParsingData gpd = new GetParsingData(autoUrl+"?averageWater="+properWater+"&averageTemperature="+properTemperature);
                    gpd.start();
                }
                if (photo != null) {
                    ChangeStrBitmap csb = new ChangeStrBitmap();
                    titlePreView.setVisibility(View.GONE);
                    titleImage.setImageBitmap(csb.StringToBitmap(photo));
                }
                running = true;
            }
        }
    }
}
