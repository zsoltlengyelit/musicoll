package org.landa.musicoll;

import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.landa.musicoll.controllers.MainController;
import org.landa.musicoll.core.MusicollModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class App {

	public static void main(final String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		File basePath = getBasePath();

		final Injector injector = Guice.createInjector(new MusicollModule(basePath));

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				MainController mainController = injector.getInstance(MainController.class);
				mainController.start();
			}
		});

	}

	private static File getBasePath() {

		try {
			return new File("./tmp").getCanonicalFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.exit(0);
		}

		return null;

		// JFileChooser chooser = new JFileChooser();
		// chooser.setCurrentDirectory(new java.io.File("."));
		// chooser.setDialogTitle("Musicoll");
		// chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// chooser.setAcceptAllFileFilterUsed(false);
		//
		// if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		// return chooser.getSelectedFile();
		//
		// } else {
		// System.exit(0);
		// }
		//
		// return null;
	}
}
