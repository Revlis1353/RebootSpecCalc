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

    private int trashval;
    
    private static final int STARFORCEWEAPATTACK130[] = {6, 7, 7, 8, 9, 10, 11, 29, 30, 31};
    private static final int STARFORCEWEAPATTACK140[] = {7, 8, 8, 9, 10, 11, 12, 30, 31, 32};
    private static final int STARFORCEWEAPATTACK150[] = {8, 9, 9, 10, 11, 12, 13, 31, 32, 33};
    private static final int STARFORCEWEAPATTACK160[] = {9, 9, 10, 11, 12, 13, 14, 32, 33, 34};
    private static final int STARFORCEWEAPATTACK200[] = {13, 13, 14, 14, 15, 16, 17, 34, 35, 36};

    public void applyStarforce(){
        int starforceAllstat = 0;
        int starforceAttmag = 0;
        
        int starforceStatToAdd = 0;
        int starForceAttmagToAdd = 0;
        int weaponAttAbove15[];
        if(reqLev == 130){
            starforceStatToAdd = 7;
            starForceAttmagToAdd = 7;
            weaponAttAbove15 = STARFORCEWEAPATTACK130;
        }
        else if(reqLev == 140) {
            starforceStatToAdd = 9;
            starForceAttmagToAdd = 8;
            weaponAttAbove15 = STARFORCEWEAPATTACK140;
        }
        else if(reqLev == 150) {
            starforceStatToAdd = 11;
            starForceAttmagToAdd = 9;
            weaponAttAbove15 = STARFORCEWEAPATTACK150;
        }
        else if(reqLev == 160) {
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

        if(modifyIndex == 13){
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
                if(i < 5){
                    starforceAllstat += 2;
                }
                else if(i < 15){
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
        substat2 += starforceAllstat;
        attmag += starforceAttmag;
    }
}