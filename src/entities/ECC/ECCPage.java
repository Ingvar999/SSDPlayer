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
import java.awt.Color;

import manager.ECCManager;
import utils.Utils;
import entities.Page;

public class ECCPage extends Page {
	public static class Builder extends Page.Builder {
		private ECCPage page;
		
		public Builder() {
			setPage(new ECCPage());
		}
		
		Builder(ECCPage page) {
			setPage(new ECCPage(page));
		}

		protected void setPage(ECCPage page) {
			super.setPage(page);
			this.page = page;
		}

		public Builder setManager(ECCManager manager) {
			page.manager = manager;
			return this;
		}
		
		public ECCPage build() {
			validate();
			return new ECCPage(page);
		}
		
		protected void validate() {
			Utils.validateNotNull(page.manager, "manager");
		}
                
                public Builder setData(long data){
                    page.data = data;
                    return this;
                }
                
                public Builder setCodeword(long word){
                    page.codeword = word;
                    return this;
                }
	}
	
	private ECCManager manager = null;
        
        public long data, codeword;
        public ECCBlock parentBlock = null;
	
	ECCPage() {}
	
	ECCPage(ECCPage other) {
		super(other);
		manager = other.manager;
                data = other.data;
                codeword = other.codeword;
                parentBlock = other.parentBlock;
	}
        
        public void setParentBlock(ECCBlock block){
                    parentBlock = block;
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
