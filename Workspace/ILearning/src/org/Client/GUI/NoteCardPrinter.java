package org.Client.GUI;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Logger;

import org.Packet;

/*
 * NoteCardPrinter.
 * Druckt eine Frage in Karteikarten-Format.
 */
public class NoteCardPrinter {

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	/*
	 * Konvertiert ein Fragenpacket in ein Stringarray.
	 * 
	 * @param p Ein Fragenpacket
	 */
	public String[] packetToArray(Packet p) {
		// TODO: Convert Packet to String-Array!
		log.severe("packetToArray not yet implemented!");
		String[] testPacket = new String[] { "Titel der Testfrage?",
				"Fragentext. Koennte auch ein bisschen laenger sein.",
				"AntwortOption A", "AntwortOption B", "AntwortOption C,AntwortOption C,AntwortOption C,AntwortOption C,AntwortOption CAntwortOption C,AntwortOption CAntwortOption CAntwortOption CAntwortOption CAntwortOption CAntwortOption CAntwortOption CAntwortOption CAntwortOption C,AntwortOption C,AntwortOption C,AntwortOption C,",
				"AntwortOption D", };
		return testPacket;
	}

	public NoteCardPrinter() {

		log.info("NoteCardPrinter initialized.");
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(new PrintTemplate(packetToArray(null)));
		boolean doPrint = pj.printDialog();
		long timestamp = 0;
		boolean printed = false;
		if (doPrint) {
			try {
				log.info("Printer: job startet!");
				timestamp = System.currentTimeMillis();
				pj.print();
				printed = true;
			} catch (PrinterException e) {
				log.severe("Printing Error!!!!");
			}
		} else {
			log.info("Print aborted.");
		}
		if (printed) {
			log.info("Printer: done. this took: "
					+ (System.currentTimeMillis() - timestamp) + "ms.");
		} else {
			log.info("Printer: done.");
		}
	}

	public static void main(String[] args) {
		new NoteCardPrinter();
	}
}
