package org.Client.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class LoginPanel represents an JPanel with login forms. *
 */

public class LoginPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LoginPanelListener listener;
	private JLabel nameLabel;
	private JLabel passwordLabel;
	private JButton loginButton;
	private JButton useLocalButton;
	private JTextField nameTextField;
	private JTextField passwordtextField;
	
	/**
	 * The constructor builds all widgets of the panel and registers the ActionListeners.
	 * @param listener	class to callback
	 */
	
	public LoginPanel(LoginPanelListener listener) {
		this.listener = listener;
		this.nameLabel = new JLabel("Benutzername:");
		this.passwordLabel = new JLabel("Passwort:");
		this.loginButton = new JButton("Einloggen");
		this.useLocalButton = new JButton("Offline arbeiten");
		this.nameTextField = new JTextField();
		this.passwordtextField = new JTextField();
	}

}
