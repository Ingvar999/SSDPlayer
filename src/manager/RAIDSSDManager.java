/*******************************************************************************
 * SSDPlayer Visualization Platform (Version 1.0)
 * Authors: Or Mauda, Roman Shor, Gala Yadgar, Eitan Yaakobi, Assaf Schuster
 * Copyright (c) 2015, Technion � Israel Institute of Technology
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that
 * the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 *******************************************************************************/
package manager;

import statistic_getters.LogicalWritesPerEraseGetter;
import statistic_getters.ValidDistributionGetter;
import statistic_getters.WriteAmplificationGetter;
import workload_generators.ZipfResizableWorkloadWidget;
import workload_generators.UniformResizableWorkloadWidget;
import java.util.ArrayList;
import java.util.List;

import entities.BlockStatusGeneral;
import entities.StatisticsGetter;
import entities.RAID.simulation.RAIDBlock;
import entities.RAID.simulation.RAIDChip;
import entities.RAID.simulation.RAIDDevice;
import entities.RAID.simulation.RAIDPage;
import entities.RAID.simulation.RAIDPlane;
import general.XMLGetter;
import general.XMLParsingException;
import manager.RAIDStatistics.ParityOverheadGetter;
import ui.AddressWidget;
import ui.WorkloadWidget;
import zoom.BlocksRaidParityZoomLevel;
import zoom.SmallBlocksRaidParityZoomLevel;

/**
 * 
 * @author Or Mauda
 *
 */
public abstract class RAIDSSDManager extends RAIDBasicSSDManager<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice> {

	protected int stripeSize;
	protected int paritiesNumber;

	RAIDSSDManager() {
	}

	public int getStripeSize() {
		return stripeSize;
	}

	public int getParitiesNumber() {
		return paritiesNumber;
	}

	protected abstract void setParitiesNumber();

	protected abstract void setStripeSize();

	@Override
	public FileTraceParser<RAIDDevice, RAIDSSDManager> getFileTraseParser() {
		return new RAIDSimulationTraceParser(this);
	}

	protected void initValues(XMLGetter xmlGetter) throws XMLParsingException {
		super.initValues(xmlGetter);
		setParitiesNumber();
		setStripeSize();
	}

	@Override
	public RAIDPage getEmptyPage() {
		return new RAIDPage.Builder().setManager(this).build();
	}

	@Override
	public List<WorkloadWidget<RAIDDevice, SSDManager<?, ?, ?, ?, RAIDDevice>>> getWorkLoadGeneratorWidgets() {
		List<WorkloadWidget<RAIDDevice, SSDManager<?, ?, ?, ?, RAIDDevice>>> creators = new ArrayList<>();
		creators.add(new UniformResizableWorkloadWidget<RAIDDevice, SSDManager<?, ?, ?, ?, RAIDDevice>>(this));
		creators.add(new ZipfResizableWorkloadWidget<RAIDDevice, SSDManager<?, ?, ?, ?, RAIDDevice>>(this));
		return creators;
	}

	@Override
	public int getLpRange() {
		int logicalChips = (int) (getChipsNum() * ((double) getStripeSize() / (getStripeSize() + getParitiesNumber())));
		int logicalNumOfBlocks = (int) ((logicalChips * getPlanesNum() * getBlocksNum())
				/ (1 + (double) getOP() / 100));
		return logicalNumOfBlocks * getPagesNum();
	}

	@Override
	public List<AddressWidget<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice, RAIDBasicSSDManager<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice>>> getAddressGetterWidgets() {

		List<AddressWidget<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice, RAIDBasicSSDManager<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice>>> addressGetters = new ArrayList<>();

		addressGetters
				.add(new PhysicalAddressWidget<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice, RAIDBasicSSDManager<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice>>(
						this));

		addressGetters
				.add(new LogicalAddressWidget<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice, RAIDBasicSSDManager<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice>>(
						this));

		addressGetters
				.add(new ParityAddressWidget<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice, RAIDBasicSSDManager<RAIDPage, RAIDBlock, RAIDPlane, RAIDChip, RAIDDevice>>(
						this));

		return addressGetters;
	}

	@Override
	protected List<StatisticsGetter> initStatisticsGetters() {
		List<StatisticsGetter> statisticsGetters = new ArrayList<StatisticsGetter>();
		statisticsGetters.add(new ParityOverheadGetter<>(this, RAIDDevice.class));
		statisticsGetters.add(new LogicalWritesPerEraseGetter(this));
		statisticsGetters.add(new WriteAmplificationGetter());
		statisticsGetters.add(new ValidDistributionGetter(this));
		return statisticsGetters;
	}

	@Override
	protected abstract RAIDDevice getEmptyDevice(List<RAIDChip> emptyChips);

	@Override
	protected RAIDChip getEmptyChip(List<RAIDPlane> planes) {
		RAIDChip.Builder builder = new RAIDChip.Builder();
		builder.setPlanes(planes);
		return builder.build();
	}

	@Override
	protected RAIDPlane getEmptyPlane(List<RAIDBlock> blocks) {
		RAIDPlane.Builder builder = new RAIDPlane.Builder();
		builder.setBlocks(blocks);
		builder.setManager(this);
		return builder.build();
	}

	@Override
	protected RAIDBlock getEmptyBlock(List<RAIDPage> pages) {
		RAIDBlock.Builder builder = new RAIDBlock.Builder();
		builder.setManager(this).setEraseCounter(0).setInGC(false).setStatus(BlockStatusGeneral.CLEAN)
				.setPagesList(pages);
		return builder.build();
	}

	@Override
	protected void setSupportedZoomLevels() {
		super.setSupportedZoomLevels();
		this.supportedZoomLevels.add(new BlocksRaidParityZoomLevel());
		this.supportedZoomLevels.add(new SmallBlocksRaidParityZoomLevel());
	}
}
