package br.com.jm.cvsearcher.model;

public class Curriculum {
	private String name;
	private String email;
	private String content;

	public Curriculum() {

	}

	public Curriculum(String name, String email, String content) {
		this.setName(name);
		this.setEmail(email);
		this.setContent(content);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
