package com.bushidosoft.myfinances.services;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bushidosoft.myfinances.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class UserServiceTest {
	
	@Autowired(required=true)
	private UserService userService;
	
	@Test
	public void testVerifyPassword() throws Exception {
		String salt = ""+System.currentTimeMillis();
		String userName = "TestUser@"+salt+".com";
		
		User user = userService.createUser(userName);
		userService.setCurrentPassword(user, "password0");
		Thread.sleep(1000);
		userService.setCurrentPassword(user, "password1");

		Assert.assertTrue(userService.verifyPassword(user, "password1"));
	}

}
