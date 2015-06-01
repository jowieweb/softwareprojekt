package org.Client.GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

/**
 * Abstract class for displaying questions.
 * 
 */
public abstract class QuestionPanel extends JPanel {
	protected static final long serialVersionUID = 1L;
	protected JButton submitButton;
	protected JRadioButton[] answerButton;
	protected QuestionPanelListener listener;
	
	/**
	 * constructor creates submitButton and all answerButtons.
	 */
	public QuestionPanel(QuestionPanelListener listener) {
		this.listener = listener;
		this.submitButton = new JButton("Absenden");
		this.answerButton = new JRadioButton[4];
		for (int i = 0; i < 4; i++) {
			this.answerButton[i] = new JRadioButton();
		}
	}
	
	protected JPanel getRadioLabel(final JRadioButton btn) {
		JPanel p = new JPanel();
		JTextArea l = new JTextArea(4,100);
		l.setBackground(this.getBackground());
		l.setText(btn.getText());
		l.setEditable(false);
		l.setLineWrap(true);
		l.setWrapStyleWord(true);
		l.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
                btn.setSelected(!btn.isSelected());
            }			 
		});
		p.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
                btn.setSelected(!btn.isSelected());
            }
		});
		l.setHighlighter(null);
		btn.setText("");
		p.add(btn);
		p.add(l);
		return p;
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
