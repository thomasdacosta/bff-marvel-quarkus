package br.com.marvel.adapters.inbound.controller;

import br.com.marvel.application.ports.in.CharacterServicePort;
import br.com.marvel.controller.dto.pagination.Pagination;
import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.controller.dto.characters.ThumbnailCharacter;
import br.com.marvel.controller.exception.core.CharactersNotFoundException;
import br.com.marvel.utils.PaginationUtils;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Counted
@Timeout(value = 5000)
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CharacterResource {

    @Inject
    Logger LOGGER;

    @Inject
    CharacterServicePort characterServicePort;

    @GET
    @Path("/characters/local")
    @Retry(maxRetries = 4, delay = 500, abortOn = CharactersNotFoundException.class)
    public Response findCharactersLocal(@QueryParam("name") String name,
                                    @QueryParam("nameStartsWith") String nameStartsWith,
                                    @HeaderParam("offset") @DefaultValue("0") @Min(0) BigDecimal offset,
                                    @HeaderParam("limit") @DefaultValue("10") @Max(100) BigDecimal limit) {
        LOGGER.info("MarvelCharacterResource#findCharactersLocal invocation...");
        return response(characterServicePort.findCharactersLocal(name, nameStartsWith, offset, limit));
    }

    @GET
    @Path("/characters/api")
    public Response findCharactersApi(@QueryParam("name") String name,
                                   @QueryParam("nameStartsWith") String nameStartsWith,
                                   @HeaderParam("offset") @DefaultValue("0") @Min(0) BigDecimal offset,
                                   @HeaderParam("limit") @DefaultValue("10") @Max(100) BigDecimal limit) {
        LOGGER.info("MarvelCharacterResource#findCharactersApi invocation...");
        return response(characterServicePort.findCharactersApi(name, nameStartsWith, offset, limit));
    }

    @POST
    @Path("/characters")
    @Retry(maxRetries = 4, delay = 500, abortOn = {ConstraintViolationException.class})
    public Response saveCharacters(@Valid Character character) {
        LOGGER.info("MarvelCharacterResource#saveCharacters invocation...");
        return Response.status(Response.Status.CREATED)
                .entity(characterServicePort.save(character)).build();
    }

    @POST
    @Path("/characters/upload")
    public Response upload(@Valid ThumbnailCharacter thumbnailCharacter) {
        LOGGER.info("MarvelCharacterResource#upload invocation...");
        return null;
    }

    @PUT
    @Path("/characters")
    @Retry(maxRetries = 4, delay = 500, abortOn = {ConstraintViolationException.class, CharactersNotFoundException.class})
    public Response updateCharacters(@Valid Character character) {
        LOGGER.info("MarvelCharacterResource#updateCharacters invocation...");
        return Response.ok(characterServicePort.update(character)).build();
    }

    @DELETE
    @Path("/characters/{id}")
    @Retry(maxRetries = 4, delay = 500, abortOn = {ConstraintViolationException.class, CharactersNotFoundException.class})
    public Response deleteCharacters(@PathParam(value = "id") Long id) {
        LOGGER.info("MarvelCharacterResource#deleteCharacters invocation...");
        characterServicePort.delete(id);
        return Response.noContent().build();
    }

    private Response response(Pagination pagination) {
        if (pagination == null) {
            throw new CharactersNotFoundException("Personagem não encontrado. Deve ser da concorrente!!!");
        }
        return PaginationUtils.pagination(pagination);
    }

}
