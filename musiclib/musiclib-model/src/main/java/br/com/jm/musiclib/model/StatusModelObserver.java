package br.com.jm.musiclib.model;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

import br.com.jm.musiclib.indexer.MusicIndexerEvent;

@Singleton
public class StatusModelObserver {
	public void process(@Observes MusicIndexerEvent event) {
		//System.out.println("SM: "+event.getMusicInfo().getFileName());
	}
}
