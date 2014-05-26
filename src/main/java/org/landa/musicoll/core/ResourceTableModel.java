package org.landa.musicoll.core;

import javax.swing.table.DefaultTableModel;

import org.landa.musicoll.model.Resource;

import com.google.inject.Inject;

public class ResourceTableModel extends DefaultTableModel {

	public static final Object[] COLUMNS = new Object[] { "Cím", "Tájegység",
			"Helység", "Hangyszer", "Műfaj", "Gyűjtő", "Gyűjtési idő" };
	/**
	 * 
	 */
	private static final long serialVersionUID = 1211496523083438574L;

	private final ResourceDataModel dataModel;

	private final int rowCount = 0;

	@Inject
	public ResourceTableModel(final ResourceDataModel dataModel) {
		super();

		this.dataModel = dataModel;
		this.dataModel.setResourceTableModel(this);
		for (Object col : COLUMNS) {
			addColumn(col);
		}

		fireTableDataChanged();

		// for (Resource resource : dataModel.getData()) {
		// addRow(new Object[] { resource.getTitle(), resource.getRegion(),
		// resource.getPlace(), resource.getInstrument(),
		// resource.getArt(), resource.getCollector(),
		// resource.getCollectionTime() });
		//
		// }
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public int getRowCount() {
		return this.dataModel == null ? 0 : dataModel.getData().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Resource resource = dataModel.getData().get(rowIndex);

		switch (columnIndex) {
		case 0:
			return resource.getTitle();

		case 1:
			return resource.getRegion();

		case 2:
			return resource.getPlace();

		case 3:
			return resource.getInstrument();

		case 4:
			return resource.getArt();

		case 5:
			return resource.getCollector();

		case 6:
			return resource.getCollectionTime();

		default:
			return null;

		}

	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	public ResourceDataModel getDataModel() {
		return dataModel;
	}

}
