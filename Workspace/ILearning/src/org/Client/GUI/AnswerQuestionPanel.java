package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	 * 
	 * @param listener
	 *            callback method object
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
		north.setLayout(new BorderLayout());
		north.add(questionLabel, "North");
		north.add(picturePanel, "South");

		south.add(submitButton);

		center.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;

		setAnswerText(text);
		for (JRadioButton radio : answerButton) {
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
	}

	private JPanel getRadioLabel(final JRadioButton btn) {
		if (btn.getText().length() == 0) {
			return null;
		}

		JPanel p = new JPanel();
		JTextArea l = new JTextArea(4, 100);
		l.setBackground(this.getBackground());
		l.setText(btn.getText());
		l.setEditable(false);
		l.setLineWrap(true);
		l.setWrapStyleWord(true);
		l.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btn.setSelected(!btn.isSelected());
			}
		});
		p.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btn.setSelected(!btn.isSelected());
			}
		});
		btn.setToolTipText(btn.getText());
		l.setHighlighter(null);
		btn.setText("");
		p.add(btn);
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
		System.out.println(text);
		for (int i = 0; i < text.length && i < 4; i++) {
			this.answerButton[i].setText(text[i]);
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
	public void setPicture(Image pic) {
		picturePanel.setImage(pic);
		picturePanel.setVisible(true);
		repaint();
	}
}
