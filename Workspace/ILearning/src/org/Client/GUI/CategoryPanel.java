package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.Packet;

/**
 * The class CategoryPanel represents a JPanel that displays all available categories.
 */
@SuppressWarnings("unused")
public class CategoryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private CategoryPanelListener listener;
	private DefaultListModel<String> categoryListModel;
	private JList<String> categoryListBox;
	private JButton printQuestionsButton;
	private JButton deleteCategoryButton;
	private JButton quitEditModeButton;
	private JButton newCategoryButton;
	private JButton downloadButton;
	private JButton submitButton;
	private JTextField editCategoryTextField;
	private JLabel editCategoryLabel;
	private JComboBox<String> levelComboBox;
	private JComboBox<String> modusComboBox;
	private ArrayList<String[]> categories;
	private boolean editMode;

	/**
	 * The constructor builds all widgets of the panel and registers the ActionListeners.
	 * @param listener	class to callback
	 */
	public CategoryPanel(final CategoryPanelListener listener) {
		this.listener = listener;
		this.editMode = false;

		createPanelContent();

		JPanel pan = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel editPanel = new JPanel();
		pan.setLayout(new BorderLayout());
		buttonPanel.add(newCategoryButton);
		buttonPanel.add(deleteCategoryButton);
		
		buttonPanel.add(quitEditModeButton);
		buttonPanel.add(submitButton);
		editPanel.add(editCategoryLabel);
		editPanel.add(editCategoryTextField);

		JPanel west = new JPanel();
		JPanel south = new JPanel();
		JPanel north = new JPanel();
		west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));
		west.add(categoryListBox);
		west.add(editPanel);
		north.add(levelComboBox);
		south.add(buttonPanel);
		pan.add(west, BorderLayout.CENTER);
		pan.add(north, "North");
		pan.add(south, "South");
		add(pan);
		

		addButtonActionListeners();
		this.categoryListBox.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (editMode) {
					setEditTextFieldToCurrentSelectedValue();
				}
			}
		});
	}

	/**
	 * Asks the user for acknowledgement for the removing of the category.
	 * If he answers yes, the category is removed by invoking the callback method.
	 */
	private void removeCategory() {
		String oldCategory = this.categoryListBox.getSelectedValue().toString();
		
		int result = JOptionPane.showConfirmDialog(this, "Möchten Sie die Kategorie '"
				+ oldCategory + "' endgültig entfernen?", "Kategorie entfernen",
				JOptionPane.YES_NO_OPTION);
		
		if (result == JOptionPane.YES_OPTION) {
			this.categoryListModel.removeElement(this.categoryListBox.getSelectedValue());
			setEditTextFieldToCurrentSelectedValue();
			this.listener.categoryRemoved(oldCategory);
		}
	}
	
	/**
	 * Sets the text of editCategoryTextField to the current selected value in categoryListBox.
	 */
	private void setEditTextFieldToCurrentSelectedValue() {
		String selectedValue = this.categoryListBox.getSelectedValue();
		if (selectedValue != null) {
			this.editCategoryTextField.setText(selectedValue);
		} else {
			this.editCategoryTextField.setText("");
		}
	}

	/**
	 * Creates a new category and focuses it.
	 */
	private void createNewCategory() {
		String newCategory = new String("Neue Kategorie");
		this.categoryListModel.addElement(newCategory);
		this.categoryListBox.setSelectedValue(newCategory, true);
		setEditTextFieldToCurrentSelectedValue();
	}

	/**
	 * Sets the provided parameters to the GUI
	 * @param categories
	 * @param level
	 * @param modus
	 */
	public void setCategories(ArrayList<String[]> categories, String[] level, int[] modus) {
		if (categories == null || level == null || modus == null) {
			return;
		}
		
		if (categoryListModel.isEmpty() == false) {
			categoryListModel.clear();
		}
		
		this.categories = categories;

		for(String[] s : categories) {
			if (s != null && s.length > 1) {
				categoryListModel.addElement(s[1]);
			}
		}

		for(String s : level){
			levelComboBox.addItem(s);
		}
	}
	
	/**
	 * Sets the category edit mode.
	 * @param edit
	 */
	public void setEditMode(boolean edit) {
		this.newCategoryButton.setVisible(edit);
		this.deleteCategoryButton.setVisible(edit);
		this.editCategoryLabel.setVisible(edit);
		this.editCategoryTextField.setVisible(edit);
		this.quitEditModeButton.setVisible(edit);
		this.editMode = edit;
		setEditTextFieldToCurrentSelectedValue();
	}

	/**
	 * Submits the changes and invokes callback methods categoryAdded() or categoryUpdated().
	 */
	private void submitChanges() {
		String oldCategory = this.categoryListBox.getSelectedValue();
		String newCategory = this.editCategoryTextField.getText();

		if (oldCategory != null && this.categoryListBox.getSelectedIndex() != -1) {
			if (newCategory != null && !newCategory.equals("")) {

				// rename element in list
				int index = this.categoryListModel.indexOf(oldCategory);
				if (index > -1) {
					this.categoryListModel.remove(index);
					this.categoryListModel.add(index, newCategory);
				}
				
				// invoke callback methods
				if (oldCategory.equals("Neue Kategorie") && !newCategory.equals("Neue Kategorie")) {
					
					this.listener.categoryAdded(newCategory);
				} else {
					
					// Search category id
					for (String[] n : categories) {
						if (n != null && n[1].equals(oldCategory)) {
							
							this.listener.categoryUpdated(n[0], oldCategory, newCategory);
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Adds ActionListeners to buttons.
	 * @param listener
	 */
	private void addButtonActionListeners() {
		this.submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editMode) {
					submitChanges();
				} else {					
					listener.categorySelected(categoryListBox.getSelectedValue().toString(),
							levelComboBox.getSelectedItem().toString(), 0);
				}
			}
		});
		this.newCategoryButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createNewCategory();
			}
		});
		this.deleteCategoryButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCategory();
			}
		});
		this.quitEditModeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listener.disableEditMode();
			}
		});
	}
	
	/**
	 * Creates panel content.
	 */
	private void createPanelContent() {
		this.categoryListModel = new DefaultListModel<String>();
		this.categoryListBox = new JList<String>(categoryListModel);
		
		this.submitButton = new JButton("Absenden");
		this.downloadButton = new JButton("Fragen herunterladen");
		this.newCategoryButton = new JButton("Neue Kategorie anlegen");
		this.quitEditModeButton = new JButton("Bearbeiten beenden");
		this.deleteCategoryButton = new JButton("Kategorie entfernen");
		this.printQuestionsButton = new JButton("Fragen ausdrucken");
		this.editCategoryTextField = new JTextField();
		this.editCategoryLabel = new JLabel("Kategorie bearbeiten:");
		this.levelComboBox = new JComboBox<String>();
		this.modusComboBox = new JComboBox<String>();
		
		this.newCategoryButton.setVisible(this.editMode);
		this.deleteCategoryButton.setVisible(this.editMode);
		this.editCategoryLabel.setVisible(this.editMode);
		this.editCategoryTextField.setVisible(this.editMode);
		this.quitEditModeButton.setVisible(this.editMode);
		
		this.editCategoryTextField.setPreferredSize(new Dimension(180, 25));
	}
}
