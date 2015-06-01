package org.Client.GUI;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class EditQuestionPanel extends QuestionPanel {
	private static final long serialVersionUID = 1L;
	private JTextField questionTextField;
	private JTextField[] answerTextFields;
	private JTextField mediaURLTextField;
	private JLabel mediaURLLabel;
	
	public EditQuestionPanel(QuestionPanelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
	}
}
