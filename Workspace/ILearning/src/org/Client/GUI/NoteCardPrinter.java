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
	 * builds a String Array containing QuestionText and Answers.
	 *
	 * @param questionText
	 * @param answerText
	 */
	private String[] buildTextArray(String questionText, String [] answerText) {
		// TODO: Convert Packet to String-Array!
		
		//log.severe("packetToArray not yet implemented!");
		String[] testPacket = new String[] { "Question",
				questionText,
				answerText[0], answerText[1], answerText[2],answerText[3]};
		return testPacket;
	}

	/**
	 * prints a set of questions
	 * @param questionText
	 * @param answerText
	 */
	public NoteCardPrinter(String questionText, String [] answerText) {
		log.info("NoteCardPrinter initialized.");
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(new PrintTemplate(buildTextArray(questionText,answerText)));
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
