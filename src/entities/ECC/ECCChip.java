/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.ECC;

/**
 *
 * @author snajp
 */
import entities.Chip;

public class ECCChip extends Chip<ECCPlane> {
	public static class Builder extends Chip.Builder<ECCPlane> {
		private ECCChip chip;
		
		public Builder () {
			setChip(new ECCChip());
		}
		
		public Builder (ECCChip chip) {
			setChip(new ECCChip(chip));
		}
		
		public ECCChip build() {
			return new ECCChip(chip);
		}
		
		protected void setChip(ECCChip chip) {
			super.setChip(chip);
			this.chip = chip;
		}
	}
	
	protected ECCChip() {}	
	
	protected ECCChip(ECCChip other) {
		super(other);
	}
        
        public ECCPage SearchLp(int lp){
         ECCPage page;
         for (ECCPlane plane : getPlanes())
             if ((page = plane.SearchLp(lp)) != null)
                 return page;
         return null;
        }               

	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}
}
