package org.landa.musicoll.view.components;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    private JButton scanButton;

    public MenuPanel() {

        buildPane();

        setVisible(true);
    }

    private void buildPane() {

        scanButton = new JButton("Scan");

        // setLayout(new HorizBagLayout());
        add(scanButton);
    }

    public JButton getScanButton() {
        return scanButton;
    }

}
