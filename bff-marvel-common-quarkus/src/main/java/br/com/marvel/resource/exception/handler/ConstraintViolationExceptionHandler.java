package br.com.marvel.resource.exception.handler;

import br.com.marvel.resource.exception.dto.BffMarvelError;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        return Response.status(Response.Status.BAD_REQUEST).entity(BffMarvelError.badRequest(ex)).build();
    }

}
