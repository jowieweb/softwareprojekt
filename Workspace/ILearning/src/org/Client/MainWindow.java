package org.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.Client.GUI.AdministrationPanelListener;
import org.Client.GUI.LoginPanelListener;
import org.Client.GUI.CategoryPanelListener;
import org.Server.Packet;

public class MainWindow extends JFrame implements ClientListener, LoginPanelListener, CategoryPanelListener, AdministrationPanelListener {
	private static final long serialVersionUID = 1L;
	private Client client;
	private JButton but;
	
	public MainWindow(){
		super("Frame");
		client = new TCPConnection(this, "127.0.0.1", 12345);
		JPanel pan = new JPanel();
		pan.add(new JLabel("Test"));
		but = new JButton("login");
		but.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				but.setEnabled(false);
				Packet p = new Packet("steven", "1" , Packet.Type.CLIENT);
				try {
					client.sendPacket(p);
				} catch (TCPClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		pan.add(but);
		add(pan);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void recieveClientData(Packet p) {
		System.out.println(p.getUsername());
		System.out.println(p.getLoginStatus());
		done();
	}

	@Override
	public void exceptionInClientData(TCPClientException e) {
		JOptionPane.showMessageDialog(this, e.getMessage() + "\n" + e.getCause().getMessage());
		done();
	}
	
	private void done(){
		but.setEnabled(true);
	}

	@Override
	public void login(String username, String password) {
		// TODO Auto-generated method stub
		
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

}
