package org.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.Client.GUI.AdministrationPanelListener;
import org.Client.GUI.CategoryPanel;
import org.Client.GUI.LoginPanel;
import org.Client.GUI.LoginPanelListener;
import org.Client.GUI.CategoryPanelListener;
import org.Client.GUI.QuestionPanelListener;
import org.Server.Packet;

public class MainWindow extends JFrame implements ClientListener, LoginPanelListener,
	CategoryPanelListener, AdministrationPanelListener, QuestionPanelListener {
	private static final long serialVersionUID = 1L;
	private Client client;
	private LoginPanel lp;
	private CategoryPanel categoryPanel;
	public MainWindow(){		
		super("Frame");
		 lp = new LoginPanel(this);
		 
		client = new TCPConnection(this, "127.0.0.1", 12345);
		add(lp);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void recieveClientData(Packet p) {
		System.out.println(p.getUsername());
		System.out.println(p.getLoginStatus());
		
		lp.setVisible(false);
		remove(lp);
		categoryPanel = new CategoryPanel(this);
		add(categoryPanel);
		pack();
		int[] test = {1,2,3};
		String[] temp = {"test", "2", "3"};
		categoryPanel.setCategories(temp, test, test);
		pack();
	}

	@Override
	public void exceptionInClientData(TCPClientException e) {
		JOptionPane.showMessageDialog(this, e.getMessage() + "\n" + e.getCause().getMessage());
		lp.enableLoginButton();
	}
	

	@Override
	public void login(String username, String password) {
		// TODO Auto-generated method stub
		Packet p = new Packet(username, password , Packet.Type.CLIENT);
		try {
			client.sendPacket(p);
		} catch (TCPClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void categorySelected(int category, int level, int modus) {
		// TODO Auto-generated method stub
		
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
