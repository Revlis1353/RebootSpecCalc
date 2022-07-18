package com.revlis1353.rebootspec.rebootspec;

import org.springframework.stereotype.Component;

@Component("FindCharacterVO")
public class FindCharacterVO{

    private String characterName;
    private int mainstatSel;
    private int substat1Sel;
    private int substat2Sel;
    private int attmagSel;

    public String getcharacterName(){
        return characterName;
    }

    public void setcharacterName(String characterName){
        this.characterName = characterName;
    }

    public FindCharacterVO(){
    }

    public int getmainstatSel() {
        return mainstatSel;
    }

    public int getsubstat1Sel() {
        return substat1Sel;
    }

    public int getsubstat2Sel() {
        return substat2Sel;
    }

    public void setmainstatSel(int mainstatSel) {
        this.mainstatSel = mainstatSel - 1;
    }

    public void setsubstat1Sel(int substat1Sel) {
        this.substat1Sel = substat1Sel - 1;
    }

    public void setSubStat2Sel(int substat2Sel) {
        this.substat2Sel = substat2Sel - 1;
    }

    public int getattmagSel() {
        return attmagSel;
    }

    public void setattmagSel(int attmagSel) {
        this.attmagSel = attmagSel - 1;
    }
}