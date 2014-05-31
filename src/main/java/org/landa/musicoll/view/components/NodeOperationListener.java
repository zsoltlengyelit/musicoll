package org.landa.musicoll.view.components;

import javax.swing.tree.DefaultTreeModel;

import org.landa.musicoll.view.components.FileTree.NodeOperation;

public interface NodeOperationListener {

    void nodeOperationPerform(FileTreeNode fileTreeNode, DefaultTreeModel treeModel, NodeOperation nodeOperation);

}
