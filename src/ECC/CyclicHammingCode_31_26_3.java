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
    
    private long bit(int n){
        return 1 << n;
    }
    
    public CyclicHammingCode_31_26_3(){
        maxValue = (1 << n) - 1;
    } 
    
    @Override
    public long maxValue(){
        return maxValue;
    }
  
    @Override
    public long encode(long data){
        int[] reg = new int[m+1];
        long codeword = data << m;
        int res = 0;
        for (int i = 0; i <= m; ++i)
            reg[i] = (int)(1 & (g >> i) );
        for (int i = k - 1; i >= 0; --i){
            res = (int)(reg[0] ^ (1 & (data >> i)));
            for (int j = 0; j < m; ++j)
                reg[j] = reg[j+1] ^ res;
            reg[m] = res;
        }
        for (int i = 0; i < m; ++i){
            codeword |= bit(m-i-1) & reg[i];
        }
        return codeword;
    }
    
    @Override
    public long decode(long codeword){
        /*long s = codeword;
        int nr;
        while(s < g){
            nr = 0;
            while ((s >> ++nr) > 0);
            s ^= g << (nr - m + 2);
        }
        int[] reg = new int[m];
        for (int i = 0; i < m; ++i){
            reg[i] = (int)((s >> i) & 1);
        }
        int res;
        for (int i = n-1; i <= 0; --i){
           res = reg[m-1];
           reg[0] = (int)((codeword >> i) & 1);
           if ((g & 1) > 0)
               reg[0] ^= res;
           for (int j = 1; j < m; ++j){
               reg[j] = reg[j-1];
               if ((g >> i) > 0)
                   reg[j] ^= res;
           }
        }*/
        return codeword;
    }
}
