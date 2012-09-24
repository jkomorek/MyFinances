package com.bushidosoft.myfinances.services.exceptions;


/**
 * Represents a failed attempt to use and Invalid Password
 * 
 * @author jkomorek
 *
 */
public class InvalidPasswordException extends RuntimeException {

	private static final long serialVersionUID = 321224778599114043L;

	public InvalidPasswordException(String arg0) {
		super(arg0);
	}

}
