package br.com.marvel.resource.exception.handler;

import br.com.marvel.resource.exception.CharactersNotFoundException;
import br.com.marvel.resource.exception.dto.BffMarvelError;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

    @Inject
    Logger LOGGER;

    @Override
    public Response toResponse(WebApplicationException ex) {
        LOGGER.error(ex.getMessage(), ex);

        if (ex instanceof CharactersNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(BffMarvelError.notFound(ex)).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(BffMarvelError.internalServerError(ex)).build();
    }

}
