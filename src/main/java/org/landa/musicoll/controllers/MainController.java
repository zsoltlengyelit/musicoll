package org.landa.musicoll.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import org.landa.musicoll.core.watch.FileSystemListener;
import org.landa.musicoll.core.watch.FileSystemWatchService;
import org.landa.musicoll.model.Resource;
import org.landa.musicoll.view.MainWindow;
import org.landa.musicoll.view.components.FileTree;
import org.landa.musicoll.view.components.FileTreeNode;

import com.avaje.ebean.EbeanServer;
import com.google.inject.Inject;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MainController implements TreeSelectionListener, FileSystemListener {

	private final EbeanServer ebeanServer;

	private AdvancedPlayer player;

	private final FileSystemWatchService watchService;

	private MainWindow mainWindow;

	@Inject
	public MainController(final EbeanServer ebeanServer, FileSystemWatchService watchService) {
		this.ebeanServer = ebeanServer;
		this.watchService = watchService;
	}

	public void attach(final MainWindow mainWindow) {

		this.mainWindow = mainWindow;
		FileTree fileTree = mainWindow.getFileTree();
		JTree tree = fileTree.getTree();
		tree.addTreeSelectionListener(this);

		JList<Resource> list = mainWindow.getList();

		List<Resource> resources = ebeanServer.find(Resource.class).findList();

		DefaultListModel<Resource> listModel = (DefaultListModel<Resource>) list.getModel();
		for (Resource resource : resources) {
			listModel.addElement(resource);
		}

		watchService.setListener(this);
		this.watchService.watch();

	}

	@Override
	public void valueChanged(final TreeSelectionEvent event) {

		FileTreeNode node = (FileTreeNode) event.getPath().getLastPathComponent();

		File selectedFile = node.getFile();
		openFile(selectedFile);
	}

	private synchronized void openFile(final File selectedFile) {

		showInfo(selectedFile);

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

	private void showInfo(File selectedFile) {
		try {
			Mp3File mp3file = new Mp3File(selectedFile.getAbsolutePath());

			if (null == mp3file) {
				System.out.println("MainController.showInfo(): null");
			}

			ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			System.out.println("Track: " + id3v1Tag.getTrack());
			System.out.println("Artist: " + id3v1Tag.getArtist());
			System.out.println("Title: " + id3v1Tag.getTitle());
			System.out.println("Album: " + id3v1Tag.getAlbum());
			System.out.println("Year: " + id3v1Tag.getYear());
			System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
			System.out.println("Comment: " + id3v1Tag.getComment());
		} catch (IOException | UnsupportedTagException | InvalidDataException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fileSystemChanged(WatchEvent<?> event) {
		FileTree fileTree = mainWindow.getFileTree();
		fileTree.reload();
	}
}
