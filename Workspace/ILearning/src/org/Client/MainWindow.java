package org.Client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker.StateValue;

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
import org.Client.GUI.NoteCardPrinter;
import org.Client.GUI.QuestionPanel;
import org.Client.GUI.QuestionPanelListener;

/**
 * The class MainWindow represents the main window, which display different
 * panels depending on the current state.
 */
public class MainWindow extends JFrame implements ClientListener,
		LoginPanelListener, CategoryPanelListener, AdministrationPanelListener,
		QuestionPanelListener {
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
	private JMenuItem showHighscoreItem;
	private JMenu questionModeMenu;
	private JMenu editMenu;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private MakeSound ms;

	private Packet lastPacket;

	private String username;
	private String password;

	private int questionCount = 0;
	private String selectedQuestionMode = "";

	/**
	 * constructor creates window.
	 */
	public MainWindow() {
		super("iLearning");
		adminPanel = new AdministrationPanel(this);
		loginPanel = new LoginPanel(this);
		menuBar = new JMenuBar();
		questionModeMenu = new JMenu("Fragenmodus");
		editMenu = new JMenu("Bearbeiten");
		fileMenu = new JMenu("Datei");
		helpMenu = new JMenu("Hilfe");

		questionModeMenu.setVisible(false);
		createMenuItems();

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(questionModeMenu);
		menuBar.add(helpMenu);
		fileMenu.add(showHighscoreItem);
		fileMenu.add(showCategoryItem);
		fileMenu.add(downloadDB);
		fileMenu.add(exitMenuItem);

		editMenu.add(editMenuItem);
		editMenu.add(userMenuItem);
		editMenu.add(editCategoryItem);
		editMenu.add(quitEditModeItem);
		helpMenu.add(aboutMenuItem);

		editMenu.setEnabled(false);
		
		setJMenuBar(menuBar);
		add(loginPanel);
		pack();
		setVisible(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		System.out.println("width: " + width);
		System.out.println("height: " + height);
		this.setSize(width, height);
		this.setPreferredSize(new Dimension(width, height));
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//new HTMLVideoFrame("http://olliswelt.de/rickroll.mp4");
		//new HTMLVideoFrame("http://www1.wdr.de/radio/digitalradio/webradio142_v-TeaserAufmacher.jpg");
	}

	/**
	 * Is invoked when a packet is received.
	 * 
	 * @param p
	 *            the received packet
	 */
	public void receiveClientData(Packet p) {
		if (p == null) {
			return;
		}

		lastPacket = p;
		
		switch (p.getPacketType()) {
		case CATEGORY:
			if (p.getLoginStatus() == Packet.Login.FAIL) {
				JOptionPane.showMessageDialog(this, "Fehler beim Login!", "Login fehlgeschlagen",
						JOptionPane.WARNING_MESSAGE);
			} else {
				this.showHighscoreItem.setVisible(true);
				System.out.println(p.getUsername());
				System.out.println(p.getLoginStatus());
				username = p.getUsername();
				password = p.getPassword();
	
				loginPanel.setVisible(false);
				remove(loginPanel);
				if (questionPanel != null) {
					remove(questionPanel);
				}
				if (categoryPanel != null) {
					remove(categoryPanel);
				}
				if (loginPanel != null) {
					remove(loginPanel);
				}
				categoryPanel = new CategoryPanel(this);
				add(categoryPanel);
	
				// enable user-edit-mode if user has admin rights
				if (p.getLoginStatus() == Packet.Login.ADMIN) {
					editMenu.setEnabled(true);
					userMenuItem.setVisible(true);
					editCategoryItem.setVisible(true);
				}
	
				int[] test = { 1, 2, 3 };
				categoryPanel.setCategories(p.getCategories(), p.getLevel(), test);
			}
			break;

		case ANSWER_QUESTION:
			remove(categoryPanel);
			editCategoryItem.setVisible(false);
			if (questionPanel != null) {
				questionPanel.setVisible(false);
				remove(questionPanel);

				if (p.getWasRight()) {
					JOptionPane.showMessageDialog(this,
							"Die Frage wurde richtig beantwortet");

				} else {
				
					
					JOptionPane.showMessageDialog(this,
							"Die Frage wurde FALSCH beantwortet\n" +p.getLink());

				}
				if(!p.getGotRightQuestion()){
					JOptionPane.showMessageDialog(this, "Es wurden alle Frage mit dieser Kategorie / schwierigkeit in den letzen 3 min benatwortet\n Die Fragen werden jetzt zuf�llig ausgew�hlt");
				}

				String[][] score = p.getUserScore();
				if (score != null) {
					
					for (int i = 0; i < score.length; i++) {
						System.out.println(score[i][0] + " " + score[i][1]);
					}
				}
				questionCount++;
				if (questionCount == 10) {
					questionCount = 0;
					ms = new MakeSound("haishort.wav");
					ms.execute();
				}
			}
			questionPanel = new AnswerQuestionPanel(this, p.getAnswers());

			add(questionPanel);
			editMenuItem.setVisible(true);

			showCategoryItem.setVisible(true);
			questionPanel.setQuestionText(p.getQuestion());

			((AnswerQuestionPanel) questionPanel).setPicture(p.getImage());
			questionPanel.setQuestionID(p.getQuestionID());
			((AnswerQuestionPanel) questionPanel).setVideo(p.getMediaURL());

			break;
		case USER_MANAGEMENT:

			changePanelToAdministrationPanel(p);
			adminPanel.addUsers(p);

			break;
		case DUMP_DB:
			String dump = p.getQuestion();
			if (dump.length() > 10) {
				LocalConnection asd = new LocalConnection(this);
				asd.createLocalDatabase(p);
			}
			break;
		case PRINT:
			Packet[] all = p.getAllPackets();
			NoteCardPrinter ncp = new NoteCardPrinter();
			for(int i = 0;i<all.length;i++){
				ncp.print(all[i].getQuestion(), all[i].getAnswers());				
			}
			break;
		default:
			break;
		}

		pack();
	}

	/**
	 * Callback method that is invoked by an exception.
	 * 
	 * @param e
	 *            exception
	 */
	public void exceptionInClientData(TCPClientException e) {
		JOptionPane.showMessageDialog(this, e.getMessage() + "\n"
				+ e.getCause().getMessage());
		loginPanel.enableLoginButton();
	}

	/**
	 * Callback method invoked when submitButton on loginPanel is pressed.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the user's password
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
	 * 
	 * @param category
	 *            the selected category
	 * @param level
	 *            the selected level
	 * @param modus
	 *            the selected modus
	 */
	@Override
	public void categorySelected(String category, String level, int modus) {
		Packet p = new Packet(username, password);
		p.setPacketType(Packet.Type.ANSWER_QUESTION);

		p.setSelectedTopic(category);
		p.setSelectedLevel(level);
		p.setSelectedModus(selectedQuestionMode);
		lastPacket = p;
		

		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method invoked when a category is updated.
	 * 
	 * @param oldCategory
	 *            the category to rename
	 * @param newCategory
	 *            the new category name
	 */
	public void categoryUpdated(String id, String oldCategory,
			String newCategory) {
		if (oldCategory == null) {
			return;
		}

		Packet p = new Packet(this.username, password);
		p.setPacketType(Packet.Type.EDIT_CATEGORY);
		p.setEditCategoryType(Packet.Edit_Category_Type.UPDATE_CATEGORY);
		p.setQuestion(newCategory);
		p.setCategoryID(id);

		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
		repaint();
	}

	/**
	 * Callback method invoked when a category is removed.
	 * 
	 * @param oldCategory
	 *            category to remove
	 */
	public void categoryRemoved(String oldCategory) {
		if (oldCategory == null) {
			return;
		}

		Packet p = new Packet(this.username, password);
		p.setPacketType(Packet.Type.EDIT_CATEGORY);
		p.setEditCategoryType(Packet.Edit_Category_Type.REMOVE_CATEGORY);
		p.setQuestion(oldCategory);

		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
		repaint();
	}

	/**
	 * Callback method invoked when a category is added.
	 * 
	 * @param newCategory
	 *            category to add
	 */
	public void categoryAdded(String newCategory) {
		if (newCategory == null) {
			return;
		}

		Packet p = new Packet(this.username, password);
		p.setPacketType(Packet.Type.EDIT_CATEGORY);
		p.setEditCategoryType(Packet.Edit_Category_Type.ADD_CATEGORY);
		p.setQuestion(newCategory);

		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
		repaint();
	}

	/**
	 * Callback method invoked to disable edit mode.
	 */
	public void disableEditMode() {
		categoryPanel.setEditMode(false);
		editCategoryItem.setVisible(true);
		quitEditModeItem.setVisible(false);
		pack();
		repaint();
	}
	
	
	/**
	 * Callback method invoked to print a Category.
	 */
	public void print(String category){
		Packet p = new Packet(this.username, password);
		p.setSelectedTopic(category);
		p.setPacketType(Packet.Type.PRINT);
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method that is invoked by the AdministrationPanel when an user
	 * is removed.
	 * 
	 * @param username
	 *            username of the user to be removed
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
	 * 
	 * @param id
	 *            the id of the user
	 * @param username
	 *            the username of the user
	 * @param password
	 *            the password of the user
	 */
	public void updateUser(String id, String username, String password) {
		Packet p = new Packet(this.username, this.password);
		p.setPacketType(Packet.Type.USER_MANAGEMENT);
		p.setManagemtType(Packet.Management_Type.CHANGE_USER);
		p.setAnswers(new String[] { id, username, password });

		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method invoked by AdminstrationPanel when a user is added.
	 * 
	 * @param username
	 *            the username of the new user
	 * @param password
	 *            the password of the new user
	 */
	public void addUser(String username, String password) {
		Packet p = new Packet(this.username, this.password);
		p.setPacketType(Packet.Type.USER_MANAGEMENT);
		p.setManagemtType(Packet.Management_Type.ADD_USER);
		p.setAnswers(new String[] { username, password });

		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback method invoked when submitButton on questionPanel is pressed.
	 * 
	 * @param answer
	 *            the index of the selected answer
	 */
	public void answerSelected(int[] answer) {

		Packet p = new Packet(username, password);
		p.setPacketType(Packet.Type.ANSWER_QUESTION);
		p.setQuestion(questionPanel.getQuestionText());
		// p.setTopics(lastPacket.getTopics());
		p.setCategories(lastPacket.getCategories());
		p.setSelectedTopic(lastPacket.getSelectedTopic());
		System.out.println(lastPacket.getLevel());
		p.setSelectedLevel(lastPacket.getSelectedLevel());
		p.setSelectedModus(selectedQuestionMode);
		p.setSelectedAnswers(answer);
		//check if CountDown round
		if(ms!=null){
			if(ms.getState() ==StateValue.DONE)
			{
				p.setSelectedAnswers(new int[4]);
				System.out.println("TIMED OUT!");
				ms = null;
			}
		}	
		
		
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
		repaint();
	}

	/**
	 * Callback method invoked from EditQuestionPanel when a question is added.
	 * 
	 * @param questionText question
	 * @param answers answers 
	 * @param mediaURL url to media
	 * @param right right answers
	 */
	public void questionAdded(String questionText, String[] answers,
			String mediaURL, int[] right, String category) {
		if (questionText == null || answers == null || mediaURL == null || right == null
				|| category == null) {
			return;
		}

		Packet p = new Packet(username, password);
		p.setPacketType(Packet.Type.EDIT_QUESTION);
		p.setEditQuestionType(Packet.Edit_Question_Type.ADD_QUESTION);
		p.setQuestion(questionText);
		p.setMediaURL(mediaURL);
		p.setAnswers(answers);
		p.setSelectedAnswers(right);
		p.setSelectedTopic(category);
		
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			e.printStackTrace();
		}
		//changeQuestionPanelToAnswerMode();
		//repaint();
	}

	/**
	 * Replaces AnswerQuestionPanel with EditQuestionPanel.
	 */
	public void changeQuestionPanelToEditMode() {
		System.out.println("changeQuestionPanelToEditMode");
		editCategoryItem.setVisible(false);
		String[] answers = questionPanel.getAnswerTexts();
		String question = questionPanel.getQuestionText();
		String id = questionPanel.getQuestionID();
		remove(questionPanel);
		EditQuestionPanel panel = new EditQuestionPanel(this);
		if (lastPacket != null) {
			panel.setCategories(lastPacket.getCategories());
			panel.setLevels(lastPacket.getLevel());
		}
		questionPanel = panel;
		questionPanel.setAnswerText(answers);
		questionPanel.setQuestionText(question);
		questionPanel.setQuestionID(id);
		add(questionPanel);
		pack();

		showCategoryItem.setVisible(false);
		repaint();
	}

	/**
	 * Replaces EditQuestionPanel with AnswerQuestionPanel.
	 */
	public void changeQuestionPanelToAnswerMode() {
		System.out.println("changeQuestionPanelToAnswerMode");
		editCategoryItem.setVisible(false);
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
		//repaint();
	}

	/**
	 * Replaces QuestionPanel with CategoryPanel.
	 */
	public void changeQuestionPanelToCategoryPanel() {
		System.out.println("changeQuestionPanelToCategoryPanel");
		editCategoryItem.setVisible(true);
		remove(questionPanel);
		questionPanel = null;
		login(username, password);
		showCategoryItem.setVisible(false);
		repaint();
	}

	/**
	 * Replaces current panel with adminPanel.
	 */
	private void changePanelToAdministrationPanel(Packet p) {
		System.out.println("changePanelToAdministrationPanel");
		if (questionPanel != null) {
			remove(questionPanel);
		}
		if (categoryPanel != null) {
			remove(categoryPanel);
		}
		if (loginPanel != null) {
			remove(loginPanel);
		}
		editCategoryItem.setVisible(false);
		add(adminPanel);
		pack();
		showCategoryItem.setVisible(false);
		editCategoryItem.setVisible(false);
		userMenuItem.setVisible(false);
		repaint();
	}

	/**
	 * Replaces currently displayed AdministrationPanel with CategoryPanel.
	 */
	public void changeAdministrationPanelToCategoryPanel() {
		System.out.println("changeAdministrationPanelToCategoryPanel");
		remove(adminPanel);
		add(categoryPanel);
		editCategoryItem.setVisible(true);
		editCategoryItem.setVisible(true);
		userMenuItem.setVisible(true);
		pack();
		showCategoryItem.setVisible(false);
		repaint();
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
					e.printStackTrace();
				}
			}
		});
		this.showHighscoreItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showHighscore();
			}
		});

		this.exitMenuItem.setText("Beenden");
		this.showCategoryItem.setText("Kategorie auswählen");
		this.downloadDB.setText("Download DB");
		this.editMenuItem.setText("Frage bearbeiten");
		this.userMenuItem.setText("Nutzerverwaltung anzeigen");
		this.editCategoryItem.setText("Kategorien bearbeiten");
		this.quitEditModeItem.setText("Bearbeiten beenden");
		this.showHighscoreItem.setText("Highscore anzeigen");

		this.showCategoryItem.setVisible(false);
		this.editCategoryItem.setVisible(false);
		this.editMenuItem.setVisible(false);
		this.userMenuItem.setVisible(false);
		this.quitEditModeItem.setVisible(false);
		this.downloadDB.setVisible(false);
		this.showHighscoreItem.setVisible(false);
		
		String[] modes = new String[3];
		String[] tooltips = new String[3];
		modes[0] = "Fragen nach Themengebiet";
		tooltips[0] = "";
		modes[1] = "Errormode";
		tooltips[1] = "Alle falsch beantworteten Fragen";
		modes[2] = "Mixmode";
		tooltips[2] = "Zufällige Fragen aus allen Themengebieten";
		setQuestionMode(modes, tooltips);
	}
	
	/**
	 * Displays a highscore list.
	 */
	private void showHighscore() {
		if (lastPacket == null) {
			return;
		}

		String[][] highscore = lastPacket.getUserScore();
		String scoreString = new String();

		if (highscore == null) {
			return;
		}
		
		for (int i = 0; i < highscore.length; i++) {
			scoreString += highscore[i][0] + " : " + highscore[i][1] + "\n"; 
		}

		JOptionPane.showMessageDialog(this, scoreString, "Highscore", JOptionPane.PLAIN_MESSAGE);
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
		repaint();
	}

	/**
	 * Callback method invoked when 'use local' button is clicked.
	 */
	public void useLocal() {
		client = new LocalConnection(this);
		((LocalConnection) client).login();
	}

	/**
	 * Sets all available question modes as menu items.
	 * @param modes modes to set
	 * @param tooltips tooltips with explanation 
	 */
	public void setQuestionMode(String[] modes, String[] tooltips) {
		if (modes == null || tooltips == null || modes.length != tooltips.length) {
			return;
		}
		
		// Remove all question mode items if there are any.
		if (this.questionModeMenu.getItemCount() > 0) {
			this.questionModeMenu.removeAll();
		}

		// Add new menu items.
		for (int i = 0; i < modes.length; i++) {
			JMenuItem mode = new JMenuItem(new AbstractAction() {
				private static final long serialVersionUID = -7300493209616319595L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedQuestionMode = ((JMenuItem)arg0.getSource()).getText();
				}
			});
			mode.setText(modes[i]);
			if (!tooltips[i].equals("")) {
				mode.setToolTipText(tooltips[i]);
			}
			this.questionModeMenu.add(mode);
		}
		
		this.questionModeMenu.setVisible(true);
	}
}
