package com.bigpicture.lee_2.smartpot;

/**
 * Created by lee-2 on 2017-10-11.
 */

public class GetDetail{
    private String plantName,code;

    public GetDetail(String plantName){
        this.plantName = plantName;
    }

    public String GetDetailText(String codeName){
        GetDetailCode gdc = new GetDetailCode(plantName);
        this.code = gdc.GetCode(codeName);
        String result = "error";

        if(code.equals("082001")){
            result = "10~15℃";
        }else if(code.equals("082002")){
            result = "16~20℃";
        }else if(code.equals("082003")){
            result = "21~25℃";
        }else if(code.equals("082004")){
            result = " 26~30℃";
        }else if(code.equals("083001")){
            result = "40% 미만";
        }else if(code.equals("083002")){
            result = "40 ~ 70%";
        }else if(code.equals("083004")){
            result = "70% 이상";
        }else if(code.equals("053001")){
            result = "항상 흙을 축축하게 유지함(물에 잠김)";
        }else if(code.equals("053002")){
            result = "흙을 촉촉하게 유지함(물에 잠기지 않도록 주의)";
        }else if(code.equals("053003")){
            result = "토양 표면이 말랐을때 충분히 관수함";
        }else if(code.equals("053004")){
            result = "화분 흙 대부분 말랐을때 충분히 관수함";
        }

        return result;
    }
    public int GetDetailValue(String codeName){
        GetDetailCode gdc = new GetDetailCode(plantName);
        this.code = gdc.GetCode(codeName);
        int result = 0;

        if(code.equals("082001")){
            result = 13;
        }else if(code.equals("082002")){
            result = 18;
        }else if(code.equals("082003")){
            result = 23;
        }else if(code.equals("082004")){
            result = 28;
        }else if(code.equals("083001")){
            result = 40;
        }else if(code.equals("083002")){
            result = 55;
        }else if(code.equals("083004")){
            result = 70;
        }

        return result;
    }
}
