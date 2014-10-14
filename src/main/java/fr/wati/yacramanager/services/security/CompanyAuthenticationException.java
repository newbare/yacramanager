package fr.wati.yacramanager.services.security;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class CompanyAuthenticationException extends AuthenticationException {

	public CompanyAuthenticationException(String msg) {
		super(msg);
	}

	public CompanyAuthenticationException(String msg, Throwable t) {
		super(msg, t);
	}

}
