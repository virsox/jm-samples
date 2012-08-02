package br.com.jm.cvsearcher.model;

public class SearchResult {

	private Curriculum curriculum;
	private float score;
	private String bestFragment;
	
	public SearchResult(Curriculum curriculum, float score, 
			String bestFragment) {
		this.curriculum = curriculum;
		this.score = score;
		this.bestFragment = bestFragment;
	}
	
	public Curriculum getCurriculum() {
		return curriculum;
	}

	public float getScore() {
		return score;
	}
	
	public String getBestFragment() {
		return this.bestFragment;
	}
	
}
