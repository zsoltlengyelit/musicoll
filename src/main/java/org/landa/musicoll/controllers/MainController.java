package org.landa.musicoll.controllers;

import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.landa.musicoll.model.Resource;
import org.landa.musicoll.view.MainWindow;
import org.landa.musicoll.view.components.FileTreeNode;

import com.avaje.ebean.EbeanServer;
import com.google.inject.Inject;

public class MainController implements TreeSelectionListener {

    private final EbeanServer ebeanServer;
    private MainWindow mainWindow;

    @Inject
    public MainController(final EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;

    }

    public void attach(final MainWindow mainWindow) {

        this.mainWindow = mainWindow;
        mainWindow.getFileTree().getTree().addTreeSelectionListener(this);

        JList<Resource> list = mainWindow.getList();

        List<Resource> resources = ebeanServer.find(Resource.class).findList();

        DefaultListModel<Resource> listModel = (DefaultListModel<Resource>) list.getModel();
        for (Resource resource : resources) {
            listModel.addElement(resource);
        }

    }

    public void valueChanged(final TreeSelectionEvent event) {

        FileTreeNode node = (FileTreeNode) event.getPath().getLastPathComponent();

        File selectedFile = node.getFile();
        openFile(selectedFile);

    }

    private void openFile(final File selectedFile) {

    }

}
