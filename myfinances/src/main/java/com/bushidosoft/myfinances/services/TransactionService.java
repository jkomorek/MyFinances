package com.bushidosoft.myfinances.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.dal.TransactionRepository;
import com.bushidosoft.myfinances.entities.Transaction;

@Service
public class TransactionService {
		
	@Autowired(required=true)
	private TransactionRepository transactionRepository;

	@Transactional
	public Transaction createTransaction(Transaction transaction) {
		transactionRepository.create(transaction);
		return transaction;
	}

	/**
	 * Identifies whether transaction already exists using external and account identifiers 
	 * 
	 * @param transaction
	 * @return
	 */
	@Transactional
	public boolean alreadyExists(Transaction transaction) {
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		attributeMap.put("account.id", transaction.getAccount().getId());
		attributeMap.put("externalId", transaction.getExternalId());
		return (transactionRepository.findByAttributes( attributeMap ) != null);
	}

	public TransactionRepository getTransactionDao() {
		return transactionRepository;
	}

	public void setTransactionDao(TransactionRepository transactionDao) {
		this.transactionRepository = transactionDao;
	}	
}
