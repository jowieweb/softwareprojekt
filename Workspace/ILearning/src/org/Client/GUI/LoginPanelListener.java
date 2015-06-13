package org.Client.GUI;

/**
 * Defines a callback method used by the LoginPanel.
 */
public interface LoginPanelListener {
	public void login(String username, String password);
	public void useLocal();
}
