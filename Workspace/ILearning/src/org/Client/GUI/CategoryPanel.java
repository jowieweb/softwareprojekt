package org.Client.GUI;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CategoryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JList<String> categoryListBox;
	private JButton changeButton;
	private JTextField editCategoryTextField;
	private JLabel editCategoryLabel;
	private JButton submitButton;
	private JComboBox<String> levelComboBox;
	private JComboBox<String> modusComboBox;
	private JButton downloadButton;
	private JButton printQuestionsButton;
	private CategoryPanelListener listener;
	
	public CategoryPanel(CategoryPanelListener listener) {
		this.listener = listener;
		this.categoryListBox = new JList<String>();
		this.changeButton = new JButton();		// TODO: Set button label
		this.editCategoryTextField = new JTextField();
		this.editCategoryLabel = new JLabel();	// TODO: Set label text
		this.submitButton = new JButton("Absenden");
		this.levelComboBox = new JComboBox<String>();
		this.modusComboBox = new JComboBox<String>();
		this.downloadButton = new JButton("Fragen herunterladen");
		this.printQuestionsButton = new JButton("Fragen ausdrucken");		
	}
	
	
}
