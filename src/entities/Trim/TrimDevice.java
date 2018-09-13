/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.Trim;

/**
 *
 * @author snajp
 */
import entities.Device;


public class TrimDevice extends Device<TrimChip> {
	public static class Builder extends Device.Builder<TrimChip> {
		private TrimDevice device;		

		public Builder() {
			setDevice(new TrimDevice());
		}
		
		public Builder(TrimDevice device) {
			setDevice(new TrimDevice(device));
		}
		
		@Override
		public TrimDevice build() {
			validate();
			return new TrimDevice(device);
		}
		
		protected void setDevice(TrimDevice device) {
			super.setDevice(device);
			this.device = device;
		}
	}
	
	protected TrimDevice() {}
	
	protected TrimDevice(TrimDevice other) {
		super(other);
	}

	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}
}
