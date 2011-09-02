package br.com.jm.musiclib.indexer;

/**
 * Evento disparado quando um arquivo mp3 é encontrado.
 * 
 */
public class MusicIndexerEvent {
	/**
	 * Informações sobre o arquivo MP3
	 */
	private MusicInfo info;

	/**
	 * Flag para indicar se o processo já foi completado.
	 */
	private Boolean completed;

	/**
	 * Construtor simples.
	 * 
	 * @param info
	 *            Objeto com as informações do arquivo mp3 encontrado
	 */
	public MusicIndexerEvent(MusicInfo info) {
		this(info, false);
	}

	/**
	 * Construtor completo.
	 * @param info flag indicando se o processo de indexação foi finalizado.
	 * @param completed Objeto com as informações do arquivo mp3 encontrado.
	 */
	public MusicIndexerEvent(MusicInfo info, Boolean completed) {
		this.info = info;
		this.completed = completed;
	}

	/**
	 * @return objeto com as informações do arquivo mp3 encontrado
	 */
	public MusicInfo getMusicInfo() {
		return info;
	}

	/**
	 * 
	 * @return retorna a informação se o processo já foi completado.
	 */
	public Boolean getCompleted() {
		return completed;
	}

}
