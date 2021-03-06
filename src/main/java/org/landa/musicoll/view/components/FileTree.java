package org.landa.musicoll.view.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.landa.musicoll.controllers.MainController;

/**
 * Display a file system in a JTree view
 * 
 * @version $Id: FileTree.java,v 1.9 2004/02/23 03:39:22 ian Exp $
 * @author Ian Darwin
 */
public class FileTree extends JPanel implements TreeWillExpandListener, TreeCellRenderer {

    public static enum NodeOperation {

        RENAME, DELETE;
    }

    private NodeOperationListener nodeOperationListener;

    private final JTree tree;
    private final File dir;
    DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

    /** Construct a FileTree */
    public FileTree(final File dir) {
        this.dir = dir;
        setLayout(new BorderLayout());

        // Make a tree list with all the nodes, and make it a JTree
        tree = new JTree(initTree(dir));

        tree.addTreeWillExpandListener(this);

        tree.setCellRenderer(this);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {

                    final TreePath path = tree.getPathForLocation(event.getX(), event.getY());

                    final Rectangle pathBounds = tree.getUI().getPathBounds(tree, path);
                    if (pathBounds != null && pathBounds.contains(event.getX(), event.getY())) {
                        final JPopupMenu menu = new JPopupMenu();
                        fillMenu(path, menu);
                        menu.show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
                    }
                }
            }

        });

        // Lastly, put the JTree into a JScrollPane.
        final JScrollPane scrollpane = new JScrollPane();
        scrollpane.getViewport().add(tree);
        add(BorderLayout.CENTER, scrollpane);

    }

    private void fillMenu(final TreePath path, final JPopupMenu menu) {

        final FileTreeNode node = (FileTreeNode) path.getLastPathComponent();
        final File file = node.getFile();
        final DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        // JMenuItem header = new JMenuItem(node.getFile().getName());
        // header.setEnabled(false);

        final JMenuItem rename = new JMenuItem("Átnevezés");
        rename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                performNodeOperation(node, treeModel, NodeOperation.RENAME);
            }

        });

        menu.add(rename);

    }

    private void performNodeOperation(final FileTreeNode node, final DefaultTreeModel treeModel, final NodeOperation nodeOperation) {
        final NodeOperationListener listener = getNodeOperationListener();
        if (null != listener) {
            listener.nodeOperationPerform(node, treeModel, nodeOperation);
        }
    }

    private FileTreeNode initTree(final File dir) {

        final FileTreeNode rootNode = new FileTreeNode(dir.getAbsoluteFile(), dir);

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
        final FileTreeNode treeNode = (FileTreeNode) event.getPath().getLastPathComponent();

        addNodes(treeNode);

    }

    private void addNodes(final FileTreeNode rootNode) {

        final File dir = rootNode.getFile();

        rootNode.removeAllChildren();

        final Vector ol = new Vector();
        final String[] tmp = dir.list();
        for (int i = 0; i < tmp.length; i++) {
            ol.addElement(tmp[i]);
        }
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        final Vector files = new Vector();
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < ol.size(); i++) {
            final String thisObject = (String) ol.elementAt(i);
            String newPath;
            if (dir.getPath().equals(".")) {
                newPath = thisObject;
            } else {
                newPath = dir.getPath() + File.separator + thisObject;
            }
            if ((f = new File(newPath)).isDirectory()) {

                final FileTreeNode dirNode = new FileTreeNode(thisObject, f);
                dirNode.setAllowsChildren(true);

                rootNode.add(dirNode);

            } else {
                files.addElement(thisObject);
            }
        }
        // Pass two: for files.
        for (int fnum = 0; fnum < files.size(); fnum++) {
            final String file = (String) files.elementAt(fnum);
            final FileTreeNode fileNode = new FileTreeNode(file, new File(dir.getPath() + File.separator + file));

            rootNode.add(fileNode);
        }

        if (null != tree) {
            final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
            // model.reload(root);
            // model.reload();
        }

    }

    public void reload() {

        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        final FileTreeNode root = (FileTreeNode) model.getRoot();
        // addNodes(root);

        model.nodeChanged(root);
        model.reload();

    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row,
            final boolean hasFocus) {

        final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value instanceof FileTreeNode) {
            final FileTreeNode node = (FileTreeNode) value;
            final File file = node.getFile();
            if (!file.isDirectory()) {

                final String fileName = file.getName();
                final boolean supported = MainController.isSupported(fileName);

                final String icon = supported ? "musicicon.png" : "empty.jpg";

                renderer.setIcon(new ImageIcon(ClassLoader.getSystemResource(icon)));

            }

        }
        return renderer;

    }

    public NodeOperationListener getNodeOperationListener() {
        return nodeOperationListener;
    }

    public void setNodeOperationListener(final NodeOperationListener nodeOperationListener) {
        this.nodeOperationListener = nodeOperationListener;
    }

}
