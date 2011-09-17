package br.com.jm.musiclib.model.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.bson.types.ObjectId;

import br.com.jm.musiclib.indexer.MusicIndexerEvent;
import br.com.jm.musiclib.indexer.MusicInfo;
import br.com.jm.musiclib.model.Comment;
import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicFile;
import br.com.jm.musiclib.model.MusicService;
import br.com.jm.musiclib.model.cdi.MusicCollection;
import br.com.jm.musiclib.model.converter.Converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Implementação EJB do serviço de manipulação de músicas.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@Stateless
@Local(MusicService.class)
public class MusicServiceBean implements MusicService {

	/** Log */
	private Logger log = Logger.getLogger("br.com.jm.musiclib.model");

	/** Instância do GridFS. */
	@Inject
	protected GridFS musicsGridFS;
	
	/** Coleção de músicas. */
	@Inject @MusicCollection
	protected DBCollection musicsColl;
	
	/** Conversos de objetos Music. */
	@Inject
	protected Converter<Music> musicConv;
	
	/** Conversos de objetos Comment. */
	@Inject
	protected Converter<Comment> commentConv;
	
	/** Conversos de objetos MusicFile. */
	@Inject
	protected Converter<MusicFile> musicFileConv;

	/** {@inheritDoc} */
	@Override
	public Music getMusic(String musicId) {
		DBObject doc = this.musicsColl.findOne(new ObjectId(musicId));
		return musicConv.toObject(doc);
	}

	/** {@inheritDoc} */
	@Override
	public List<Music> searchMusics(String search) {
		DBObject titleQueryDoc = new BasicDBObject();

		// constrói a expressão regular e a utiliza como valor para title
		Pattern searchPattern = Pattern.compile(".*" + search + ".*",
				Pattern.CASE_INSENSITIVE);
		titleQueryDoc.put("title", searchPattern);

		List<Music> musics = new ArrayList<Music>();

		DBCursor cursor = this.musicsColl.find(titleQueryDoc);
		while (cursor.hasNext()) {
			DBObject currentDoc = cursor.next();
			musics.add(musicConv.toObject(currentDoc));
		}

		return musics;
	}

	/** {@inheritDoc} */
	@Override
	public void addTag(Music music, String tag) {
		DBObject key = new BasicDBObject("_id", new ObjectId(music.getId()));
		DBObject update = new BasicDBObject("$push", new BasicDBObject("tags",
				tag));

		this.musicsColl.update(key, update);
	}

	/** {@inheritDoc} */
	@Override
	public void addComment(Music music, Comment comment) {
		DBObject key = new BasicDBObject("_id", new ObjectId(music.getId()));
		DBObject update = new BasicDBObject("$push", new BasicDBObject(
				"comments", commentConv.toDBObject(comment)));

		this.musicsColl.update(key, update);

	}
	
	/** {@inheritDoc} */
	@Override
	public MusicFile getMusicFile(String musicFileId) {
		DBObject obj = this.musicsGridFS.find(new ObjectId(musicFileId));				
		return musicFileConv.toObject(obj);
	}

	/** {@inheritDoc} */
	@Override
	public Music processIndexerEvent(@Observes MusicIndexerEvent event) {
		if (!event.getCompleted()) { // não terminou ainda o processamento
			
			MusicInfo info = event.getMusicInfo();
			log.fine("Processando[" + info.getFileName() + "]");
			
			// Cria arquivo no GridFS
			String fileId = createFile(info.getFileName());

			String genre = info.getTags().size() > 0 ?
					info.getTags().get(0) :
					null;
					
			Integer trackId;
			try {
				trackId = Integer.parseInt(info.getTrackNumber());
			} catch (NumberFormatException ex) {
				trackId = 0;
			}

			// cria música
			Music music = new Music(trackId, info.getTitle(), info.getArtist(),
					info.getAlbum(), fileId, genre);
			music.setId(this.createMusic(music));
			
			return music;
		}
		return null;

	}

	/** 
	 * Persiste música no MongoDB.
	 * @param music Música a ser persistida.
	 * @return Identificador do objeto criado pelo MongoDB.
	 */
	protected String createMusic(Music music) {

		DBObject doc = musicConv.toDBObject(music);

		WriteResult result = this.musicsColl.insert(doc);
		if (result.getError() != null) {
			throw new RuntimeException("Erro na inserção da Música");
		}

		ObjectId id = (ObjectId) doc.get("_id");
		music.setId(id.toString());
		return music.getId();
	}

	/**
	 * Persiste o arquivo no banco de dados.
	 * @param fileName Caminho completo do arquivo a ser persistido.
	 * @return 
	 */
	protected String createFile(String fileName) {		
		try {
			GridFSInputFile input = musicsGridFS.createFile(new File(fileName));
			input.save();
			return ((ObjectId) input.getId()).toString();

		} catch (IOException ex) {
			throw new RuntimeException("Erro na persistência do arquivo");
		}
	}

//	/**
//	 * Configura objeto que representa a coleção de músicas.
//	 * @param musicsColl objeto que representa a coleção de músicas.
//	 */
//	public void setMusicCollection(DBCollection musicsColl) {
//		this.musicsColl = musicsColl;
//	}
//
//	/**
//	 * Configura instância do GridFS. 
//	 * @param gridFS Instância do GridFS a ser configurada.
//	 */
//	public void setGridFS(GridFS gridFS) {
//		this.musicsGridFS = gridFS;
//	}
//	
	
}
