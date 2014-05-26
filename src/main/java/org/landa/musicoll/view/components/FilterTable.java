package org.landa.musicoll.view.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.landa.musicoll.core.ResourceTableModel;

import com.google.inject.Inject;

public class FilterTable extends JPanel {

	private JTable table;
	private final ResourceTableModel resourceTableModel;

	@Inject
	public FilterTable(final ResourceTableModel resourceTableModel) {
		super(new BorderLayout());
		this.resourceTableModel = resourceTableModel;

		setTable(new JTable(resourceTableModel) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -851112250953628076L;

			@Override
			public Class getColumnClass(int column) {
				for (int row = 0; row < getRowCount(); row++) {
					Object o = getValueAt(row, column);

					if (o != null) {
						return o.getClass();
					}
				}

				return Object.class;
			}
		});
		getTable().setRowSelectionAllowed(true);
		ListSelectionModel selectionModel = getTable().getSelectionModel();

		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionModel.setValueIsAdjusting(false);

		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				resourceTableModel);
		getTable().setRowSorter(sorter);
		JScrollPane pane = new JScrollPane(getTable());
		add(pane, BorderLayout.CENTER);
		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Keresés");
		panel.add(label, BorderLayout.WEST);
		final JTextField filterText = new JTextField();
		panel.add(filterText, BorderLayout.CENTER);
		add(panel, BorderLayout.NORTH);

		JButton button = new JButton("Filter");
		filterText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = filterText.getText();
				if (text.length() == 0) {
					sorter.setRowFilter(null);
				} else {
					try {
						sorter.setRowFilter(RowFilter.regexFilter(text));
					} catch (PatternSyntaxException pse) {
						System.err.println("Bad regex pattern");
					}
				}
			}
		});
		add(button, BorderLayout.SOUTH);

		setVisible(true);
	}

	public ResourceTableModel getResourceTableModel() {
		return resourceTableModel;
	}

	public void addSelectionListener(ListSelectionListener listener) {
		getTable().getSelectionModel().addListSelectionListener(listener);
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

}