package br.com.marvel.resource;

import br.com.marvel.resource.dto.Pagination;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.dto.characters.ThumbnailCharacter;
import br.com.marvel.resource.exception.CharactersNotFoundException;
import br.com.marvel.service.MarvelCharacterService;
import br.com.marvel.utils.PaginationUtils;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
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

@Timeout(value = 5000)
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MarvelCharacterResource {

    @Inject
    Logger LOGGER;

    @Inject
    MarvelCharacterService marvelCharacterService;

    @GET
    @Path("/characters/local")
    @Retry(maxRetries = 4, delay = 500, abortOn = CharactersNotFoundException.class)
    public Response findCharactersLocal(@QueryParam("name") String name,
                                    @QueryParam("nameStartsWith") String nameStartsWith,
                                    @HeaderParam("offset") @DefaultValue("0") @Min(0) BigDecimal offset,
                                    @HeaderParam("limit") @DefaultValue("10") @Max(100) BigDecimal limit) {
        LOGGER.info("MarvelCharacterResource#findCharactersLocal invocation...");
        return response(marvelCharacterService.findCharactersLocal(name, nameStartsWith, offset, limit));
    }

    @GET
    @Path("/characters/api")
    public Response findCharactersApi(@QueryParam("name") String name,
                                   @QueryParam("nameStartsWith") String nameStartsWith,
                                   @HeaderParam("offset") @DefaultValue("0") @Min(0) BigDecimal offset,
                                   @HeaderParam("limit") @DefaultValue("10") @Max(100) BigDecimal limit) {
        LOGGER.info("MarvelCharacterResource#findCharactersApi invocation...");
        return response(marvelCharacterService.findCharactersApi(name, nameStartsWith, offset, limit));
    }

    @POST
    @Path("/characters")
    @Retry(maxRetries = 4, delay = 500, abortOn = {ConstraintViolationException.class})
    public Response saveCharacters(@Valid MarvelCharacter marvelCharacter) {
        LOGGER.info("MarvelCharacterResource#saveCharacters invocation...");
        return Response.status(Response.Status.CREATED)
                .entity(marvelCharacterService.save(marvelCharacter)).build();
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
    public Response updateCharacters(@Valid MarvelCharacter marvelCharacter) {
        LOGGER.info("MarvelCharacterResource#updateCharacters invocation...");
        return Response.ok(marvelCharacterService.update(marvelCharacter)).build();
    }

    @DELETE
    @Path("/characters/{id}")
    @Retry(maxRetries = 4, delay = 500, abortOn = {ConstraintViolationException.class, CharactersNotFoundException.class})
    public Response deleteCharacters(@PathParam(value = "id") Long id) {
        LOGGER.info("MarvelCharacterResource#deleteCharacters invocation...");
        marvelCharacterService.delete(id);
        return Response.noContent().build();
    }

    private Response response(Pagination pagination) {
        if (pagination == null) {
            throw new CharactersNotFoundException("Personagem não encontrado. Deve ser da concorrente!!!");
        }
        return PaginationUtils.pagination(pagination);
    }

}
