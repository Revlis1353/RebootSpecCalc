package com.revlis1353.rebootspec.rebootspec;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

public class Crawler{
    private String siteUrl = "asdf";

    public static String getCharacterItemData(String CharacterName){
        String data = "null";
        String characterurl = getCharacterUrl(CharacterName);
        String equipmenturl = getCharacterEquipmentUrl(characterurl);
        
        // equipmenturl에서 아이템 데이터를 크롤링, 분류하여 list?dict?에 담아 return

        return data;
    }
    
    public static String getCharacterUrl(String CharacterName){
        return "Null";
    }

    public static String getCharacterEquipmentUrl(String characterurl){
        return "Null";
    }    
}