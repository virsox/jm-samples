package br.com.jm.cvsearcher.service.impl;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.CurriculumService;

/**
 * Bean agendado para executar a cada minuto para recriar o �ndice.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 * @see CurriculumService
 * 
 */
@Startup
@Singleton
public class ScheduleBean {

  /** Logger */
  private static final Logger log = Logger.getLogger(ScheduleBean.class
      .getName());
  /** Servi�o de indexa��o injetado pelo container EJB. */
  @Inject
  private CurriculumService service;

  /**
   * M�todo que ser� invocado pelo schedule para reindexar os curr�culos.
   * 
   * 
   * @see CurriculumService#indexAll()
   */
  @PostConstruct
  @Schedule(minute = "*/1", hour = "*")
  public void reindex() {
    log.info("Recriando o �ndice");
    try {
      service.indexAll();
      log.info("�ndice recriado com sucesso.");
    }
    catch (CurriculumException e) {
      // Logar o erro
      log.throwing(ScheduleBean.class.getName(), "reindex", e);
    }

  }
}
