package org.Client.GUI;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
/**
 * VideoPanel - A Panel displaying Media via the VLC-Plugin
 * @author Lukas Stuckstette
 * @version 1.0
 */
public class VideoPanel extends JPanel {
	private static final long serialVersionUID = 1165928246603877513L;
	private static final String NATIVE_LIBRARY_SEARCH_PATH = "vlcLib";

	private String vidUrl;
	private String username;
	private String password;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private PlayerControlsPanel controlsPanel;
	
	
    public static boolean isWindows() {
   	 
		return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
 
	}
    
    
	/**
	 * Constructor.
	 * @param vidUrl URL identifying the Media-Object. Syntax: 'http://<server-ip>:<server-port>/<media-object>'
	 * @param username The username of the current user. Used for authentification.
	 * @param password The password of the current user. Used for authentification.
	 */
	public VideoPanel(String vidUrl, String username, String password) {
		this.vidUrl = vidUrl;
		this.username = username;
		this.password = password;

      if(isWindows()){
      NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
              "C:\\Program Files\\VideoLAN\\VLC");
      Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
      }
		
		buildPlayer(); // Set up the VLC-Enviroment
		
		
		
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		EmbeddedMediaPlayer embeddedMediaPlayer = mediaPlayerComponent
				.getMediaPlayer();
		
		Canvas videoSurface = new Canvas();
		videoSurface.setBackground(Color.black);
		videoSurface.setSize(800, 600);
		
		List<String> vlcArgs = new ArrayList<String>();
		vlcArgs.add("--no-plugins-cache");
		vlcArgs.add("--no-video-title-show");
		vlcArgs.add("--no-snapshot-preview");
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(
				vlcArgs.toArray(new String[vlcArgs.size()]));
		mediaPlayerFactory.setUserAgent("vlcj test player");
		embeddedMediaPlayer.setVideoSurface(mediaPlayerFactory
				.newVideoSurface(videoSurface));
		embeddedMediaPlayer.setPlaySubItems(true);

		controlsPanel = new PlayerControlsPanel(
				embeddedMediaPlayer);

		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.setBackground(Color.black);
		this.add(videoSurface, BorderLayout.CENTER);
		this.add(controlsPanel, BorderLayout.SOUTH);
		System.out.println(buildRequestURL());
	}
	
	/**
	 * Plays the initialized Media.
	 * WARNING! Only call this Method AFTER the JFrame using this Panel is initialized and visible!
	 */
	public void playVideo() {
		mediaPlayerComponent.getMediaPlayer().playMedia(buildRequestURL());
	}
	
	private String buildRequestURL(){
		String auth;
		
		if (username == null || password == null) {
			return vidUrl;
		} else {
			auth = username+":"+password+"@";
		}
		return new StringBuilder(vidUrl).insert(vidUrl.indexOf(':')+3, auth).toString();
	}
	
	//ToDo: Build OS-Switch (Mac/Unix support)
	private void buildPlayer() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
				NATIVE_LIBRARY_SEARCH_PATH);
		NativeLibrary.addSearchPath(RuntimeUtil.getPluginsDirectoryName(),
				NATIVE_LIBRARY_SEARCH_PATH);
	}
}
