package br.com.jm.cvsearcher.web;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named
public class CurriculumBean {
	private String name;
	private String email;
	private String description;
	
	@Inject
	private ConfigBean config;
	
	private static int ID = 1; 
	
	public String create() {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(
					config.getFilesDirectory() + ID++ + ".txt"));
			
			writer.println(this.getName());
			writer.println(this.getEmail());
			writer.println(this.getDescription());
			
			
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return "curriculum";
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
