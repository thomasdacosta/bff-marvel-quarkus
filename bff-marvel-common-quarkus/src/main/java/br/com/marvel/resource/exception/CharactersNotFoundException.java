package br.com.marvel.resource.exception;

import javax.ws.rs.WebApplicationException;

public class CharactersNotFoundException extends WebApplicationException {

    public CharactersNotFoundException(String message) {
        super(message);
    }

}
