package org.Client.GUI;

public interface AdministrationPanelListener {
	public void removeUser(String username);
	public void updateUser(String username, String password);
	public void addUser(String username, String password);
}
