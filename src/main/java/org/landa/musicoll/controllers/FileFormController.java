package org.landa.musicoll.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.landa.musicoll.core.FilePlaceResolver;
import org.landa.musicoll.core.ResourceDataModel;
import org.landa.musicoll.model.Resource;
import org.landa.musicoll.view.components.FileForm;

import com.avaje.ebean.EbeanServer;
import com.google.inject.Inject;

/**
 * 
 * @author Zsolti
 * 
 */
public class FileFormController implements ActionListener, DocumentListener,
		ChangeListener {

	private final FilePlaceResolver filePlaceResolver;
	private FileForm fileForm;
	private final EbeanServer ebeanServer;

	private Resource resource;
	private final ResourceDataModel resourceDataModel;

	@Inject
	public FileFormController(FilePlaceResolver filePlaceResolver,
			EbeanServer ebeanServer, ResourceDataModel resourceDataModel) {
		this.filePlaceResolver = filePlaceResolver;
		this.ebeanServer = ebeanServer;
		this.resourceDataModel = resourceDataModel;
	}

	public void attach(FileForm fileForm, File file) {
		this.fileForm = fileForm;

		this.resource = getResource(file);

		fileForm.getTitleText().setText(resource.getTitle());
		fileForm.getArtText().setText(resource.getArt());
		fileForm.getInstrumentText().setText(resource.getInstrument());
		fileForm.getRegionText().setText(resource.getRegion());
		fileForm.getPlaceText().setText(resource.getPlace());
		fileForm.getTransCheck().setSelected(resource.isTrans());
		fileForm.getArtistText().setText(resource.getArtist());
		fileForm.getCollectorText().setText(resource.getCollector());
		fileForm.getCollectionTimeText().setText(resource.getCollectionTime());
		fileForm.getNoteText().setText(resource.getNote());

		// add change listeners
		fileForm.getTitleText().getDocument().addDocumentListener(this);
		fileForm.getArtText().getDocument().addDocumentListener(this);
		fileForm.getInstrumentText().getDocument().addDocumentListener(this);
		fileForm.getRegionText().getDocument().addDocumentListener(this);
		fileForm.getPlaceText().getDocument().addDocumentListener(this);
		fileForm.getTransCheck().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stateChanged();
			}
		});
		fileForm.getArtistText().getDocument().addDocumentListener(this);
		fileForm.getCollectorText().getDocument().addDocumentListener(this);
		fileForm.getCollectionTimeText().getDocument()
				.addDocumentListener(this);
		fileForm.getNoteText().getDocument().addDocumentListener(this);

		fileForm.getSaveButton().addActionListener(this);

	}

	private Resource getResource(File file) {

		String relativePath = filePlaceResolver.getRelativePath(file);
		Resource resource = ebeanServer.find(Resource.class).where()
				.eq("relativePath", relativePath).findUnique();

		if (null == resource) {
			resource = new Resource();
			resource.setRelativePath(relativePath);
		}

		return resource;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		resource.setTitle(fileForm.getTitleText().getText());
		resource.setArt(fileForm.getArtText().getText());
		resource.setInstrument(fileForm.getInstrumentText().getText());
		resource.setRegion(fileForm.getRegionText().getText());
		resource.setPlace(fileForm.getPlaceText().getText());
		resource.setTrans(fileForm.getTransCheck().isSelected());
		resource.setArtist(fileForm.getArtistText().getText());
		resource.setCollector(fileForm.getCollectorText().getText());
		resource.setCollectionTime(fileForm.getCollectionTimeText().getText());
		resource.setNote(fileForm.getNoteText().getText());

		ebeanServer.save(resource);

		fileForm.getTabTitlePanel().setSaved();
		resourceDataModel.refresh();

	}

	private void stateChanged() {
		fileForm.getTabTitlePanel().setUnsaved();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		stateChanged();
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		stateChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		stateChanged();

	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		stateChanged();

	}

}
