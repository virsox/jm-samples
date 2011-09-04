package br.com.jm.musiclib.model;

import java.io.InputStream;

public class MusicFile {
	private String fileName;
	
	private InputStream stream;

	
	public MusicFile(String fileName, InputStream inputStream) {
		this.fileName = fileName;
		this.stream = inputStream;
	}

	public String getFileName() {
		return this.fileName;
	}
	
	public InputStream getInputStream() {
		return this.stream;
	}
	
	
	
}
