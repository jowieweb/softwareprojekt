package org.Client.GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;


public class VideoFrame {
    private JFrame frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public static boolean isWindows() {
    	 
		return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
 
	}
    
    public VideoFrame(final String url) {
        SwingUtilities.invokeLater(new Runnable() {
        	@Override
        	public void run() {
        		frame = new JFrame();
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
                if(isWindows()){
                NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
    	                "C:\\Program Files\\VideoLAN\\VLC");
    	        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
                }
    	        
        		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        		frame.setContentPane(mediaPlayerComponent);
        		frame.setVisible(true);
        		mediaPlayerComponent.getMediaPlayer().playMedia(url);

        		

        	    new NativeDiscovery().discover();

   
        	}
        });
    }
}