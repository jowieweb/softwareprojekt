package org.Client.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class AdministrationPanel represents a form thats allows users to edit user information.
 *
 */

public class AdministrationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JList<String> userList;
	private JTextField userTextField;
	private JTextField passwordTextField;
	private JButton submitButton;
	private JButton removeUserButton;
	private JLabel descriptionLabel;
	private AdministrationPanelListener listener;
	
	public AdministrationPanel(AdministrationPanelListener listener) {
		this.listener = listener;
		this.userList = new JList<String>();
		this.userTextField = new JTextField();
		this.passwordTextField = new JTextField();
		this.submitButton = new JButton("Absenden");
		this.removeUserButton = new JButton("Benutzer entfernen");
		this.descriptionLabel = new JLabel();
	}
}
