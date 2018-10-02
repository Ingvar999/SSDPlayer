/*******************************************************************************
 * SSDPlayer Visualization Platform (Version 1.0)
 * Authors: Roman Shor, Gala Yadgar, Eitan Yaakobi, Assaf Schuster
 * Copyright (c) 2015, Technion – Israel Institute of Technology
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
package statistic_getters;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import general.ConfigProperties;
import entities.Block;
import entities.Chip;
import entities.Device;
import entities.ECC.ECCDevice;
import entities.Plane;
import entities.StatisticsColumn;
import entities.StatisticsGetter;
import manager.ECCManager;
import manager.SSDManager;
import ui.GeneralStatisticsGraph;
import ui.HistogramGraph;

public class ReadDistributionGetter implements StatisticsGetter {


	private static int columnDisplayFreq = 2;
	private ECCManager manager;

	public ReadDistributionGetter(SSDManager<?,?,?,?,?> manager) {
		this.manager = (ECCManager)manager; 
	}

	@Override
	public int getNumberOfColumns() {
		return 3;
	}

	@Override
	public List<StatisticsColumn> getStatistics(Device<?> device) {
                ECCDevice dev = (ECCDevice)device;
		List<StatisticsColumn> list = new ArrayList<StatisticsColumn>();
		list.add(new StatisticsColumn("success", (double)dev.success / dev.total * 100,true));
                list.add(new StatisticsColumn("recovered", (double)dev.recovered / dev.total * 100,true));
                list.add(new StatisticsColumn("failed", (double)dev.failed / dev.total * 100,true));
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entry<String, String> getInfoEntry(Device<?> device) {
		List<StatisticsColumn> statistics = getStatistics(device);
		StringBuilder sb = new StringBuilder();
		int colCount = 0;
		for (StatisticsColumn col : statistics) {
			sb.append(col.getColumnName());
			sb.append(": ");
			sb.append((int) col.getValue());
			colCount++;
			if (colCount != statistics.size()) {
				if (colCount % 6 == 5) {
					sb.append(",\n");
				} else {
					sb.append(", ");
				}
			}
		}

		return new AbstractMap.SimpleEntry("Reads", sb.toString());
	}

	@Override
	public GeneralStatisticsGraph getStatisticsGraph() {
		return new HistogramGraph("Reads", this);
	}
}