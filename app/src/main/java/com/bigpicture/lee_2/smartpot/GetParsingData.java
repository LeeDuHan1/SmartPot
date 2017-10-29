package com.bigpicture.lee_2.smartpot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lee-2 on 2017-10-11.
 */

public class  GetParsingData extends Thread{
    private String pUrl;
    private String result;

    public GetParsingData(String pUrl) {
        this.pUrl = pUrl;
    }

    public String GetResult(){
        return result;
    }
    public void run(){

                BufferedReader bufreader = null;
                HttpURLConnection urlConnection = null;

                StringBuffer page = new StringBuffer(); //읽어온 데이터를 저장할 StringBuffer객체 생성

                try {
                    URL url = new URL(pUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream contentStream = urlConnection.getInputStream();
                    Log.d("대상URL", pUrl);
                    bufreader = new BufferedReader(new InputStreamReader(contentStream, "UTF-8"));
                    String line = null;

                    //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
                    while ((line = bufreader.readLine()) != null) {
                        Log.d("line:", line);
                        page.append(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                finally{
//            //자원해제
//            try {
//                bufreader.close();
//                urlConnection.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
                Log.d("페이지", page.toString());
                result = page.toString();
            }
}
