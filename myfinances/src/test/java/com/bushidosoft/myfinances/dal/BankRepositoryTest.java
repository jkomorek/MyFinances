package com.bushidosoft.myfinances.dal;

import junit.framework.Assert;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.entities.Bank;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class BankRepositoryTest {
	
	@Autowired(required=true)
	BankRepository bankRepository;
	
	@Test
	@Transactional
	public void smokeTest() {
		String bankName = "Test Bank "+System.currentTimeMillis();
		
		Bank bank = new Bank();
		bank.setName( bankName );
		bankRepository.create(bank);
		Assert.assertNotSame(0, bank.getId());
		
		//bank = bankRepository.findBankLogin(bankName);
		bank = bankRepository.findByAttribute("name", bankName);
		Assert.assertNotSame(0, bank.getId());
		Assert.assertEquals(bankName, bank.getName());
	}
	
	@Test
	@Transactional
	public void assertCannotCreateTwoBanksWithSameName() {
		boolean caughtExpectedException = false;
		String bankName = "assertCannotCreateTwoBanksWithSameName "+System.currentTimeMillis();
		Bank bank = new Bank();
		bank.setName(bankName);
		bankRepository.create(bank);
		
		bank = new Bank();
		bank.setName(bankName);
		try {
			bankRepository.create(bank);
		} catch (ConstraintViolationException e) {
			caughtExpectedException = true;
		}
		
		Assert.assertTrue("We should have thrown a unique constraint exception because we tried adding two banks with the same name", caughtExpectedException);
	}

}
