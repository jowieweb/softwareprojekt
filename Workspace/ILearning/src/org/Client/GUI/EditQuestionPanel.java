package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class EditQuestionPanel represents a QuestionPanel with editable JTextFields to edit questions.
 */
public class EditQuestionPanel extends QuestionPanel {
	private static final long serialVersionUID = 1L;
	private JTextField questionTextField;
	private JTextField[] answerTextFields;
	private JTextField mediaURLTextField;
	private JButton abortButton;
	private JLabel mediaURLLabel;
	
	// Hold old question values so old question can be restored on abort
	private String backupQuestionText;
	private String[] backupAnswersText;
	private String backupMediaURL;
	
	/**
	 * The constructor creates the panel.
	 * @param listener callback method object
	 */
	public EditQuestionPanel(QuestionPanelListener listener) {
		super(listener);
		this.questionTextField = new JTextField();
		this.answerTextFields = new JTextField[4];
		this.backupAnswersText = new String[4];
		
		for (int i = 0; i < 4; i++) {
			answerTextFields[i] = new JTextField(50);
		}
		
		this.mediaURLTextField = new JTextField(40);
		this.mediaURLLabel = new JLabel("Hier könnte Ihre URL stehen:");
		
		this.abortButton = new JButton("Abbrechen");
		this.abortButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				abortButtonClicked();
			}
		});

		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());

		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JPanel north = new JPanel();
		JPanel URLPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		URLPanel.add(mediaURLLabel);
		URLPanel.add(mediaURLTextField);
		buttonPanel.add(abortButton);
		buttonPanel.add(submitButton);
		
		north.setLayout(new GridLayout(2, 1));
		north.add(questionTextField);
		north.add(URLPanel);
		south.add(buttonPanel);
		
		center.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		
		for (int i = 0; i < 4; i++) {
			JPanel p = new JPanel();
			p.add(answerButton[i]);
			p.add(answerTextFields[i]);
			
			center.add(p, gbc);
			gbc.gridy++;
		}

		pan.add(center, "Center");
		pan.add(north, "North");
		pan.add(south, "South");
		add(pan);
	}
	
	/**
	 * Is invoked when abortButton is clicked (eventhandler).
	 */
	private void abortButtonClicked() {
		resetValues();
		listener.changeQuestionPanelToAnswerMode();	
	}
	
	/**
	 * Resets all Values to standard values.
	 */
	private void resetValues() {
		this.questionTextField.setText(this.backupQuestionText);
		this.mediaURLTextField.setText(this.backupMediaURL);
		
		for (int i = 0; i < 4; i++) {
			this.answerTextFields[i].setText(this.backupAnswersText[i]);
		}
	}
	
	/**
	 * Returns the currently displayed answers.
	 * @return answers
	 */
	public String[] getAnswerTexts() {
		String[] answers = new String[4];
		
		for (int i = 0; i < 4; i++) {
			answers[i] = this.answerTextFields[i].getText();
		}
		
		return answers;
	}
	
	/**
	 * Returns the currently displayed question.
	 * @return question
	 */
	public String getQuestionText() {
		return this.questionTextField.getText();
	}
	
	/**
	 * Sets the answers.
	 * @param answers answers
	 */
	public void setAnswerText(String[] answers) {
		if (answers != null) {
			for (int i = 0; i < answers.length && i < 4; i++) {
				this.answerTextFields[i].setText(answers[i]);
				this.backupAnswersText[i] = answers[i];
			}
		}
	}
	
	/**
	 * Sets the question text.
	 * @param question qeustion text
	 */
	public void setQuestionText(String question) {
		this.questionTextField.setText(question);
		this.backupQuestionText = question;
	}
	
	/**
	 * Set JRadioButton to selected.
	 * @param answer index of the radiobutton to be selected
	 */
	public void setSelectedAnswer(int answer) {
		if (answer < 4 && answer >= 0) {
			this.answerButton[answer].setSelected(true);
		}
	}
	
	/**
	 * Return selected answer.
	 * @return selected answer or -1 if no answer is selected
	 */
	public int getSelectedAnswer() {
		for (int i = 0; i < 4; i++) {
			if (this.answerButton[i].isSelected()) {
				return i;
			}
		}
	
		return -1;
	}
}
