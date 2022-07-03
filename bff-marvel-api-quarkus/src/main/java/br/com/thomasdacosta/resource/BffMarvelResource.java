package br.com.thomasdacosta.resource;

import br.com.marvel.resource.dto.Pagination;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.thomasdacosta.client.impl.MarvelApiClientImpl;
import br.com.thomasdacosta.resource.exception.CharactersNotFoundException;
import br.com.thomasdacosta.utils.PaginationUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class BffMarvelResource {

    @Inject
    MarvelApiClientImpl marvelApiClientImpl;

    // TODO Incluir um erro de BadRequest caso o parametro não exista
    @GET
    @Path("/characters")
    public Response findCharacterse(@QueryParam("name") String name,
                                    @QueryParam("nameStartsWith") String nameStartsWith,
                                    @HeaderParam("limit") @DefaultValue("10") BigDecimal limit,
                                    @HeaderParam("offset") @DefaultValue("1") BigDecimal offset) {
        Pagination pagination = marvelApiClientImpl.listCharacters(name, nameStartsWith, limit, offset);
        return response(pagination);
    }

    @POST
    @Path("/characters")
    public Response saveCharacters(MarvelCharacter marvelCharacter) {
        return Response.ok(new MarvelCharacter()).build();
    }

    private Response response(Pagination pagination) {
        if (pagination == null) {
            throw new CharactersNotFoundException("Personagem não encontrado. Deve ser da concorrente!!!");
        }
        return PaginationUtils.pagination(pagination);
    }

}
