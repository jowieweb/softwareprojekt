package org.Client.GUI;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Logger;

import org.Packet;

/**
 * NoteCardPrinter.
 * prints questions in card format
 * @author Lukas Stuckstette
 */
public class NoteCardPrinter {

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	/**
	 * converts a package into a string[]
	 *
	 * @param p package
	 */
	private String[] packetToArray(Packet p) {
		// TODO: Convert Packet to String-Array!
		log.severe("packetToArray not yet implemented!");
		String[] testPacket = new String[] { "Question",
				p.getQuestion(),
				p.getAnswers()[0], p.getAnswers()[1], p.getAnswers()[2],p.getAnswers()[3]};
		return testPacket;
	}


	/**
	 * prints a set of questions
	 * @param p the package containing the questions
	 */
	public NoteCardPrinter(Packet p) {
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
}
