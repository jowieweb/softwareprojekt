package org;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Packet implements Serializable {

	private Socket client;
	private String username;
	private String password;
	private String frage;
	private String[] topics;
	private String[] level;
	public enum Type {
		UNUSED, SERVER, CLIENT
	};

	public enum Login {
		FAIL, USER, ADMIN
	}

	private Type type = Type.UNUSED;
	private Login login = Login.FAIL;

	public Packet(String username, String password, Type t) {
		this(username, password, t, Login.FAIL);
	}

	public Packet(String username, String password, Type t, Login l) {
		if (username == null || password == null) {
			password = "";
			username = "";
		}
		type = t;
		login = l;
		this.username = username;
		this.password = password;
	}

	public void setSocket(Socket s) {
		client = s;
	}

	public Socket getSocket() {
		return client;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public Login getLoginStatus() {
		return login;
	}

	public String getFrage() {
		return frage;
	}

	public void setFrage(String p) {
		frage = p;
	}
	public void setTopics(String[] para)
	{
		this.topics = para;
	}
	
	public String[] getTopics() {
		return this.topics;
	}
	
	public void setLevel(String[] lvl)
	{
		level = lvl;
	}
	
	public String[] getLevel()
	{
		return level;
	}

}
