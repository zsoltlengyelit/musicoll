package org.landa.musicoll.view.components;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileTreeNode extends DefaultMutableTreeNode {

    private final File file;

    public FileTreeNode(final Object userObject, final boolean allowsChildren, final File file) {
        super(userObject, allowsChildren);
        this.file = file;
    }

    public FileTreeNode(final Object userObject, final File file) {
        super(userObject);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public boolean isLeaf() {
        return !file.isDirectory();
    }

}
