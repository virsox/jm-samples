package br.com.jm.musiclib.model.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

@Stateless()
@Local(MusicService.class)
public class MusicServiceBean implements MusicService {
	@Inject
	private DB db;

	@Inject
	private GridFS musicsGridFS;
	
	@Inject
	@MusicCollection
	private DBCollection musicsColl;
	
	@Inject
	private Converter<Music> musicConv;
	
	@Inject
	private Converter<Comment> commentConv;
	
	@Inject
	private Converter<MusicFile> musicFileConv;

	@Override
	public Music getMusic(String musicId) {
		DBObject doc = this.musicsColl.findOne(new ObjectId(musicId));
		return musicConv.toObject(doc);
	}

	@Override
	public List<Music> searchMusics(String search) {
		DBObject titleQueryDoc = new BasicDBObject();

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

	@Override
	public void addTag(Music music, String tag) {
		music.addTag(tag);

		DBObject key = new BasicDBObject("_id", new ObjectId(music.getId()));
		DBObject update = new BasicDBObject("$push", new BasicDBObject("tags",
				tag));

		this.musicsColl.update(key, update);
	}

	@Override
	public void addComment(Music music, Comment comment) {
		music.addComment(comment);

		BasicDBObject key = new BasicDBObject("_id",
				new ObjectId(music.getId()));
		BasicDBObject update = new BasicDBObject("$push", new BasicDBObject(
				"comments", commentConv.toDBObject(comment)));

		this.musicsColl.update(key, update);

	}
	
	@Override
	public MusicFile getMusicFile(String musicFileId) {
		DBObject obj = this.musicsGridFS.find(new ObjectId(musicFileId));				
		return musicFileConv.toObject(obj);
	}

	public void processIndexerEvent(@Observes MusicIndexerEvent event) {
		if (!event.getCompleted()) {
			MusicInfo info = event.getMusicInfo();
			System.out.println("Processing [" + info.getFileName() + "]");
			Integer trackId;
			ObjectId fileId = createFile(info.getFileName());

			String genre = info.getTags().size() > 0 ? info.getTags().get(0)
					: null;

			// TODO mudei pois estava dando exception quando nao tem track id
			try {
				trackId = Integer.parseInt(info.getTrackNumber());
			} catch (NumberFormatException ex) {
				trackId = 0;
			}

			Music music = new Music(trackId, info.getTitle(), info.getArtist(),
					info.getAlbum(), fileId.toString(), genre);

			this.createMusic(music);
			// return music;
		}

	}

	protected String createMusic(Music music) {

		DBObject doc = musicConv.toDBObject(music);

		WriteResult result = this.musicsColl.insert(doc);
		if (result.getError() != null) {
			throw new RuntimeException("Deu pane!");
		}

		ObjectId id = (ObjectId) doc.get("_id");
		music.setId(id.toString());

		return music.getId();

	}

	protected ObjectId createFile(String fileName) {		
		try {
			GridFSInputFile input = musicsGridFS.createFile(new File(fileName));
			input.save();

			return (ObjectId) input.getId();

		} catch (IOException ex) {
			throw new RuntimeException("Deu pane!");
		}
	}

	public void setMusicCollection(DBCollection musicsColl) {
		this.musicsColl = musicsColl;
	}

	public void setDataBase(DB db) {
		this.db = db;
	}




}
