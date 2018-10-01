/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workload_generators;

import manager.*;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;

import entities.Device;
import entities.ECC.ECCDevice;

public class ECCWorkloadWidget<D extends ECCDevice, S extends SSDManager<?,?,?,?,D>>
	extends ZipfResizableWorkloadWidget<D, S> {

	private static final long serialVersionUID = 1L;
	
	private JFormattedTextField writesInput;
	
	public ECCWorkloadWidget(S manager) {
		super("Read", manager);
		writesInput = new JFormattedTextField(new DecimalFormat("##"));
		writesInput.setValue(2);
		addField(writesInput, "Reads per write");
	}

	@Override
	public WorkloadGenerator<D, S> createWorkloadGenerator() {
		return new ECCWorkloadGenerator<D,S>("Read", manager, getWorkloadLength(), getSeed(), getExponent(), getMaxWriteSize(), isWriteSizeUniform(), getWritesPerDelete());
	}
	
	protected int getWritesPerDelete() {
		return ((Number)writesInput.getValue()).intValue();
	}
	
	public void validateParms() {
		super.validateParms();
		int wpr = ((Number)writesInput.getValue()).intValue();
		if (wpr < 1) {
			throw new IllegalArgumentException("Reads per write should be larger than 1 or equal");
		}
	}
}
