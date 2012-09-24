package com.bushidosoft.myfinances.services;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bushidosoft.myfinances.dal.UserPasswordRepository;
import com.bushidosoft.myfinances.dal.UserRepository;
import com.bushidosoft.myfinances.entities.User;
import com.bushidosoft.myfinances.entities.UserPassword;
import com.bushidosoft.myfinances.services.exceptions.InvalidPasswordException;
import com.bushidosoft.myfinances.services.exceptions.InvalidUserNameException;
import com.bushidosoft.myfinances.services.exceptions.UserAlreadyExistsException;

@Service
public class UserService {
	
	@Autowired(required=true)
	private UserRepository userRepository;
	
	@Autowired(required=true)
	private UserPasswordRepository userPasswordRepository;
	
	private static Logger logger = Logger.getLogger(UserService.class);
	
	
	/**
	 * Asserts that the current password for the provided user matches the supplied password 
	 * 
	 * @param user
	 * @param password
	 * @return true if current password matches
	 */
	public boolean verifyPassword(User user, String password) {
		if (user == null) throw new RuntimeException("Cannot verify password for null User");
		logger.debug("Verifying password for user ["+user.getUserName()+"]");
		
		String currentPassword = userPasswordRepository.findMostRecentPasswordForUser(user);
		if (currentPassword == null) {
			logger.debug("Current password is null");
			return false;
		}
		
		return (currentPassword.equals(password));
	}
	
	
	/**
	 * Set current Password for User
	 */
	public void setCurrentPassword(User user, String password) {
		validatePassword(user, password);
		logger.debug("Changing password for user ["+user.getUserName()+"]");
		
		UserPassword userPassword = new UserPassword();
		userPassword.setPassword(password);
		userPassword.setUser(user);
		
		userPasswordRepository.create(userPassword);
	}
	
	
	/**
	 * Attempts to create a new User with the provided UserName. 
	 * 
	 * @throws InvalidUserNameException
	 * @throws UserAlreadyExistsException
	 * @param userName
	 * @return
	 */
	public User createUser(String userName) {
		validateUserName(userName);
		if (userRepository.findByAttribute("userName", userName) != null) throw new UserAlreadyExistsException(userName);

		User user = new User();
		user.setUserName(userName);
		userRepository.create(user);
		
		return user;
	}
	
	/**
	 * Assert that the supplied userName:
	 * <ul>
	 *  <li>is not null</li>
	 *  <li>is a valid email address</li>
	 * </ul> 
	 * 
	 * @throws InvalidUserNameException
	 * @param userName
	 */
	public void validateUserName(String userName) {
		if (userName == null) throw new InvalidUserNameException(userName, "UserName cannot be null");
		if (!EmailValidator.getInstance().isValid(userName)) throw new InvalidUserNameException(userName, "UserName is not a valid email address");
	}
	
	/**
	 * Asserts that the supplied password:
	 * <ul>
	 *  <li>is not null</li>
	 *  <li>is at least 8 characters</li>
	 *  <li>does not match the userName for the user it applies to</li>
	 * </ul>
	 * @param password
	 */
	public void validatePassword(User user, String password) {
		if (user == null) throw new RuntimeException("Cannot validate password against null user");
		if (password == null) throw new InvalidPasswordException("Null passwords are not allowed");
		if (password.length() < 8) throw new InvalidPasswordException("Password must be at least 8 characters");
		if (password.equals(user.getUserName())) throw new InvalidPasswordException("Password must not match user name");
		
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
}
