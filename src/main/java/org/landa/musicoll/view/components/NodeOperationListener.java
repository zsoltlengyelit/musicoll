package org.landa.musicoll.view.components;

import java.io.File;

import org.landa.musicoll.view.components.FileTree.NodeOperation;

public interface NodeOperationListener {

	void nodeOperationPerform(File file, NodeOperation nodeOperation);

}
