package br.com.jm.musiclib.indexer.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import br.com.jm.musiclib.indexer.MusicIndexer;
import br.com.jm.musiclib.indexer.MusicIndexerEvent;
import br.com.jm.musiclib.indexer.MusicInfo;

/**
 * Procura por arquivos com extens�o mp3 em uma pasta especificada e extrai as
 * informa��es para poder realizar uma indexa��o. Quando as informa��es s�o
 * extra�das, listeners cadastrados s�o invocados para poder exibir ou salvar as
 * informa��es.
 * 
 * Para ser informado sobre os eventos
 * 
 * <code>
 	public void process(@Observes MusicIndexerEvent event) {
		System.out.println(event.getMusicInfo().getTitle());
	}
 * </code>
 * 
 * @see MusicInfo
 * @see MusicIndexerEvent
 */
@Named
@Stateless
public class MusicIndexerImpl implements MusicIndexer {
  /** Log */
  private Logger log = Logger.getLogger("br.com.jm.musiclib.indexer");

  /** Evento injetado pelo container. */
  @Inject
  Event<MusicIndexerEvent> event;

  /*
   * (non-Javadoc)
   * 
   * @see
   * br.com.jm.musiclib.indexer.MusicIndexer#createIndex(java.lang.String)
   */
  @Override
  public void createIndex(String root) {
    createIndex(new File(root));
  }

  /*
   * (non-Javadoc)
   * 
   * @see br.com.jm.musiclib.indexer.MusicIndexer#createIndex(java.io.File)
   */
  @Override
  public void createIndex(File root) {
    doCreateIndex(root);
    log.info("Indexa��o finalizada. Pasta: '" + root + "'");
    event.fire(new MusicIndexerEvent(null, true));
  }

  /**
   * M�todo recursivo que vasculha pastas e sub-pastas em busca de arquivos
   * mp3
   * 
   * @param root
   *            A pasta raiz para procurar os arquivos.
   */
  private void doCreateIndex(File root) {
    MP3File f;
    FileFilter filter = new FileFilter() {
      public boolean accept(File pathname) {

        return ((pathname.isDirectory()) && (!pathname.getName().equals(".")) && (!pathname
            .getName().equals(".."))) || pathname.getName().endsWith(".mp3");
      }
    };

    for (File file : root.listFiles(filter)) {

      if (file.isDirectory()) {
        doCreateIndex(file);
      } else {
        try {
          f = (MP3File) AudioFileIO.read(file);
        } catch (Exception e) {
          log.throwing("MusicIndexerImpl", "doCreateIndex", e);
          continue;
        }

        MusicInfo info = new MusicInfo();
        info.setFileName(file.getAbsolutePath());

        Tag tag = f.getTag();

        info.setAlbum(tag.getFirst(FieldKey.ALBUM));
        info.setArtist(tag.getFirst(FieldKey.ARTIST));
        info.setTitle(tag.getFirst(FieldKey.TITLE));
        try {
          info.setTrackNumber(tag.getFirst(FieldKey.TRACK));
        } catch (Throwable e) {
          info.setTrackNumber("");
        }
        info.addTag(tag.getFirst(FieldKey.GENRE));

        // Disparar o evento
        event.fire(new MusicIndexerEvent(info));

      }
    }

  }
}
