package generators;

import manager.*;
import java.text.DecimalFormat;

import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;

import entities.Device;

/**
 * 
 * @author Or Mauda
 *
 */
public class ECCWorkloadGenerator<D extends Device<?>, S extends SSDManager<?,?,?,?,D>> 
	extends ResizableWorkloadGenerator<D, S> {
	
	protected ZipfDistribution zipf;
	protected int seed;
	protected double exponent;

	public ECCWorkloadGenerator(String name, S manager, int traceLength,
			int seed, double exponent, int maxWriteSize, boolean isWriteSizeUniform) {
		
		super(name, manager, traceLength, maxWriteSize, isWriteSizeUniform);
		this.seed = seed;
		this.exponent = exponent;
		JDKRandomGenerator jdkRandomGenerator = new JDKRandomGenerator();
		jdkRandomGenerator.setSeed(seed);
		zipf = new ZipfDistribution(jdkRandomGenerator, lpRange, exponent);
	}

	@Override
	protected int getLP() {
		return zipf.sample()-1;
	}
	
	public String getName() {
		String string = super.getName() + "_seed(" + seed + ")" + "_exp(" + new DecimalFormat("0.##").format(exponent) + ")";
		return string;
	}
	
}