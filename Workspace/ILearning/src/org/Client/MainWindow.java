﻿package org.Client;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.Client.GUI.AdministrationPanel;
import org.Client.GUI.AdministrationPanelListener;
import org.Client.GUI.AnswerQuestionPanel;
import org.Client.GUI.CategoryPanel;
import org.Client.GUI.EditQuestionPanel;
import org.Client.GUI.LoginPanel;
import org.Client.GUI.LoginPanelListener;
import org.Client.GUI.CategoryPanelListener;
import org.Client.GUI.QuestionPanel;
import org.Client.GUI.QuestionPanelListener;
import org.Packet;

/**
 * The class MainWindow represents the main window, which display different panels
 * depending on the current state.
 */
public class MainWindow extends JFrame implements ClientListener, LoginPanelListener,
	CategoryPanelListener, AdministrationPanelListener, QuestionPanelListener {
	private static final long serialVersionUID = 1L;
	private Client client;
	private LoginPanel loginPanel;
	private CategoryPanel categoryPanel;
	private QuestionPanel questionPanel;
	private AdministrationPanel adminPanel;
	private JMenuBar menuBar;
	private JMenuItem editMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem userMenuItem;
	private JMenu editMenu;
	private JMenu fileMenu;
	private JMenu helpMenu;
	
	private Packet lastPacket;
	
	private String username;
	private String password;
	
	/**
	 * constructor creates window.
	 */
	public MainWindow(){		
		super("Frame");
		loginPanel = new LoginPanel(this);
		adminPanel = new AdministrationPanel(this);
		menuBar = new JMenuBar();
		editMenu = new JMenu("Bearbeiten");
		fileMenu = new JMenu("Datei");
		helpMenu = new JMenu("Hilfe");
		
		aboutMenuItem = new JMenuItem("Über..");
		userMenuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = -358338731196690668L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePanelToAdministrationPanel();
				
			}
		});
		exitMenuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = -2684501250646388101L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(NORMAL);
			}
		});
		editMenuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeQuestionPanelToEditMode();
			}
		});
		
		exitMenuItem.setText("Beenden");
		editMenuItem.setText("Bearbeiten");
		userMenuItem.setText("Nutzerverwaltung anzeigen");
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		fileMenu.add(exitMenuItem);
		editMenu.add(editMenuItem);
		editMenu.add(userMenuItem);
		helpMenu.add(aboutMenuItem);
		
		setJMenuBar(menuBar);
		
		editMenuItem.setVisible(false);
		client = new TCPConnection(this, "127.0.0.1", 12345);
		add(loginPanel);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setMinimumSize(getSize());
	}

	/**
	 * Is invoked when a packet is received.
	 * @param p the received packet
	 */
	@Override
	public void receiveClientData(Packet p) {
		if (p == null) {
			return;
		}
		
		switch (p.getPacketType()) {
		case CATEGORY:
			System.out.println(p.getUsername());
			System.out.println(p.getLoginStatus());
			username = p.getUsername();
			password = p.getPassword();
			loginPanel.setVisible(false);
			remove(loginPanel);
			categoryPanel = new CategoryPanel(this);
			add(categoryPanel);
			
			int[] test = {1,2,3};	// TODO: remove after testing
			categoryPanel.setCategories(p.getTopics(), p.getLevel(), test);
			break;
			
		case ANSWER_QUESTION:
			remove(categoryPanel);
			if (questionPanel != null) {
				questionPanel.setVisible(false);
				remove(questionPanel);
				
				//TODO: lassen wir das so?
				if(p.getWasRight()){
					JOptionPane.showMessageDialog(this,"Die Frage wurde richtig beantwortet");
					
				}
				else{
					JOptionPane.showMessageDialog(this,"Die Frage wurde FALSCH beantwortet");
					
				} 
				
				System.out.println(p.getWasRight());
			}
			else
			{
			}
			questionPanel = new AnswerQuestionPanel(this, p.getAnswers());
			add(questionPanel);
			editMenuItem.setVisible(true);
			questionPanel.setQuestionText(p.getFrage());
			((AnswerQuestionPanel)questionPanel).setPicture(p.getImage());
			
//			questionPanel.setAnswerText(p.getAnswers());
			break;
			
		default:
			break;
		}

		pack();
//		setMinimumSize(getSize());
	}

	@Override
	public void exceptionInClientData(TCPClientException e) {
		JOptionPane.showMessageDialog(this, e.getMessage() + "\n" + e.getCause().getMessage());
		loginPanel.enableLoginButton();
	}
	
	/**
	 * Callbackmethod invoked when submitButton on loginPanel is pressed.
	 * @param username the username
	 * @param password the user's password
	 */
	@Override
	public void login(String username, String password) {
		Packet p = new Packet(username, password);
		p.setPacketType(Packet.Type.CATEGORY);
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Callbackmethod is invoked when submitButton on categoryPanel is pressed.
	 * @param category the selected category
	 * @param level the selected level
	 * @param modus the selected modus
	 */
	@Override
	public void categorySelected(String category, String level, int modus) {
		Packet p = new Packet(username,password);
		p.setPacketType(Packet.Type.ANSWER_QUESTION);
		
		p.setSelectedTopic(category);
		p.setSelectedLevel(level);
		lastPacket = p;
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void categoryAdded(String newCategory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUser(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Callbackmethod invoked when submitButton on questionPanel is pressed.
	 * @param answer the index of the selected answer
	 */
	@Override
	public void answerSelected(int[] answer) {
		Packet p = new Packet(username, password);
		p.setPacketType(Packet.Type.ANSWER_QUESTION);
		p.setFrage(questionPanel.getQuestionText());
		p.setTopics(lastPacket.getTopics());
		p.setSelectedTopic(lastPacket.getSelectedTopic());
		p.setLevel(p.getLevel());
		
		p.setSelectedAnswer(answer);		
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void questionAdded(String questionText, String[] answers,
			String mediaURL) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Replaces AnswerQuestionPanel with EditQuestionPanel.
	 */
	public void changeQuestionPanelToEditMode() {
		String[] answers = questionPanel.getAnswerTexts();
		String question = questionPanel.getQuestionText();
		
		remove(questionPanel);
		questionPanel = new EditQuestionPanel(this);
		questionPanel.setAnswerText(answers);
		questionPanel.setQuestionText(question);
		add(questionPanel);
		pack();
	}
	
	/**
	 * Replaces EditQuestionPanel with AnswerQuestionPanel.
	 */
	public void changeQuestionPanelToAnswerMode() {
		String[] answers = questionPanel.getAnswerTexts();
		String question = questionPanel.getQuestionText();
		
		remove(questionPanel);
		questionPanel = new AnswerQuestionPanel(this, answers);
		questionPanel.setQuestionText(question);
		add(questionPanel);
		pack();
	}
	
	/**
	 * Replaces QuestionPanel with CategoryPanel.
	 */
	public void changeQuestionPanelToCategoryPanel() {
		remove(questionPanel);
		questionPanel = null;
		add(categoryPanel);
		pack();
		// TODO: Reload category list
	}
	
	/**
	 * Replaces current panel with adminPanel.
	 */
	private void changePanelToAdministrationPanel() {
		if (questionPanel != null) {
			remove(questionPanel);
		}
		if (categoryPanel != null) {	
			remove(categoryPanel);
		}
		if (loginPanel != null) {
			remove(loginPanel);
		}
		
		userMenuItem.setVisible(false);
		add(adminPanel);
		pack();
	}

	@Override
	public void changeAdministrationPanelToCategoryPanel() {
		// TODO Auto-generated method stub
		
	}
}
