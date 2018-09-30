/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ECC;

import entities.ECC.ECCPage;

/**
 *
 * @author snajp
 */
public class MistakeFromErasures<P extends ECCPage> implements IMistakeMaker<P>{
    @Override
    public long generateMistake(P page, long maxValue){
        return 0;
    }
}
