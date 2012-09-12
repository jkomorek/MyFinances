package com.bushidosoft.myfinances.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ChallengeQuestion {
	
	@Id
	@GeneratedValue	
	private long id;
	
	private String answer;
	
	@ManyToOne()
	private BankLogin bankLogin;
	
	private String question;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	} 
	
	public BankLogin getBankLogin() {
		return bankLogin;
	}

	public void setBankLogin(BankLogin bankLogin) {
		this.bankLogin = bankLogin;
	}

	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}

}
