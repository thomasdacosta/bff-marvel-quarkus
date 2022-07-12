package br.com.marvel.controller.exception.handler;

import br.com.marvel.controller.exception.dto.ApiMessageStatus;
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
        return Response.status(Response.Status.BAD_REQUEST).entity(ApiMessageStatus.badRequest(ex)).build();
    }

}
