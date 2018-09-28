/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;

import entities.Device;

public class TrimZipfResizableWorkloadWidget<D extends Device<?>, S extends SSDManager<?,?,?,?,D>>
	extends ZipfResizableWorkloadWidget<D, S> {

	private static final long serialVersionUID = 1L;
	
	private JFormattedTextField writesInput;
	
	public TrimZipfResizableWorkloadWidget(S manager) {
		super("Trim", manager);
		writesInput = new JFormattedTextField(new DecimalFormat("##"));
		writesInput.setValue(2);
		addField(writesInput, "Writes per delete");
	}

	@Override
	public WorkloadGenerator<D, S> createWorkloadGenerator() {
		return new TrimZipfResizableWorkloadGenerator<D,S>("Trim", manager, getWorkloadLength(), getSeed(), getExponent(), getMaxWriteSize(), isWriteSizeUniform(), getWritesPerDelete());
	}
	
	protected int getWritesPerDelete() {
		return ((Number)writesInput.getValue()).intValue();
	}
	
	public void validateParms() {
		super.validateParms();
		int wpr = ((Number)writesInput.getValue()).intValue();
		if (wpr < 1) {
			throw new IllegalArgumentException("Writes per delete should be larger than 1");
		}
	}
}
