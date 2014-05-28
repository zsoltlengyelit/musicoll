package org.landa.musicoll.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

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

public class MainController implements TreeSelectionListener, FileSystemListener {

	public static final List<String> SUPPORTED_TYPE = Collections.unmodifiableList(Arrays.asList("mp3", "flac", "ogg", "wav", "mp4"));

	private final EbeanServer ebeanServer;

	private final FileSystemWatchService watchService;

	private final MainWindow mainWindow;

	private Mp3File mp3file;

	private final Injector injector;

	private final Map<String, FileForm> tabMap = new HashMap<String, FileForm>();

	private final FilePlaceResolver filePlaceResolver;

	private final ResourceDataModel resourceDataModel;

	@Inject
	public MainController(final EbeanServer ebeanServer, FileSystemWatchService watchService, MainWindow mainWindow, Injector injector, FilePlaceResolver filePlaceResolver,
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

		mainWindow.getFilterTable().getTable().addMouseListener(new MouseAdapter() {
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

	@Override
	public void valueChanged(final TreeSelectionEvent event) {

		FileTreeNode node = (FileTreeNode) event.getPath().getLastPathComponent();

		File selectedFile = node.getFile();

		if (isSupported(selectedFile.getName())) {
			openFile(selectedFile);
		}
	}

	private boolean isSupported(String name) {

		String[] parts = name.split("\\.");
		String ext = parts[parts.length - 1].toLowerCase();

		return SUPPORTED_TYPE.contains(ext);
	}

	private synchronized void openFile(final File selectedFile) {

		showInfo(selectedFile);

		openTab(selectedFile);
	}

	private void openTab(File selectedFile) {

		String relativePath = filePlaceResolver.getRelativePath(selectedFile);
		JTabbedPane tabbedPane = mainWindow.getTabbedPane();
		FileForm fileForm;

		if (tabMap.containsKey(relativePath)) {
			fileForm = tabMap.get(relativePath);
		} else {

			TabTitlePanel tabTitlePanel = new TabTitlePanel(selectedFile.getName(), this, relativePath);

			fileForm = new FileForm(selectedFile, injector.getInstance(FileFormController.class), tabTitlePanel);
			fileForm.setOpaque(false);
			tabbedPane.add(fileForm);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(fileForm), tabTitlePanel);

			tabMap.put(relativePath, fileForm);
		}
		tabbedPane.setSelectedComponent(fileForm);
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
				System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
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
