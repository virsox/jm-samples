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
 * Implementação EJB de CurriculumService.
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
	 * O arquivo do currículo é salvao da seguinte forma:
	 * <ol>
	 * <li>A primeira linha contém o nome do candidato</li>
	 * <li>A segunda linha contém o email do candidato</li>
	 * <li>A terceira linha em dianta, contém o conteúdo do currículo</li>
	 * </ol>
	 * 
	 * O nome do arquivo é formado pela quantidade de arquivos na pasta de
	 * armazenamento + 1, adicionado a extensão '.txt'.
	 * 
	 * Após armazenar o arquivo em disco, o engine de indexação é chamado para
	 * indexar o novo currículo.
	 * 
	 * @see ConfigBean#getFilesDirectory()
	 * @see #getNextId()
	 * @see #index(Curriculum, File)
	 */
	@Override
	public void addCurriculum(Curriculum cv) throws CurriculumException {

		// Verifica se o currículo não está nulo
		if (cv == null) {
			throw new CurriculumException("Curriculum cannot be null");
		}

		this.entityManager.persist(cv);

	}

}
