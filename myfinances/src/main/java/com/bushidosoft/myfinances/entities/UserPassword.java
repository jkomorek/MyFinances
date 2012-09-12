package com.bushidosoft.myfinances.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;


/**
 * Represents the password used by a User to authenticate to this system in clear text (not encrypted, hashed, etc.) 
 * 
 * @author jkomorek
 *
 */
@Entity
public class UserPassword {

	@Id
	@GeneratedValue
	private long id;
	private String password;
	@OneToOne(cascade= {CascadeType.ALL})
	private User user;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
