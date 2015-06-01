package org.Client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.Client.GUI.AdministrationPanelListener;
import org.Client.GUI.CategoryPanel;
import org.Client.GUI.LoginPanel;
import org.Client.GUI.LoginPanelListener;
import org.Client.GUI.CategoryPanelListener;
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
	private LoginPanel lp;
	private CategoryPanel categoryPanel;
	private String username;
	private String password;
	
	/**
	 * constructor creates window.
	 */
	public MainWindow(){		
		super("Frame");
		 lp = new LoginPanel(this);
		 
		client = new TCPConnection(this, "127.0.0.1", 12345);
		add(lp);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(getSize());
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
			lp.setVisible(false);
			remove(lp);
			categoryPanel = new CategoryPanel(this);
			add(categoryPanel);
			
			int[] test = {1,2,3};
			categoryPanel.setCategories(p.getTopics(), p.getLevel(), test);
			break;
		default:
			break;
		}

		pack();
		setMinimumSize(getSize());
	}

	@Override
	public void exceptionInClientData(TCPClientException e) {
		JOptionPane.showMessageDialog(this, e.getMessage() + "\n" + e.getCause().getMessage());
		lp.enableLoginButton();
	}
	
	/**
	 * Callbackmethod invoked when submitButton on loginPanel is pressed.
	 * @param username the username
	 * @param password the user's password
	 */
	@Override
	public void login(String username, String password) {
		Packet p = new Packet(username, password);
		p.setPacketType(Packet.Type.LOGIN);
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
		p.setPacketType(Packet.Type.CATEGORY);
		p.setSelectedTopic(category);
		p.setSelectedLevel(level);
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

	@Override
	public void answerSelected(int answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void questionAdded(String questionText, String[] answers,
			String mediaURL) {
		// TODO Auto-generated method stub
		
	}
}
