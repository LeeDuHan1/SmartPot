package com.bigpicture.lee_2.smartpot;
import java.io.IOException;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by lee-2 on 2017-10-10.
 */

public class XmlParser {
    private String xmlString;
    private String result,tagName;

    public XmlParser(String xmlString){
        this.xmlString = xmlString;
    }
    public String GetString(){
        return this.xmlString;
    }

    public String DoXmlParser(String tag)throws XmlPullParserException, IOException {
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xmlString));
//          parser.setInput(인풋스트림,"euc-kr");  //인풋스트림의 인코딩이 필요한 경우쓰자
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = parser.getName();
                } else if (eventType == XmlPullParser.TEXT) {
                    if(tagName.equals(tag))
                       result = parser.getText();
                } else if(eventType == XmlPullParser.END_TAG){
                    tagName = parser.getName();
                }
                eventType = parser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
