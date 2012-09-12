package com.bushidosoft.myfinances.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Transaction {

	@Id
	@GeneratedValue
	public long id;
	@ManyToOne(cascade= {CascadeType.ALL})
	public Account account;
	public double amount;
	public String description;
	public String externalId;
	public Date postedDate;
	public double resultingBalance;
	
	@Enumerated(EnumType.STRING)
	public TransactionType type;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Date getPostedDate() {
		return postedDate;
	}
	
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	
	public double getResultingBalance() {
		return resultingBalance;
	}

	public void setResultingBalance(double resultingBalance) {
		this.resultingBalance = resultingBalance;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Transaction [id= " + id + ", amount=" + amount + ", description=" + description
				+ ", externalId=" + externalId + ", postedDate=" + postedDate
				+ ", type=" + type + "]";
	}
	
	
	/**
	 * Type of Transaction
	 * 
	 * @author jkomorek
	 */
	public enum TransactionType {
		BankCharge, 	// Not really needed, I should determine bank charges from rules.
		BillPay,		// Not really useful, only a handful of transactions anyway
		Cards,			// I think that this is assumed, though it might be nice to capture
		Check,			// Definitely good, when we have checks we have images that are useful
		Credit,			// Sounds like something useful.. but maybe not, if I know what account is used.
		Deposit,		// Sometimes there are checks... right? If so maybe we call this a check so that we have the image
		OtherPayment,	// Not really useful
		Transfer,		// Good to know, especially if we can identify the account and link them
		Withdrawal		// Good to know I think, especially if we can identify the location
	}

}
