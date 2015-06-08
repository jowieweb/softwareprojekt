package org.Client.GUI;

/**
 * Defines callback methods used by the AdministrationPanel.
 */
public interface AdministrationPanelListener {
	public void removeUser(String username);
	public void updateUser(String username, String password);
	public void addUser(String username, String password);
	public void changeAdministrationPanelToCategoryPanel();
}
