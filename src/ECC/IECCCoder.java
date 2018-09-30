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
public interface IECCCoder {
    long encode(long data);
    long decode(long codeword);
    long maxValue();
}
