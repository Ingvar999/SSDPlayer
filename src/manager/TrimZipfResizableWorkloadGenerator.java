/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import entities.Device;
import java.text.DecimalFormat;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 *
 * @author snajp
 */
public class TrimZipfResizableWorkloadGenerator<D extends Device<?>, S extends SSDManager<?,?,?,?,D>> 
	extends ZipfResizableWorkloadGenerator<D, S>  {
    
    protected int writesPerDelete;
    private int writesCounter = 0;
    
    public TrimZipfResizableWorkloadGenerator(String name, S manager, int traceLength,
			int seed, double exponent, int maxWriteSize, boolean isWriteSizeUniform, int _writesPerDelete) {
		
		super(name, manager, traceLength, seed, exponent, maxWriteSize, isWriteSizeUniform);
                writesPerDelete = _writesPerDelete;
	}
    
        @Override
	public D parseNextCommand() {
		if (device != null) {
                    lp = getLP();
                    temp = getLPArg(lp);
                    if (writesCounter < writesPerDelete){
			device = manager.writeLP(device, lp, temp);
                        writesCounter++;
                    }
                    else{
                        device = manager.deleteLP(device, lp, temp);
                        writesCounter = 0;
                    }
		}
		return device;
	}
	
	public String getName() {
		return super.getName() + "_writes per delete(" + writesPerDelete + ")";
	}
    
}
