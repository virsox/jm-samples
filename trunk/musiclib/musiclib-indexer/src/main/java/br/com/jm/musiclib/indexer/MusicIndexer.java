package br.com.jm.musiclib.indexer;

import java.io.File;

/**
 * Interface que define os métodos que um indexador de músicas deve implementar.
 * @author Paulo Sigrist / Wilson A. Higashino
 *
 */
public interface MusicIndexer {

	/**
	 * Procura por arquivos com extensão mp3 nas pastas especificadas pelo
	 * parametro <tt>root</tt>
	 * 
	 * @param root
	 *            o caminho completo da pasta para procurar pelos arquivos
	 * @see MusicIndexerImpl#createIndex(File)
	 */
	public abstract void createIndex(String root);

	/**
	 * Procura por arquivos com extensão mp3 nas pastas especificadas pelo
	 * parametro <tt>root</tt>
	 * 
	 * @param root
	 *            um objeto File com o caminho completo da pasta para procurar
	 *            pelos arquivos
	 * @see MusicIndexerImpl#createIndex(String)
	 */
	public abstract void createIndex(File root);

}