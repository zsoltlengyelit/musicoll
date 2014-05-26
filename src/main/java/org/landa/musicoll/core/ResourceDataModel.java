package org.landa.musicoll.core;

import java.util.List;

import org.landa.musicoll.model.Resource;

import com.avaje.ebean.EbeanServer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ResourceDataModel {

	private final EbeanServer ebeanServer;
	private List<Resource> data;

	private ResourceTableModel resourceTableModel;

	public ResourceTableModel getResourceTableModel() {
		return resourceTableModel;
	}

	public void setResourceTableModel(ResourceTableModel resourceTableModel) {
		this.resourceTableModel = resourceTableModel;
	}

	@Inject
	public ResourceDataModel(EbeanServer ebeanServer) {
		this.ebeanServer = ebeanServer;

	}

	public List<Resource> getData() {
		if (null == data)
			refresh();
		return data;
	}

	public void refresh() {
		this.data = ebeanServer.find(Resource.class).findList();
		if (null != resourceTableModel) {
			resourceTableModel.fireTableDataChanged();
		}
	}

}
