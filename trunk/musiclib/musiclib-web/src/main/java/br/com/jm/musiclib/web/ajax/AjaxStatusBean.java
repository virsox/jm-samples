package br.com.jm.musiclib.web.ajax;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.jm.musiclib.web.StatusBean;

/**
 * Bean responsável por monitorar o andamento da indexação e atualizar o status
 * para o usuário. Verifica o bean StatusBean para obter as informações.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 * @see StatusBean
 * 
 */
@Named(value = "ajaxStatusBean")
@SessionScoped
public class AjaxStatusBean implements Serializable {
  /** Log */
  private Logger log = Logger.getLogger("br.com.jm.musiclib.web");

  /**
   * 
   */
  private static final long serialVersionUID = -2461648661629070947L;

  /**
   * StatusBean injetado pelo container.
   */
  @Inject
  private StatusBean bean;
  /** Título da música indexada */
  private String title;
  /** Album da música indexada */
  private String album;
  /** Artista da música indexada */
  private String artist;
  /** Nome do arquivo da música indexada */
  private String fileName;
  /** Faixa da música indexada */
  private String trackNumber;

  /**
   * Atualiza o status dos campos de acordo com o estado do bean StatusBean.
   * Esse método deve ser invocado pela página que irá exibir o estado da
   * indexação.
   * 
   * Quando a indexação é finalizada, o fluxo é redirecionado para a página
   * main.
   * 
   * @see StatusBean
   * @see StatusBean#getCompleted()
   */
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
          context.getExternalContext().redirect("main.jsf");
        } catch (IOException e) {
          log.throwing("AjaxStatusBean", "updateStatus", e);
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

  /**
   * @return the log
   */
  public Logger getLog() {
    return log;
  }

  /**
   * @param log
   *            the log to set
   */
  public void setLog(Logger log) {
    this.log = log;
  }

  /**
   * @return the bean
   */
  public StatusBean getBean() {
    return bean;
  }

  /**
   * @param bean
   *            the bean to set
   */
  public void setBean(StatusBean bean) {
    this.bean = bean;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   *            the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the album
   */
  public String getAlbum() {
    return album;
  }

  /**
   * @param album
   *            the album to set
   */
  public void setAlbum(String album) {
    this.album = album;
  }

  /**
   * @return the artist
   */
  public String getArtist() {
    return artist;
  }

  /**
   * @param artist
   *            the artist to set
   */
  public void setArtist(String artist) {
    this.artist = artist;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName
   *            the fileName to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * @return the trackNumber
   */
  public String getTrackNumber() {
    return trackNumber;
  }

  /**
   * @param trackNumber
   *            the trackNumber to set
   */
  public void setTrackNumber(String trackNumber) {
    this.trackNumber = trackNumber;
  }

}
