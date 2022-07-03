package br.com.thomasdacosta.resource.exception;

import javax.ws.rs.NotFoundException;

public class CharactersNotFoundException extends NotFoundException {

    public CharactersNotFoundException(String message) {
        super(message);
    }

}
