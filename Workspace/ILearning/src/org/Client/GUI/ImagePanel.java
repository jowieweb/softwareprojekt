package org.Client.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * 
 *source:http://wiki.byte-welt.net/wiki/Grafikdateien_laden_und_anzeigen_%28Java%29
 *
 */
public class ImagePanel extends JPanel {
	   private Image image;
	 
	   public ImagePanel()
	   {
		   this.setVisible(false);
	   }
	   
	   public ImagePanel(Image image) {
	      setImage(image);
	   }
	 
	   public void setImage(Image image) {
		   this.setVisible(true);
	      this.image = image;
	      repaint();
	   }
	 
	   public Dimension getPreferredSize() {
		   if(image != null){
			   return new Dimension(image.getWidth(this), image.getHeight(this));			   
		   }
		   return new Dimension();
	   }
	 
	   public void paint(Graphics g) {
	      super.paint(g);
	      if(image != null) {
	         g.drawImage(image, 0, 0, this);
	      }
	   }
	}