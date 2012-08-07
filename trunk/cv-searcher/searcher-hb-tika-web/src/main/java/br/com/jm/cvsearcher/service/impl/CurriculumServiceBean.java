package br.com.jm.cvsearcher.service.impl;

import java.io.File;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.CurriculumService;

/**
 * Implementa��o EJB de CurriculumService.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Stateless
@Local(CurriculumService.class)
public class CurriculumServiceBean implements CurriculumService {

	/** Entity manager. */
	@PersistenceContext(name = "default")
	protected EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 * 
	 * Salva o arquivo na pasta de armazenamento de arquivos.
	 * 
	 * O arquivo do curr�culo � salvao da seguinte forma:
	 * <ol>
	 * <li>A primeira linha cont�m o nome do candidato</li>
	 * <li>A segunda linha cont�m o email do candidato</li>
	 * <li>A terceira linha em dianta, cont�m o conte�do do curr�culo</li>
	 * </ol>
	 * 
	 * O nome do arquivo � formado pela quantidade de arquivos na pasta de
	 * armazenamento + 1, adicionado a extens�o '.txt'.
	 * 
	 * Ap�s armazenar o arquivo em disco, o engine de indexa��o � chamado para
	 * indexar o novo curr�culo.
	 * 
	 * @see ConfigBean#getFilesDirectory()
	 * @see #getNextId()
	 * @see #index(Curriculum, File)
	 */
	@Override
	public void addCurriculum(Curriculum cv) throws CurriculumException {

		// Verifica se o curr�culo n�o est� nulo
		if (cv == null) {
			throw new CurriculumException("Curriculum cannot be null");
		}

		this.entityManager.persist(cv);

	}

}
