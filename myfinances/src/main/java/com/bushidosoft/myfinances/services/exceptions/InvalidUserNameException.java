package com.bushidosoft.myfinances.services.exceptions;

/**
 * Thrown when a UserName validation check fails, indicating that the supplied UserName does not meet our requirements.
 * 
 * @author jkomorek
 *
 */
public class InvalidUserNameException extends RuntimeException {

	private static final long serialVersionUID = 6091508082760276215L;
	
	
	public InvalidUserNameException(String userName, String reason) {
		super("The supplied UserName ["+userName+"] is invalid for reason ["+reason+"]");
	}
}
