package com.revlis1353.rebootspec.rebootspec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler{
    private static final String siteUrlBase = "https://maplestory.nexon.com";
    private static final String siteUrlRankingPrefix = "/Ranking/World/Total?c=";
    private static final String siteUrlPostfix = "&w=254";
    private static final String STATSSELECTER[] = {"STR", "DEX", "INT", "LUK"};
    private static final String ATTSELECTER[] = {"공격력", "마력"};

    //private String characterName;
    private int mainstatSel;   //0:STR, 1:DEX, 2:INT, 3:LUK
    private int substat1Sel;
    private int substat2Sel; 
    private int attmagSel;     //0:ATTACK, 1:MAGIC

    private String siteUrl;
    private String characterUrl;
    private String equipmentUrl;

    //TODO: Expected Bug) If some equipment are empty, ~


    public Crawler(FindCharacterVO character){
        //this.characterName = characterName;
        this.mainstatSel = character.getmainstatSel();
        this.substat1Sel = character.getsubstat1Sel();
        this.substat2Sel = character.getsubstat2Sel();
        this.attmagSel = character.getattmagSel();
        this.siteUrl = siteUrlBase + siteUrlRankingPrefix + character.getcharacterName() + siteUrlPostfix;
        characterUrl = getCharacterUrl(siteUrl);
        equipmentUrl = getCharacterEquipmentUrl(characterUrl);
    }

    public ArrayList<DataItem> getCharacterItemData(){        
        //Get url of each equipments
        Elements equipmentsTags = process(equipmentUrl).getElementsByClass("weapon_wrap").select("ul > li > span > a");

        //Get data from each equipments
        ArrayList<DataItem> equipeditem = new ArrayList<DataItem>();
        Iterator<Element> iter = equipmentsTags.iterator();
        for(int i = 0; i < 25; i++){
            Element element = (Element)iter.next();
            String elementUrl = element.attr("href");

            if(elementUrl.isBlank()) continue;

            //Get Item data from Url
            String itemInfo = jsoupXMLHttpRequest(siteUrlBase + elementUrl, equipmentUrl).body();
            //Convert unicode & escape character to document
            itemInfo = StringEscapeUtils.unescapeJava(itemInfo.substring(18, itemInfo.length()-2));
            Document itemDocument = Jsoup.parse(itemInfo);

            equipeditem.add(getEquipment(itemDocument));
        }

        //TODO: equipeditem.add(getCharacterHyperstat());
        //TODO: Crawl arcane symbol data 

        return equipeditem;
    }
    
    public int getCharacterLevel() {
        Document document = process(siteUrl);
        Elements urlElements = document.getElementsByClass("search_com_chk");
        int characterLevel = Integer.parseInt(urlElements.select("td").get(2).text().substring(3));
        return characterLevel;
    }

    private DataItem getCharacterHyperstat(){
        //TODO: get Hyper Stat
        return null;
    }

    public String getCharacterImgUrl(){
        Document document = process(characterUrl);
        String charImgUrl = document.getElementsByClass("char_img").select("div > img").attr("src");
        return charImgUrl;
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

    private DataItem getEquipment(Document itemDocument){

        DataItem item = new DataItem();

        String itemName = itemDocument.getElementsByClass("item_memo_title").text();
        item.setItemName(itemName);
        item.setItemImg(itemDocument.getElementsByClass("item_img").select("img").attr("src"));
        //TODO: Bug - If not select secondary substat, error occur below line.
        item.setReqLev(Integer.parseInt(itemDocument.getElementsByClass("ablilty01").select("ul > li > em").text().split(" ")[0]));
        
        Elements Stats = itemDocument.getElementsByClass("stet_info").select("ul > li");

        //Find set
        String itemNameSplited = itemName.split(" ")[0];
        if(itemNameSplited.equals("아쿠아틱") || itemNameSplited.equals("블랙빈") || itemNameSplited.equals("파풀라투스") || 
        itemNameSplited.equals("응축된") || itemNameSplited.equals("골든") || itemNameSplited.equals("분노한") || 
        itemNameSplited.equals("혼테일의") || itemNameSplited.equals("카오스") || itemNameSplited.equals("매커네이터") || 
        itemNameSplited.equals("도미네이터") || itemNameSplited.equals("데아") || itemNameSplited.equals("지옥의")
        || itemNameSplited.equals("실버블라썸") || itemNameSplited.equals("고귀한") || itemNameSplited.equals("가디언")
        || itemNameSplited.equals("크리스탈") || itemNameSplited.equals("핑크빛") || itemNameSplited.equals("영생의")){
            item.setSet(1);
        }
        else if(itemNameSplited.equals("로얄")){
            if(itemName.split(" ")[1].equals("블랙메탈")){
                item.setSet(1);
            }
        }
        else if(itemNameSplited.equals("트와일라이트") || itemNameSplited.equals("에스텔라") || itemNameSplited.equals("데이브레이크") ||
        itemNameSplited.equals("여명의")){
            item.setSet(2);
        }
        else if(itemNameSplited.equals("블랙") || itemNameSplited.equals("몽환의") || itemNameSplited.equals("루즈") || 
        itemNameSplited.equals("마력이") || itemNameSplited.equals("저주받은") || itemNameSplited.equals("고통의") || 
        itemNameSplited.equals("창세의") || itemNameSplited.equals("거대한") || itemNameSplited.equals("커맨더") || itemNameSplited.equals("미트라의")){
            item.setSet(3);
        }
        else if(itemNameSplited.equals("하이네스") || itemNameSplited.equals("이글아이") || itemNameSplited.equals("트릭스터") || itemNameSplited.equals("파프니르")){
            item.setSet(4);
        }
        else if(itemNameSplited.equals("앱솔랩스")){
            item.setSet(5);
        }
        else if(itemNameSplited.equals("아케인셰이드")){
            item.setSet(6);
        }

        for(Element stat : Stats){  //Find main Stats
            if(stat.select("div > span").text().equals(STATSSELECTER[mainstatSel])){
                item.setMainstat(Integer.parseInt(stat.getElementsByClass("point_td").text().split(" ")[0]));
                break;
            }
        }
        for(Element stat : Stats){  //Find substat1
            if(stat.select("div > span").text().equals(STATSSELECTER[substat1Sel])){
                item.setSubstat1(Integer.parseInt(stat.getElementsByClass("point_td").text().split(" ")[0]));
                break;
            }
        }
        if(substat2Sel >= 0){  //If subStat2 is exist
            for(Element stat : Stats){  //Find substat2
                if(stat.select("div > span").text().equals(STATSSELECTER[substat2Sel])){
                    item.setSubstat2(Integer.parseInt(stat.getElementsByClass("point_td").text().split(" ")[0]));
                    break;
                }
            }
        }
        for(Element stat : Stats){  //Find Att or Mag
            if(stat.select("div > span").text().equals(ATTSELECTER[attmagSel])){
                item.setAttmag(Integer.parseInt(stat.getElementsByClass("point_td").text().split(" ")[0]));                break;
            }
        }
        for(Element stat : Stats){  //Find AllstatPercent
            if(stat.select("div > span").text().equals("올스탯")){
                item.setAllstatPercent(Integer.parseInt(StringUtils.chop(stat.getElementsByClass("point_td").text().split(" ")[0])));
                break;
            }
        }
        for(Element stat : Stats){  //Find BossDMG
            if(stat.select("div > span").text().equals("보스 몬스터 공격 시 데미지")){
                item.setBossDMG(Integer.parseInt(StringUtils.chop(stat.getElementsByClass("point_td").text().split(" ")[0])));
                break;
            }
        }
        for(Element stat : Stats){  //Find Penetrate
            if(stat.select("div > span").text().equals("몬스터 방어력 무시")){
                item.setPenetrate(Integer.parseInt(StringUtils.chop(stat.getElementsByClass("point_td").text().split(" ")[0])));
                break;
            }
        }

        for(Element stat : Stats){  //Find Potential
            if(stat.select("div > span").text().split(" ")[0].equals("잠재옵션")){
                String potential[] = stat.getElementsByClass("point_td").text().split(" ");
                for(int i = 0; i < potential.length; i = i+3){
                    if(potential[i].equals(STATSSELECTER[mainstatSel])){
                        if(potential[i+2].endsWith("%")) item.setMainstatPercent(item.getMainstatPercent() + Integer.parseInt(StringUtils.chop(potential[i+2])));
                        else item.setMainstat(item.getMainstat() + Integer.parseInt(potential[i+2]));
                        continue;
                    }
                    else if(potential[i].equals(STATSSELECTER[substat1Sel])){
                        if(potential[i+2].endsWith("%")) item.setSubstat1Percent(item.getSubstat1Percent() + Integer.parseInt(StringUtils.chop(potential[i+2])));
                        else item.setSubstat1(item.getSubstat1() + Integer.parseInt(potential[i+2]));
                        continue;
                    }
                    else if(substat2Sel >= 0 && potential[i].equals(STATSSELECTER[substat2Sel])){
                        if(potential[i+2].endsWith("%")) item.setSubstat2Percent(item.getSubstat2Percent() + Integer.parseInt(StringUtils.chop(potential[i+2])));
                        else item.setSubstat2(item.getSubstat2() + Integer.parseInt(potential[i+2]));
                        continue;
                    }
                    else if(potential[i].equals(ATTSELECTER[attmagSel])){
                        if(potential[i+2].endsWith("%")) item.setAttmagPercent(item.getAttmagPercent() + Integer.parseInt(StringUtils.chop(potential[i+2])));
                        else item.setAttmag(item.getAttmag() + Integer.parseInt(potential[i+2]));
                        continue;
                    }
                    else if(potential[i].equals("올스탯")){
                        if(potential[i+2].endsWith("%")) item.setAllstatPercent(item.getAllstatPercent() + Integer.parseInt(StringUtils.chop(potential[i+2])));
                        else{
                            item.setMainstat(item.getMainstat() + Integer.parseInt(potential[i+2]));
                            item.setSubstat1(item.getSubstat1() + Integer.parseInt(potential[i+2]));
                            item.setSubstat2(item.getSubstat2() + Integer.parseInt(potential[i+2]));
                        }
                        continue;
                    }
                    else if(potential[i].equals("크리티컬") && potential[i+1].equals("데미지")){
                        item.setCritDMG(item.getCritDMG() + Integer.parseInt(StringUtils.chop(potential[i+3])));
                        i = i+1;
                        continue;
                    }
                    else if(potential[i].equals("보스") && potential[i+1].equals("몬스터")){
                        item.setBossDMG(item.getBossDMG() + Integer.parseInt(StringUtils.chop(potential[i+6])));
                        i = i+4;
                        continue;
                    }
                    else if(potential[i].equals("몬스터") && potential[i+1].equals("방어율")){
                        item.setPenetrate((10000 - (100 - item.getPenetrate()) * (100 - Integer.parseInt(StringUtils.chop(potential[i+4])))) / 100.0f);
                        i = i+2;
                        continue;
                    }
                    else{
                        i = i-2;
                    }
                }
                break;
            }
        }
        return item;
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