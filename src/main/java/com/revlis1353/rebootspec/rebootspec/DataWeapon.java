package com.revlis1353.rebootspec.rebootspec;

import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class DataWeapon{
    //1: 한손검, 2: 한손도끼, 3: 한손둔기, 4: 두손검, 5: 두손도끼, 6: 두손둔기, 7: 창, 8: 폴암, 
    //9: 데스페라도, 10: 건틀렛 리볼버, 11: 튜너, 12: 완드, 13: 스태프, 14: 샤이닝 로드, 15: esp 리미터, 16: 매직 건틀렛, 
    //17: 활, 18: 석궁, 19: 에인션트 보우, 20: 듀얼 보우건, 21: 아대, 22: 단검, 23: 케인, 24: 에너지소드, 25: 체인, 26: 부채,
    //27: 너클, 28: 건, 29: 핸드캐논, 30: 소울 슈터
    @CsvBindByName
    int weaponIndex;
    @CsvBindByName
    int additional1;
    @CsvBindByName
    int additional2;
    @CsvBindByName
    int additional3;
    @CsvBindByName
    int additional4;
    @CsvBindByName
    int additional5;
}