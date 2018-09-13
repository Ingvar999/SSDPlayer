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
import entities.Block;
import manager.TrimSSDManager;
import utils.Utils;

public class TrimBlock extends Block<TrimPage> {
	public static class Builder extends Block.Builder<TrimPage> {
		private TrimBlock block;
		
		public Builder() {
			setBlock(new TrimBlock());
		}
		
		protected Builder(TrimBlock block) {
			setBlock(new TrimBlock(block));
		}
		
		public Builder setManager(TrimSSDManager manager) {
			super.setManager(manager);
			block.manager = manager;
			return this;
		}
		
		@Override
		public TrimBlock build() {
			validate();
			return new TrimBlock(block);
		}

		protected void setBlock(TrimBlock block) {
			super.setBlock(block);
			this.block = block;
		}
		
		@Override
		protected void validate() {
			super.validate();
			Utils.validateNotNull(block.manager, "manager");
		}
	}
	
	private TrimSSDManager manager = null;

	protected TrimBlock() { }
	
	protected TrimBlock(TrimBlock other) {
		super(other);
		this.manager = other.manager;
	}
	
	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}
}
