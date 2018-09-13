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
import java.util.List;

import org.javatuples.Pair;

import entities.BlockStatusGeneral;
import entities.Plane;

public class TrimPlane extends Plane<TrimBlock> {
	public static class Builder extends Plane.Builder<TrimBlock> {
		private TrimPlane plane;
		
		public Builder() {
			setPlane(new TrimPlane());
		}
		
		public Builder(TrimPlane plane) {
			setPlane(new TrimPlane(plane));
		}

		public TrimPlane build() {
			validate();
			return new TrimPlane(plane);
		}
		
		protected void setPlane(TrimPlane plane) {
			super.setPlane(plane);
			this.plane = plane;
		}
		
		protected void validate() {
			super.validate();
		}
	}
	
	protected TrimPlane() {}
		
	protected TrimPlane(TrimPlane other) {
		super(other);
	}

	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}

	@Override
	public TrimPlane writeLP(int lp, int dummy) {
		List<TrimBlock> updatedBlocks = getNewBlocksList();
		int active = getActiveBlockIndex();
		if (active == -1) {
			active = getLowestEraseCleanBlockIndex();
			updatedBlocks.set(active, (TrimBlock) updatedBlocks.get(active).setStatus(BlockStatusGeneral.ACTIVE));
		}
		TrimBlock activeBlock = updatedBlocks.get(active);
		activeBlock = (TrimBlock) activeBlock.writeLP(lp, 0);
		if(!activeBlock.hasRoomForWrite()) {
			activeBlock = (TrimBlock) activeBlock.setStatus(BlockStatusGeneral.USED);
		}
		updatedBlocks.set(active, activeBlock);
		Builder builder = getSelfBuilder();
		builder.setBlocks(updatedBlocks).setTotalWritten(getTotalWritten() + 1);
		return builder.build();
	}
	
	@Override
	protected Pair<TrimPlane,Integer> cleanPlane() {
		List<TrimBlock> cleanBlocks = getNewBlocksList();
		Pair<Integer, TrimBlock> pickedToClean =  pickBlockToClean();
		int toMove = pickedToClean.getValue1().getValidCounter();
		int active = getActiveBlockIndex();
		TrimBlock activeBlock = null;
		if (active != -1) {			
			activeBlock = cleanBlocks.get(active);
		} 
		for (TrimPage page : pickedToClean.getValue1().getPages()) {
			if (page.isValid()) {
				if (active == -1) {
					active = getLowestEraseCleanBlockIndex();
					activeBlock = (TrimBlock) cleanBlocks.get(active).setStatus(BlockStatusGeneral.ACTIVE);
				}
				activeBlock = (TrimBlock) activeBlock.move(page.getLp(), 0);
				if(!activeBlock.hasRoomForWrite()) {
					activeBlock = (TrimBlock) activeBlock.setStatus(BlockStatusGeneral.USED);
					cleanBlocks.set(active, activeBlock);
					active = -1;
				}
			}
		}
		if (active != -1) {			
			cleanBlocks.set(active, activeBlock);
		}
		cleanBlocks.set(pickedToClean.getValue0(), (TrimBlock) pickedToClean.getValue1().eraseBlock());
		Builder builder = getSelfBuilder();
		int gcInvocations = (toMove > 0)? getTotalGCInvocations() + 1 : getTotalGCInvocations();
		builder.setBlocks(cleanBlocks).setTotalGCInvocations(gcInvocations);
		return new Pair<>(builder.build(), toMove);
	}
}