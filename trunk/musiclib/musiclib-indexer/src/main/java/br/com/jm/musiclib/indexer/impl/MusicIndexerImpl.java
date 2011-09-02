package br.com.jm.musiclib.indexer.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;

import br.com.jm.musiclib.indexer.MusicIndexer;
import br.com.jm.musiclib.indexer.MusicIndexerEvent;
import br.com.jm.musiclib.indexer.MusicInfo;

/**
 * Procura por arquivos com extensão mp3 em uma pasta especificada e extrai as
 * informações para poder realizar uma indexação. Quando as informações são
 * extraídas, listeners cadastrados são invocados para poder exibir ou salvar as
 * informações.
 * 
 * <code>
 * 
 * @Inject private MusicIndexer indexer;
 * 
 *         // Cria o indice indexer.createIndex("C:\\Users\\Public\\Music\\");
 *         </code>
 * 
 *         Para ser informado sobre os eventos <code>
 	public void process(@Observes MusicIndexerEvent event) {
		System.out.println(event.getMusicInfo().getTitle());
	}
 * </code>
 * @see MusicInfo
 * @see MusicIndexerEvent
 */
@Named
@Stateless
public class MusicIndexerImpl implements MusicIndexer {
	/** Lista com os listeners a serem invocados */
	@Inject
	Event<MusicIndexerEvent> events;

	public MusicIndexerImpl() {
		// Desabilitar o log do jaudiotagger
		Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
	}

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

		events.fire(new MusicIndexerEvent(null, true));
	}

	/**
	 * Método recursivo que vasculha pastas e sub-pastas em busca de arquivos
	 * mp3
	 * 
	 * @param root
	 *            A pasta raiz para procurar os arquivos.
	 */
	private void doCreateIndex(File root) {
		MP3File f;
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				
				// adicionei verificação de que não são os diretório "." e
				// ".." - no Mac estava entrando em loop infinito
				return ((pathname.isDirectory()) &&
							(!pathname.getName().equals(".")) &&
							(!pathname.getName().equals("..")))
						|| pathname.getName().endsWith(".mp3");
			}
		};

		for (File file : root.listFiles(filter)) {

			if (file.isDirectory()) {
				doCreateIndex(file);
			} else {
				try {
					f = (MP3File) AudioFileIO.read(file);
				} catch (Exception e) {
					// TODO Logar o erro
					continue;
				}
				
				MusicInfo info = new MusicInfo();
				info.setFileName(file.getAbsolutePath());
				
				// Mudei o tipo para o genérico "Tag" - estava capotando com
				// alguns mp3 que eu tinha cuja versão do id3 não era 1.0
				Tag tag = f.getTag();
				
				info.setAlbum(tag.getFirst(FieldKey.ALBUM));
				info.setArtist(tag.getFirst(FieldKey.ARTIST));
				info.setTitle(tag.getFirst(FieldKey.TITLE));
				try {
					info.setTrackNumber(tag.getFirst(FieldKey.TRACK));
				} catch (UnsupportedOperationException e) {
					info.setTrackNumber("");
				}
				info.addTag(tag.getFirst(FieldKey.GENRE));

				if (tag.getFirstArtwork() != null) {
					info.setCover(tag.getFirstArtwork().getBinaryData());
				}
					
				// Disparar o evento
				events.fire(new MusicIndexerEvent(info));

			}
		}

	}
}
