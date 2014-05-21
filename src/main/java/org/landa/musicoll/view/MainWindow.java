package org.landa.musicoll.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;

import org.landa.musicoll.controllers.player.SwingAudioPlayer;
import org.landa.musicoll.model.Resource;
import org.landa.musicoll.view.components.FileForm;
import org.landa.musicoll.view.components.FileTree;
import org.landa.musicoll.view.components.MenuPanel;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * 
 * @author lzsolt
 * 
 */
@Singleton
public class MainWindow extends JFrame {

	FileTree fileTree;

	JList<Resource> list;

	MenuPanel menuPanel;

	File basePath;

	private final SwingAudioPlayer audioPlayer;

	private final FileForm fileForm;

	@Inject
	public MainWindow(@Named("basePath") final File basePath,
			final MenuPanel menuPanel, final SwingAudioPlayer audioPlayer,
			FileForm fileForm) {

		this.fileForm = fileForm;
		this.basePath = basePath;
		this.menuPanel = menuPanel;
		this.audioPlayer = audioPlayer;

		System.out.println("MainWindow.MainWindow()");

		setLayout(new BorderLayout());
		setTitle("Musicoll");

		buildPane(getContentPane());

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void buildPane(final Container pane) {

		fileTree = new FileTree(basePath);

		pane.add(fileTree, BorderLayout.WEST);

		list = new JList<Resource>(new DefaultListModel<Resource>());
		list.setCellRenderer(new ListCellRenderer<Resource>() {

			@Override
			public Component getListCellRendererComponent(
					final JList<? extends Resource> list, final Resource value,
					final int index, final boolean isSelected,
					final boolean cellHasFocus) {
				return new JLabel(value.getRelativePath());
			}
		});

		JScrollPane jScrollPane = new JScrollPane(list);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Form", fileForm);
		tabbedPane.addTab("List", jScrollPane);

		contentPanel.add(audioPlayer, BorderLayout.NORTH);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				fileTree, contentPanel);
		// splitPane.setPreferredSize(new Dimension(arg0, arg1));

		pane.add(splitPane, BorderLayout.CENTER);
		pane.add(menuPanel, BorderLayout.NORTH);

	}

	public FileTree getFileTree() {
		return fileTree;
	}

	public JList<Resource> getList() {
		return list;
	}

	public MenuPanel getMenuPanel() {
		return menuPanel;
	}

	public SwingAudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

}
