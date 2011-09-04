/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jm.musiclib.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.com.jm.musiclib.model.User;
import br.com.jm.musiclib.model.UserService;

/**
 * 
 * @author virso
 */
@SessionScoped
@Named
public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7905281858125385718L;

	private User currentUser = new User();

	@EJB
	private UserService userService;

	public User getCurrentUser() {
		return this.currentUser;
	}

	public String login() {

		currentUser = userService.login(currentUser.getLogin(),
				currentUser.getPassword());

		if (currentUser == null) {
			return "login";
		}

		return "indexer?faces-redirect=true";

	}

	public String create() {

		userService.createUser(currentUser);

		return "login?faces-redirect=true";

	}

}
