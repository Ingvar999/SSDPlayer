/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ECC;

import entities.ECC.ECCPage;
import java.util.Random;

/**
 *
 * @author snajp
 */
public class MistakeFromErasures<P extends ECCPage> implements IMistakeMaker<P>{
    private final double y = 0.06;
    
    @Override
    public long generateMistake(P page, long maxValue){
        long e = 0;
        long currentBit = maxValue+1;
        Random rand = new Random();
        double probability = y * Math.pow(Math.E, y * page.parentBlock.getEraseCounter());
        while (currentBit > 0){
            if(probability + rand.nextGaussian()/4 >= 1)
              e |= currentBit;  
            currentBit >>= 1;
        }
        return e;
    }
}
