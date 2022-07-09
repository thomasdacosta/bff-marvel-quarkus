package br.com.marvel.resource;

import br.com.marvel.resource.dto.Pagination;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.dto.characters.ThumbnailCharacter;
import br.com.marvel.resource.exception.CharactersNotFoundException;
import br.com.marvel.service.MarvelCharacterService;
import br.com.marvel.utils.PaginationUtils;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MarvelCharacterResource {

    @Inject
    MarvelCharacterService marvelCharacterService;

    @GET
    @Path("/characters/local")
    public Response findCharactersLocal(@QueryParam("name") String name,
                                    @QueryParam("nameStartsWith") String nameStartsWith,
                                    @HeaderParam("offset") @DefaultValue("0") @Min(0) BigDecimal offset,
                                    @HeaderParam("limit") @DefaultValue("10") @Max(100) BigDecimal limit) {
        return response(marvelCharacterService.findCharactersLocal(name, nameStartsWith, offset, limit));
    }

    @GET
    @Path("/characters/api")
    public Response findCharactersApi(@QueryParam("name") String name,
                                   @QueryParam("nameStartsWith") String nameStartsWith,
                                   @HeaderParam("offset") @DefaultValue("0") @Min(0) BigDecimal offset,
                                   @HeaderParam("limit") @DefaultValue("10") @Max(100) BigDecimal limit) {
        return response(marvelCharacterService.findCharactersApi(name, nameStartsWith, offset, limit));
    }

    @POST
    @Path("/characters")
    public Response saveCharacters(@Valid MarvelCharacter marvelCharacter) {
        return Response.status(Response.Status.CREATED)
                .entity(marvelCharacterService.save(marvelCharacter)).build();
    }

    @POST
    @Path("/characters/upload")
    public Response upload(@Valid ThumbnailCharacter thumbnailCharacter) {
        return null;
    }

    @PUT
    @Path("/characters")
    public Response updateCharacters(@Valid MarvelCharacter marvelCharacter) {
        return Response.ok(marvelCharacterService.update(marvelCharacter)).build();
    }

    @DELETE
    @Path("/characters/{id}")
    public Response deleteCharacters(@PathParam(value = "id") Long id) {
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
