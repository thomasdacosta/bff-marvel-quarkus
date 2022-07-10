package br.com.marvel.resource.exception.handler;

import br.com.marvel.resource.exception.dto.BffMarvelError;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    Logger LOGGER;

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return Response.status(Response.Status.BAD_REQUEST).entity(BffMarvelError.badRequest(ex)).build();
    }

}
