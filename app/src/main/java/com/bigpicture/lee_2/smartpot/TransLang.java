package com.bigpicture.lee_2.smartpot;

/**
 * Created by lee-2 on 2017-10-29.
 */

public class TransLang {
    private String engName;

    public TransLang(String string) {
        this.engName = string;
    }

    public String GetKorName() {
        String name = "";

        if (engName.equals("guzmania")) {
            name = "구즈마니아";
        } else if (engName.equals("neriumoleander")) {
            name = "협죽도";
        } else if (engName.equals("virburnum")) {
            name = "아왜나무";
        } else if (engName.equals("rohdeajaponica")) {
            name = "만년청";
        } else if (engName.equals("syngonium")) {
            name = "싱고니움";
        } else if (engName.equals("ficuselastica")) {
            name = "소피아고무나무";
        } else if (engName.equals("saxifragastolonifera")) {
            name = "바위취";
        } else if(engName.equals("rohdeajaponica")){
            name = "더피고사리";
        } else {
            name = "결과 없음";
        }
        return name;
    }
}