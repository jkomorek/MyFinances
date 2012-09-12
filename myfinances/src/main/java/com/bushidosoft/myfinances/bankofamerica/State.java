package com.bushidosoft.myfinances.bankofamerica;

public enum State {

	SouthCarolina;
	
	public String toStringWithSpaces() {
		return this.name().replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		);
	}
}
