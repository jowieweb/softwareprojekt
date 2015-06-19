package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentIdVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.mac.MacVideoSurfaceAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * Defines a Window which displays a video.
 */
public class VideoFrame {
	private JFrame frame;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private PlayerControlsPanel controlsPanel;

	/**
	 * Returns whether we are on a windows system or not.
	 * 
	 * @return true if operating system is windows.
	 */
	public static boolean isWindows() {
		return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0);
	}

	/**
	 * Constructor builds the window.
	 * 
	 * @param url
	 *            the url to the video to display.
	 */
	public VideoFrame(final String url) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new JFrame();
				frame.setBounds(100, 100, 600, 400);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						// mediaPlayerComponent.release();
						// frame.dispose();
					}
				});
				
				if (isWindows()) {
					NativeLibrary.addSearchPath(
							RuntimeUtil.getLibVlcLibraryName(),
							"C:\\Program Files\\VideoLAN\\VLC");
					Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),
							LibVlc.class);
					mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
				} else if (isMac()) {
					uk.co.caprica.vlcj.binding.LibC.INSTANCE.setenv(
							"VLC_PLUGIN_PATH",
							"/Applications/VLC.app/Contents/MacOS/plugins/", 0);

					NativeLibrary.addSearchPath(
							RuntimeUtil.getLibVlcLibraryName(),
							"/Applications/VLC.app/Contents/MacOS/lib/");
					Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),
							LibVlc.class);
					
					mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
					MacVideoSurfaceAdapter mvsa = new MacVideoSurfaceAdapter();
					ComponentIdVideoSurface videoSurface = new ComponentIdVideoSurface(
							1337, mvsa);
					videoSurface.attach(LibVlc.INSTANCE,
							mediaPlayerComponent.getMediaPlayer());
					mvsa.attach(LibVlc.INSTANCE,
							mediaPlayerComponent.getMediaPlayer(), 1337);

				}

				controlsPanel = new PlayerControlsPanel(mediaPlayerComponent
						.getMediaPlayer());
				// frame.setContentPane(mediaPlayerComponent);
				frame.setVisible(true);

				frame.setLayout(new BorderLayout());
				frame.setVisible(true);
				frame.setBackground(Color.black);
				frame.add(mediaPlayerComponent, BorderLayout.CENTER);
				frame.add(controlsPanel, BorderLayout.SOUTH);

				new NativeDiscovery().discover();

				mediaPlayerComponent.getMediaPlayer().playMedia(url);
			}
		});
	}
}