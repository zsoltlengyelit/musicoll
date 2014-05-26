package org.landa.musicoll.view.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.landa.musicoll.controllers.FileFormController;

/**
 * 
 * @author Zsolti
 * 
 */
public class FileForm extends JPanel {

	private final File selectedFile;
	private final JTextField titleText;
	private final JTextField artText;
	private final JTextField instrumentText;
	private final JTextField regionText;
	private final JTextField placeText;
	private final JCheckBox transCheck;
	private final JTextField artistText;
	private final JTextField collectorText;
	private final JTextField collectionTimeText;
	private final JTextArea noteText;
	private final JButton saveButton;
	private final TabTitlePanel tabTitlePanel;

	public FileForm(File selectedFile, FileFormController fileFormController,
			TabTitlePanel tabTitlePanel) {
		super();
		this.selectedFile = selectedFile;
		this.tabTitlePanel = tabTitlePanel;

		setLayout(new BoxLayout(this, WIDTH));

		JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		saveButton = new JButton("Mentés");
		menuPanel.add(saveButton);
		menuPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.BLACK));

		add(menuPanel, "span, grow, wrap");

		JPanel formsPanel = new JPanel();
		formsPanel.setLayout(new GridLayout(0, 2));
		add(formsPanel);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new MigLayout());
		leftPanel.setBorder(BorderFactory.createTitledBorder("Alap adatok"));
		// Lsz., kezdősor/cím, műfaj, hangszer, tájegység, helység, előadó,
		// feldolgozás, gyűjtő, gyüjtés ideje,
		// jegyzet

		JLabel titleLabel = createLabel("Kezdősor/cím");
		leftPanel.add(titleLabel);
		titleText = createTextField();
		leftPanel.add(titleText, "span, grow, wrap");

		JLabel artLabel = createLabel("Műfaj");
		leftPanel.add(artLabel);
		artText = createTextField();
		leftPanel.add(artText, "span, grow, wrap");

		JLabel instrumentLabel = createLabel("Hangszer");
		leftPanel.add(instrumentLabel);
		instrumentText = createTextField();
		leftPanel.add(instrumentText, "span, grow, wrap");

		JLabel regionLabel = createLabel("Tájegység");
		leftPanel.add(regionLabel);
		regionText = createTextField();
		leftPanel.add(regionText, "span, grow, wrap");

		JLabel placeLabel = createLabel("Helység");
		leftPanel.add(placeLabel);
		placeText = createTextField();
		leftPanel.add(placeText, "span, grow, wrap");

		// right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new MigLayout());
		rightPanel.setBorder(BorderFactory
				.createTitledBorder("Gyűjtési adatok"));

		JLabel transLabel = createLabel("Feldolgozás");
		rightPanel.add(transLabel);
		transCheck = new JCheckBox();
		rightPanel.add(transCheck, "span, wrap");

		JLabel artistLabel = createLabel("Előadó");
		rightPanel.add(artistLabel);
		artistText = createTextField();
		rightPanel.add(artistText, "span, grow, wrap");

		JLabel collectorLabel = createLabel("Gyűjtő");
		rightPanel.add(collectorLabel);
		collectorText = createTextField();
		rightPanel.add(collectorText, "span, grow, wrap");

		JLabel collectionTimeLabel = createLabel("Gyűjtés ideje");
		rightPanel.add(collectionTimeLabel);
		collectionTimeText = createTextField(15);
		rightPanel.add(collectionTimeText, "span, grow, wrap");

		JLabel noteLabel = createLabel("Jegyzet");
		rightPanel.add(noteLabel);
		noteText = new JTextArea(50, 35);
		noteText.setFont(new Font("Arial", 0, 12));

		JScrollPane noteJScrollPane = new JScrollPane(noteText);
		rightPanel.add(noteJScrollPane, "span, grow, wrap");

		formsPanel.add(leftPanel);
		formsPanel.add(rightPanel);

		fileFormController.attach(this, selectedFile);

		setVisible(true);

	}

	public TabTitlePanel getTabTitlePanel() {
		return tabTitlePanel;
	}

	private JLabel createLabel(String value) {
		return new JLabel(value + ':');
	}

	private JTextField createTextField() {
		return createTextField(45);
	}

	private JTextField createTextField(int size) {
		JTextField jTextField = new JTextField(size);
		return jTextField;
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public JTextField getTitleText() {
		return titleText;
	}

	public JTextField getArtText() {
		return artText;
	}

	public JTextField getInstrumentText() {
		return instrumentText;
	}

	public JTextField getRegionText() {
		return regionText;
	}

	public JTextField getPlaceText() {
		return placeText;
	}

	public JCheckBox getTransCheck() {
		return transCheck;
	}

	public JTextField getArtistText() {
		return artistText;
	}

	public JTextField getCollectorText() {
		return collectorText;
	}

	public JTextField getCollectionTimeText() {
		return collectionTimeText;
	}

	public JTextArea getNoteText() {
		return noteText;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

}
