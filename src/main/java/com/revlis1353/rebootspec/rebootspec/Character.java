package com.revlis1353.rebootspec.rebootspec;

import java.util.ArrayList;

public class Character {
    
    public enum itemEnum {Ring1, Cap, Emblem, Ring2, Pendant1, 
        Forehead, Badge, Ring3, Pendant2, Eyeacc, Earacc, Medal,
        Ring4, Weapon, Clothes, Shoulder, Subweapon, Poket, Belt, Pants, Gloves, Cape,
        Shoes, Android, Heart
    };

    private ArrayList<DataItem> equipeditem;
    private int mainstat, substat1, substat2;
    private int mainstatPercent, substat1Percent, substat2Percent;
    private int allstatPercent;
    private int attmag, attmagPercent;
    private int critDMG, bossDMG;
    private float penetrate;

    public Character(){
        equipeditem = new ArrayList<DataItem>();
    }

    public Character(int level, ArrayList<DataItem> equipeditem){
        this.equipeditem = equipeditem;
        this.mainstat = level*5 + 10;
        this.substat1 = 4;
        this.substat2 = 4;
    }
    
    public void calculateSpec(){
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
        }
    }

    public void print(){
        System.out.println(mainstat + " " + substat1 + " " + substat2 + " ");
        System.out.println(mainstatPercent + " " + substat1Percent + " " + substat2Percent + " " + allstatPercent);
        System.out.println(attmag + " " + attmagPercent);
        System.out.println(critDMG + " " + bossDMG + " " + penetrate);
    }
}
