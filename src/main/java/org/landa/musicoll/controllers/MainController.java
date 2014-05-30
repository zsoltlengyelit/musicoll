package org.landa.musicoll.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
import com.mpatric.mp3agic.Mp3File;

public class MainController implements TreeSelectionListener, FileSystemListener {

	public static final List<String> SUPPORTED_TYPE = Collections.unmodifiableList(Arrays.asList("mp3", "flac", "ogg", "wav", "mp4", "wma", "mov", "flv", "avi", "vid"));

	private final EbeanServer ebeanServer;

	private final FileSystemWatchService watchService;

	private final MainWindow mainWindow;

	private Mp3File mp3file;

	private final Injector injector;

	private final Map<String, FileForm> tabMap = new HashMap<String, FileForm>();

	private final FilePlaceResolver filePlaceResolver;

	private final ResourceDataModel resourceDataModel;

	private final NodeOperationController nodeOperationController;

	@Inject
	public MainController(final EbeanServer ebeanServer, FileSystemWatchService watchService, MainWindow mainWindow, Injector injector, FilePlaceResolver filePlaceResolver,
	        ResourceDataModel resourceDataModel, NodeOperationController nodeOperationController) {
		this.ebeanServer = ebeanServer;
		this.watchService = watchService;
		this.mainWindow = mainWindow;
		this.injector = injector;
		this.filePlaceResolver = filePlaceResolver;
		this.resourceDataModel = resourceDataModel;
		this.nodeOperationController = nodeOperationController;

	}

	public void start(Runnable afterStart) {

		FileTree fileTree = mainWindow.getFileTree();
		JTree tree = fileTree.getTree();
		tree.addTreeSelectionListener(this);

		fileTree.setNodeOperationListener(nodeOperationController);

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

		afterStart.run();
	}

	@Override
	public void valueChanged(final TreeSelectionEvent event) {

		FileTreeNode node = (FileTreeNode) event.getPath().getLastPathComponent();

		File selectedFile = node.getFile();

		if (isSupported(selectedFile.getName())) {
			openFile(selectedFile);
		}
	}

	public static boolean isSupported(String name) {

		String[] parts = name.split("\\.");
		String ext = parts[parts.length - 1].toLowerCase();

		return SUPPORTED_TYPE.contains(ext);
	}

	public static boolean isMp3(String name) {
		if (name.contains(".")) {
			String[] parts = name.split("\\.");
			String ext = parts[parts.length - 1];

			return "mp3".equalsIgnoreCase(ext);
		}

		return false;

	}

	private synchronized void openFile(final File selectedFile) {

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
		openFile(file);
	}

}
