/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ECC;

/**
 *
 * @author snajp
 */
public class CyclicHammingCode_31_26_3 implements IECCCoder{
    private final int n = 31;
    private final int k = 26;
    private final int m = n-k;
    private final int g = 0b101001;
    private final long maxValue;
    
    public CyclicHammingCode_31_26_3(){
        maxValue = (1 << n) - 1;
    }
    
    @Override
    public long maxValue(){
        return maxValue;
    }
  
    @Override
    public long encode(long data){
        return data;
    }
    
    @Override
    public long decode(long codeword){
        return codeword;
    }
}
