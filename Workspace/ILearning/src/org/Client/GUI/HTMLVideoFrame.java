package org.Client.GUI;

import static javafx.concurrent.Worker.State.FAILED;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * Creates an independant JFrame displaying an JavaFX-Panel that shows Media-Content.
 * @author Lukas
 *
 */
public class HTMLVideoFrame {

	private final JFXPanel jfxPanel = new JFXPanel();
	private WebEngine engine;

	private final JPanel panel = new JPanel(new BorderLayout());
	private final JLabel lblStatus = new JLabel("Buffering:");

	private final JProgressBar progressBar = new JProgressBar();

	/**
	 * Constructor - creates an independent jFX-Thread running the JFrame.
	 * @param mediaURL URL to remote Media Content (Video/Audio/Image).(Or any other site!) Usage e.g. "new HTMLVideoFrame("http://olliswelt.de/rickroll.mp4");"
	 */
	public HTMLVideoFrame(final String mediaURL) {

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				JFrame htmlFrame = new JFrame();
				htmlFrame.addWindowListener(new WindowListener(){

					@Override
					public void windowActivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowClosed(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowClosing(WindowEvent arg0) {
						System.out.println("exit!");
						
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
						engine.load(null);
							}});
						
					}

					@Override
					public void windowDeactivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeiconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowIconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowOpened(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
				initComponents(htmlFrame);
				loadURL(mediaURL);
				htmlFrame.setVisible(true);
			}
		});
		
		

	}
	/**
	 * Initializes the JFrame + Layout
	 * @param htmlFrame parent JFrame
	 */
	private void initComponents(JFrame htmlFrame) {
		createScene();

		progressBar.setPreferredSize(new Dimension(150, 18));
		progressBar.setStringPainted(true);

		JPanel topBar = new JPanel(new BorderLayout(5, 0));
		topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

		JPanel statusBar = new JPanel(new BorderLayout(5, 0));
		statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		statusBar.add(lblStatus, BorderLayout.CENTER);
		statusBar.add(progressBar, BorderLayout.EAST);

		panel.add(topBar, BorderLayout.NORTH);
		panel.add(jfxPanel, BorderLayout.CENTER);
		panel.add(statusBar, BorderLayout.SOUTH);

		htmlFrame.getContentPane().add(panel);

		htmlFrame.setPreferredSize(new Dimension(500, 500));
		htmlFrame.pack();

	}
	/**
	 * Creates the JavaFX-Scene used to display HTML-Context
	 */
	private void createScene() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				WebView view = new WebView();
				engine = view.getEngine();
				engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
							final Number newValue) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								progressBar.setValue(newValue.intValue());
							}
						});
					}
				});



				jfxPanel.setScene(new Scene(view));
			}
		});
	}
	/**
	 * Used to load a specific url to the FX-Panel
	 * @param url MediaURL
	 */
	private void loadURL(final String url) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String tmp = toURL(url);

				if (tmp == null) {
					tmp = toURL("http://" + url);
				}

				engine.load(tmp);
			}
		});
	}
	/**
	 * Converts an given URL in a specific format.
	 * @param str URL to remote Media
	 * @return converted URL in a specific format.
	 */
	private static String toURL(String str) {
		try {
			return new URL(str).toExternalForm();
		} catch (MalformedURLException exception) {
			return null;
		}
	}

//	public static void main(String[] args) {
//		
//		 new HTMLVideoFrame("http://olliswelt.de/rickroll.mp4");
//	}

}