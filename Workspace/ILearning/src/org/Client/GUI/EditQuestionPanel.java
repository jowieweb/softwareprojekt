package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditQuestionPanel extends QuestionPanel {
	private static final long serialVersionUID = 1L;
	private JTextField questionTextField;
	private JTextField[] answerTextFields;
	private JTextField mediaURLTextField;
	private JLabel mediaURLLabel;
	
	public EditQuestionPanel(QuestionPanelListener listener) {
		super(listener);
		this.questionTextField = new JTextField();
		this.answerTextFields = new JTextField[4];
		
		for (int i = 0; i < 4; i++) {
			answerTextFields[i] = new JTextField();
			answerTextFields[i].setMinimumSize(new Dimension(100, 20));
		}
		
		this.mediaURLTextField = new JTextField();
		this.mediaURLLabel = new JLabel("Hier könnte Ihre URL stehen!");
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());

		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JPanel north = new JPanel();
		JPanel URLPanel = new JPanel();
		north.setLayout(new GridLayout(2, 1));
		north.add(questionTextField);
		URLPanel.add(mediaURLLabel);
		URLPanel.add(mediaURLTextField);
		north.add(URLPanel);
		south.add(submitButton);
		
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
}
