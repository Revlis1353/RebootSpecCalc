package com.revlis1353.rebootspec.rebootspec;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CharacterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return FindCharacterVO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FindCharacterVO character = (FindCharacterVO) target;
        if(character.getcharacterName() == null || character.getcharacterName().length() == 0){
            errors.rejectValue("characterName", "required");
        }
        if(character.getattmagSel() < 0){
            errors.rejectValue("attmagSel", "required");
        }
        if(character.getmainstatSel() < 0){
            errors.rejectValue("mainstatSel", "required");
        }
        if(character.getsubstat1Sel() < 0){
            errors.rejectValue("substat1Sel", "required");
        }
        
    }
    
}