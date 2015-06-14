package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
	private JPasswordField passwordtextField;

	/**
	 * The constructor builds all widgets of the panel and registers the
	 * ActionListeners.
	 *
	 * @param listener
	 *            class to callback
	 */
	public LoginPanel(final LoginPanelListener listener) {
		this.listener = listener;
		this.nameLabel = new JLabel("Benutzername:");
		this.passwordLabel = new JLabel("Passwort:");
		this.loginButton = new JButton("Einloggen");
		this.useLocalButton = new JButton("Offline arbeiten");
		this.nameTextField = new JTextField(8);
		this.passwordtextField = new JPasswordField(8);

		passwordtextField.setText("1");
		nameTextField.setText("klaus");
		JPanel pan = new JPanel();
		JPanel north = new JPanel();
		JPanel south = new JPanel();
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				login();
			}

		});
		passwordtextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				login();
			}

		});

		nameTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				login();
			}

		});
		
		useLocalButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				listener.useLocal();
			}
		});

		pan.setLayout(new BorderLayout());
		north.add(nameLabel);
		north.add(nameTextField);
		north.add(passwordLabel);
		north.add(passwordtextField);

		south.add(useLocalButton);
		south.add(loginButton);

		pan.add(north, "North");

		pan.add(south, "South");
		this.add(pan);
	}

	/**
	 * This function enables the loginButton. Should be called to allow a second
	 * login attempt.
	 */
	public void enableLoginButton() {
		if (loginButton != null) {
			loginButton.setEnabled(true);
		}
	}

	/**
	 * This function gets the username and the password from the textfield and
	 * calls the listener
	 */
	private void login() {
		if (loginButton != null) {
			loginButton.setEnabled(false);
			listener.login(nameTextField.getText(), new String(
					passwordtextField.getPassword()));
		}
	}

}
