package org.Client.GUI;

import javax.swing.JLabel;

/**
 * The class AnswerQuestionPanel represents a JPanel that displays a question and possible answers.
 */
public class AnswerQuestionPanel extends QuestionPanel {
	private static final long serialVersionUID = 1L;
	private JLabel questionLabel;

	public AnswerQuestionPanel () {
		super();
		this.questionLabel = new JLabel();
	}
	
	/**
	 * Sets question
	 * @param text question
	 */
	public void setQuestionText(String text) {
		this.questionLabel.setText(text);
	}
	
	/**
	 * Sets provided text to radiobuttons.
	 * @param text array with answer options
	 */
	public void setAnswerText(String[] text) {
		for (int i = 0; i < text.length && i < 4; i++) {
			this.answerButton[i].setText(text[i]);
		}
	}
	
	/**
	 * Returns all possible answers.
	 * @return all answers
	 */
	public String[] getAnswerTexts () {
		String[] answers = new String[4];
		
		for (int i = 0; i < 4; i++) {
			answers[i] = this.answerButton[i].getText();
		}
		
		return answers;
	}
}
