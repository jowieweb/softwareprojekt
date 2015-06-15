package org.Client.GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * Formats a given 2D String Array
 * @author Lukas Stuckstette
 */
public class PrintTemplate implements Printable {
	private String[] data;

	/**
	 * The constructor sets the data to print.
	 * @param data data to print
	 */
	public PrintTemplate(String[] data) {
		this.data = data;
	}

	/**
	 * Prints the data.
	 * @param g
	 * @param pf
	 * @param page
	 */
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {

		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());

		int yt = 0; // temorary y, will be counted up
		// title dynamic
		int charlauf = 0;
		while (charlauf < data[0].length()) {
			if (charlauf + 70 < data[0].length()) {
				g.drawString(String.valueOf(data[0].subSequence(charlauf,
						charlauf + 70)), 110, 120 + yt);
				yt += 15;
			} else {
				g.drawString(
						String.valueOf(data[0].subSequence(charlauf,
								data[0].length())), 110, 120 + yt);
			}
			charlauf += 70;

		}

		// questiontext dynamic
		charlauf = 0;
		while (charlauf < data[1].length()) {
			if (charlauf + 70 < data[1].length()) {
				g.drawString(String.valueOf(data[1].subSequence(charlauf,
						charlauf + 70)), 110, 145 + yt);
				yt += 15;
			} else {
				g.drawString(
						String.valueOf(data[1].subSequence(charlauf,
								data[1].length())), 110, 145 + yt);
			}
			charlauf += 70;

		}
		// g.drawString(data[1], 110, 145 + yt);

		// choise A
		int yt2 = 0;
		charlauf = 0;
		while (charlauf < data[2].length()) {
			if (charlauf + 27 < data[2].length()) {
				g.drawString(String.valueOf(data[2].subSequence(charlauf,
						charlauf + 27)), 120, 225 + yt + yt2);
				yt2 += 15;
			} else {
				g.drawString(
						String.valueOf(data[2].subSequence(charlauf,
								data[2].length())), 120, 225 + yt + yt2);
			}
			charlauf += 27;
		}
		// choise C
		charlauf = 0;
		int yt3 = 0;
		while (charlauf < data[4].length()) {
			if (charlauf + 27 < data[4].length()) {
				g.drawString(String.valueOf(data[4].subSequence(charlauf,
						charlauf + 27)), 320, 225 + yt + yt3);
				yt3 += 15;
				if (yt3 > yt2) {
					yt2 = yt3;
				}
			} else {
				g.drawString(
						String.valueOf(data[4].subSequence(charlauf,
								data[4].length())), 320, 225 + yt + yt3);
			}
			charlauf += 27;
		}

		charlauf = 0;
		int yt4 = 0;
		while (charlauf < data[3].length()) {
			if (charlauf + 27 < data[3].length()) {
				g.drawString(String.valueOf(data[3].subSequence(charlauf,
						charlauf + 27)), 120, 325 + yt + yt2 + yt4);
				yt4 += 15;
			} else {
				g.drawString(
						String.valueOf(data[3].subSequence(charlauf,
								data[3].length())), 120, 325 + yt + yt2 + yt4);
			}
			charlauf += 27;
		}

		charlauf = 0;
		int yt5 = 0;
		while(charlauf < data[5].length()){
			if(charlauf + 27 < data[5].length()){
				g.drawString(String.valueOf(data[5].subSequence(charlauf,
						charlauf + 27)), 320, 325 + yt + yt2 + yt5);
				yt5+=15;
				if(yt5 > yt4){
					yt4 = yt5;
				}
			} else {
				g.drawString(String.valueOf(data[5].subSequence(charlauf,
						data[5].length())), 320, 325 + yt + yt2 + yt5);
			}
			charlauf += 27;
		}
		//g.drawString(data[3], 120, 325 + yt + yt2 + yt4);

		//g.drawString(data[5], 320, 325 + yt + yt2 + yt4);

		// frame of the answer choise
		g.drawRect(110, 210 + yt, 180, 80 + yt2);
		g.drawRect(310, 210 + yt, 180, 80 + yt2);

		g.drawRect(110, 310 + yt + yt2, 180, 80 + yt4);
		g.drawRect(310, 310 + yt + yt2, 180, 80 + yt4);

		// outter frame
		g.drawRect(100, 100, 400, 300 + yt + yt2 + yt4);
		// tell the caller that this page is part
		// of the printed document
		return PAGE_EXISTS;
	}
}
