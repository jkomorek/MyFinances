package com.bushidosoft.myfinances.dal;

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
import com.bushidosoft.myfinances.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class AccountRepositoryTest {
	
	@Autowired(required=true)
	AccountRepository accountRepository;
	
	@Autowired(required=true)
	BankRepository bankRepository;
	
	@Autowired(required=true)
	UserRepository userRepository;
	
	@Test
	@Transactional
	public void smokeTest() {
		User user = new User();
		user.setUserName("smokeTestAccountRepository "+System.currentTimeMillis());
		userRepository.create(user);
		
		Bank bank = new Bank();
		bank.setName("Test Bank "+System.currentTimeMillis());
		bankRepository.create(bank);
		Assert.assertNotSame(bank.getId(), 0);
		
		Account account = new Account();
		account.setBank(bank);
		account.setName("My First Checking Account "+System.currentTimeMillis());
		account.setUser(user);
		accountRepository.create(account);
		Assert.assertNotSame(account.getId(), 0);
	}

	@Test
	@Transactional
	public void assertCannotCreateTwoAccountsWithSameNameAndBankForUser() {
		boolean caughtExpectedException = false;
		String accountName = "assertCannotCreateTwoAccountsWithSameNameAndBankForUser "+System.currentTimeMillis();

		User user = new User();
		user.setUserName("assertCannotCreateTwoAccountsWithSameNameAndBankForUser "+System.currentTimeMillis());
		userRepository.create(user);
		
		Bank bank = new Bank();
		bank.setName("assertCannotCreateTwoAccountsWithSameNameAndBankForUser "+System.currentTimeMillis());
		bankRepository.create(bank);
		Assert.assertNotSame(bank.getId(), 0);
		
		Account account = new Account();
		account.setBank(bank);
		account.setName(accountName);
		account.setUser(user);
		accountRepository.create(account);
		Assert.assertNotSame(account.getId(), 0);

		try {
			account = new Account();
			account.setBank(bank);
			account.setName(accountName);
			account.setUser(user);
			accountRepository.create(account);
			Assert.assertNotSame(account.getId(), 0);
		} catch (ConstraintViolationException e) {
			caughtExpectedException = true;
		}
		
		Assert.assertTrue("We should have thrown a unique constraint exception because we tried adding two accounts with the same namem bank, and user", caughtExpectedException);
	}


}
