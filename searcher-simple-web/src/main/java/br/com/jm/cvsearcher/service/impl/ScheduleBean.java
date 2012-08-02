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
 * Bean agendado para executar a cada minuto para recriar o índice.
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
  /** Serviço de indexação injetado pelo container EJB. */
  @Inject
  private CurriculumService service;

  /**
   * Método que será invocado pelo schedule para reindexar os currículos.
   * 
   * 
   * @see CurriculumService#indexAll()
   */
  @PostConstruct
  @Schedule(minute = "*/1", hour = "*")
  public void reindex() {
    log.info("Recriando o índice");
    try {
      service.indexAll();
      log.info("Índice recriado com sucesso.");
    }
    catch (CurriculumException e) {
      // Logar o erro
      log.throwing(ScheduleBean.class.getName(), "reindex", e);
    }

  }
}
