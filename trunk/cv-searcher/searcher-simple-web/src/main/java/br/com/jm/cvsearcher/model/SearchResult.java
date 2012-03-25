package br.com.jm.cvsearcher.model;

public class SearchResult {

	private Curriculum curriculum;
	private float score;
	
	public SearchResult(Curriculum curriculum, float score) {
		this.curriculum = curriculum;
		this.score = score;
	}
	
	public Curriculum getCurriculum() {
		return curriculum;
	}

	public float getScore() {
		return score;
	}
	
}
