package org.Client.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 *
 * source:http://wiki.byte-welt.net/wiki/Grafikdateien_laden_und_anzeigen_%28
 * Java%29
 *
 */
public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 3782359230949998803L;
	private Image image;
	private boolean show = true;

	public ImagePanel() {
		this.setVisible(false);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				show = false;

			}
		});
	}

	public ImagePanel(Image image) {
		setImage(image);
	}

	public void setImage(Image image) {
		this.setVisible(true);
		this.image = image;
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		if (image != null && show) {
			return new Dimension(image.getWidth(this), image.getHeight(this));
		}
		return new Dimension(0,0);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image != null && show) {
			g.drawImage(image, 0, 0, this);
		}
	}
}