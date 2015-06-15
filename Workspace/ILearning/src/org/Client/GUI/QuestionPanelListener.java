package org.Client.GUI;

/**
 * Defines callback methods for the question panel.
 */
public interface QuestionPanelListener {
	public void answerSelected(int[] answer);
	public void questionAdded(String questionText, String[] answers, String mediaURL, int[] right, String category);
	public void changeQuestionPanelToAnswerMode();
	public void changeQuestionPanelToCategoryPanel();
	public void updateQuestion(String id, String newQuestionText, String[] newAnswers, int[] answersChecked, String newMediaURL);
}
