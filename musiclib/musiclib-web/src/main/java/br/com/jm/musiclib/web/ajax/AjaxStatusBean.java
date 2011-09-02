package br.com.jm.musiclib.web.ajax;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import br.com.jm.musiclib.indexer.StatusBean;

@Named(value = "ajaxStatusBean")
@Singleton
public class AjaxStatusBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2461648661629070947L;

	@Inject
	private StatusBean bean;
	private String title;
	private String album;
	private String artist;
	private String fileName;
	private String trackNumber;

	public void updateStatus() {
		if (bean == null) {
			setTitle("");
			setAlbum("");
			setArtist("");
			setFileName("");
			setTrackNumber("");
		} else {
			if (bean.getCompleted()) {
				FacesContext context = FacesContext.getCurrentInstance();
				try {
					context.getExternalContext().redirect("playlist.jsf");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				setTitle(bean.getTitle());
				setAlbum(bean.getAlbum());
				setArtist(bean.getArtist());
				setFileName(bean.getFileName());
				setTrackNumber(bean.getTrackNumber());
			}
		}
	}

	public StatusBean getBean() {
		return bean;
	}

	public void setBean(StatusBean bean) {
		this.bean = bean;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	 
}
