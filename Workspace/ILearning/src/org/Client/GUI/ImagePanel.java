package org.Client.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * Class the displays an image on an panel.
 * 
 * source:http://wiki.byte-welt.net/wiki/Grafikdateien_laden_und_anzeigen_%28
 * Java%29
 *
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 3782359230949998803L;
	private Image image;
	private boolean show = true;

	/**
	 * The constructor sets the panel to invisible.
	 */
	public ImagePanel() {
		this.setVisible(false);;
	}

	/**
	 * The constructor sets an image to display.
	 * @param image
	 */
	public ImagePanel(Image image) {
		setImage(image);
	}

	/**
	 * Sets the image to display.
	 * @param image image to display
	 */
	public void setImage(Image image) {
		this.setVisible(true);
		this.image = image;
		repaint();
	}

	/**
	 * Returns the preferredSize.
	 * @return preferred size
	 */
	public Dimension getPreferredSize() {
		if (image != null && show) {
			return new Dimension(image.getWidth(this), image.getHeight(this));
		}
		return new Dimension(0,0);
	}

	/**
	 * Paints the image.
	 * @param g Graphics object
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if (image != null && show) {
			g.drawImage(image, 0, 0, this);
		}
	}
}