package com.bushidosoft.myfinances.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A bank
 * 
 * @author jkomorek
 *
 */
@Entity
public class Bank {

	@Id
	@GeneratedValue
	private long id;
	private String name;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
