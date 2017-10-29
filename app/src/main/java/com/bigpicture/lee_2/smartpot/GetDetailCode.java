package com.bigpicture.lee_2.smartpot;

/**
 * Created by lee-2 on 2017-10-11.
 */

public class GetDetailCode {
    private String plantName,contentsNum;

    public GetDetailCode(String plantName){
        this.plantName = plantName;
    }

    public String GetCode(String codeName){
        String getCode ="error";
        GetParsingData gpd = new GetParsingData("http://api.nongsaro.go.kr/service/garden/gardenList?apiKey=20171010HECT2JBCAWKELEKZWLHA&sType=sCntntsSj&sText="+plantName);
        gpd.start();

        try{
            gpd.join();
            XmlParser xp = new XmlParser(gpd.GetResult());
            contentsNum = xp.DoXmlParser("cntntsNo");
        }catch (Exception e){
            e.printStackTrace();
        }

        GetParsingData detailGpd = new GetParsingData("http://api.nongsaro.go.kr/service/garden/gardenDtl?apiKey=20171010HECT2JBCAWKELEKZWLHA&cntntsNo="+contentsNum);
        detailGpd.start();

        try{
            detailGpd.join();
            XmlParser xp = new XmlParser(detailGpd.GetResult());
            getCode = xp.DoXmlParser(codeName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return getCode;
    }

}
