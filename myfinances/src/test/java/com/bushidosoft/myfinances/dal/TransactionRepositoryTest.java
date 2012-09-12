package com.bushidosoft.myfinances.dal;

import java.util.Date;

import junit.framework.Assert;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.entities.Account;
import com.bushidosoft.myfinances.entities.Bank;
import com.bushidosoft.myfinances.entities.Transaction;
import com.bushidosoft.myfinances.entities.Transaction.TransactionType;
import com.bushidosoft.myfinances.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TransactionRepositoryTest {
	
	@Autowired(required=true)
	AccountRepository accountRepository;
	
	@Autowired(required=true)
	BankRepository bankRepository;
	
	@Autowired(required=true)
	TransactionRepository transactionRepository;
	
	@Autowired(required=true)
	UserRepository userRepository;
	
	@Test
	@Transactional
	public void smokeTest() {
		String UID = "smokeTest "+System.currentTimeMillis(); 
		User user = new User();
		user.setUserName(UID);
		userRepository.create(user);
		
		Bank bank = new Bank();
		bank.setName(UID);
		bankRepository.create(bank);
		
		Account account = new Account();
		account.setBank(bank);
		account.setName(UID);
		account.setUser(user);
		accountRepository.create(account);
		
		Date postedDate = new Date();
		
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setAmount(123.45);
		transaction.setDescription("description");
		transaction.setExternalId("externalId");
		transaction.setPostedDate(postedDate);
		transaction.setType(TransactionType.Check);
		transactionRepository.create(transaction);
		Assert.assertNotSame(transaction.getId(), 0);
		
		Assert.assertEquals(123.45, transaction.getAmount());
		Assert.assertEquals("description", transaction.getDescription());
		Assert.assertEquals("externalId", transaction.getExternalId());
		Assert.assertEquals(postedDate, transaction.getPostedDate());
		Assert.assertEquals(TransactionType.Check, transaction.getType());
	}
	
	@Test
	@Transactional
	public void assertCannotCreateNonUniqueTransaction() {
		boolean caughtExpectedException = false;
		String UID = "assertCannotCreateNonUniqueTransaction "+System.currentTimeMillis(); 
		User user = new User();
		user.setUserName(UID);
		userRepository.create(user);
		
		Bank bank = new Bank();
		bank.setName(UID);
		bankRepository.create(bank);
		
		Account account = new Account();
		account.setBank(bank);
		account.setName(UID);
		account.setUser(user);
		accountRepository.create(account);
		
		Date postedDate = new Date();
		
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setAmount(123.45);
		transaction.setDescription("description");
		transaction.setExternalId("externalId");
		transaction.setPostedDate(postedDate);
		transaction.setType(TransactionType.Check);
		transactionRepository.create(transaction);

		transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setAmount(678.90);
		transaction.setDescription("another description");
		transaction.setExternalId("externalId");
		transaction.setPostedDate(new Date());
		transaction.setType(TransactionType.BillPay);
		
		try {
			transactionRepository.create(transaction);
		} catch (ConstraintViolationException e) {
			caughtExpectedException = true;
		}
		
		Assert.assertTrue("We should have thrown a unique constraint exception because we tried adding two transaction for the same account with the same external ID", caughtExpectedException);
	}

}
