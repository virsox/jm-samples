package br.com.jm.musiclib.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import br.com.jm.musiclib.model.User;
import br.com.jm.musiclib.model.UserService;

/**
 * Bean reponsável pela criação de novos usuários e pela autenticação dos já
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
	 * Usuário atual. Os outros beans consultam o UserBean para recuperar o
	 * usuário
	 */
	private User currentUser = new User();
	/** Nome do novo usuário */
	private String name;
	/** Login do novo usuário */
	private String login;
	/** Senha do novo usuário */
	private String password;

	/**
	 * EJB UserService com as operações para criar e autenticar os usuários.
	 * Injetado pelo container através da anotação @EJB
	 */
	@EJB
	private UserService userService;

	/**
	 * @return Retorna o usuário atual.
	 */
	public User getCurrentUser() {
		return this.currentUser;
	}

	/**
	 * Autentica o usuário. Os dados são preenchidos no formulário da página de
	 * login e através do UserService autenticamos o usuário. Caso o login seja
	 * inválido, adicionamos uma mensagem para ser exibida para o usuário e
	 * voltamos para a página de login. Caso o login seja válido, redirecionamos
	 * para a página principal
	 * 
	 * @return qual página deverá ser redirecionado dependendo do resultado da
	 *         autenticação
	 * 
	 * @see UserService
	 * @see UserService#login(String, String)
	 */
	public String login() {
		// Autentica o usuário
		currentUser = userService.login(currentUser.getLogin(),
				currentUser.getPassword());

		// Se o usuário for inválido
		if (currentUser == null) {
			// Cria uma nova instância para poder preencher o formulário
			currentUser = new User();
			// Adiciona uma mensagem no contexto
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Usuário inválido!", ""));
			// Redireciona novamente para a página de login
			return "login";
		}
		// Usuário válido, redirecionar para a página principal
		// Adicionar mensagem se o indexador ainda não foi executado
		// FIXME Verificar o indexador
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Indexador",
						"O indexador não foi executado"));
		return "main";

	}

	/**
	 * Remove o usuário atual e retorna para a tela de login.
	 * 
	 * @return Retorna para a tela de login
	 */
	public String logout() {
		currentUser = new User();

		return "login?faces-redirect=true";
	}

	/**
	 * Cria um novo usuário. Os dados são preenchidos no formulário da página de
	 * login e criamos um usuário através do UserService. Após a criação do
	 * usuario, o fluxo é redirecionado para a página de login com uma mensagem
	 * de sucesso.
	 * 
	 * @return redirecionamento para a página de login
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
						"Usuário criado com sucesso!", ""));
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
