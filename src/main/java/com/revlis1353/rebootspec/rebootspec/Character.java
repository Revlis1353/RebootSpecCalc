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

    private String STATSSELECTER[] = {"STR", "DEX", "INT", "LUK"};
    private String ATTSELECTER[] = {"공격력", "마력"};

    private ArrayList<DataItem> equipeditem;
    private HyperstatVO hyperstat;
    private DataItem union;
    private DataItem classSpec;

    private String characterName;
    private String characterImgUrl;

    private int basemainstat;
    private int basesubstat1;
    private int basesubstat2;
    private int baseFixedMainstat;

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
    private int dmg;
    private float penetrate;

    private int fixedMainstat;

    private int mainstatSel;
    private int substat1Sel;
    private int substat2Sel;
    private int attmagSel;

    private int totalmainstat;
    private int totalsubstat1;
    private int totalsubstat2;
    private int totalattmag;

    private int mapleSoldier;
    private int enemyDefense;

    /* 0: None, 1: Boss, 2: Dawn, 3: Pitched, 4: Rootabyss, 5: Absol, 6: Arcane, 77: Lucky Item */
    private int set[];

    public Character(){
        equipeditem = new ArrayList<DataItem>();
        hyperstat = new HyperstatVO();
        union = new DataItem();
        classSpec = new DataItem();
        enemyDefense = 300;
    }

    @SuppressWarnings("unchecked")
    public Character(Character player){
        equipeditem = (ArrayList<DataItem>)player.getEquipeditem().clone();

        this.characterName = player.getCharacterName();
        this.characterImgUrl = player.getCharacterImgUrl();
    
        this.basemainstat = player.getBasemainstat();
        this.basesubstat1 = player.getBasesubstat1();
        this.basesubstat2 = player.getBasesubstat2();
        this.baseFixedMainstat = player.getBaseFixedMainstat();
    
        this.mainstat = player.getMainstat();
        this.substat1 = player.getSubstat1();
        this.substat2 = player.getSubstat2();
        this.mainstatPercent = player.getMainstatPercent();
        this.substat1Percent = player.getSubstat1Percent();
        this.substat2Percent = player.getSubstat2Percent();
        this.allstatPercent = player.getAllstatPercent();
        this.attmag = player.getAttmag();
        this.attmagPercent = player.getAttmagPercent();
        this.critDMG = player.getCritDMG();
        this.bossDMG = player.getBossDMG();
        this.dmg = player.getDmg();
        this.penetrate = player.getPenetrate();

        this.fixedMainstat = player.getFixedMainstat();
    
        this.mainstatSel = player.getMainstatSel();
        this.substat1Sel = player.getSubstat1Sel();
        this.substat2Sel = player.getSubstat2Sel();
        this.attmagSel = player.getAttmagSel();
    
        this.totalmainstat = player.getTotalmainstat();
        this.totalsubstat1 = player.getTotalsubstat1();
        this.totalsubstat2 = player.getTotalsubstat2();
        this.totalattmag = player.getTotalattmag();

        this.hyperstat = player.getHyperstat();
        this.union = player.getUnion();
        this.classSpec = player.getClassSpec();
        this.set = player.getSet().clone();

        this.mapleSoldier = player.getMapleSoldier();
        this.enemyDefense = player.getEnemyDefense();
    }

    public Character(FindCharacterVO charVO, int level, ArrayList<DataItem> equipeditem){
        this();
        this.characterName = charVO.getcharacterName();
        this.mainstatSel = charVO.getmainstatSel();
        this.substat1Sel = charVO.getsubstat1Sel();
        this.substat2Sel = charVO.getsubstat2Sel();
        this.attmagSel = charVO.getattmagSel();
        this.equipeditem = equipeditem;
        this.basemainstat = level*5 + 18;
        this.basesubstat1 = 4;
        this.basesubstat2 = 4;
    }

    public void clearEquipmentsModifyLog(){
        for(DataItem target : equipeditem){
            target.setIsModified(0);
        }
    }

    public void printset(){
        for(int i : set){
            System.out.print(i + " ");
        }
        System.out.println();
    }
    
    public void calculateSpec(){
        //Initialize variables
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
        dmg = 0;
        fixedMainstat = 0;
        boolean isLuckyItemApplied = false; //Set to true if the lucky item is applied to the set option
        set = new int[7];

        //Apply item spec
        for(DataItem item : equipeditem){
            //System.out.println(item.getItemName() + ": " + item.getAttmagPercent());
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
            dmg += item.getDmg();
            addPenetrate(item.getPenetrate());
            if(item.getSet() == 77){   
                isLuckyItemApplied = true;
            }
            else{
                set[item.getSet()] += 1;
            }
        }
        mainstat += basemainstat;
        substat1 += basesubstat1;
        substat2 += basesubstat2;
        fixedMainstat += baseFixedMainstat;

        if(mapleSoldier == 1){
            mainstat += 15 * (basemainstat - 18) / 100;
        }

        applyClassSpec();

        //Apply union & additional specs
        applyUnion();

        //Apply hyperstat
        applyHyperstat();

        //Apply Lucky item's effect
        if(isLuckyItemApplied){
            for(int i = 0; i < 7; i++){
                if(set[i] != 0) set[i] += 1;
            }
        }
        //Apply set options
        applySetOption();

        //Apply percent options
        totalmainstat = fixedMainstat + mainstat * (100 + mainstatPercent + allstatPercent) / 100;
        totalsubstat1 = substat1 * (100 + substat1Percent + allstatPercent) / 100;
        if(substat2Sel >= 0){
            totalsubstat2 = substat2 * (100 + substat2Percent + allstatPercent) / 100;
        }
        else{
            totalsubstat2 = 0;
        }
        totalattmag = attmag * (100 + attmagPercent) / 100;
    }

    private void applyClassSpec(){
        mainstat += classSpec.getMainstat();
        substat1 += classSpec.getSubstat1();
        attmag += classSpec.getAttmag();
        attmagPercent += classSpec.getAttmagPercent();
        dmg += classSpec.getDmg();
        bossDMG += classSpec.getBossDMG();
        addPenetrate(classSpec.getPenetrate());
        critDMG += classSpec.getCritDMG();
        return;
    }

    private void applyHyperstat(){
        fixedMainstat += hyperstat.getFixedMainstat();
        attmag += hyperstat.getAttmag();
        bossDMG += hyperstat.getBossDMG();
        critDMG += hyperstat.getCritDMG();
        dmg += hyperstat.getDmg();
        addPenetrate(hyperstat.getPenetrate());
    }

    private void applyUnion(){
        mainstat += union.getMainstat();
        fixedMainstat += union.getFixedMainstat();
        substat1 += union.getSubstat1();
        substat2 += union.getSubstat2();
        attmag += union.getAttmag();
        critDMG += union.getCritDMG();
        bossDMG += union.getBossDMG();
        dmg += union.getDmg();
        addPenetrate(union.getPenetrate());
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

    private void addPenetrate(float itemPenetrate){
        penetrate = (10000 - (100 - penetrate) * (100 - itemPenetrate)) / 100.0f;
    }

    private void applySetOption(){
        //boss set
        if(set[1] >= 3){
            mainstat += 10;
            substat1 += 10;
            substat2 += 10;
            attmag += 5;
        }
        if(set[1] >= 5){
            mainstat += 10;
            substat1 += 10;
            substat2 += 10;
            attmag += 5;
        }
        if(set[1] >= 7){
            mainstat += 10;
            substat1 += 10;
            substat2 += 10;
            attmag += 10;
            addPenetrate(10.0f);
        }
        if(set[1] >= 9){
            mainstat += 15;
            substat1 += 15;
            substat2 += 15;
            attmag += 10;
            bossDMG += 10;
        }

        //dawn boss set
        if(set[2] >= 2){
            mainstat += 10;
            substat1 += 10;
            substat2 += 10;
            attmag += 10;
            bossDMG += 10;
        }
        if(set[2] >= 3){
            mainstat += 10;
            substat1 += 10;
            substat2 += 10;
            attmag += 10;
        }
        if(set[2] >= 4){
            mainstat += 10;
            substat1 += 10;
            substat2 += 10;
            attmag += 10;
            addPenetrate(10.0f);
        }

        //black boss set
        if(set[3] >= 2){
            mainstat += 10;
            substat1 += 10;
            substat2 += 10;
            attmag += 10;
            bossDMG += 10;
        }
        if(set[3] >= 3){
            mainstat += 10;
            substat1 += 10;
            substat2 += 10;
            attmag += 10;
            addPenetrate(10.0f);
        }
        if(set[3] >= 4){
            mainstat += 15;
            substat1 += 15;
            substat2 += 15;
            attmag += 15;
            critDMG += 5;
        }
        if(set[3] >= 5){
            mainstat += 15;
            substat1 += 15;
            substat2 += 15;
            attmag += 15;
            bossDMG += 10;
        }
        if(set[3] >= 6){
            mainstat += 15;
            substat1 += 15;
            substat2 += 15;
            attmag += 15;
            addPenetrate(10.0f);
        }
        if(set[3] >= 7){
            mainstat += 15;
            substat1 += 15;
            substat2 += 15;
            attmag += 15;
            critDMG += 5;
        }
        if(set[3] >= 8){
            mainstat += 15;
            substat1 += 15;
            substat2 += 15;
            attmag += 15;
            bossDMG += 10;
        }
        if(set[3] >= 9){
            mainstat += 15;
            substat1 += 15;
            substat2 += 15;
            attmag += 15;
            critDMG += 5;
        }

        //rutabis set
        if(set[4] >= 2){
            mainstat += 20;
            substat1 += 20;
        }
        if(set[4] >= 3){
            attmag += 50;
        }
        if(set[4] >= 4){
            bossDMG += 30;
        }

        //absol set
        if(set[5] >= 2){
            attmag += 20;
            bossDMG += 10;
        }
        if(set[5] >= 3){
            mainstat += 30;
            substat1 += 30;
            substat2 += 30;
            attmag += 20;
            bossDMG += 10;
        }
        if(set[5] >= 4){
            attmag += 25;
            addPenetrate(10.0f);
        }
        if(set[5] >= 5){
            attmag += 30;
            bossDMG += 10;
        }
        if(set[5] >= 6){
            attmag += 20;
        }
        if(set[5] >= 7){
            attmag += 20;
            addPenetrate(10.0f);
        }

        //arcane set
        if(set[6] >= 2){
            attmag += 30;
            bossDMG += 10;
        }
        if(set[6] >= 3){
            attmag += 30;
            addPenetrate(10.0f);
        }
        if(set[6] >= 4){
            mainstat += 50;
            substat1 += 50;
            substat2 += 50;
            attmag += 35;
            bossDMG += 10;
        }
        if(set[6] >= 5){
            attmag += 40;
            bossDMG += 10;
        }
        if(set[6] >= 6){
            attmag += 30;
        }
        if(set[6] >= 7){
            attmag += 30;
            addPenetrate(10.0f);
        }

    }
}
