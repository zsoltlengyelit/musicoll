package org.landa.musicoll.view.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

/**
 * Display a file system in a JTree view
 * 
 * @version $Id: FileTree.java,v 1.9 2004/02/23 03:39:22 ian Exp $
 * @author Ian Darwin
 */
public class FileTree extends JPanel implements TreeWillExpandListener {

	private final JTree tree;

	/** Construct a FileTree */
	public FileTree(final File dir) {
		setLayout(new BorderLayout());

		// Make a tree list with all the nodes, and make it a JTree
		tree = new JTree(initTree(dir));

		tree.addTreeWillExpandListener(this);

		// Lastly, put the JTree into a JScrollPane.
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(tree);
		add(BorderLayout.CENTER, scrollpane);

	}

	private FileTreeNode initTree(final File dir) {

		FileTreeNode rootNode = new FileTreeNode(dir.getAbsoluteFile(), dir);

		addNodes(rootNode);

		return rootNode;
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(200, 400);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 400);
	}

	public JTree getTree() {
		return tree;
	}

	@Override
	public void treeWillCollapse(final TreeExpansionEvent event) throws ExpandVetoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void treeWillExpand(final TreeExpansionEvent event) throws ExpandVetoException {
		FileTreeNode treeNode = (FileTreeNode) event.getPath().getLastPathComponent();

		addNodes(treeNode);

	}

	private void addNodes(final FileTreeNode rootNode) {

		File dir = rootNode.getFile();

		rootNode.removeAllChildren();

		Vector ol = new Vector();
		String[] tmp = dir.list();
		for (int i = 0; i < tmp.length; i++) {
			ol.addElement(tmp[i]);
		}
		Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
		File f;
		Vector files = new Vector();
		// Make two passes, one for Dirs and one for Files. This is #1.
		for (int i = 0; i < ol.size(); i++) {
			String thisObject = (String) ol.elementAt(i);
			String newPath;
			if (dir.getPath().equals(".")) {
				newPath = thisObject;
			} else {
				newPath = dir.getPath() + File.separator + thisObject;
			}
			if ((f = new File(newPath)).isDirectory()) {

				FileTreeNode dirNode = new FileTreeNode(thisObject, f);
				dirNode.setAllowsChildren(true);
				rootNode.add(dirNode);

			} else {
				files.addElement(thisObject);
			}
		}
		// Pass two: for files.
		for (int fnum = 0; fnum < files.size(); fnum++) {
			String file = (String) files.elementAt(fnum);
			rootNode.add(new FileTreeNode(file, new File(dir.getPath() + File.separator + file)));
		}

		if (null != tree) {
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			model.reload(root);
		}

	}

}
