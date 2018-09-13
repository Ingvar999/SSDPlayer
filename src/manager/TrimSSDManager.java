/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ui.WorkloadWidget;
import entities.BlockStatusGeneral;
import entities.StatisticsGetter;
import entities.Trim.TrimPage;
import entities.Trim.TrimBlock;
import entities.Trim.TrimPlane;
import entities.Trim.TrimChip;
import entities.Trim.TrimDevice;
import general.XMLGetter;
import general.XMLParsingException;

public class TrimSSDManager extends SSDManager<TrimPage, TrimBlock, TrimPlane, TrimChip ,TrimDevice> {
	private Color writtenPageColor;

	TrimSSDManager() {
	}

	public Color getWritenPageColor() {
		return writtenPageColor;
	}
	
	@Override
	public FileTraceParser<TrimDevice, TrimSSDManager> getFileTraseParser() {
		return new BasicTraceParser<TrimDevice, TrimSSDManager>(this);
	}

	protected void initValues(XMLGetter xmlGetter) throws XMLParsingException {
		super.initValues(xmlGetter);
		writtenPageColor = getColorField(xmlGetter, "written_color");
	}

	@Override
	public TrimPage getEmptyPage() {
		return new TrimPage.Builder().setManager(this).build();
	}
	
	@Override
	public List<WorkloadWidget<TrimDevice,	SSDManager<?, ?, ?, ?, TrimDevice>>> getWorkLoadGeneratorWidgets() {
		List<WorkloadWidget<TrimDevice,SSDManager<?, ?, ?, ?, TrimDevice>>> creators = new ArrayList<>();
		creators.add(new UniformWorkloadWidget<TrimDevice,SSDManager<?, ?, ?, ?, TrimDevice>>(this));
		creators.add(new ZipfWorkloadWidget<TrimDevice,SSDManager<?, ?, ?, ?, TrimDevice>>(this));
		return creators;
	}
	
	@Override
	protected List<StatisticsGetter> initStatisticsGetters() {
		List<StatisticsGetter> statisticsGetters = new ArrayList<StatisticsGetter>();
		statisticsGetters.add(new LogicalWritesPerEraseGetter(this));
		statisticsGetters.add(new WriteAmplificationGetter());
		statisticsGetters.add(new ValidDistributionGetter(this));
		return statisticsGetters;
	}
	
	@Override
	protected TrimDevice getEmptyDevice(List<TrimChip> emptyChips) {
		TrimDevice.Builder builder = new TrimDevice.Builder();
		builder.setChips(emptyChips);
		return builder.build();
	}

	@Override
	protected TrimChip getEmptyChip(List<TrimPlane> planes) {
		TrimChip.Builder builder = new TrimChip.Builder();
		builder.setPlanes(planes);
		return builder.build();
	}

	@Override
	protected TrimPlane getEmptyPlane(List<TrimBlock> blocks) {
		TrimPlane.Builder builder = new TrimPlane.Builder();
		builder.setBlocks(blocks);
		builder.setManager(this);
		return builder.build();
	}

	@Override
	protected TrimBlock getEmptyBlock(List<TrimPage> pages) {
		TrimBlock.Builder builder = new TrimBlock.Builder();
		builder.setManager(this).setEraseCounter(0).setInGC(false)
				.setStatus(BlockStatusGeneral.CLEAN).setPagesList(pages);
		return builder.build();
	}
}
