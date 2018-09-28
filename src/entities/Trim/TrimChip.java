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
import entities.Chip;

public class TrimChip extends Chip<TrimPlane> {
	public static class Builder extends Chip.Builder<TrimPlane> {
		private TrimChip chip;
		
		public Builder () {
			setChip(new TrimChip());
		}
		
		public Builder (TrimChip chip) {
			setChip(new TrimChip(chip));
		}
		
		public TrimChip build() {
			return new TrimChip(chip);
		}
		
		protected void setChip(TrimChip chip) {
			super.setChip(chip);
			this.chip = chip;
		}
	}
	
	protected TrimChip() {}	
	
	protected TrimChip(TrimChip other) {
		super(other);
	}

	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}
        
        @Override
        protected int getMinValidCountPlaneIndex() {
		int minIndex = 0;
		int minValue = Integer.MAX_VALUE;
		
		int planeIndex = 0;
		for (TrimPlane plane : getPlanes()) {
			int temp = plane.getNumOfBlockErasures();
			if (temp < minValue) {
				minIndex = planeIndex;
				minValue = temp;
			}
			++planeIndex;
		}
		return minIndex;
	}
        
        public int getNumOfPlaneErasures() {
		int numOfErasures = 0;
		for (TrimPlane plane : getPlanes()) {
			numOfErasures += plane.getNumOfBlockErasures();
		}
		return numOfErasures;
	}
}
