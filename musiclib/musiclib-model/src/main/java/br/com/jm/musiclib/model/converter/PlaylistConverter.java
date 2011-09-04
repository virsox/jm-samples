package br.com.jm.musiclib.model.converter;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import br.com.jm.musiclib.model.Playlist;

@ApplicationScoped
public class PlaylistConverter implements Converter<Playlist> {

	@Override
	public DBObject toDBObject(Playlist playlist) {
		BasicDBObject doc = new BasicDBObject();		
		doc.put("name", playlist.getName());
		
		List<ObjectId> musicIds = new ArrayList<ObjectId>();
		for (String music : playlist.getMusics()) {
			musicIds.add(new ObjectId(music));
		}		
		doc.put("musics", musicIds);
		
		return doc;
	}

	@Override
	public Playlist toObject(DBObject doc) {
		
		@SuppressWarnings("unchecked")
		List<ObjectId> musicIds = (List<ObjectId>) doc.get("musics");
		List<String> musics = new ArrayList<String>();
		for (ObjectId id : musicIds) {
			musics.add(id.toString());
		}
		
		Playlist playlist = new Playlist((String) doc.get("name"), musics);
		return playlist;
	}

}
