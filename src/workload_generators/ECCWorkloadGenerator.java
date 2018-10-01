package workload_generators;

import manager.*;
import java.text.DecimalFormat;

import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;

import entities.Device;
import entities.ECC.ECCDevice;

/**
 * 
 * @author Or Mauda
 *
 */
public class ECCWorkloadGenerator<D extends ECCDevice, S extends SSDManager<?,?,?,?,D>> 
	extends ZipfResizableWorkloadGenerator<D,S>{
	
    protected int readsPerWrite;
    private int readsCounter = 0;
    
    public ECCWorkloadGenerator(String name, S manager, int traceLength,
			int seed, double exponent, int maxWriteSize, boolean isWriteSizeUniform, int _readsPerWrite) {
		
		super(name, manager, traceLength, seed, exponent, maxWriteSize, isWriteSizeUniform);
                readsPerWrite = _readsPerWrite;
	}
    
        @Override
	public D parseNextCommand() {
		if (device != null) {
                    lp = getLP();
                    temp = getLPArg(lp);
                    if (readsCounter < readsPerWrite){
			device.readLp(lp, (ECCManager)manager);
                        readsCounter++;
                    }
                    else{
                        device = manager.writeLP(device, lp, temp);
                        readsCounter = 0;
                    }
		}
		return (D)device;
	}
	
	public String getName() {
		return super.getName() + "_reads per write(" + readsPerWrite + ")";
	}
	
}