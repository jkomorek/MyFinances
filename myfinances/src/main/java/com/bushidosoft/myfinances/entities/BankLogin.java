package com.bushidosoft.myfinances.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.bushidosoft.myfinances.bankofamerica.State;

@Entity
public class BankLogin {
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne(cascade={CascadeType.ALL})
	private User user;
	
	@ManyToOne(cascade={CascadeType.ALL})
	private Bank bank;
	
	public String userName;
	public String password;
	
	@Enumerated(EnumType.STRING)
	public State state;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}

}
