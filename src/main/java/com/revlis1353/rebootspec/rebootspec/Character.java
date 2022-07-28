package com.revlis1353.rebootspec.rebootspec;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Component("Character")
public class Character {
    
    public enum itemEnum {Ring1, Cap, Emblem, Ring2, Pendant1, 
        Forehead, Badge, Ring3, Pendant2, Eyeacc, Earacc, Medal,
        Ring4, Weapon, Clothes, Shoulder, Subweapon, Poket, Belt, Pants, Gloves, Cape,
        Shoes, Android, Heart
    };

    private ArrayList<DataItem> equipeditem;
    private String characterName;
    private String characterImgUrl;

    private int basemainstat;
    private int basesubstat1;
    private int basesubstat2;

    private int mainstat;
    private int substat1;
    private int substat2;
    private int mainstatPercent;
    private int substat1Percent;
    private int substat2Percent;
    private int allstatPercent;
    private int attmag;
    private int attmagPercent;
    private int critDMG;
    private int bossDMG;
    private float penetrate;

    private int mainstatSel;
    private int substat1Sel;
    private int substat2Sel;
    private int attmagSel;

    public Character(){
        equipeditem = new ArrayList<DataItem>();
    }

    public Character(FindCharacterVO charVO, int level, ArrayList<DataItem> equipeditem){
        this.characterName = charVO.getcharacterName();
        this.mainstatSel = charVO.getmainstatSel();
        this.substat1Sel = charVO.getsubstat1Sel();
        this.substat2Sel = charVO.getsubstat2Sel();
        this.attmagSel = charVO.getattmagSel();
        this.equipeditem = equipeditem;
        this.basemainstat = level*5 + 10;
        this.basesubstat1 = 4;
        this.basesubstat2 = 4;
    }

    public void characterSetter(FindCharacterVO charVO, int level, ArrayList<DataItem> equipeditem){
        this.characterName = charVO.getcharacterName();
        this.mainstatSel = charVO.getmainstatSel();
        this.substat1Sel = charVO.getsubstat1Sel();
        this.substat2Sel = charVO.getsubstat2Sel();
        this.attmagSel = charVO.getattmagSel();
        this.equipeditem = equipeditem;
        this.basemainstat = level*5 + 10;
        this.basesubstat1 = 4;
        this.basesubstat2 = 4;
    }
    
    public void calculateSpec(){
        mainstat = 0;
        substat1 = 0;
        substat2 = 0;
        mainstatPercent = 0;
        substat2Percent = 0;
        allstatPercent = 0;
        attmag = 0;
        attmagPercent = 0;
        critDMG = 0;
        bossDMG = 0;
        penetrate = 0;
        for(DataItem item : equipeditem){
            //System.out.println(item.getItemName() + " : " + item.getMainstatPercent());
            mainstat += item.getMainstat();
            substat1 += item.getSubstat1();
            substat2 += item.getSubstat2();
            mainstatPercent += item.getMainstatPercent();
            substat2Percent += item.getSubstat2Percent();
            allstatPercent += item.getAllstatPercent();
            attmag += item.getAttmag();
            attmagPercent += item.getAttmagPercent();
            critDMG += item.getCritDMG();
            bossDMG += item.getBossDMG();
            penetrate = (10000 - (100 - penetrate) * (100 - item.getPenetrate())) / 100.0f;
            //Add set Map and calculate number of each set
        }
        mainstat += basemainstat;
        substat1 += basesubstat1;
        substat2 += basesubstat2;
        //Add spec of set
    }

    public void print(){
        System.out.println(mainstat + " " + substat1 + " " + substat2 + " ");
        System.out.println(mainstatPercent + " " + substat1Percent + " " + substat2Percent + " " + allstatPercent);
        System.out.println(attmag + " " + attmagPercent);
        System.out.println(critDMG + " " + bossDMG + " " + penetrate);
    }

    public void modifyItem(DataItem data){
        equipeditem.set(data.getModifyIndex(), data);
        calculateSpec();
    }
}
