package com.revlis1353.rebootspec.rebootspec;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;

@Component("FindCharacterVO")
public class FindCharacterVO{

    @NotEmpty
    private String characterName;

    public String getcharacterName(){
        return characterName;
    }

    public void setcharacterName(String characterName){
        this.characterName = characterName;
    }

    public FindCharacterVO(){
    }

    public FindCharacterVO(String characterName){
        this.characterName = characterName;
    }

}