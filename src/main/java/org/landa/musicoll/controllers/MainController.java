package org.landa.musicoll.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import org.landa.musicoll.model.Resource;
import org.landa.musicoll.view.MainWindow;
import org.landa.musicoll.view.components.FileTreeNode;

import com.avaje.ebean.EbeanServer;
import com.google.inject.Inject;

public class MainController implements TreeSelectionListener {

	private final EbeanServer ebeanServer;

	private AdvancedPlayer player;

	@Inject
	public MainController(final EbeanServer ebeanServer) {
		this.ebeanServer = ebeanServer;

	}

	public void attach(final MainWindow mainWindow) {

		mainWindow.getFileTree().getTree().addTreeSelectionListener(this);

		JList<Resource> list = mainWindow.getList();

		List<Resource> resources = ebeanServer.find(Resource.class).findList();

		DefaultListModel<Resource> listModel = (DefaultListModel<Resource>) list.getModel();
		for (Resource resource : resources) {
			listModel.addElement(resource);
		}

	}

	@Override
	public void valueChanged(final TreeSelectionEvent event) {

		FileTreeNode node = (FileTreeNode) event.getPath().getLastPathComponent();

		File selectedFile = node.getFile();
		openFile(selectedFile);
	}

	private synchronized void openFile(final File selectedFile) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					System.out.println("MainController.openFile()");

					if (null != player) {
						player.close();
					}
					player = new AdvancedPlayer(new FileInputStream(selectedFile.getAbsoluteFile()));

					player.setPlayBackListener(new PlaybackListener() {
						@Override
						public void playbackStarted(PlaybackEvent evt) {
							System.out.println("MainController.openFile(...).new PlaybackListener() {...}.playbackStarted()");
						}
					});

					player.play();

				} catch (FileNotFoundException | JavaLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}
}
