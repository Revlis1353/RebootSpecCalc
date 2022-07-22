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
    private int critDMG;
    @CsvBindByName
    private int bossDMG;
    @CsvBindByName
    private float penetrate;
    @CsvBindByName
    private float set;
}