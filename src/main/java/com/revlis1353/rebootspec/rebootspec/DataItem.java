package com.revlis1353.rebootspec.rebootspec;

import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class DataItem{
    private int modifyIndex;
    @CsvBindByName
    private String itemName;
    @CsvBindByName
    private String itemImg;
    @CsvBindByName
    private int reqLev;
    @CsvBindByName
    private int mainstat;
    private int fixedMainstat;
    @CsvBindByName
    private int substat1;
    @CsvBindByName
    private int substat2;
    private int mainstatPercent;
    private int substat1Percent;
    private int substat2Percent;
    private int allstatPercent;
    @CsvBindByName
    private int attmag;
    private int attmagPercent;
    private int pureattmag;
    private int critDMG;
    @CsvBindByName
    private int bossDMG;
    @CsvBindByName
    private float penetrate;
    private int dmg;
    @CsvBindByName
    private int set;    //0: None, 1: boss, 2: dawn, 3: black, 4: ruta, 5: absol, 6: arcane
    private int starforce;

    private int isModified;
    private int trashval;
    
    private static final int STARFORCEWEAPATTACK130[] = {6, 7, 7, 8, 9, 10, 11, 29, 30, 31};
    private static final int STARFORCEWEAPATTACK140[] = {7, 8, 8, 9, 10, 11, 12, 30, 31, 32};
    private static final int STARFORCEWEAPATTACK150[] = {8, 9, 9, 10, 11, 12, 13, 31, 32, 33};
    private static final int STARFORCEWEAPATTACK160[] = {9, 9, 10, 11, 12, 13, 14, 32, 33, 34};
    private static final int STARFORCEWEAPATTACK200[] = {13, 13, 14, 14, 15, 16, 17, 34, 35, 36};

    public void applyStarforce(){
        if(starforce == 0)
            return;
        else{
            itemName = itemName + " " + starforce + "성 강화";
        }
        int starforceAllstat = 0;
        int starforceAttmag = 0;
        
        int starforceStatToAdd = 0;
        int starForceAttmagToAdd = 0;
        int weaponAttAbove15[];
        if(reqLev <= 139){
            starforceStatToAdd = 7;
            starForceAttmagToAdd = 7;
            weaponAttAbove15 = STARFORCEWEAPATTACK130;
        }
        else if(reqLev <= 149) {
            starforceStatToAdd = 9;
            starForceAttmagToAdd = 8;
            weaponAttAbove15 = STARFORCEWEAPATTACK140;
        }
        else if(reqLev <= 159) {
            starforceStatToAdd = 11;
            starForceAttmagToAdd = 9;
            weaponAttAbove15 = STARFORCEWEAPATTACK150;
        }
        else if(reqLev <= 169) {
            starforceStatToAdd = 13;
            starForceAttmagToAdd = 10;
            weaponAttAbove15 = STARFORCEWEAPATTACK160;
        }
        else if(reqLev == 200) {
            starforceStatToAdd = 15;
            starForceAttmagToAdd = 12;
            weaponAttAbove15 = STARFORCEWEAPATTACK200;
        }
        else{
            weaponAttAbove15 = STARFORCEWEAPATTACK130;
        }

        if(modifyIndex == 13){  //If Weapon
            for(int i = 0; i < starforce && i < 25; i++){
                if(i < 5){
                    starforceAllstat += 2;
                    starforceAttmag += ((pureattmag + starforceAttmag) / 50) + 1;
                }
                else if(i < 15){
                    starforceAllstat += 3;
                    starforceAttmag += ((pureattmag + starforceAttmag) / 50) + 1;
                }
                else if(i < 21){
                    starforceAllstat += starforceStatToAdd;
                    starforceAttmag += weaponAttAbove15[i-15];
                }
            }
        }
        else{
            for(int i = 0; i < starforce && i < 25; i++){
                if(modifyIndex == 20){  //If glove
                    if(i == 4 || i == 6 || i == 8 || i == 10 || i == 12 || i == 13 || i == 14){
                        starforceAttmag += 1;
                    }
                }
                
                if(i < 5){
                    if(modifyIndex != 15 && modifyIndex != 21)
                        starforceAllstat += 2;
                }
                else if(i < 15){
                    if(modifyIndex != 15 && modifyIndex != 21)
                        starforceAllstat += 3;
                }
                else if(i < 21){
                    starforceAllstat += starforceStatToAdd;
                    starforceAttmag += starForceAttmagToAdd;
                    starForceAttmagToAdd += 1;
                }
                else if(i < 25){
                    starforceAllstat += starforceStatToAdd;
                    starforceAttmag += starForceAttmagToAdd;
                    starForceAttmagToAdd += 2;
                }
            }
        }

        mainstat += starforceAllstat;
        substat1 += starforceAllstat;
        if(modifyIndex != 13 && modifyIndex != 1 && modifyIndex != 14 && modifyIndex != 19 && modifyIndex != 20 && modifyIndex != 22){
            substat2 += starforceAllstat;
        }
        attmag += starforceAttmag;
    }
}