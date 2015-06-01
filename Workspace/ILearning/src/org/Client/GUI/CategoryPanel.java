package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
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
	private DefaultListModel<String> categoryListModel;

	 
	/**
	 * The constructor builds all widgets of the panel and registers the ActionListeners.
	 * @param listener	class to callback
	 */
	public CategoryPanel(final CategoryPanelListener listener) {
		this.listener = listener;
		this.categoryListModel = new DefaultListModel();
		this.categoryListBox = new JList<String>(categoryListModel);
		this.changeButton = new JButton();		// TODO: Set button label
		this.editCategoryTextField = new JTextField();
		this.editCategoryLabel = new JLabel();	// TODO: Set label text
		this.submitButton = new JButton("Absenden");
		this.levelComboBox = new JComboBox<String>();
		this.modusComboBox = new JComboBox<String>();
		this.downloadButton = new JButton("Fragen herunterladen");
		this.printQuestionsButton = new JButton("Fragen ausdrucken");	
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		
		JPanel west = new JPanel();
		JPanel south = new JPanel();
		JPanel north = new JPanel();
		west.add(categoryListBox);
		north.add(levelComboBox);
		south.add(submitButton);
		pan.add(west,"West");
		pan.add(north, "North");
		pan.add(south, "South");
		add(pan);
		
		
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				listener.categorySelected(categoryListBox.getSelectedValue().toString(),levelComboBox.getSelectedItem().toString() , 0);
			}
		});
		
		
	}
	
	
	/**
	 * Sets the provided parameters to the GUI
	 * @param categories
	 * @param level
	 * @param modus
	 */
	public void setCategories(String[] categories, String[] level,int[] modus)
	{
		for(String s:categories){
			categoryListModel.addElement(s);			
		}	
		for(String s:level){
			levelComboBox.addItem(s);
		}
	}
	
}
