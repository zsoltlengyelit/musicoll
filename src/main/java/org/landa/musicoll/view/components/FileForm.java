package org.landa.musicoll.view.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class FileForm extends JPanel {

	public FileForm() {

		super();

		MigLayout migLayout = new MigLayout();
		setLayout(migLayout);

		String[] labels = { "Name", "Fax", "Email", "Address" };
		int numPairs = labels.length;

		for (String label : labels) {

			JLabel jLabel = new JLabel(label);
			add(jLabel);
			JTextField jTextField = new JTextField(25);
			add(jTextField, "growx, wrap");

		}

		setVisible(true);

	}

}
