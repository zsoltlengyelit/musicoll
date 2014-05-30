package org.landa.musicoll;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.landa.musicoll.controllers.MainController;
import org.landa.musicoll.core.MusicollModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class App {

	public static final Logger LOGGER = org.apache.log4j.Logger.getLogger(App.class);

	public static final Image IMAGE = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("icon.jpg"));

	private JDialog dialog;
	private JFrame frame;
	private JProgressBar progress;

	private JFrame splashFrame;

	public static void main(final String[] args) {

		LOGGER.info("Start app");

		try {
			new App();
		} catch (Throwable throwable) {
			LOGGER.error("Fatal error", throwable);
		}
	}

	private App() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ex) {
			LOGGER.error("Nem win rendszer", ex);
		} catch (IllegalAccessException ex) {
			LOGGER.error("Hiba", ex);
		} catch (InstantiationException ex) {
			LOGGER.error("Instantiatio error", ex);
		} catch (ClassNotFoundException ex) {
			LOGGER.error("Class not found", ex);
		}

		showSplashScreen();

		File basePath = getBasePath();

		final Injector injector = Guice.createInjector(new MusicollModule(basePath));

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				MainController mainController = injector.getInstance(MainController.class);
				mainController.start(new Runnable() {

					@Override
					public void run() {
						hideSplashScreen();
					}
				});
			}
		});

	}

	protected void hideSplashScreen() {
		dialog.setVisible(false);
		dialog.dispose();
		splashFrame.setVisible(false);
		splashFrame.dispose();
	}

	protected void showSplashScreen() {
		splashFrame = new JFrame("Musicoll");

		splashFrame.setIconImage(IMAGE);
		splashFrame.setVisible(true);
		splashFrame.setSize(0, 0);

		dialog = new JDialog(splashFrame);
		dialog.setModal(false);
		dialog.setUndecorated(true);

		dialog.setIconImage(IMAGE);

		JLabel background = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("splash.jpg")));
		background.setLayout(new BorderLayout());
		dialog.add(background);
		progress = new JProgressBar();
		progress.setIndeterminate(true);
		background.add(progress, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private static File getBasePath() {

		// try {
		// return new File("./tmp").getCanonicalFile();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		//
		// System.exit(0);
		// }
		//
		// return null;

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Musicoll");

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();

		} else {
			System.exit(0);
		}

		return null;
	}

	public static void showError(String string, Exception exception) {

		JOptionPane.showMessageDialog(null, exception.getLocalizedMessage(), string, JOptionPane.OK_OPTION);

	}
}
