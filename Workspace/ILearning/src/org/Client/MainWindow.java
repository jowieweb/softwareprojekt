package org.Client;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.Packet;
import org.Client.GUI.AdministrationPanel;
import org.Client.GUI.AdministrationPanelListener;
import org.Client.GUI.AnswerQuestionPanel;
import org.Client.GUI.CategoryPanel;
import org.Client.GUI.CategoryPanelListener;
import org.Client.GUI.EditQuestionPanel;
import org.Client.GUI.LoginPanel;
import org.Client.GUI.LoginPanelListener;
import org.Client.GUI.MakeSound;
import org.Client.GUI.QuestionPanel;
import org.Client.GUI.QuestionPanelListener;

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
	private JMenuItem showCategoryItem;
	private JMenuItem downloadDB;
	private JMenuItem editCategoryItem;
	private JMenuItem quitEditModeItem;
	private JMenu editMenu;
	private JMenu fileMenu;
	private JMenu helpMenu;

	private Packet lastPacket;

	private String username;
	private String password;
	
	private int questionCount = 0;

	/**
	 * constructor creates window.
	 */
	public MainWindow(){
		super("Frame");
		adminPanel = new AdministrationPanel(this);
		loginPanel = new LoginPanel(this);
		menuBar = new JMenuBar();
		editMenu = new JMenu("Bearbeiten");
		fileMenu = new JMenu("Datei");
		helpMenu = new JMenu("Hilfe");

		createMenuItems();

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		fileMenu.add(showCategoryItem);
		fileMenu.add(downloadDB);
		fileMenu.add(exitMenuItem);

		editMenu.add(editMenuItem);
		editMenu.add(userMenuItem);
		editMenu.add(editCategoryItem);
		editMenu.add(quitEditModeItem);
		helpMenu.add(aboutMenuItem);

		setJMenuBar(menuBar);

		add(loginPanel);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Is invoked when a packet is received.
	 * @param p the received packet
	 */
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

			// enable user-edit-mode if user has admin rights
			if (p.getLoginStatus() == Packet.Login.ADMIN) {
				userMenuItem.setVisible(true);
				editCategoryItem.setVisible(true);
			}

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
				
				String[][] score = p.getUserScore();
				if(score != null){
					for(int i =0;i< score.length;i++){
						System.out.println(score[i][0] + " " + score[i][1]);
					}
				}
				questionCount ++;
				if(questionCount == 10){
					questionCount =0;
					new MakeSound("haishort.wav").execute();
				}
			}
			questionPanel = new AnswerQuestionPanel(this, p.getAnswers());
			
			add(questionPanel);
			editMenuItem.setVisible(true);

			showCategoryItem.setVisible(true);
			questionPanel.setQuestionText(p.getQuestion());
			
			((AnswerQuestionPanel)questionPanel).setPicture(p.getImage());
			questionPanel.setQuestionID(p.getQuestionID());
			((AnswerQuestionPanel)questionPanel).setVideo(p.getMediaURL());

			break;
		case  USER_MANAGEMENT:

			changePanelToAdministrationPanel(p);
			adminPanel.addUsers(p);
			System.out.println("asd");

			break;
		case DUMP_DB:
			String dump = p.getQuestion();
			if(dump.length() > 10){
				LocalConnection asd = new LocalConnection(this);
				asd.insert(p);				
			}
			break;
		default:
			break;
		}

		pack();
	}

	/**
	 * Callback method that is invoked by an exception.
	 * @param e exception
	 */
	public void exceptionInClientData(TCPClientException e) {
		JOptionPane.showMessageDialog(this, e.getMessage() + "\n" + e.getCause().getMessage());
		loginPanel.enableLoginButton();
	}

	/**
	 * Callback method invoked when submitButton on loginPanel is pressed.
	 * @param username the username
	 * @param password the user's password
	 */
	@Override
	public void login(String username, String password) {
		client = new TCPConnection(this, "127.0.0.1", 12345);
		this.downloadDB.setVisible(true);
		Packet p = new Packet(username, password);
		p.setPacketType(Packet.Type.CATEGORY);
		
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method is invoked when submitButton on categoryPanel is pressed.
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
			e.printStackTrace();
		}
	}

	/**
	 * Callback method invoked when a category is updated.
	 * @param oldCategory the category to rename
	 * @param newCategory the new category name
	 */
	public void categoryUpdated(String oldCategory, String newCategory) {
		System.out.println("Updated: " + oldCategory);
		
	}

	/**
	 * Callback method invoked when a category is removed.
	 * @param oldCategory category to remove
	 */
	public void categoryRemoved(String oldCategory) {
		System.out.println("Removed: " + oldCategory);
		// TODO: Send packet to server
	}

	/**
	 * Callback method invoked when a category is added.
	 * @param newCategory category to add
	 */
	public void categoryAdded(String newCategory) {
		System.out.println("Added: " + newCategory);
		// TODO: Send packet to server
	}
	
	/**
	 * Callback method invoked to disable edit mode.
	 */
	public void disableEditMode() {
		categoryPanel.setEditMode(false);
		editCategoryItem.setVisible(true);
		quitEditModeItem.setVisible(false);
		pack();
	}

	/**
	 * Callback method that is invoked by the AdministrationPanel when an user is removed.
	 * @param username username of the user to be removed
	 */
	public void removeUser(String username) {
		Packet p = new Packet(this.username, password);
		p.setPacketType(Packet.Type.USER_MANAGEMENT);
		p.setManagemtType(Packet.Management_Type.REMOVE_USER);
		p.setQuestion(username);
		
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method invoked by AdministrationPanel when an user is updated.
	 * @param id	the id of the user
	 * @param username	the username of the user
	 * @param password the password of the user
	 */
	public void updateUser(String id, String username, String password) {
		Packet p = new Packet(this.username, this.password);
		p.setPacketType(Packet.Type.USER_MANAGEMENT);
		p.setManagemtType(Packet.Management_Type.CHANGE_USER);
		p.setAnswers(new String[] {id,username,password});
		
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method invoked by AdminstrationPanel when a user is added.
	 * @param username the username of the new user
	 * @param password the password of the new user
	 */
	public void addUser(String username, String password) {
		Packet p = new Packet(this.username, this.password);
		p.setPacketType(Packet.Type.USER_MANAGEMENT);
		p.setManagemtType(Packet.Management_Type.ADD_USER);
		p.setAnswers(new String[] {username,password});
		
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method invoked when submitButton on questionPanel is pressed.
	 * @param answer the index of the selected answer
	 */
	public void answerSelected(int[] answer) {
		Packet p = new Packet(username, password);
		p.setPacketType(Packet.Type.ANSWER_QUESTION);
		p.setQuestion(questionPanel.getQuestionText());
		p.setTopics(lastPacket.getTopics());
		p.setSelectedTopic(lastPacket.getSelectedTopic());
		p.setLevel(p.getLevel());

		p.setSelectedAnswers(answer);
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method invoked when a question is added.
	 * @param questionText question
	 * @param answers answers
	 */
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
		String id = questionPanel.getQuestionID();
		remove(questionPanel);
		questionPanel = new EditQuestionPanel(this);
		questionPanel.setAnswerText(answers);
		questionPanel.setQuestionText(question);
		questionPanel.setQuestionID(id);
		add(questionPanel);
		pack();

		showCategoryItem.setVisible(false);
	}

	/**
	 * Replaces EditQuestionPanel with AnswerQuestionPanel.
	 */
	public void changeQuestionPanelToAnswerMode() {
		String[] answers = questionPanel.getAnswerTexts();
		String question = questionPanel.getQuestionText();
		String id = questionPanel.getQuestionID();
		remove(questionPanel);
		questionPanel = new AnswerQuestionPanel(this, answers);
		questionPanel.setQuestionText(question);
		questionPanel.setQuestionID(id);
		add(questionPanel);
		pack();

		showCategoryItem.setVisible(true);
	}

	/**
	 * Replaces QuestionPanel with CategoryPanel.
	 */
	public void changeQuestionPanelToCategoryPanel() {
		remove(questionPanel);
		questionPanel = null;
		login(username, password);
		showCategoryItem.setVisible(false);
	}

	/**
	 * Replaces current panel with adminPanel.
	 */
	private void changePanelToAdministrationPanel(Packet p) {
		if (questionPanel != null) {
			remove(questionPanel);
		}
		if (categoryPanel != null) {
			remove(categoryPanel);
		}
		if (loginPanel != null) {
			remove(loginPanel);
		}

		add(adminPanel);
		pack();
		showCategoryItem.setVisible(false);
		editCategoryItem.setVisible(false);
		userMenuItem.setVisible(false);
	}

	/**
	 * Replaces currently displayed AdministrationPanel with CategoryPanel.
	 */
	public void changeAdministrationPanelToCategoryPanel() {
		remove(adminPanel);
		add(categoryPanel);
		userMenuItem.setVisible(true);
		pack();
		showCategoryItem.setVisible(false);
	}
	
	/**
	 * Creates menu items.
	 */
	private void createMenuItems() {
		this.aboutMenuItem = new JMenuItem("Über..");
		this.userMenuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = -358338731196690668L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Packet p = new Packet(username, password);
				p.setPacketType(Packet.Type.USER_MANAGEMENT);
				
				try {
					client.sendPacket(p);
				} catch (TCPClientException e) {
					e.printStackTrace();
				}
			}
		});
		this.exitMenuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = -2684501250646388101L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(NORMAL);
			}
		});
		this.editMenuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeQuestionPanelToEditMode();
			}
		});
		this.showCategoryItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeQuestionPanelToCategoryPanel();
			}
		});
		this.editCategoryItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = -390527580915841884L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				categoryPanel.setEditMode(true);
				editCategoryItem.setVisible(false);
				quitEditModeItem.setVisible(true);
				pack();
			}
		});
		this.quitEditModeItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 6117109169911884912L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				disableEditMode();
			}
		});
		
		this.downloadDB = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Packet p = new Packet(username, password);
				p.setPacketType(Packet.Type.DUMP_DB);
				try {
					client.sendPacket(p);
				} catch (TCPClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		this.exitMenuItem.setText("Beenden");
		this.showCategoryItem.setText("Kategorie auswählen");
		this.downloadDB.setText("Download DB");
		this.editMenuItem.setText("Bearbeiten");
		this.userMenuItem.setText("Nutzerverwaltung anzeigen");
		this.editCategoryItem.setText("Kategorien bearbeiten");
		this.quitEditModeItem.setText("Bearbeiten beenden");
		
		this.showCategoryItem.setVisible(false);
		this.editCategoryItem.setVisible(false);
		this.editMenuItem.setVisible(false);
		this.userMenuItem.setVisible(false);
		this.quitEditModeItem.setVisible(false);
		this.downloadDB.setVisible(false);
	}

	/**
	 * callback for EditQuestionPanel
	 */
	public void updateQuestion(String id, String newQuestionText,
			String[] newAnswers, int[] answersChecked, String newMediaURL) {

		Packet p = new Packet(this.username, this.password);
		p.setPacketType(Packet.Type.EDIT_QUESTION);
		p.setEditQuestionType(Packet.Edit_Question_Type.UPDATE_QUESTION);
		p.setQuestionID(id);
		p.setAnswers(newAnswers);
		p.setQuestion(newQuestionText);
		p.setSelectedAnswers(answersChecked);
		p.setMediaURL(newMediaURL);
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
		changeQuestionPanelToAnswerMode();
	}

	@Override
	public void useLocal() {
		// TODO Auto-generated method stub
		client = new LocalConnection(this);
		((LocalConnection)client).login();
	}
}
