package org.landa.musicoll.controllers;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

import org.landa.musicoll.App;
import org.landa.musicoll.view.components.FileTree.NodeOperation;
import org.landa.musicoll.view.components.FileTreeNode;
import org.landa.musicoll.view.components.NodeOperationListener;

public class NodeOperationController implements NodeOperationListener {

    @Override
    public void nodeOperationPerform(final FileTreeNode fileTreeNode, final DefaultTreeModel model, final NodeOperation nodeOperation) {

        switch (nodeOperation) {
        case RENAME:
            rename(fileTreeNode, model);
            break;

        default:
            JOptionPane.showMessageDialog(null, "Nincs még kész");
            break;
        }

    }

    private void rename(final FileTreeNode fileTreeNode, final DefaultTreeModel model) {

        final String newName = JOptionPane.showInputDialog("Új név", fileTreeNode.getFile().getName());

        final File origFile = fileTreeNode.getFile();
        final File newFile = new File(origFile.getParentFile().getAbsoluteFile().getAbsolutePath() + File.separator + newName);

        try {
            origFile.renameTo(newFile);

            fileTreeNode.setUserObject(newName);
            model.nodeChanged(fileTreeNode);

        } catch (final Exception exception) {
            App.LOGGER.error("Cannot rename file " + fileTreeNode, exception);
            JOptionPane.showMessageDialog(null, "Hiba az átnevezés közben", "Hiba", JOptionPane.OK_OPTION);
        }
    }
}
