package org.landa.musicoll.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.landa.musicoll.App;
import org.landa.musicoll.view.components.FileTree;
import org.landa.musicoll.view.components.FilterTable;
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

	MenuPanel menuPanel;

	File basePath;

	private JTabbedPane tabbedPane;

	private final FilterTable filterTable;

	@Inject
	public MainWindow(@Named("basePath") final File basePath,
			final MenuPanel menuPanel, FilterTable filterTable) {

		this.basePath = basePath;
		this.menuPanel = menuPanel;

		this.filterTable = filterTable;

		setLayout(new BorderLayout());
		setTitle("Musicoll");
		setIconImage(App.IMAGE);

		buildPane(getContentPane());

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void buildPane(final Container pane) {

		fileTree = new FileTree(basePath);

		pane.add(fileTree, BorderLayout.WEST);

		JScrollPane jScrollPane = new JScrollPane(filterTable);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();
		// tabbedPane.addTab("Form", fileForm);
		tabbedPane.addTab("Lista", jScrollPane);

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

	public MenuPanel getMenuPanel() {
		return menuPanel;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public FilterTable getFilterTable() {
		return filterTable;
	}

}
