package br.com.jm.musiclib.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.jm.musiclib.model.User;
import br.com.jm.musiclib.model.UserService;

/**
 * Bean repons�vel pela cria��o de novos usu�rios e pela autentica��o dos j�
 * existentes
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@SessionScoped
@Named
public class UserBean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7905281858125385718L;
  /**
   * Usu�rio atual. Os outros beans consultam o UserBean para recuperar o
   * usu�rio
   */
  private User currentUser = new User();
  /** Nome do novo usu�rio */
  private String name;
  /** Login do novo usu�rio */
  private String login;
  /** Senha do novo usu�rio */
  private String password;

  /**
   * EJB UserService com as opera��es para criar e autenticar os usu�rios.
   * Injetado pelo container atrav�s da anota��o @EJB
   */
  @Inject
  private UserService userService;

  /**
   * @return Retorna o usu�rio atual.
   */
  public User getCurrentUser() {
    return this.currentUser;
  }

  /**
   * Autentica o usu�rio. Os dados s�o preenchidos no formul�rio da p�gina de
   * login e atrav�s do UserService autenticamos o usu�rio. Caso o login seja
   * inv�lido, adicionamos uma mensagem para ser exibida para o usu�rio e
   * voltamos para a p�gina de login. Caso o login seja v�lido, redirecionamos
   * para a p�gina principal
   * 
   * @return qual p�gina dever� ser redirecionado dependendo do resultado da
   *         autentica��o
   * 
   * @see UserService
   * @see UserService#login(String, String)
   */
  public String login() {
    // Autentica o usu�rio
    currentUser = userService.login(currentUser.getLogin(),
        currentUser.getPassword());

    // Se o usu�rio for inv�lido
    if (currentUser == null) {
      // Cria uma nova inst�ncia para poder preencher o formul�rio
      currentUser = new User();
      // Adiciona uma mensagem no contexto
      FacesContext.getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR,
                  "Usu�rio inv�lido!", ""));
      // Redireciona novamente para a p�gina de login
      return "login";
    }

    return "main";

  }

  /**
   * Remove o usu�rio atual e retorna para a tela de login.
   * 
   * @return Retorna para a tela de login
   */
  public String logout() {
    currentUser = new User();

    return "login?faces-redirect=true";
  }

  /**
   * Cria um novo usu�rio. Os dados s�o preenchidos no formul�rio da p�gina de
   * login e criamos um usu�rio atrav�s do UserService. Ap�s a cria��o do
   * usuario, o fluxo � redirecionado para a p�gina de login com uma mensagem
   * de sucesso.
   * 
   * @return redirecionamento para a p�gina de login
   * 
   * @see UserService
   * @see UserService#createUser(User)
   */
  public String create() {
    User newUser = new User(name, login, password);
    userService.createUser(newUser);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Usu�rio criado com sucesso!", ""));
    return "login";

  }

  /**
   * 
   * @return retorna o nome
   */
  public String getName() {
    return name;
  }

  /**
   * Altera o nome
   * 
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 
   * @return retorna o login
   */
  public String getLogin() {
    return login;
  }

  /**
   * Altera o login
   * 
   * @param login
   */
  public void setLogin(String login) {
    this.login = login;
  }

  /**
   * @return Retorna a senha
   */
  public String getPassword() {
    return password;
  }

  /**
   * Altera a senha
   * 
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }

}
