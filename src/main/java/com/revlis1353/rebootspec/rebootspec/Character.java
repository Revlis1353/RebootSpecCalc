package com.revlis1353.rebootspec.rebootspec;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component("Character")
public class Character {
    
    public enum itemEnum {Ring1, Cap, Emblem, Ring2, Pendant1, 
        Forehead, Badge, Ring3, Pendant2, Eyeacc, Earacc, Medal,
        Ring4, Weapon, Clothes, Shoulder, Subweapon, Poket, Belt, Pants, Gloves, Cape,
        Shoes, Android, Heart
    };

    @Getter @Setter
    private ArrayList<DataItem> equipeditem;
    private String characterName;
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

    public Character(){
        equipeditem = new ArrayList<DataItem>();
    }

    public Character(String characterName, int level, ArrayList<DataItem> equipeditem){
        this.characterName = characterName;
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

    public String getCharacterName() {
        return this.characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getmainstat() {
        return this.mainstat;
    }

    public void setMainstat(int mainstat) {
        this.mainstat = mainstat;
    }

    public int getSubstat1() {
        return this.substat1;
    }

    public void setSubstat1(int substat1) {
        this.substat1 = substat1;
    }

    public int getSubstat2() {
        return this.substat2;
    }

    public void setSubstat2(int substat2) {
        this.substat2 = substat2;
    }

    public int getMainstatPercent() {
        return this.mainstatPercent;
    }

    public void setMainstatPercent(int mainstatPercent) {
        this.mainstatPercent = mainstatPercent;
    }

    public int getSubstat1Percent() {
        return this.substat1Percent;
    }

    public void setSubstat1Percent(int substat1Percent) {
        this.substat1Percent = substat1Percent;
    }

    public int getSubstat2Percent() {
        return this.substat2Percent;
    }

    public void setSubstat2Percent(int substat2Percent) {
        this.substat2Percent = substat2Percent;
    }

    public int getAllstatPercent() {
        return this.allstatPercent;
    }

    public void setAllstatPercent(int allstatPercent) {
        this.allstatPercent = allstatPercent;
    }

    public int getAttmag() {
        return this.attmag;
    }

    public void setAttmag(int attmag) {
        this.attmag = attmag;
    }

    public int getAttmagPercent() {
        return this.attmagPercent;
    }

    public void setAttmagPercent(int attmagPercent) {
        this.attmagPercent = attmagPercent;
    }

    public int getCritDMG() {
        return this.critDMG;
    }

    public void setCritDMG(int critDMG) {
        this.critDMG = critDMG;
    }

    public int getBossDMG() {
        return this.bossDMG;
    }

    public void setBossDMG(int bossDMG) {
        this.bossDMG = bossDMG;
    }

    public float getPenetrate() {
        return this.penetrate;
    }

    public void setPenetrate(float penetrate) {
        this.penetrate = penetrate;
    }
}
