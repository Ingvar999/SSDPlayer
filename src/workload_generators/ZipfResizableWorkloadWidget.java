package workload_generators;

import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;

import entities.Device;
import manager.SSDManager;
import manager.WorkloadGenerator;

public class ZipfResizableWorkloadWidget<D extends Device<?>, S extends SSDManager<?,?,?,?,D>>
	extends UniformResizableWorkloadWidget<D, S> {

	private static final long serialVersionUID = 1L;
	
	private JFormattedTextField exponentInput;

	public ZipfResizableWorkloadWidget(S manager) {
		this("Zipf", manager);
	}
	
	public ZipfResizableWorkloadWidget(String name, S manager) {
		super(name, manager);
		exponentInput = new JFormattedTextField(new DecimalFormat("#0.00"));
		exponentInput.setValue(0.5);
		addField(exponentInput, "Exponent");
	}

	@Override
	public WorkloadGenerator<D, S> createWorkloadGenerator() {
		return new ZipfResizableWorkloadGenerator<D,S>("Zipf Resizable", manager, getWorkloadLength(), getSeed(), getExponent(), getMaxWriteSize(), isWriteSizeUniform());
	}
	
	protected double getExponent() {
		return ((Number)exponentInput.getValue()).doubleValue();
	}
	
	public void validateParms() {
		super.validateParms();
		double exp = ((Number)exponentInput.getValue()).doubleValue();
		if (exp <= 0) {
			throw new IllegalArgumentException("Exponent should be a positive number");
		}
	}
}