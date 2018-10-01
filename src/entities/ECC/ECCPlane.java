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
import java.util.List;

import org.javatuples.Pair;

import entities.BlockStatusGeneral;
import entities.Plane;

public class ECCPlane extends Plane<ECCBlock> {
	public static class Builder extends Plane.Builder<ECCBlock> {
		private ECCPlane plane;
		
		public Builder() {
			setPlane(new ECCPlane());
		}
		
		public Builder(ECCPlane plane) {
			setPlane(new ECCPlane(plane));
		}

		public ECCPlane build() {
			validate();
			return new ECCPlane(plane);
		}
		
		protected void setPlane(ECCPlane plane) {
			super.setPlane(plane);
			this.plane = plane;
		}
		
		protected void validate() {
			super.validate();
		}
	}
	
	protected ECCPlane() {}
		
	protected ECCPlane(ECCPlane other) {
		super(other);
	}

	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}

	@Override
	public ECCPlane writeLP(int lp, int dummy) {
		List<ECCBlock> updatedBlocks = getNewBlocksList();
		int active = getActiveBlockIndex();
		if (active == -1) {
			active = getLowestEraseCleanBlockIndex();
			updatedBlocks.set(active, (ECCBlock) updatedBlocks.get(active).setStatus(BlockStatusGeneral.ACTIVE));
		}
		ECCBlock activeBlock = updatedBlocks.get(active);
		activeBlock = (ECCBlock) activeBlock.writeLP(lp, 0);
		if(!activeBlock.hasRoomForWrite()) {
			activeBlock = (ECCBlock) activeBlock.setStatus(BlockStatusGeneral.USED);
		}
		updatedBlocks.set(active, activeBlock);
		Builder builder = getSelfBuilder();
		builder.setBlocks(updatedBlocks).setTotalWritten(getTotalWritten() + 1);
		return builder.build();
	}
        
        public ECCPage SearchLp(int lp){
            ECCPage page;
            for (ECCBlock block : getBlocks())
                if ((page = block.SearchLp(lp)) != null)
                    return page;
            return null;
        }
	
	@Override
	protected Pair<ECCPlane,Integer> cleanPlane() {
		List<ECCBlock> cleanBlocks = getNewBlocksList();
		Pair<Integer, ECCBlock> pickedToClean =  pickBlockToClean();
		int toMove = pickedToClean.getValue1().getValidCounter();
		int active = getActiveBlockIndex();
		ECCBlock activeBlock = null;
		if (active != -1) {			
			activeBlock = cleanBlocks.get(active);
		} 
		for (ECCPage page : pickedToClean.getValue1().getPages()) {
			if (page.isValid()) {
				if (active == -1) {
					active = getLowestEraseCleanBlockIndex();
					activeBlock = (ECCBlock) cleanBlocks.get(active).setStatus(BlockStatusGeneral.ACTIVE);
				}
				activeBlock = (ECCBlock) activeBlock.move(page.getLp(), 0);
				if(!activeBlock.hasRoomForWrite()) {
					activeBlock = (ECCBlock) activeBlock.setStatus(BlockStatusGeneral.USED);
					cleanBlocks.set(active, activeBlock);
					active = -1;
				}
			}
		}
		if (active != -1) {			
			cleanBlocks.set(active, activeBlock);
		}
		cleanBlocks.set(pickedToClean.getValue0(), (ECCBlock)pickedToClean.getValue1().eraseBlock());
		Builder builder = getSelfBuilder();
		int gcInvocations = (toMove > 0)? getTotalGCInvocations() + 1 : getTotalGCInvocations();
		builder.setBlocks(cleanBlocks).setTotalGCInvocations(gcInvocations);
		return new Pair<>(builder.build(), toMove);
	}
}