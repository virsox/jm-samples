package br.com.jm.musiclib.model.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.bson.types.ObjectId;

import br.com.jm.musiclib.model.Comment;
import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicService;

import br.com.jm.musiclib.indexer.MusicIndexer;
import br.com.jm.musiclib.indexer.MusicIndexerEvent;
import br.com.jm.musiclib.indexer.MusicInfo;

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

//	@Inject
	private MusicIndexer indexer;
	
	@Inject
	private MongoProviderBean mongo;
	
	public void setMongoProvider(MongoProviderBean mongo) {
		this.mongo = mongo;
	}
	
	
	@Override
	public Music getMusic(String musicId) {
		DBObject doc = mongo.getMusicCollection().findOne(new ObjectId(musicId));
		return Music.getMusic(doc);
	}


	@Override
	public List<Music> searchMusics(String search) {
		DBObject titleQueryDoc = new BasicDBObject();
		
		Pattern searchPattern = Pattern.compile(".*" + search + ".*",
				Pattern.CASE_INSENSITIVE);
		titleQueryDoc.put("title", searchPattern);
		
		DBCollection musicColl = mongo.getMusicCollection();
		List<Music> musics = new ArrayList<Music>();
		
		DBCursor cursor = musicColl.find(titleQueryDoc);
		while (cursor.hasNext()) {
			DBObject currentDoc = cursor.next();
			musics.add(Music.getMusic(currentDoc));
		}		
		
		return musics;
	}


	
    /* (non-Javadoc)
	 * @see com.javamagazine.musiccollection.model.impl.MusicService#createIndex(java.io.File)
	 */
    @Override
	public void createIndex(File dir) {
    	indexer.createIndex(dir);
    }
       
    @Override
	public void addTag(Music music, String tag) {
    	music.addTag(tag);
    	
    	DBObject key = new BasicDBObject("_id", new ObjectId(music.getId()));
    	DBObject update = new BasicDBObject("$push",
    			new BasicDBObject("tags", tag));
    	
    	DBCollection musicsColl = mongo.getMusicCollection();
    	musicsColl.update(key, update);
    }
    

    @Override
	public void addComment(Music music, Comment comment) {
    	music.addComment(comment);
    	
    	BasicDBObject key = new BasicDBObject("_id", new ObjectId(music.getId()));
    	BasicDBObject update = new BasicDBObject("$push",
    			new BasicDBObject("comments", comment.toDBObject()));    	
    	
    	DBCollection musicsColl = mongo.getMusicCollection();
    	musicsColl.update(key, update);
    	
    }
    
/*    public Music processIndexerEvent(@Observes MusicIndexerEvent event) {
    	MusicInfo info = event.getMusicInfo();
    	System.out.println("Processing [" + info.getFileName() + "]");
    	
    	String genre = info.getTags().size() > 0 ? info.getTags().get(0) : null;     	    	    	
    	Music music = new Music(
    			Integer.parseInt(info.getTrackNumber()),
    			info.getTitle(),
    			info.getArtist(),
    			info.getAlbum(),
    			genre);

    	this.createMusic(music);
    	return music;
    }
*/
    
    protected String createMusic(Music music) {
        DBObject doc = music.toDBObject();
        
        DBCollection musicsColl = mongo.getMusicCollection();
        
        WriteResult result = musicsColl.insert(doc);
        
        // TODO corrigir este tratamento
        if (result.getError() != null) {
        	throw new RuntimeException("Deu pane!");
        }
        
        ObjectId id = (ObjectId) doc.get("_id");        
        music.setId(id.toString());
        return music.getId();           
    }
    
    protected void createFile(File file) {
    	DB db = mongo.getDataBase();
    	GridFS musicasFS = new GridFS(db, "musicas");
    	try {
			GridFSInputFile input = musicasFS.createFile(file);
			input.save();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }

    
}
