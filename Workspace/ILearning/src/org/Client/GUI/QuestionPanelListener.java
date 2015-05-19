package org.Client.GUI;

public interface QuestionPanelListener {
	public void answerSelected(int answer);
	public void questionAdded(String questionText, String[] answers, String mediaURL);
}
