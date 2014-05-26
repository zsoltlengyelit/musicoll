package org.landa.musicoll.view.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.landa.musicoll.controllers.MainController;

/**
 * 
 * @author Zsolti
 * 
 */
public class TabTitlePanel extends JPanel implements MouseListener {

	private final JLabel titleLbl;
	private boolean saved = true;
	private final MainController mainController;
	private final String relativePath;

	public TabTitlePanel(String title, final MainController mainController,
			final String relativePath) {
		super(new BorderLayout());
		this.mainController = mainController;
		this.relativePath = relativePath;

		setOpaque(false);
		titleLbl = new JLabel(title);
		titleLbl.setFont(new Font("Arial", Font.PLAIN, 11));
		titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		add(titleLbl, BorderLayout.CENTER);
		JButton closeButton = new JButton("x");
		closeButton.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		closeButton.setFont(new Font("Arial", Font.BOLD, 10));

		closeButton.addMouseListener(this);
		add(closeButton, BorderLayout.EAST);
	}

	public void setUnsaved() {
		this.saved = false;
		titleLbl.setFont(new Font("Arial", Font.BOLD, 11));
	}

	public void setSaved() {
		this.saved = true;
		titleLbl.setFont(new Font("Arial", Font.PLAIN, 11));
	}

	private void close() {

		if (!saved) {

			int dialogResult = JOptionPane.showConfirmDialog(this,
					"Bezárod mentés nélkül?", "Megerősítés",
					JOptionPane.OK_CANCEL_OPTION);

			if (dialogResult == JOptionPane.YES_OPTION) {
				mainController.removeTab(relativePath);
			}
		} else {
			mainController.removeTab(relativePath);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.close();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
