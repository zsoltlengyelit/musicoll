package org.landa.musicoll.controllers.player;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * A Swing-based audio player program. NOTE: Can play only WAVE (*.wav) file.
 * 
 * @author www.codejava.net
 * 
 */
public class SwingAudioPlayer extends JPanel {

	private final JLabel labelFileName = new JLabel("Playing File:");
	private final JLabel labelTimeCounter = new JLabel("00:00:00");
	private final JLabel labelDuration = new JLabel("00:00:00");

	private final JButton buttonPlay = new JButton("Play");
	private final JButton buttonPause = new JButton("Pause");

	private final JSlider sliderTime = new JSlider(0, 1000);

	public SwingAudioPlayer() {
		super();
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.WEST;

		buttonPlay.setFont(new Font("Sans", Font.BOLD, 14));
		// buttonPlay.setIcon(iconPlay);
		buttonPlay.setEnabled(false);

		buttonPause.setFont(new Font("Sans", Font.BOLD, 14));
		// buttonPause.setIcon(iconPause);
		buttonPause.setEnabled(false);

		labelTimeCounter.setFont(new Font("Sans", Font.BOLD, 12));
		labelDuration.setFont(new Font("Sans", Font.BOLD, 12));

		sliderTime.setPreferredSize(new Dimension(400, 20));
		sliderTime.setEnabled(false);
		sliderTime.setValue(0);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		add(labelFileName, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		add(labelTimeCounter, constraints);

		constraints.gridx = 1;
		add(sliderTime, constraints);

		constraints.gridx = 2;
		add(labelDuration, constraints);

		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,
				5));
		panelButtons.add(buttonPlay);
		panelButtons.add(buttonPause);

		constraints.gridwidth = 3;
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(panelButtons, constraints);

		setVisible(true);
	}

	public JSlider getSliderTime() {
		return sliderTime;
	}

	public JLabel getLabelTimeCounter() {
		return labelTimeCounter;
	}

	public JLabel getLabelDuration() {
		return labelDuration;
	}

	public JButton getButtonPlay() {
		return buttonPlay;
	}

	public JButton getButtonPause() {
		return buttonPause;
	}

}