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
import entities.ActionLog;
import entities.Chip;
import entities.Device;
import entities.WriteLpAction;
import java.util.ArrayList;
import java.util.List;


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
        
        public Device<TrimChip>  invalidate(int lp) {
                List<TrimChip> updatedChips = new ArrayList<TrimChip>();
		for (TrimChip chip : getChips()) {
			updatedChips.add((TrimChip) chip.invalidate(lp));
		}
		Device.Builder<TrimChip> builder = getSelfBuilder();
		builder.setChips(updatedChips);
		return builder.build();
	}

	public Device<TrimChip> writeLP(int lp, int arg) {
		int minIndex = 0;
		int minValue = Integer.MAX_VALUE;		
		int chipIndex = 0;
		for (TrimChip chip : getChips()) {
			int temp = chip.getNumOfBlockErasures();
			if (temp < minValue) {
				minIndex = chipIndex;
				minValue = temp;
			}
			++chipIndex;
		}
		
                List<TrimChip> updatedChips = getNewChipsList();
		updatedChips.set(minIndex, (TrimChip) getChip(minIndex).writeLP(lp, arg));
		ActionLog.addAction(new WriteLpAction(lp));
		Device.Builder<TrimChip> builder = getSelfBuilder();
		builder.setChips(updatedChips).setTotalWritten(getTotalWritten() + 1);
		return builder.build();
	}
}
