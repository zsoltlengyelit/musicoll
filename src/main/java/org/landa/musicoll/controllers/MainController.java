package org.landa.musicoll.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

import org.landa.musicoll.controllers.player.SwingAudioPlayer;
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

public class MainController implements TreeSelectionListener,
		FileSystemListener, BasicPlayerListener, ActionListener {

	private final EbeanServer ebeanServer;

	private BasicPlayer basicPlayer;

	private final FileSystemWatchService watchService;

	private final MainWindow mainWindow;

	@Inject
	public MainController(final EbeanServer ebeanServer,
			FileSystemWatchService watchService, MainWindow mainWindow) {
		this.ebeanServer = ebeanServer;
		this.watchService = watchService;
		this.mainWindow = mainWindow;

	}

	public void start() {

		FileTree fileTree = mainWindow.getFileTree();
		JTree tree = fileTree.getTree();
		tree.addTreeSelectionListener(this);

		SwingAudioPlayer audioPlayer = mainWindow.getAudioPlayer();
		audioPlayer.getButtonPause().addActionListener(this);
		final JSlider sliderTime = audioPlayer.getSliderTime();
		sliderTime.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				float percent = sliderTime.getValue() / sliderTime.getMaximum();

				seekMusic(percent);
			}

		});

		JList<Resource> list = mainWindow.getList();

		List<Resource> resources = ebeanServer.find(Resource.class).findList();

		DefaultListModel<Resource> listModel = (DefaultListModel<Resource>) list
				.getModel();
		for (Resource resource : resources) {
			listModel.addElement(resource);
		}

		watchService.setListener(this);
		this.watchService.watch();

		// finaly show window
		mainWindow.setVisible(true);
	}

	private void seekMusic(float percent) {

		if (basicPlayer != null) {

			System.out.println("MainController.seekMusic()");

		} else {
			System.err.println("Cannot track no player");
		}

	}

	@Override
	public void valueChanged(final TreeSelectionEvent event) {

		FileTreeNode node = (FileTreeNode) event.getPath()
				.getLastPathComponent();

		File selectedFile = node.getFile();
		openFile(selectedFile);
	}

	private synchronized void openFile(final File selectedFile) {

		showInfo(selectedFile);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					System.out.println("MainController.openFile()");

					if (null != basicPlayer) {
						basicPlayer.stop();
					}

					basicPlayer = new BasicPlayer();
					basicPlayer.open(selectedFile.getAbsoluteFile());

					basicPlayer.addBasicPlayerListener(MainController.this);

					basicPlayer.play();

					setPlayState(true);

				} catch (BasicPlayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}).start();

	}

	private void setPlayState(boolean play) {
		mainWindow.getAudioPlayer().getButtonPause().setEnabled(play);
		mainWindow.getAudioPlayer().getSliderTime().setEnabled(play);
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
			System.out.println("Genre: " + id3v1Tag.getGenre() + " ("
					+ id3v1Tag.getGenreDescription() + ")");
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

	@Override
	public void opened(Object arg0, Map arg1) {

		System.out.println("MainController.opened()");
		System.out.println(arg0);
		System.out.println(arg1);

	}

	@Override
	public void progress(int arg0, long microseconds, byte[] arg2, Map arg3) {
		int deciseconds = (int) (microseconds / 1000 / 100);
		JSlider sliderTime = mainWindow.getAudioPlayer().getSliderTime();
		sliderTime.setValue(deciseconds);
	}

	@Override
	public void setController(BasicController controller) {
	}

	@Override
	public void stateUpdated(BasicPlayerEvent event) {
		System.out.println("MainController.stateUpdated()");

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == mainWindow.getAudioPlayer().getButtonPause()) {

			try {
				basicPlayer.pause();
			} catch (BasicPlayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
