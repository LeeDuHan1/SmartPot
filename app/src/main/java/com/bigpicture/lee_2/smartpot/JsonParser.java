package com.bigpicture.lee_2.smartpot;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by lee-2 on 2017-10-10.
 */

public class JsonParser {
    private String jsonString, key;
    private String value;

    public JsonParser(String str){
        jsonString = str;
    }

    public String DoJsonPasing(String keyValue)throws IOException{
        this.key = keyValue;

        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            value = jsonObject.get(key).toString();
//            Log.d("제이슨",jsonString);
//            JSONArray jarray = new JSONArray(jsonString);
////            for(int i = 0; i < jarray.length(); i++){
//                JSONObject jObject = jarray.getJSONObject(0);
//                value = jObject.getString(key);
//            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return value;
    }
}
