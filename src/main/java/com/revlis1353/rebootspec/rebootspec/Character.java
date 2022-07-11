package com.revlis1353.rebootspec.rebootspec;

public class Character {
    
    public enum itemEnum {Ring1, Cap, Emblem, Ring2, Pendant1, 
        Forehead, Badge, Ring3, Pendant2, Eyeacc, Earacc, Medal,
        Ring4, Weapon, Clothes, Shoulder, Subweapon, Poket, Belt, Pants, Gloves, Cape,
        Shoes, Android, Heart
    };

    private DataItem equipeditem[];
    private int basemainstat;

    public Character(){
        equipeditem = new DataItem[25];
    }
    
    public void calculateSpec(){
        //TODO
    }
}
