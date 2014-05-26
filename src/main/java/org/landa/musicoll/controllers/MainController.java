package org.landa.musicoll.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
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
import org.landa.musicoll.core.FilePlaceResolver;
import org.landa.musicoll.core.ResourceDataModel;
import org.landa.musicoll.core.watch.FileSystemListener;
import org.landa.musicoll.core.watch.FileSystemWatchService;
import org.landa.musicoll.model.Resource;
import org.landa.musicoll.view.MainWindow;
import org.landa.musicoll.view.components.FileForm;
import org.landa.musicoll.view.components.FileTree;
import org.landa.musicoll.view.components.FileTreeNode;
import org.landa.musicoll.view.components.TabTitlePanel;

import com.avaje.ebean.EbeanServer;
import com.google.inject.Inject;
import com.google.inject.Injector;
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

	private Mp3File mp3file;

	private final Injector injector;

	private final Map<String, FileForm> tabMap = new HashMap<String, FileForm>();

	private final FilePlaceResolver filePlaceResolver;

	private final ResourceDataModel resourceDataModel;

	@Inject
	public MainController(final EbeanServer ebeanServer,
			FileSystemWatchService watchService, MainWindow mainWindow,
			Injector injector, FilePlaceResolver filePlaceResolver,
			ResourceDataModel resourceDataModel) {
		this.ebeanServer = ebeanServer;
		this.watchService = watchService;
		this.mainWindow = mainWindow;
		this.injector = injector;
		this.filePlaceResolver = filePlaceResolver;
		this.resourceDataModel = resourceDataModel;

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

				// JSlider slider = (JSlider) arg0.getSource();
				// if (slider.getValueIsAdjusting()) {
				// float percent = sliderTime.getValue()
				// / sliderTime.getMaximum();
				// seekMusic(percent);
				// }
			}

		});

		mainWindow.getFilterTable().getTable()
				.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							JTable target = (JTable) e.getSource();
							int row = target.getSelectedRow();

							openSelectedLineFromTable(row);
						}
					}
				});

		watchService.setListener(this);
		this.watchService.watch();

		// finaly show window
		mainWindow.setVisible(true);
	}

	private void seekMusic(float percent) {

		if (basicPlayer != null && mp3file != null) {

			System.out.println("MainController.seekMusic()");

			try {
				basicPlayer.seek((long) (percent * mp3file.getLength()));
			} catch (BasicPlayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			System.err.println("Cannot track no player");
		}

	}

	@Override
	public void valueChanged(final TreeSelectionEvent event) {

		FileTreeNode node = (FileTreeNode) event.getPath()
				.getLastPathComponent();

		File selectedFile = node.getFile();

		if (selectedFile.getName().toLowerCase().endsWith("mp3")) {
			openFile(selectedFile);
		}
	}

	private synchronized void openFile(final File selectedFile) {

		showInfo(selectedFile);

		openTab(selectedFile);

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		//
		// System.out.println("MainController.openFile()");
		//
		// if (null != basicPlayer) {
		// basicPlayer.stop();
		// }
		//
		// basicPlayer = new BasicPlayer();
		// basicPlayer.open(selectedFile.getAbsoluteFile());
		//
		// basicPlayer.addBasicPlayerListener(MainController.this);
		//
		// basicPlayer.play();
		//
		// setPlayState(true);
		//
		// } catch (BasicPlayerException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		//
		// }).start();

	}

	private void openTab(File selectedFile) {

		String relativePath = filePlaceResolver.getRelativePath(selectedFile);
		JTabbedPane tabbedPane = mainWindow.getTabbedPane();
		FileForm fileForm;

		if (tabMap.containsKey(relativePath)) {
			fileForm = tabMap.get(relativePath);
		} else {

			TabTitlePanel tabTitlePanel = new TabTitlePanel(
					selectedFile.getName(), this, relativePath);

			fileForm = new FileForm(selectedFile,
					injector.getInstance(FileFormController.class),
					tabTitlePanel);
			fileForm.setOpaque(false);
			tabbedPane.add(fileForm);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(fileForm),
					tabTitlePanel);

			tabMap.put(relativePath, fileForm);
		}
		tabbedPane.setSelectedComponent(fileForm);
	}

	private void setPlayState(boolean play) {
		mainWindow.getAudioPlayer().getButtonPause().setEnabled(play);
		mainWindow.getAudioPlayer().getSliderTime().setEnabled(play);
	}

	private void showInfo(File selectedFile) {
		try {
			mp3file = new Mp3File(selectedFile.getAbsolutePath());

			if (null == mp3file) {
				System.out.println("MainController.showInfo(): null");
			}

			ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			if (null != id3v1Tag) {
				System.out.println("Track: " + id3v1Tag.getTrack());
				System.out.println("Artist: " + id3v1Tag.getArtist());
				System.out.println("Title: " + id3v1Tag.getTitle());
				System.out.println("Album: " + id3v1Tag.getAlbum());
				System.out.println("Year: " + id3v1Tag.getYear());
				System.out.println("Genre: " + id3v1Tag.getGenre() + " ("
						+ id3v1Tag.getGenreDescription() + ")");
				System.out.println("Comment: " + id3v1Tag.getComment());
			}
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

	public void removeTab(String relativePath) {

		FileForm fileForm = tabMap.get(relativePath);
		mainWindow.getTabbedPane().remove(fileForm);
		tabMap.remove(relativePath);
	}

	private void openSelectedLineFromTable(int index) {
		Resource resource = resourceDataModel.getData().get(index);

		File file = filePlaceResolver.getFile(resource);
		openTab(file);
	}

}
