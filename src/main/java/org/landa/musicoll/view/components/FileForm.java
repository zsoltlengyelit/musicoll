package org.landa.musicoll.view.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author Zsolti
 * 
 */
public class FileForm extends JPanel {

	public FileForm() {

		super();

		setLayout(new MigLayout());

		JPanel menuPanel = new JPanel();
		menuPanel.add(new JButton("Mentés"));
		menuPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.BLACK));
		;
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
		JTextField titleText = createTextField();
		leftPanel.add(titleText, "span, grow, wrap");

		JLabel artLabel = createLabel("Műfaj");
		leftPanel.add(artLabel);
		JTextField artText = createTextField();
		leftPanel.add(artText, "span, grow, wrap");

		JLabel instrumentLabel = createLabel("Hangszer");
		leftPanel.add(instrumentLabel);
		JTextField instrumentText = createTextField();
		leftPanel.add(instrumentText, "span, grow, wrap");

		JLabel regionLabel = createLabel("Tájegység");
		leftPanel.add(regionLabel);
		JTextField regionText = createTextField();
		leftPanel.add(regionText, "span, grow, wrap");

		JLabel placeLabel = createLabel("Helység");
		leftPanel.add(placeLabel);
		JTextField placeText = createTextField();
		leftPanel.add(placeText, "span, grow, wrap");

		// right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new MigLayout());
		rightPanel.setBorder(BorderFactory
				.createTitledBorder("Gyűjtési adatok"));

		JLabel transLabel = createLabel("Feldolgozás");
		rightPanel.add(transLabel);
		JCheckBox transCheck = new JCheckBox();
		rightPanel.add(transCheck, "span, wrap");

		JLabel artistLabel = createLabel("Előadó");
		rightPanel.add(artistLabel);
		JTextField artistText = createTextField();
		rightPanel.add(artistText, "span, grow, wrap");

		JLabel collectorLabel = createLabel("Gyűjtő");
		rightPanel.add(collectorLabel);
		JTextField collectorText = createTextField();
		rightPanel.add(collectorText, "span, grow, wrap");

		JLabel collectionTimeLabel = createLabel("Gyűjtés ideje");
		rightPanel.add(collectionTimeLabel);
		JTextField collectionTimeText = createTextField(15);
		rightPanel.add(collectionTimeText, "span, grow, wrap");

		JLabel noteLabel = createLabel("Jegyzet");
		rightPanel.add(noteLabel);
		JTextArea noteText = new JTextArea(10, 25);
		noteText.setFont(new Font("Arial", 0, 12));

		JScrollPane noteJScrollPane = new JScrollPane(noteText);
		rightPanel.add(noteJScrollPane, "span, grow, wrap");

		formsPanel.add(leftPanel);
		formsPanel.add(rightPanel);

		setVisible(true);

	}

	private JLabel createLabel(String value) {
		return new JLabel(value + ':');
	}

	private JTextField createTextField() {
		return createTextField(25);
	}

	private JTextField createTextField(int size) {
		JTextField jTextField = new JTextField(size);
		return jTextField;
	}

}
