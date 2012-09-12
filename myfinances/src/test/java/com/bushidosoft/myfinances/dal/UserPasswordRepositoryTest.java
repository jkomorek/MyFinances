package com.bushidosoft.myfinances.dal;

import junit.framework.Assert;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.entities.User;
import com.bushidosoft.myfinances.entities.UserPassword;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class UserPasswordRepositoryTest {
	
	@Autowired(required=true)
	UserRepository userRepository;
	
	@Autowired(required=true)
	UserPasswordRepository userPasswordRepository;
	
	@Test
	@Transactional
	public void smokeTest() {
		String UID = "smokeTest "+System.currentTimeMillis(); 
		User user = new User();
		user.setUserName(UID);
		userRepository.create(user);
		
		UserPassword userPassword = new UserPassword();
		userPassword.setPassword(UID);
		userPassword.setUser(user);
		userPasswordRepository.create(userPassword);
		Assert.assertNotSame(0, userPassword.getId());
		
		Assert.assertEquals(UID, userPassword.getPassword());
		Assert.assertEquals(UID, userPassword.getUser().getUserName());
	}
	
	@Test
	@Transactional
	public void assertCannotCreateMultipleForSameUser() {
		boolean caughtExpectedException = false;
		String UID = "assertCannotCreateMultipleForSameUser "+System.currentTimeMillis(); 
		User user = new User();
		user.setUserName(UID);
		userRepository.create(user);
		
		UserPassword userPassword = new UserPassword();
		userPassword.setPassword(UID);
		userPassword.setUser(user);
		userPasswordRepository.create(userPassword);

		userPassword = new UserPassword();
		userPassword.setPassword(UID+"!");
		userPassword.setUser(user);

		try {
			userPasswordRepository.create(userPassword);
		} catch (ConstraintViolationException e) {
			caughtExpectedException = true;
		}
		
		Assert.assertTrue("We should have thrown a unique constraint exception because we tried adding two UserPasswords for the same user", caughtExpectedException);
	}

}
