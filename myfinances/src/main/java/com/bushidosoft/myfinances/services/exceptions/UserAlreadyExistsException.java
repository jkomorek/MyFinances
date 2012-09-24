package com.bushidosoft.myfinances.services.exceptions;

/**
 * Thrown when we attempt to create a user that already exists. 
 * 
 * @author jkomorek
 *
 */
public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1946216179371657438L;

	public UserAlreadyExistsException(String userName) {
		super("A user already exists with the name ["+userName+"]");
	}

}
