/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import statistic_getters.ValidDistributionGetter;
import statistic_getters.EraseDistributionGetter;
import statistic_getters.WriteAmplificationGetter;
import workload_generators.UniformWorkloadWidget;
import workload_generators.ZipfWorkloadWidget;
import ECC.CyclicHammingCode_31_26_3;
import ECC.IECCCoder;
import ECC.IMistakeMaker;
import ECC.MistakeFromErasures;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ui.WorkloadWidget;
import entities.BlockStatusGeneral;
import entities.StatisticsGetter;
import entities.ECC.ECCPage;
import entities.ECC.ECCBlock;
import entities.ECC.ECCPlane;
import entities.ECC.ECCChip;
import entities.ECC.ECCDevice;
import general.XMLGetter;
import general.XMLParsingException;
import workload_generators.ECCWorkloadWidget;

public class ECCManager extends SSDManager<ECCPage, ECCBlock, ECCPlane, ECCChip ,ECCDevice> {
	private Color writtenPageColor;
        public IECCCoder eccCoder;
        public IMistakeMaker<ECCPage> mistakeMaker;

	ECCManager() {
            eccCoder = new CyclicHammingCode_31_26_3();
            mistakeMaker = new MistakeFromErasures<ECCPage>();
	}

	public Color getWritenPageColor() {
		return writtenPageColor;
	}
        
        public void readLp(ECCDevice device, int lp){
            device.readLp(lp, this);
        }
        
	
	@Override
	public FileTraceParser<ECCDevice, ECCManager> getFileTraseParser() {
		return new BasicTraceParser<ECCDevice, ECCManager>(this);
	}

	protected void initValues(XMLGetter xmlGetter) throws XMLParsingException {
		super.initValues(xmlGetter);
		writtenPageColor = getColorField(xmlGetter, "written_color");
	}
	
	@Override
	public List<WorkloadWidget<ECCDevice,	SSDManager<?, ?, ?, ?, ECCDevice>>> getWorkLoadGeneratorWidgets() {
		List<WorkloadWidget<ECCDevice,SSDManager<?, ?, ?, ?, ECCDevice>>> creators = new ArrayList<>();
		creators.add(new UniformWorkloadWidget<ECCDevice,SSDManager<?, ?, ?, ?, ECCDevice>>(this));
		creators.add(new ZipfWorkloadWidget<ECCDevice,SSDManager<?, ?, ?, ?, ECCDevice>>(this));
                creators.add(new ECCWorkloadWidget<ECCDevice,SSDManager<?, ?, ?, ?, ECCDevice>>(this));
		return creators;
	}
	
	@Override
	protected List<StatisticsGetter> initStatisticsGetters() {
		List<StatisticsGetter> statisticsGetters = new ArrayList<StatisticsGetter>();
		statisticsGetters.add(new WriteAmplificationGetter());
		statisticsGetters.add(new ValidDistributionGetter(this));
                statisticsGetters.add(new EraseDistributionGetter(this));
		return statisticsGetters;
	}
        
        
	
	@Override
	protected ECCDevice getEmptyDevice(List<ECCChip> emptyChips) {
		ECCDevice.Builder builder = new ECCDevice.Builder();
		builder.setChips(emptyChips);
		return builder.build();
	}

	@Override
	protected ECCChip getEmptyChip(List<ECCPlane> planes) {
		ECCChip.Builder builder = new ECCChip.Builder();
		builder.setPlanes(planes);
		return builder.build();
	}

	@Override
	protected ECCPlane getEmptyPlane(List<ECCBlock> blocks) {
		ECCPlane.Builder builder = new ECCPlane.Builder();
		builder.setBlocks(blocks);
		builder.setManager(this);
		return builder.build();
	}

	@Override
	protected ECCBlock getEmptyBlock(List<ECCPage> pages) {
		ECCBlock.Builder builder = new ECCBlock.Builder();
		builder.setManager(this).setEraseCounter(0).setInGC(false)
				.setStatus(BlockStatusGeneral.CLEAN).setPagesList(pages);
		return builder.build();
	}
        
        @Override
	public ECCPage getEmptyPage() {
		return new ECCPage.Builder().setManager(this).build();
	}
}
