package org.Client.GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;


public class VideoFrame {
    private JFrame frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public VideoFrame(final String url) {
        SwingUtilities.invokeLater(new Runnable() {
        	@Override
        	public void run() {
        		frame.setBounds(100, 100, 600, 400);
        		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        mediaPlayerComponent.release();
                        frame.dispose();
                    }
                });
        		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        		frame.setContentPane(mediaPlayerComponent);
        		frame.setVisible(true);
        		mediaPlayerComponent.getMediaPlayer().playMedia(url);
        		new NativeDiscovery().discover();
 
        	}
        });
    }
}