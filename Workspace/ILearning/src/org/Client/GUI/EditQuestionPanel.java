package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
		this.mediaURLTextField = new JTextField();
		this.mediaURLLabel = new JLabel("Hier könnte Ihre URL stehen!");
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());

		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JPanel north = new JPanel();
		north.add(questionTextField);
		south.add(submitButton);
		
		center.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		
		for(JRadioButton radio: answerButton){
			center.add(getRadioLabel(radio),gbc);
			gbc.gridy++;
		}
//		center.setPreferredSize(new Dimension(1000, 1000));
		pan.add(center, "Center");
		pan.add(north, "North");
		pan.add(south, "South");
		add(pan);
	}
}
