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

import entities.Block;
import entities.Page;
import java.util.Random;
import manager.ECCManager;
import utils.Utils;

public class ECCBlock extends Block<ECCPage> {
	public static class Builder extends Block.Builder<ECCPage> {
		private ECCBlock block;
		
		public Builder() {
			setBlock(new ECCBlock());
		}
		
		protected Builder(ECCBlock block) {
			setBlock(new ECCBlock(block));
		}
		
		public Builder setManager(ECCManager manager) {
			super.setManager(manager);
			block.manager = manager;
			return this;
		}
		
		@Override
		public ECCBlock build() {
			validate();
			return new ECCBlock(block);
		}

		protected void setBlock(ECCBlock block) {
			super.setBlock(block);
			this.block = block;
		}
		
		@Override
		protected void validate() {
			super.validate();
			Utils.validateNotNull(block.manager, "manager");
		}
	}
	
	private ECCManager manager = null;

	protected ECCBlock() { }
	
	protected ECCBlock(ECCBlock other) {
		super(other);
		this.manager = other.manager;
	}
        
         public ECCPage SearchLp(int lp){
            for (ECCPage page : getPages())
                if (page.getLp() == lp)
                    return page;
            return null;
        }
         
        @Override
	public ECCBlock writeLP(int lp, int lpArg) {
		int index = 0;
		for (ECCPage page : getPages()) {
			if (page.isClean()) {
                                Random rand = new Random();
                                long data = rand.nextLong() & manager.eccCoder.maxValue();
				ECCPage.Builder builder = getWrittenPageBuilder(lp, lpArg, page);
				builder.setData(data).setCodeword(manager.eccCoder.encode(data)).setClean(false).setLp(lp).setGC(false).setValid(true);
                                ECCPage newpage = builder.build();
                                ECCBlock newblock = (ECCBlock)addValidPage(index, newpage);
                                newpage.setParentBlock(newblock);
				return newblock;
			}
			++index;
		}
		return null;
	}
	
        @Override
        protected ECCPage.Builder getWrittenPageBuilder(int lp, int lpArg, ECCPage page) {
		return page.getSelfBuilder();
	}
        
	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}
}
