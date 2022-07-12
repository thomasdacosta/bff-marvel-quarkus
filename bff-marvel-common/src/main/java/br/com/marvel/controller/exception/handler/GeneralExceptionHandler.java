package br.com.marvel.controller.exception.handler;

import br.com.marvel.controller.exception.dto.ApiMessageStatus;
import com.fasterxml.jackson.core.JacksonException;
import org.apache.http.MethodNotSupportedException;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralExceptionHandler implements ExceptionMapper<Exception> {

    @Inject
    Logger LOGGER;

    @Override
    public Response toResponse(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);

        if (ex instanceof BadRequestException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiMessageStatus.badRequest(ex)).build();
        }

        if (ex instanceof JacksonException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiMessageStatus.badRequest(ex)).build();
        }

        if (ex instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(ApiMessageStatus.notFound(ex)).build();
        }

        if (ex instanceof MethodNotSupportedException) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(ApiMessageStatus.methodNotSupported(ex)).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiMessageStatus.internalServerError(ex)).build();
    }

}
