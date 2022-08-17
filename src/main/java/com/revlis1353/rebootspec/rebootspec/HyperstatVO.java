package com.revlis1353.rebootspec.rebootspec;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component("HyperstatVO")
public class HyperstatVO {
    private int fixedMainstat;
    private int attmag;
    private int bossDMG;
    private int critDMG;
    private int dmg;
    private float penetrate;
    
}
