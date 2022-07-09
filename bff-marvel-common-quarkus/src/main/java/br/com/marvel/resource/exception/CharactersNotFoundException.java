package br.com.marvel.resource.exception;

import javax.ws.rs.WebApplicationException;

public class CharactersNotFoundException extends WebApplicationException {

	private static final long serialVersionUID = -5560345230462781018L;

	public CharactersNotFoundException(String message) {
        super(message);
    }

}
