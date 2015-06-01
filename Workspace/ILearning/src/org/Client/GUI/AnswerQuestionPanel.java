package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

/**
 * The class AnswerQuestionPanel represents a JPanel that displays a question
 * and possible answers.
 */
public class AnswerQuestionPanel extends QuestionPanel {
	private static final long serialVersionUID = 1L;
	private JLabel questionLabel;
	private ImagePanel picturePanel;
	

	/**
	 * constructor builds JPanel.
	 * @param listener callback method object
	 * @param text
	 */
	public AnswerQuestionPanel(QuestionPanelListener listener, String[] text) {
		super(listener);
		this.questionLabel = new JLabel();

		this.picturePanel = new ImagePanel();
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());

		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JPanel north = new JPanel();
		north.setLayout(new GridLayout(2, 1));
		north.add(questionLabel);
		north.add(picturePanel);
		
		south.add(submitButton);
		
		center.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		
		setAnswerText(text);
		for(JRadioButton radio: answerButton){
			center.add(getRadioLabel(radio),gbc);
			gbc.gridy++;
		}
		pan.add(center, "Center");
		pan.add(north, "North");
		pan.add(south, "South");
		add(pan);
	}

	/**
	 * Sets question
	 * 
	 * @param text
	 *            question
	 */
	public void setQuestionText(String text) {
		this.questionLabel.setText(text);
		questionLabel.setFont (questionLabel.getFont ().deriveFont (24.0f));
	}

	/**
	 * Sets provided text to radiobuttons.
	 * 
	 * @param text
	 *            array with answer options
	 */
	public void setAnswerText(String[] text) {
		for (int i = 0; i < text.length && i < 4; i++) {
			this.answerButton[i].setText( text[i]);
		}
	}

	/**
	 * Returns all possible answers.
	 * 
	 * @return all answers
	 */
	public String[] getAnswerTexts() {
		String[] answers = new String[4];

		for (int i = 0; i < 4; i++) {
			answers[i] = this.answerButton[i].getText();
		}

		return answers;
	}

	/**
	 * Returns the question.
	 * 
	 * @return question
	 */
	public String getQuestionText() {
		return questionLabel.getText();
	}
	
	/**
	 * 
	 * @param pic
	 */
	public void setPicture(Image pic)
	{
		picturePanel = new ImagePanel(pic);
		picturePanel.setVisible(true);
		repaint();
	}
}
