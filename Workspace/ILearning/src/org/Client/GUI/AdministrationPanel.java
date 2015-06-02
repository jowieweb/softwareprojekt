package org.Client.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.border.EtchedBorder;
import javax.swing.JPasswordField;

/**
 * The class AdministrationPanel represents a form thats allows users to edit user information.
 *
 */
public class AdministrationPanel extends JPanel {
	private final JTextField userTextField = new JTextField();
	private JPasswordField passwordTextField;
	private JList<String> userList;
	private JButton backButton;
	private JButton submitButton;
	private JButton removeUserButton;
	private JButton newUserButton;
	private AdministrationPanelListener listener;
	private JPanel oldPanel;
	
	/**
	 * The constructor builds the panel.
	 * @param listener callback method object
	 */
	public AdministrationPanel(AdministrationPanelListener l) {
		this.listener = l;
		String blub[] = {"dummy","dummy","dummy","dummy","dummy","dummy","dummy"};
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{30, 129, 62, 75, 75, 75, 75, 0};
		gridBagLayout.rowHeights = new int[]{37, 41, 59, 140, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Benutzerliste");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.SOUTH;
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		userList = new JList(blub);
		userList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_userList = new GridBagConstraints();
		gbc_userList.fill = GridBagConstraints.VERTICAL;
		gbc_userList.gridheight = 3;
		gbc_userList.gridwidth = 2;
		gbc_userList.insets = new Insets(0, 0, 5, 5);
		gbc_userList.gridx = 1;
		gbc_userList.gridy = 1;
		add(userList, gbc_userList);
		
		JLabel lblAsd = new JLabel("Benutzername");
		GridBagConstraints gbc_lblAsd = new GridBagConstraints();
		gbc_lblAsd.insets = new Insets(0, 0, 5, 5);
		gbc_lblAsd.anchor = GridBagConstraints.EAST;
		gbc_lblAsd.gridx = 3;
		gbc_lblAsd.gridy = 1;
		add(lblAsd, gbc_lblAsd);
		GridBagConstraints gbc_userTextField = new GridBagConstraints();
		gbc_userTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_userTextField.insets = new Insets(0, 0, 5, 5);
		gbc_userTextField.gridx = 4;
		gbc_userTextField.gridy = 1;
		add(userTextField, gbc_userTextField);
		userTextField.setColumns(10);
		
		JLabel lblPasswort = new JLabel("Passwort");
		GridBagConstraints gbc_lblPasswort = new GridBagConstraints();
		gbc_lblPasswort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPasswort.anchor = GridBagConstraints.EAST;
		gbc_lblPasswort.gridx = 3;
		gbc_lblPasswort.gridy = 2;
		add(lblPasswort, gbc_lblPasswort);
		
		passwordTextField = new JPasswordField();
		GridBagConstraints gbc_passwordTextField = new GridBagConstraints();
		gbc_passwordTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordTextField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordTextField.gridx = 4;
		gbc_passwordTextField.gridy = 2;
		add(passwordTextField, gbc_passwordTextField);
		passwordTextField.setColumns(10);
		
		backButton = new JButton("Zurück");
		removeUserButton = new JButton("Benutzer entfernen");
		newUserButton = new JButton("Neuen Benutzer anlegen");
		submitButton = new JButton("Absenden");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(backButton);
		buttonPanel.add(removeUserButton);
		buttonPanel.add(newUserButton);
		buttonPanel.add(submitButton);

		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener != null) {
					listener.changeAdministrationPanelToCategoryPanel();
				}
			}
		});
		removeUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO remove user
				
			}
		});
		newUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				listener.addUser(userTextField.getText(), getPasswordHash(passwordTextField.getPassword()));
			}	
		});
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO submit
				
			}
		});
		
		GridBagConstraints gbc_submitButton = new GridBagConstraints();
		gbc_submitButton.anchor = GridBagConstraints.SOUTH;
		gbc_submitButton.insets = new Insets(0, 0, 0, 5);
		gbc_submitButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_submitButton.gridx = 4;
		gbc_submitButton.gridy = 3;
		add(buttonPanel, gbc_submitButton);
	}
	
	/**
	 * Hashes a char array.
	 * @param password password to hash
	 * @return hashed password
	 */
	private String getPasswordHash(char[] password) {
		String hash = null;
		
		if (password == null) {
			return null;
		}
		
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String pw = new String(password);
			byte[] bytes = md5.digest(pw.getBytes("UTF-8"));
			hash = new String(bytes);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return hash;
	}
}
