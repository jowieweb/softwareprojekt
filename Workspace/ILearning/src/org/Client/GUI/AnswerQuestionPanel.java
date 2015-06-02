package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


/**
 * The class AnswerQuestionPanel represents a JPanel that displays a question
 * and possible answers.
 */
public class AnswerQuestionPanel extends QuestionPanel {
	private static final long serialVersionUID = 1L;
	private JLabel questionLabel;
	private ImagePanel picturePanel;
	private JButton nextButton;
	private JButton backButton;

	/**
	 * constructor builds JPanel.
	 * 
	 * @param listener
	 *            callback method object
	 * @param text
	 */
	public AnswerQuestionPanel(QuestionPanelListener listener, String[] text) {
		super(listener);
		this.questionLabel = new JLabel();
		
		this.nextButton = new JButton("Nächste Frage");
		this.backButton = new JButton("Zurück zur Kategorieauswahl");
		this.nextButton.setVisible(false);
		this.backButton.setVisible(false);
		
		this.picturePanel = new ImagePanel();
		JPanel southPan = new JPanel();
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		southPan.add(backButton);
		southPan.add(submitButton);
		southPan.add(nextButton);

		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JPanel north = new JPanel();
		north.setLayout(new BorderLayout());
		north.add(questionLabel, "North");
		north.add(picturePanel, "South");

		south.add(southPan);

		center.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;

		setAnswerText(text);

		for (JCheckBox radio : answerButton) {
			JPanel p = getRadioLabel(radio);
			if (p != null) {
				center.add(p, gbc);
				gbc.gridy++;
			}
		}
		pan.add(center, "Center");
		pan.add(north, "North");
		pan.add(south, "South");
		add(pan);
		
		new MakeSound("haishort.wav").execute();
		
		this.submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				submitButtonClicked();
			}
		});
		this.backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				backButtonClicked();
			}
		});
		this.nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nextButtonClicked();
			}
		});
	}
	
	/**
	 * This method is invoked when submitButton is clicked.
	 */
	private void submitButtonClicked() {
		this.submitButton.setVisible(false);
		this.backButton.setVisible(true);
		this.nextButton.setVisible(true);
		int[] selected = new int[4];
		for(int i =0;i<answerButton.length;i++)
		{
			selected[i] = (answerButton[i].isSelected()==false)?0:1;
		}
		
		this.listener.answerSelected(selected);
		// Invoke callback method
		//listener.answerSelected(answer);
	}
	
	/**
	 * This method is invoked when backButton is clicked.	
	 */
	private void backButtonClicked() {
		this.listener.changeQuestionPanelToCategoryPanel();
	}
	
	/**
	 * This method is invoked when nextButton is clicked.
	 */
	private void nextButtonClicked() {
		
	}

	private JPanel getRadioLabel(final JCheckBox radio) {
		if (radio.getText().length() == 0) {
			return null;
		}

		JPanel p = new JPanel();
		JTextArea l = new JTextArea(4, 100);
		l.setBackground(this.getBackground());
		l.setText(radio.getText());
		l.setEditable(false);
		l.setLineWrap(true);
		l.setWrapStyleWord(true);
		l.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				radio.setSelected(!radio.isSelected());
			}
		});
		p.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				radio.setSelected(!radio.isSelected());
			}
		});
		radio.setToolTipText(radio.getText());
		l.setHighlighter(null);
		radio.setText("");
		p.add(radio);
		p.add(l);
		return p;

	}

	/**
	 * Sets question
	 * 
	 * @param text
	 *            question
	 */
	public void setQuestionText(String text) {
		this.questionLabel.setText(text);
		questionLabel.setFont(questionLabel.getFont().deriveFont(24.0f));
	}

	/**
	 * Sets provided text to radiobuttons.
	 * 
	 * @param text
	 *            array with answer options
	 */
	public void setAnswerText(String[] text) {
		if (text != null) {
			for (int i = 0; i < text.length && i < 4; i++) {
				this.answerButton[i].setText(text[i]);
			}
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
			answers[i] = this.answerButton[i].getToolTipText();
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
	public void setPicture(Image pic) {
		picturePanel.setImage(pic);
		picturePanel.setVisible(true);
		repaint();
	}
}
