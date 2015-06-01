package org.Client.GUI;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Abstract class for displaying questions.
 * 
 */
public abstract class QuestionPanel extends JPanel {
	protected static final long serialVersionUID = 1L;
	protected JButton submitButton;
	protected JRadioButton[] answerButton;
	
	/**
	 * constructor creates submitButton and all answerButtons.
	 */
	public QuestionPanel() {
		this.submitButton = new JButton("Absenden");
		this.answerButton = new JRadioButton[4];
		for (int i = 0; i < 4; i++) {
			this.answerButton[i] = new JRadioButton();
		}
	}
	
	/**
	 * Returns number of the selected answer.
	 * @return selected answer
	 */
	public int getSelectedAnswer() {
		return 0;
	}
	
	/**
	 * Sets question text.
	 * @param question
	 */
	public void setQuestionText(String question) {
		
	}
	
	/**
	 * Sets answers.
	 * @param answer
	 */
	public void setAnswerText(String[] answer) {
		
	}
	
	/**
	 * Returns texts from anwers.
	 * @return answers
	 */
	public String[] getAnswerTexts() {
		return null;
	}
	
	public String getQuestionText() {
		return null;
	}
}
