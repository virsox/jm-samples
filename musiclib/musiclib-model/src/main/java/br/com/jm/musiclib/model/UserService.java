package br.com.jm.musiclib.model;

/**
 * Interface de servi�o contendo os m�todos para manipula��o de usu�rios.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public interface UserService {

  /**
   * Efetua o "login", verificando se existe um usu�rio com o login e a
   * senha informadas. Caso exista, retorna este usu�rio. Caso contr�rio,
   * retorna null.
   * @param login Login do usu�rio.
   * @param password Senha.
   * @return Objeto que representa o usu�rio, caso exista um cadastrado
   * com o login e senha informados.
   */
  public User login(String login, String password);

  /**
   * Atualiza os dados do usu�rio.
   * @param user Documento contendo os novos dados do usu�rio. O
   * atributo id deve estar preenchido, permitindo identific�-lo.
   */
  public void update(User user);

  /**
   * Contabiliza a execu��o de uma m�sica.
   * @param user Usu�rio que executou a m�sica.
   * @param music M�sica que foi executada.
   */
  public void play(User user, Music music);

  /**
   * Adiciona um novo usu�rio na base de dados.
   * @param user Usu�rio a ser criado. 
   * @return Identificador interno do usu�rio rec�m criado.
   */
  public String createUser(User user);

}