package org.landa.musicoll.controllers;

import java.io.File;

import javax.swing.JOptionPane;

import org.landa.musicoll.App;
import org.landa.musicoll.view.components.FileTree.NodeOperation;
import org.landa.musicoll.view.components.NodeOperationListener;

public class NodeOperationController implements NodeOperationListener {

	@Override
	public void nodeOperationPerform(File file, NodeOperation nodeOperation) {

		switch (nodeOperation) {
		case RENAME:
			rename(file);
			break;

		default:
			break;
		}

	}

	private void rename(File file) {

		String newName = JOptionPane.showInputDialog("Új név", file.getName());

		File newFile = new File(file.getParentFile().getAbsoluteFile().getAbsolutePath() + File.separator + newName);

		try {
			file.renameTo(newFile);
		} catch (Exception exception) {
			App.LOGGER.error("Cannot rename file " + file, exception);
			JOptionPane.showMessageDialog(null, "Hiba az átnevezés közben", "Hiba", JOptionPane.OK_OPTION);
		}
	}
}
