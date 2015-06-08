package org.Client.GUI;

/**
 * Defines callback methods used by the CategoryPanel.
 */
public interface CategoryPanelListener {
	public void categorySelected(String category, String level, int modus);
	public void categoryAdded(String newCategory);
}
