package com.revlis1353.rebootspec.rebootspec;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

public class Crawler{
    private static final String siteUrlBase = "https://maplestory.nexon.com";
    private static final String siteUrlRankingPrefix = "/Ranking/World/Total?c=";
    private static final String siteUrlPostfix = "&w=254";

    private String characterName;

    public Crawler(String characterName){
        this.characterName = characterName;
    }

    public String getCharacterItemData(){
        String siteUrl = siteUrlBase + siteUrlRankingPrefix + characterName + siteUrlPostfix;
        String characterUrl = getCharacterUrl(siteUrl);
        String equipmentUrl = getCharacterEquipmentUrl(characterUrl);
        System.out.println("#######characterUrl = " + characterUrl);
        System.out.println("#######equipmentUrl = " + equipmentUrl);
        
        String testUrl = process(equipmentUrl).getElementsByClass("item_pot").select("li > span > a").attr("href");
        System.out.println("#######testUrl = " + siteUrlBase + testUrl);
        //String testUrl = "/Common/Resource/Item?p=ESiGOaxye8ZazwJEjUhfLAYcZDqAikORwKq87%2b0QbMVC75O8X%2b6oMHRoMKsUvqlefD3TjOcfotZtUHNwIhc4Gh2TrmxV36BWXQpwuzBx61QuNZI2z4KFuRmRS23XQAJ0WkR2fzBiV39Ku9sqy9OFJAGQW47NPIi40By%2bNYuxLg3ZEfIvRneJDjh4BkM6gbbRaNHdnA4hshz3%2bOh8hc%2frPA8VwvOe979g3KNtDV%2bCa2yhwwqbyMYfMu8KpEm7gsy780nVEYw35%2b7B7k6os5ol%2b9lSX6bEc4crlHJjEOMarhWK%2bev9phWI%2bt%2bPJbzA6R%2bup9DyiwBecYQw%2bivDsWvfsQ%3d%3d&_=1657161394637";
        System.out.println(jsoupXMLHttpRequest(siteUrlBase + testUrl, equipmentUrl).body());
        // equipmenturl에서 아이템 데이터를 크롤링, 분류하여 MAP에 담아 return
        return "";
    }
    
    private String getCharacterUrl(String siteUrl){
        Document document = process(siteUrl);
        Elements urlElements = document.getElementsByClass("search_com_chk");
        String characterUrl = urlElements.select(".left > dl > dt > a").attr("href");
        return siteUrlBase + characterUrl;
    }

    private String getCharacterEquipmentUrl(String characterurl){
        Document document = process(characterurl);
        Elements urlElements = document.getElementsByClass("lnb_list").select("li");
        for(Element element : urlElements){
            if(element.text().equalsIgnoreCase("장비") == true){
                return siteUrlBase + element.select("a").attr("href");
            }
        }
        return "Null";
    }

    private String getEquipmentData(){
        return "0";
    }

    private Document process(String url) {
        Connection conn = Jsoup.connect(url);

        Document document = null;
        try {
            document = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private Connection.Response jsoupXMLHttpRequest(String url, String referer){
        
        Connection.Response response = null;
        try{
            response = Jsoup.connect(url)
            .header("Host","maplestory.nexon.com")
            .header("Sec-Ch-Ua", "\"Chromium\";v=\"103\", \".Not/A)Brand\";v=\"99\"")
            .header("Accept","*/*")
            .header("X-Requested-With", "XMLHttpRequest")
            .header("Sec-Ch-Ua-Mobile", "?0")
            .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.53 Safari/537.36")
            .header("Sec-Ch-Ua-Platform", "\"Windows\"")
            .header("Sec-Fetch-Site", "same-origin")
            .header("Sec-Fetch-Mode", "cors")
            .header("Sec-Fetch-Dest", "empty")
            .header("Referer", referer)
            .header("Accept-Encoding","gzip, deflate")
            .header("Accept-Language","ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
            .header("Connection","close")
            .ignoreContentType(true)
            .method(Connection.Method.GET)
            .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}