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
import java.awt.Color;

import manager.TrimSSDManager;
import utils.Utils;
import entities.Page;

public class TrimPage extends Page {
	public static class Builder extends Page.Builder {
		private TrimPage page;
		
		public Builder() {
			setPage(new TrimPage());
		}
		
		Builder(TrimPage page) {
			setPage(new TrimPage(page));
		}

		protected void setPage(TrimPage page) {
			super.setPage(page);
			this.page = page;
		}

		public Builder setManager(TrimSSDManager manager) {
			page.manager = manager;
			return this;
		}
		
		public TrimPage build() {
			validate();
			return new TrimPage(page);
		}
		
		protected void validate() {
			Utils.validateNotNull(page.manager, "manager");
		}
	}
	
	private TrimSSDManager manager = null;
	
	TrimPage() {}
	
	TrimPage(TrimPage other) {
		super(other);
		manager = other.manager;
	}

	public Builder getSelfBuilder() {
		return new Builder(this);
	}

	@Override
	public Color getBGColor() {
		if (isClean()) {
			return manager.getCleanColor();
		} 
		return manager.getWritenPageColor();
	}
}
