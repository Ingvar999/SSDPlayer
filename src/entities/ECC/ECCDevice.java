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

import entities.ActionLog;
import entities.Device;
import entities.WriteLpAction;
import java.util.ArrayList;
import java.util.List;
import manager.ECCManager;


public class ECCDevice extends Device<ECCChip> {
	public static class Builder extends Device.Builder<ECCChip> {
		private ECCDevice device;		

		public Builder() {
			setDevice(new ECCDevice());
		}
		
		public Builder(ECCDevice device) {
			setDevice(new ECCDevice(device));
		}
		
		@Override
		public ECCDevice build() {
			validate();
			return new ECCDevice(device);
		}
		
		protected void setDevice(ECCDevice device) {
			super.setDevice(device);
			this.device = device;
		}
	}
	
        public int success = 0, recovered = 0, failed = 0, total = 0; 
        
	protected ECCDevice() {}
	
	protected ECCDevice(ECCDevice other) {
		super(other);
                success = other.success;
                recovered = other.recovered;
                failed = other.failed;
                total = other.total;
	}
        
        public ECCPage SearchLp(int lp){
            return getChipByIndex(getChipIndex(lp)).SearchLp(lp);
        }     
        
        public void readLp(int lp, ECCManager manager){
            ECCPage page;
            if ((page = SearchLp(lp)) != null){
                long e = manager.mistakeMaker.generateMistake(page, manager.eccCoder.maxValue());
                if (e == 0)
                    success++;
                else{
                    long r = page.codeword ^ e;
                    if (manager.eccCoder.decode(r) == page.data)
                        recovered++;
                    else
                        failed++;
                }
                total++;
            }
        }

	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}
        
}
