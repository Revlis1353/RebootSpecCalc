package com.revlis1353.rebootspec.rebootspec;

public class DataItem{
    private String itemName, itemImg;
    private int reqLev;
    private int mainstat, substat1, substat2;
    private int mainstatPercent, substat1Percent, substat2Percent;
    private int allstatPercent;
    private int attmag, attmagPercent;
    private int critDMG, bossDMG, penetrate;

    public DataItem(){
    }

    public String getItemName() {
        return itemName;
    }
    public String getItemImg() {
        return itemImg;
    }
    public int getReqLev() {
        return reqLev;
    }
    public int getMainstat() {
        return mainstat;
    }
    public int getSubstat1() {
        return substat1;
    }
    public int getSubstat2() {
        return substat2;
    }
    public int getMainstatPercent() {
        return mainstatPercent;
    }
    public int getSubstat1Percent() {
        return substat1Percent;
    }
    public int getSubstat2Percent() {
        return substat2Percent;
    }
    public int getAllstatPercent() {
        return allstatPercent;
    }
    public int getAttmag() {
        return attmag;
    }
    public int getAttmagPercent() {
        return attmagPercent;
    }
    public int getCritDMG() {
        return critDMG;
    }
    public int getBossDMG() {
        return bossDMG;
    }
    public int getPenetrate() {
        return penetrate;
    }

    public void setAllstatPercent(int allstatPercent) {
        this.allstatPercent = allstatPercent;
    }
    public void setAttmag(int attmag) {
        this.attmag = attmag;
    }
    public void setAttmagPercent(int attmagPercent) {
        this.attmagPercent = attmagPercent;
    }
    public void setBossDMG(int bossDMG) {
        this.bossDMG = bossDMG;
    }
    public void setCritDMG(int critDMG) {
        this.critDMG = critDMG;
    }
    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setMainstat(int mainstat) {
        this.mainstat = mainstat;
    }
    public void setMainstatPercent(int mainstatPercent) {
        this.mainstatPercent = mainstatPercent;
    }
    public void setPenetrate(int penetrate) {
        this.penetrate = penetrate;
    }
    public void setReqLev(int reqLev) {
        this.reqLev = reqLev;
    }
    public void setSubstat1(int substat1) {
        this.substat1 = substat1;
    }
    public void setSubstat1Percent(int substat1Percent) {
        this.substat1Percent = substat1Percent;
    }
    public void setSubstat2(int substat2) {
        this.substat2 = substat2;
    }
    public void setSubstat2Percent(int substat2Percent) {
        this.substat2Percent = substat2Percent;
    }
}