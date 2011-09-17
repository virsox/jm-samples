package br.com.jm.musiclib.model;

/**
 * Interface de serviço contendo os métodos para manipulação de usuários.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public interface UserService {

  /**
   * Efetua o "login", verificando se existe um usuário com o login e a
   * senha informadas. Caso exista, retorna este usuário. Caso contrário,
   * retorna null.
   * @param login Login do usuário.
   * @param password Senha.
   * @return Objeto que representa o usuário, caso exista um cadastrado
   * com o login e senha informados.
   */
  public User login(String login, String password);

  /**
   * Atualiza os dados do usuário.
   * @param user Documento contendo os novos dados do usuário. O
   * atributo id deve estar preenchido, permitindo identificá-lo.
   */
  public void update(User user);

  /**
   * Contabiliza a execução de uma música.
   * @param user Usuário que executou a música.
   * @param music Música que foi executada.
   */
  public void play(User user, Music music);

  /**
   * Adiciona um novo usuário na base de dados.
   * @param user Usuário a ser criado. 
   * @return Identificador interno do usuário recém criado.
   */
  public String createUser(User user);

}